package features;

import objects.Sphere;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    @Test
    void testIntersectionEncapsulatesTAndObject() {
        Sphere s = new Sphere();
        Intersection i = new Intersection(3.5, s);

        assertEquals(3.5, i.getTime());
        assertEquals(s, i.getShape());
    }

    @Test
    void testHitWhenAllIntersectionsHavePositiveT() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(1, s);
        Intersection i2 = new Intersection(2, s);
        ArrayList<Intersection> xs = new ArrayList<>(Arrays.asList(i1, i2));
        Intersection i = Intersection.hit(xs);

        assertEquals(i1, i);
    }

    @Test
    void testHitWhenSomeIntersectionsHaveNegativeT() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(-1, s);
        Intersection i2 = new Intersection(1, s);
        ArrayList<Intersection> xs = new ArrayList<>(Arrays.asList(i1, i2));
        Intersection i = Intersection.hit(xs);
        assertEquals(i2, i);
    }

    @Test
    void testHitWhenAllIntersectionsHaveNegativeT() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(-2, s);
        Intersection i2 = new Intersection(-1, s);
        ArrayList<Intersection> xs = new ArrayList<>(Arrays.asList(i1, i2));
        Intersection i = Intersection.hit(xs);
        assertNull(i);
    }

    @Test
    void testHitIsAlwaysLowestNonNegativeIntersection() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(5, s);
        Intersection i2 = new Intersection(7, s);
        Intersection i3 = new Intersection(-3, s);
        Intersection i4 = new Intersection(2, s);
        ArrayList<Intersection> xs = new ArrayList<>(Arrays.asList(i1, i2, i3, i4));
        Intersection i = Intersection.hit(xs);
        assertEquals(i4, i);
    }

    @Test
    void testPrecomputationOfTheStateOfAnIntersection() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere shape = new Sphere();
        Intersection i = new Intersection(4, shape);

        Precompute comps = new Precompute(i, r);

        assertEquals(i.getTime(), comps.t);
        assertEquals(i.getShape(), comps.object);
        assertEquals(new Point(0,0,-1), comps.point);
        assertEquals(new Vector(0,0,-1), comps.eye);
        assertEquals(new Vector(0,0,-1), comps.normal);
    }

    @Test
    void testHitWhenIntersectionOccursOnTheOutside() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere shape = new Sphere();
        Intersection i = new Intersection(4, shape);

        Precompute comps = new Precompute(i, r);

        assertFalse(comps.inside);
    }

    @Test
    void testHitWhenIntersectionOccursOnTheInside() {
        Ray r = new Ray(new Point(0,0,0), new Vector(0,0,1));
        Sphere shape = new Sphere();
        Intersection i = new Intersection(1, shape);

        Precompute comps = new Precompute(i, r);

        assertEquals(new Point(0,0,1), comps.point);
        assertEquals(new Vector(0,0,-1), comps.eye);
        assertTrue(comps.inside);
        assertEquals(new Vector(0,0,-1), comps.normal);
    }


}

package objects;

import features.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test
    void testIntersectScaledSphereWithRay() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.scaling(2,2,2));

        ArrayList<Intersection> xs = s.intersect(r);

        assertEquals(2, xs.size());
        assertEquals(3, xs.get(0).getTime());
        assertEquals(7, xs.get(1).getTime());
    }

    @Test
    void testIntersectTranslatedSphereWithRay() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5,0,0));

        ArrayList<Intersection> xs = s.intersect(r);

        assertEquals(0, xs.size());
    }

    @Test
    void testNormalAtXAxisOnSphere() {
        Sphere s = new Sphere();
        Vector n = s.normal_at(new Point(1, 0, 0));

        assertEquals(new Vector(1, 0,0), n);
    }

    @Test
    void testNormalAtYAxisOnSphere()  {
        Sphere s = new Sphere();
        Vector n = s.normal_at(new Point(0, 1, 0));

        assertEquals(new Vector(0, 1, 0), n);
    }

    @Test
    void testNormalAtZAxisOnSphere() {
        Sphere s = new Sphere();
        Vector n = s.normal_at(new Point(0, 0, 1));

        assertEquals(new Vector(0, 0, 1), n);
    }

    @Test
    void testNormalOnSphereAtNonAxialPoint() {
        Sphere s = new Sphere();
        double val = Math.sqrt(3)/3;
        Vector n = s.normal_at(new Point(val, val, val));

        assertEquals(new Vector(val, val, val), n);
    }

    @Test
    void testNormalIsANormalisedVector() {
        Sphere s = new Sphere();
        double val = Math.sqrt(3)/3;
        Vector n = s.normal_at(new Point(val, val, val));

        assertEquals(n.normalize(), n);
    }
}

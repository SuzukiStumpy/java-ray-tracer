package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    @Test
    void testNormalOfAPlaneIsConstantEverywhere() {
        Plane p = new Plane();
        Vector n1 = p.normal_at(new Point(0,0,0));
        Vector n2 = p.normal_at(new Point(10, 0, -10));
        Vector n3 = p.normal_at(new Point(-5,0,150));
        Vector result = new Vector(0,1,0);

        assertEquals(result, n1);
        assertEquals(result, n2);
        assertEquals(result, n3);
    }

    @Test
    void testIntersectRayParallelToPlane() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0, 10, 0), new Vector(0,0,1));
        ArrayList<Intersection> xs = p.intersect(r);

        assertTrue(xs.isEmpty());
    }

    @Test
    void testIntersectPlaneWithCoplanarRay() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0,0,0), new Vector(0,0,1));
        ArrayList<Intersection> xs = p.intersect(r);

        assertTrue(xs.isEmpty());
    }

    @Test
    void testIntersectPlaneWithRayFromAbove() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0, 1, 0), new Vector(0, -1, 0));
        ArrayList<Intersection> xs = p.intersect(r);

        assertEquals(1, xs.size());
        assertEquals(1, xs.get(0).getTime());
        assertEquals(p, xs.get(0).getShape());
    }

    @Test
    void testIntersectPlaneWithRayFromBelow() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0, -1, 0), new Vector(0, 1, 0));
        ArrayList<Intersection> xs = p.intersect(r);

        assertEquals(1, xs.size());
        assertEquals(1, xs.get(0).getTime());
        assertEquals(p, xs.get(0).getShape());
    }
}

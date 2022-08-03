package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    @Test
    void testConstructingATriangle() {
        Point p1 = new Point(0, 1, 0);
        Point p2 = new Point(-1, 0,0);
        Point p3 = new Point(1,0,0);

        Triangle t = new Triangle(p1, p2, p3);

        assertEquals(p1, t.point(0));
        assertEquals(p2, t.point(1));
        assertEquals(p3, t.point(2));
        assertEquals(new Vector(-1, -1, 0), t.edge(0));
        assertEquals(new Vector(1, -1, 0), t.edge(1));
        assertEquals(new Vector(0,0,-1), t.normal());
    }

    @Test
    void testFindingTheNormalOfATriangle() {
        Triangle t = new Triangle(new Point(0,1,0), new Point(-1,0,0), new Point(1,0,0));
        Vector n1 = t.local_normal_at(new Point(0,0.5,0));
        Vector n2 = t.local_normal_at(new Point(-0.5, 0.75, 0));
        Vector n3 = t.local_normal_at(new Point(0.5, 0.25, 0));

        assertEquals(n1, t.normal());
        assertEquals(n2, t.normal());
        assertEquals(n3, t.normal());
    }

    @Test
    void testIntersectingARayParallelToATriangle() {
        Triangle t = new Triangle(new Point(0,1,0), new Point(-1,0,0), new Point(1,0,0));
        Ray r = new Ray(new Point(0,-1,-2), new Vector(0,1,0));
        ArrayList<Intersection> xs = t.local_intersect(r);
        assertTrue(xs.isEmpty());
    }

    @Test
    void testRayMissesOnP0P2Edge() {
        Triangle t = new Triangle(new Point(0,1,0), new Point(-1,0,0), new Point(1,0,0));
        Ray r = new Ray(new Point(1,1,-2), new Vector(0,0,1));
        ArrayList<Intersection> xs = t.local_intersect(r);
        assertTrue(xs.isEmpty());
    }

    @Test
    void testRayMissesOnP0P1Edge() {
        Triangle t = new Triangle(new Point(0,1,0), new Point(-1,0,0), new Point(1,0,0));
        Ray r = new Ray(new Point(-1,1,-2), new Vector(0,0,1));
        ArrayList<Intersection> xs = t.local_intersect(r);
        assertTrue(xs.isEmpty());
    }

    @Test
    void testRayMissesOnP1P2Edge() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Ray r = new Ray(new Point(0, -1, -2), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = t.local_intersect(r);
        assertTrue(xs.isEmpty());
    }

    @Test
    void testRayIntersectsTriangle() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Ray r = new Ray(new Point(0, 0.5, -2), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = t.local_intersect(r);
        assertEquals(1, xs.size());
        assertEquals(2, xs.get(0).getTime());
    }
}

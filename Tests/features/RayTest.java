package features;

import objects.Sphere;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {
    @Test
    void testCreateAndQueryARay() {
        Point origin = new Point(1, 2, 3);
        Vector direction = new Vector(4, 5, 6);
        Ray r = new Ray(origin, direction);

        assertEquals(origin, r.getOrigin());
        assertEquals(direction, r.getDirection());
    }

    @Test
    void testComputeAPointFromADistance() {
        Ray r = new Ray(new Point(2, 3, 4), new Vector(1, 0, 0));

        assertEquals(new Point(2, 3, 4), r.getPosition(0));
        assertEquals(new Point(3, 3, 4), r.getPosition(1));
        assertEquals(new Point(1, 3, 4), r.getPosition(-1));
        assertEquals(new Point(4.5, 3, 4), r.getPosition(2.5));
    }

    @Test
    void testRayIntersectsSphereAtTwoPoints() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        List<Intersection> xs = r.intersect(s);

        assertEquals(2, xs.size());
        assertEquals(4.0, xs.get(0).getTime());
        assertEquals(6.0, xs.get(1).getTime());
    }

    @Test
    void testRayIntersectsSphereAtTangent() {
        Ray r = new Ray(new Point(0,1,-5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        List<Intersection> xs = r.intersect(s);

        assertEquals(2, xs.size());
        assertEquals(5.0, xs.get(0).getTime());
        assertEquals(5.0, xs.get(1).getTime());
    }

    @Test
    void testRayMissesASphere() {
        Ray r = new Ray(new Point(0, 2, -5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        List<Intersection> xs = r.intersect(s);

        assertEquals(0, xs.size());
    }

    @Test
    void testRayOriginatesInsideSphere() {
        Ray r = new Ray(new Point(0, 0, 0), new Vector(0,0,1));
        Sphere s = new Sphere();
        List<Intersection> xs = r.intersect(s);

        assertEquals(2, xs.size());
        assertEquals(-1.0, xs.get(0).getTime());
        assertEquals(1.0, xs.get(1).getTime());
    }

    @Test
    void testSphereIsBehindRay() {
        Ray r = new Ray(new Point(0, 0, 5), new Vector(0,0,1));
        Sphere s = new Sphere();
        List<Intersection> xs = r.intersect(s);

        assertEquals(2, xs.size());
        assertEquals(-6.0, xs.get(0).getTime());
        assertEquals(-4.0, xs.get(1).getTime());
    }

    @Test
    void testIntersectSetsTheObjectOnTheIntersection() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere s = new Sphere();
        List<Intersection> xs = r.intersect(s);

        assertEquals(2, xs.size());
        assertEquals(s, xs.get(0).getShape());
        assertEquals(s, xs.get(1).getShape());
    }
}

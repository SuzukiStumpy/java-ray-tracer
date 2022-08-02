package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static features.Precompute.EPSILON;
import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {
    @Test
    void testRayMissesACylinder() {
        Cylinder cyl = new Cylinder();
        ArrayList<Ray> rays = new ArrayList<>(List.of(
            new Ray(new Point(1,0,0), new Vector(0,1,0).normalize()),
            new Ray(new Point(0,0,0), new Vector(0,1,0).normalize()),
            new Ray(new Point(0,0,-5), new Vector(1,1,1).normalize())
        ));

        for (Ray r: rays) {
            ArrayList<Intersection> xs = cyl.local_intersect(r);
            assertEquals(0, xs.size());
        }
    }

    @Test
    void testRayHitsACylinder() {
        Cylinder cyl = new Cylinder();
        ArrayList<Intersection> xs;
        Ray r;

        r = new Ray(new Point(1,0,-5), new Vector(0,0,1).normalize());
        xs = cyl.local_intersect(r);
        assertEquals(2, xs.size());
        assertEquals(5, xs.get(0).getTime(), EPSILON);
        assertEquals(5, xs.get(1).getTime(), EPSILON);

        r = new Ray(new Point(0,0,-5), new Vector(0,0,1).normalize());
        xs = cyl.local_intersect(r);
        assertEquals(2, xs.size());
        assertEquals(4, xs.get(0).getTime(), EPSILON);
        assertEquals(6, xs.get(1).getTime(), EPSILON);

        r = new Ray(new Point(0.5, 0, -5), new Vector(0.1,1,1).normalize());
        xs = cyl.local_intersect(r);
        assertEquals(2, xs.size());
        assertEquals(6.80798, xs.get(0).getTime(), EPSILON);
        assertEquals(7.08872, xs.get(1).getTime(), EPSILON);
    }

    @Test
    void testNormalVectorOnCylinder() {
        Cylinder cyl = new Cylinder();

        assertEquals(new Vector(1, 0, 0), cyl.local_normal_at(new Point(1,0,0)));
        assertEquals(new Vector(0, 0, -1), cyl.local_normal_at(new Point(0,5,-1)));
        assertEquals(new Vector(0,0,1), cyl.local_normal_at(new Point(0,-2,1)));
        assertEquals(new Vector(-1,0,0), cyl.local_normal_at(new Point(-1, 1, 0)));
    }

    @Test
    void testCylinderExtents() {
        Cylinder cyl = new Cylinder();
        assertEquals(Double.POSITIVE_INFINITY, cyl.maxY());
        assertEquals(Double.NEGATIVE_INFINITY, cyl.minY());
    }

    @Test
    void testIntersectingAConstrainedCylinder() {
        Cylinder cyl = new Cylinder();
        cyl.minY(1);
        cyl.maxY(2);

        ArrayList<Ray> rays = new ArrayList<>(List.of(
            new Ray(new Point(0,1.5,0), new Vector(0.1,1,0).normalize()),
            new Ray(new Point(0,3,-5), new Vector(0,0,1).normalize()),
            new Ray(new Point(0,0,-5), new Vector(0,0,1).normalize()),
            new Ray(new Point(0,2,-5),new Vector(0,0,1).normalize()),
            new Ray(new Point(0,1,-5), new Vector(0,0,1).normalize())
        ));

        for (Ray r: rays) {
            ArrayList<Intersection> xs = cyl.local_intersect(r);
            assertEquals(0, xs.size());
        }

        Ray r = new Ray(new Point(0,1.5,-2), new Vector(0,0,1).normalize());
        ArrayList<Intersection>  xs = cyl.local_intersect(r);
        assertEquals(2, xs.size());
    }

    @Test
    void testCylinderClosedByDefault() {
        Cylinder cyl = new Cylinder();
        assertFalse(cyl.isClosed());
    }

    @Test
    void testIntersectingTheCapsOfAClosedCylinder() {
        Cylinder cyl = new Cylinder();
        cyl.minY(1);
        cyl.maxY(2);
        cyl.closed(true);

        ArrayList<Ray> rays = new ArrayList<>(List.of(
            new Ray(new Point(0,3,0), new Vector(0,-1,0).normalize()),
            new Ray(new Point(0,3,-2), new Vector(0,-1,2).normalize()),
            new Ray(new Point(0,4,-2), new Vector(0,-1,1).normalize()),
            new Ray(new Point(0,0,-2), new Vector(0,1,2).normalize()),
            new Ray(new Point(0,-1,-2), new Vector(0,1,1).normalize())
        ));

        for (Ray r: rays) {
            ArrayList<Intersection> xs = cyl.local_intersect(r);
            assertEquals(2, xs.size());
        }
    }

    @Test
    void testNormalVectorOnCylinderEndCaps() {
        Cylinder cyl = new Cylinder();
        cyl.minY(1);
        cyl.maxY(2);
        cyl.closed(true);

        assertEquals(new Vector(0,-1,0), cyl.local_normal_at(new Point(0,1,0)));
        assertEquals(new Vector(0,-1,0), cyl.local_normal_at(new Point(0.5,1,0)));
        assertEquals(new Vector(0,-1,0), cyl.local_normal_at(new Point(0,1,0.5)));
        assertEquals(new Vector(0,1,0), cyl.local_normal_at(new Point(0,2,0)));
        assertEquals(new Vector(0,1,0), cyl.local_normal_at(new Point(0.5,2,0)));
        assertEquals(new Vector(0,1,0), cyl.local_normal_at(new Point(0,2,0.5)));
    }
}

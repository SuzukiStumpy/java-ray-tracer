package objects;

import features.Matrix;
import features.Point;
import features.Ray;
import features.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static features.Precompute.EPSILON;
import static org.junit.jupiter.api.Assertions.*;

class BoundingBoxTest {
    @Test
    void testCreatingAnEmptyBoundingBox() {
        BoundingBox box = new BoundingBox();
        Point min = box.min();
        Point max = box.max();

        // Can't directly compare the points since the infinities throw the equality checker off, so instead perform assertions
        // on individual components
        //assertEquals(new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), box.min());
        //assertEquals(new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), box.max());
        assertEquals(Double.POSITIVE_INFINITY, min.getX());
        assertEquals(Double.POSITIVE_INFINITY, min.getY());
        assertEquals(Double.POSITIVE_INFINITY, min.getZ());

        assertEquals(Double.NEGATIVE_INFINITY, max.getX());
        assertEquals(Double.NEGATIVE_INFINITY, max.getY());
        assertEquals(Double.NEGATIVE_INFINITY, max.getZ());
    }

    @Test
    void testCreatingABoundingBoxWithVolume() {
        BoundingBox box = new BoundingBox(new Point(-1, -2, -3), new Point(3,2,1));
        assertEquals(new Point(-1, -2, -3), box.min());
        assertEquals(new Point(3,2,1), box.max());
    }

    @Test
    void testAddingPointsToABoundingBox() {
        BoundingBox box = new BoundingBox();
        Point p1 = new Point(-5, 2, 0);
        Point p2 = new Point(7, 0, -3);

        box.add(p1);
        box.add(p2);

        assertEquals(new Point(-5, 0, -3), box.min());
        assertEquals(new Point(7, 2, 0), box.max());
    }

    @Test
    void testMergingBoundingBoxes() {
        BoundingBox box1 = new BoundingBox(new Point(-5, -2, 0), new Point(7, 4, 4));
        BoundingBox box2 = new BoundingBox(new Point(8, -7, -2), new Point(14,2,8));

        box1.add(box2);

        assertEquals(new Point(-5, -7, -2), box1.min());
        assertEquals(new Point(14, 4,8), box1.max());
    }

    @Test
    void testBoxContainsAGivenPoint() {
        BoundingBox box = new BoundingBox(new Point(5, -2, 0), new Point(11, 4, 7));

        assertTrue(box.contains(new Point(5, -2, 0)));
        assertTrue(box.contains(new Point(11,4,7)));
        assertTrue(box.contains(new Point(8,1,3)));
        assertFalse(box.contains(new Point(3, 0, 3)));
        assertFalse(box.contains(new Point(8,-4,3)));
        assertFalse(box.contains(new Point(8,1,-1)));
        assertFalse(box.contains(new Point(13,1,1)));
        assertFalse(box.contains(new Point(8, 5, 3)));
        assertFalse(box.contains(new Point(8,1,8)));
    }

    @Test
    void testBoxContainsAGivenBoundingBox() {
        BoundingBox box1 = new BoundingBox(new Point(5, -2, 0), new Point(11,4,7));

        assertTrue(box1.contains(new BoundingBox(new Point(5,-2,0), new Point(11,4,7))));
        assertTrue(box1.contains(new BoundingBox(new Point(6,-1,1), new Point(10,3,6))));
        assertFalse(box1.contains(new BoundingBox(new Point(4,-3,-1), new Point(10,3,6))));
        assertFalse(box1.contains(new BoundingBox(new Point(6,-1,1), new Point(12,5,8))));
    }

    @Test
    void testTransformingABoundingBox() {
        BoundingBox box = new BoundingBox(new Point(-1,-1,-1), new Point(1,1,1));
        Matrix xform = Matrix.rotation_x(Math.PI/4).rotate_y(Math.PI/4);
        BoundingBox box2 = box.transform(xform);

        assertEquals(new Point(-1.41421, -1.70711, -1.70711), box2.min());
        assertEquals(new Point(1.41421, 1.70711, 1.70711), box2.max());
    }

    @Test
    void testIntersectingARayWithABoundingBoxAtOrigin() {
        BoundingBox box = new BoundingBox(new Point(-1,-1,-1), new Point(1,1,1));
        ArrayList<Ray> rays = new ArrayList<>(List.of(
            new Ray(new Point(5,0.5,0), new Vector(-1,0,0).normalize()),
            new Ray(new Point(-5,0.5,0), new Vector(1,0,0).normalize()),
            new Ray(new Point(0.5,5,0), new Vector(0,-1,0).normalize()),
            new Ray(new Point(0.5, -5, 0), new Vector(0,1,0).normalize()),
            new Ray(new Point(0.5,0,5), new Vector(0,0,-1).normalize()),
            new Ray(new Point(0.5,0,-5), new Vector(0,0,1).normalize())
        ));

        for (Ray r: rays) {
            assertTrue(box.intersects(r));
        }

        rays = new ArrayList<>(List.of(
            new Ray(new Point(-2,0,0), new Vector(2,4,6).normalize()),
            new Ray(new Point(0,-2,0), new Vector(6,2,4).normalize()),
            new Ray(new Point(0,0,-2), new Vector(4,6,2).normalize()),
            new Ray(new Point(2,0,2), new Vector(0,0,-1).normalize()),
            new Ray(new Point(0,2,2), new Vector(0,-1,0).normalize()),
            new Ray(new Point(2,2,0), new Vector(-1,0,0).normalize())
        ));

        for (Ray r: rays) {
            assertFalse(box.intersects(r));
        }
    }

    @Test
    void testIntersectingARayWithANonCubicBoundingBox() {
        BoundingBox box = new BoundingBox(new Point(5,-2,0), new Point(11,4,7));

        ArrayList<Ray> rays = new ArrayList<>(List.of(
            new Ray(new Point(15,1,2), new Vector(-1,0,0).normalize()),
            new Ray(new Point(-5,-1,4), new Vector(1,0,0).normalize()),
            new Ray(new Point(7,6,5), new Vector(0,-1,0).normalize()),
            new Ray(new Point(9,-5,6), new Vector(0,1,0).normalize()),
            new Ray(new Point(8,2,12), new Vector(0,0,-1).normalize()),
            new Ray(new Point(6,0,-5), new Vector(0,0,1).normalize()),
            new Ray(new Point(8,1,3.5), new Vector(0,0,1).normalize())
        ));

        for (Ray r: rays) {
            assertTrue(box.intersects(r));
        }

        rays = new ArrayList<>(List.of(
            new Ray(new Point(9,-1,-8), new Vector(2,4,6).normalize()),
            new Ray(new Point(8,3,-4), new Vector(6,2,4).normalize()),
            new Ray(new Point(9,-1,-2), new Vector(4,6,2).normalize()),
            new Ray(new Point(4,0,9), new Vector(0,0,-1).normalize()),
            new Ray(new Point(8,6,-1), new Vector(0,-1,0).normalize()),
            new Ray(new Point(12,5,4), new Vector(-1,0,0).normalize())
        ));

        for (Ray r: rays) {
            assertFalse(box.intersects(r));
        }
    }
}

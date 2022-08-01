package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class CubeTest {
    @Test
    void testRayIntersectsCube() {
        Cube c = new Cube();
        ArrayList<Ray> rays = new ArrayList<>(List.of(
            new Ray(new Point(5, 0.5, 0), new Vector(-1, 0, 0)),
            new Ray(new Point(-5, 0.5, 0), new Vector(1, 0, 0)),
            new Ray(new Point(0.5, 5, 0), new Vector(0, -1, 0)),
            new Ray(new Point(0.5, -5, 0), new Vector(0, 1, 0)),
            new Ray(new Point(0.5, 0, 5), new Vector(0, 0, -1)),
            new Ray(new Point(0.5, 0, -5), new Vector(0, 0, 1))
        ));

        // Show results for all rays crossing the shape boundaries
        for (Ray r: rays) {
            ArrayList<Intersection> xs = c.local_intersect(r);

            assertEquals(2, xs.size());
            assertEquals(4, xs.get(0).getTime());
            assertEquals(6, xs.get(1).getTime());
        }

        // Now one test for a ray starting inside the shape
        Ray r = new Ray(new Point(0,0.5,0), new Vector(0,0,1));
        ArrayList<Intersection> xs = c.local_intersect(r);
        assertEquals(2, xs.size());
        assertEquals(-1, xs.get(0).getTime());
        assertEquals(1, xs.get(1).getTime());
    }

    @Test
    void testRayMissesCube() {
        Cube c = new Cube();
        ArrayList<Ray> rays = new ArrayList<>(List.of(
            new Ray(new Point(-2, 0, 0), new Vector(0.2673, 0.5345, 0.8018)),
            new Ray(new Point(0, -2, 0), new Vector(0.8018, 0.2673, 0.5345)),
            new Ray(new Point(0, 0, 2), new Vector(0.5345, 0.8018, 0.2673)),
            new Ray(new Point(2, 0, 2), new Vector(0, 0, -1)),
            new Ray(new Point(0, 2, 2), new Vector(0, -1, 0)),
            new Ray(new Point(2, 2, 0), new Vector(-1, 0, 0))
        ));

        // Show results for all rays crossing the shape boundaries
        for (Ray r: rays) {
            ArrayList<Intersection> xs = c.local_intersect(r);

            assertEquals(0, xs.size());
        }
    }

    @Test
    void testNormalOnCubeSurface() {
        Cube c = new Cube();
        // Use rays as a convenient vehicle for a point/vector pair
        ArrayList<Ray> data = new ArrayList<>(List.of(
            new Ray(new Point(1,0.5,-0.8), new Vector(1,0,0)),
            new Ray(new Point(-1, -0.2, 0.9 ), new Vector(-1,0,0)),
            new Ray(new Point(-0.4,1,-0.1), new Vector(0,1,0)),
            new Ray(new Point(0.3,-1,-0.7), new Vector(0,-1,0)),
            new Ray(new Point(-0.6,0.3,1), new Vector(0,0,1)),
            new Ray(new Point(0.4,0.4,-1), new Vector(0,0,-1)),
            new Ray(new Point(1,1,1), new Vector(1,0,0)),
            new Ray(new Point(-1,-1,-1), new Vector(-1,0,0))
        ));

        for (Ray r: data) {
            assertEquals(r.getDirection(), c.local_normal_at(r.getOrigin()));
        }
    }
}

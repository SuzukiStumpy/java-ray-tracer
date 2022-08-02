package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static features.Precompute.EPSILON;
import static org.junit.jupiter.api.Assertions.*;

class ConeTest {

    @Test
    void testIntersectingAConeWithARay() {
        Cone c = new Cone();
        Ray r;
        ArrayList<Intersection> xs;

        r = new Ray(new Point(0,0,-5), new Vector(0,0,1).normalize());
        xs = c.local_intersect(r);
        assertEquals(2, xs.size());
        assertEquals(5, xs.get(0).getTime(), EPSILON);
        assertEquals(5, xs.get(1).getTime(), EPSILON);

        r = new Ray(new Point(0,0,-5), new Vector(1,1,1).normalize());
        xs = c.local_intersect(r);
        assertEquals(2, xs.size());
        assertEquals(8.66025, xs.get(0).getTime(), EPSILON);
        assertEquals(8.66025, xs.get(1).getTime(), EPSILON);

        r = new Ray(new Point(1,1,-5), new Vector(-0.5,-1,1).normalize());
        xs = c.local_intersect(r);
        assertEquals(2, xs.size());
        assertEquals(4.55006, xs.get(0).getTime(), EPSILON);
        assertEquals(49.44994, xs.get(1).getTime(), EPSILON);
    }

    @Test
    void testIntersectConeWithRayParallelToOneOfItsHalves() {
        Cone c = new Cone();
        Vector dir = new Vector(0,1,1).normalize();
        Ray r = new Ray(new Point(0,0,-1), dir);
        ArrayList<Intersection> xs = c.local_intersect(r);

        assertEquals(1, xs.size());
        assertEquals(0.35355, xs.get(0).getTime(), EPSILON);
    }

    @Test
    void testIntersectingAConesEndCaps() {
        Cone c = new Cone();
        c.minY(-0.5);
        c.maxY(0.5);
        c.closed(true);

        Ray r;
        ArrayList<Intersection> xs;

        r = new Ray(new Point(0,0,-5), new Vector(0,1,0).normalize());
        xs = c.local_intersect(r);
        assertEquals(0, xs.size());

        r = new Ray(new Point(0,0,-0.25), new Vector(0,1,1).normalize());
        xs = c.local_intersect(r);
        assertEquals(2, xs.size());

        r = new Ray(new Point(0,0,-0.25), new Vector(0,1,0).normalize());
        xs = c.local_intersect(r);
        assertEquals(4, xs.size());
    }

    @Test
    void testComputingNormalVectorOnCone() {
        Cone c = new Cone();
        assertEquals(new Vector(0,0,0), c.local_normal_at(new Point(0,0,0)));
        assertEquals(new Vector(1,-Math.sqrt(2),1), c.local_normal_at(new Point(1,1,1)));
        assertEquals(new Vector(-1, 1, 0), c.local_normal_at(new Point(-1,-1,0)));
    }
}

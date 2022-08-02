package objects;

import features.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    @Test
    void testCreationOfANewGroup() {
        Group g = new Group();
        assertEquals(Matrix.identity(4), g.getTransform());
        assertEquals(0, g.contents().size());
    }

    @Test
    void testAddingAChildToAGroup() {
        Group g = new Group();
        TestShape s = new TestShape();
        g.addObject(s);

        assertEquals(1, g.contents().size());
        assertTrue(g.contents().contains(s));
        assertEquals(g, s.parent());
    }

    @Test
    void testIntersectingARayWithAnEmptyGroup() {
        Group g = new Group();
        Ray r = new Ray(new Point(0,0,0), new Vector(0,0,1));
        ArrayList<Intersection> xs = g.local_intersect(r);
        assertTrue(xs.isEmpty());
    }

    @Test
    void testIntersectingARayWithANonEmptyGroup() {
        Group g = new Group();
        Sphere s1 = new Sphere();
        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(0,0,-3));
        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.translation(5, 0, 0));
        g.addObject(s1);
        g.addObject(s2);
        g.addObject(s3);
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        ArrayList<Intersection> xs = g.local_intersect(r);

        assertEquals(4, xs.size());
        assertEquals(s2, xs.get(0).getShape());
        assertEquals(s2, xs.get(1).getShape());
        assertEquals(s1, xs.get(2).getShape());
        assertEquals(s1, xs.get(3).getShape());
    }

    @Test
    void testIntersectingATransformedGroup() {
        Group g = new Group();
        g.setTransform(Matrix.scaling(2,2,2));
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5,0,0));
        g.addObject(s);

        Ray r = new Ray(new Point(10, 0, -10), new Vector(0,0,1));
        ArrayList<Intersection> xs = g.intersect(r);
        assertEquals(2, xs.size());
    }
}

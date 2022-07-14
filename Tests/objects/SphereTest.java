package objects;

import features.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {
    @Test
    void testASphereDefaultTranslation() {
        Sphere s = new Sphere();
        assertEquals(Matrix.identity(4), s.getTransform());
    }

    @Test
    void testChangeSphereTransform() {
        Sphere s = new Sphere();
        Matrix t = Matrix.translation(2,3,4);
        s.setTransform(t);

        assertEquals(t, s.getTransform());
    }

    @Test
    void testIntersectScaledSphereWithRay() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.scaling(2,2,2));

        ArrayList<Intersection> xs = r.intersect(s);

        assertEquals(2, xs.size());
        assertEquals(3, xs.get(0).getTime());
        assertEquals(7, xs.get(1).getTime());
    }

    @Test
    void testIntersectTranslatedSphereWithRay() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5,0,0));

        ArrayList<Intersection> xs = r.intersect(s);

        assertEquals(0, xs.size());
    }
}

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

    @Test
    void testComputeNormalOnTranslatedSphere() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(0, 1, 0));
        Vector n = s.normal_at(new Point(0, 1.70711, -0.70711));

        assertEquals(new Vector(0, 0.70711, -0.70711), n);
    }

    @Test
    void testComputeNormalOnTransformedSphere() {
        Sphere s = new Sphere();
        Matrix m = Matrix.scaling(1, 0.5, 1).multiply(Matrix.rotation_z(Math.PI/5));
        s.setTransform(m);
        Vector n = s.normal_at(new Point(0, Math.sqrt(2)/2, -Math.sqrt(2)/2));

        assertEquals(new Vector(0, 0.97014, -0.24254), n);
    }

    @Test
    void testSphereHasDefaultMaterial() {
        Sphere s = new Sphere();
        assertEquals(new Material(), s.getMaterial());
    }

    @Test
    void testAssigningAMaterialToASphere() {
        Sphere s = new Sphere();
        Material m = new Material();
        m.setAmbient(1);
        s.setMaterial(m);
        assertEquals(m, s.getMaterial());
    }
}

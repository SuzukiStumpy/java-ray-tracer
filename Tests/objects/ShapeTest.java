package objects;

import features.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {
    @Test
    void testShapesHaveADefaultTransform() {
        TestShape s = new TestShape();
        assertEquals(Matrix.identity(4), s.getTransform());
    }

    @Test
    void testShapeAssignedNewTransform() {
        TestShape s = new TestShape();
        s.setTransform(Matrix.translation(2,3,4));
        assertEquals(Matrix.translation(2,3,4), s.getTransform());
    }

    @Test
    void testShapeHasADefaultMaterial() {
        TestShape s = new TestShape();
        assertEquals(new Material(), s.getMaterial());
    }

    @Test
    void testShapeCanHaveAnAssignedMaterial() {
        TestShape s = new TestShape();
        Material m = new Material();
        m.setAmbient(1);
        s.setMaterial(m);

        assertEquals(m, s.getMaterial());
    }

    @Test
    void testIntersectingAScaledShapeWithARay() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        TestShape s = new TestShape();
        s.setTransform(Matrix.scaling(2,2,2));
        ArrayList<Intersection> xs = s.intersect(r);

        assertEquals(new Point(0, 0, -2.5), s.saved_ray.getOrigin());
        assertEquals(new Vector(0,0,0.5), s.saved_ray.getDirection());
    }

    @Test
    void testIntersectingATranslatedShapeWithARay() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        TestShape s = new TestShape();
        s.setTransform(Matrix.translation(5,0,0));
        ArrayList<Intersection> xs = s.intersect(r);

        assertEquals(new Point(-5,0,-5), s.saved_ray.getOrigin());
        assertEquals(new Vector(0,0,1), s.saved_ray.getDirection());
    }

    @Test
    void testComputeNormalOnTranslatedShape() {
        TestShape s = new TestShape();
        s.setTransform(Matrix.translation(0, 1, 0));
        Vector n = s.normal_at(new Point(0, 1.70711, -0.70711));
        assertEquals(new Vector(0,0.70711, -0.70711), n);
    }

    @Test
    void testComputeNormalOnTransformedShape() {
        TestShape s = new TestShape();
        Matrix m = Matrix.scaling(1, 0.5, 1)
            .rotate_z(Math.PI/5);
        s.setTransform(m);

        Vector n = s.normal_at(new Point(0, Math.sqrt(2)/2, -Math.sqrt(2)/2));
        assertEquals(new Vector(0,0.97014,-0.24254), n);
    }
}

package objects;

import features.*;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;

import static features.Precompute.EPSILON;
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

    @Test
    void testShapeHasOptionalParent() {
        TestShape s = new TestShape();
        assertNull(s.parent());
    }

    @Test
    void testConvertPointFromWorldToObjectSpace() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotation_y(Math.PI/2));
        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(2,2,2));
        g1.addObject(g2);
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));
        g2.addObject(s);

        Point p = s.worldToObject(new Point(-2, 0, -10));

        assertEquals(new Point(0,0,-1), p);
    }

    @Test
    void testConvertNormalFromObjectToWorldSpace() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotation_y(Math.PI/2));
        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(1,2,3));
        g1.addObject(g2);
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5,0,0));
        g2.addObject(s);

        Vector n = s.normalToWorld(new Vector(Math.sqrt(3)/3, Math.sqrt(3)/3, Math.sqrt(3)/3));
        //assertEquals(new Vector(0.2857, 0.4286, -0.8571), n);
        assertEquals(0.28571, n.getX(), EPSILON);
        assertEquals(0.42857, n.getY(), EPSILON);
        assertEquals(-0.85714, n.getZ(), EPSILON);
    }

    @Test
    void testFindNormalOnChildObject() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotation_y(Math.PI/2));
        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(1,2,3));
        g1.addObject(g2);
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5,0,0));
        g2.addObject(s);

        Vector n = s.normal_at(new Point(1.7321, 1.1547, -5.5774));
        assertEquals(0.2857, n.getX(), EPSILON);
        assertEquals(0.42854, n.getY(), EPSILON);
        assertEquals(-0.85716, n.getZ(), EPSILON);
    }
}

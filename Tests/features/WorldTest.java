package features;

import features.lights.Light;
import features.lights.PointLight;
import objects.Shape;
import objects.Sphere;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
    @Test
    void testWorldCreation() {
        World w = new World();

        assertEquals(0, w.objectCount());
        assertEquals(0, w.lightCount());
    }

    @Test
    void testDefaultWorldGeneration() {
        Light light = new PointLight(new Point(-10, 10, -10), new Colour(1, 1, 1));
        Shape s1 = new Sphere();
        Material m1 = new Material();
        m1.setColour(new Colour(0.8, 1.0, 0.6));
        m1.setDiffuse(0.7);
        m1.setSpecular(0.2);
        s1.setMaterial(m1);

        Shape s2 = new Sphere();
        s2.setTransform(Matrix.scaling(0.5,0.5,0.5));

        World w = World.defaultWorld();

        ArrayList<Light> lights = w.getLights();
        ArrayList<Shape> objects = w.getObjects();

        assertTrue(lights.contains(light));
        assertTrue(objects.contains(s1));
        assertTrue(objects.contains(s2));
    }

    @Test
    void testIntersectWorldWithRay() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = r.intersect(w);

        assertEquals(4, xs.size());
        assertEquals(4, xs.get(0).getTime());
        assertEquals(4.5, xs.get(1).getTime());
        assertEquals(5.5, xs.get(2).getTime());
        assertEquals(6, xs.get(3).getTime());
    }

    @Test
    void testShadingAnIntersection() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Shape shape = w.getObjects().get(0);
        Intersection i = new Intersection(4, shape);
        Precompute comps = new Precompute(i, r);
        Colour c = w.shadeHit(comps);

        assertEquals(new Colour(0.38066, 0.47583, 0.2855), c);
    }

    @Test
    void testShadingAnIntersectionFromTheInside() {
        World w = World.defaultWorld();
        w.clearLights();
        w.addLight(new PointLight(new Point(0,0.25,0), new Colour(1,1,1)));
        Ray r = new Ray(new Point(0,0,0), new Vector(0,0,1));
        Shape shape = w.getObjects().get(1);
        Intersection i = new Intersection(0.5, shape);
        Precompute comps = new Precompute(i, r);
        Colour c = w.shadeHit(comps);

        assertEquals(new Colour(0.90498, 0.90498, 0.90498), c);
    }

    @Test
    void testTheColourWhenARayMisses() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,1,0));
        Colour c = w.colourAt(r);

        assertEquals(new Colour(0,0,0), c);
    }

    @Test
    void testTheColourWhenARayHits() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Colour c = w.colourAt(r);

        assertEquals(new Colour(0.38066, 0.47583, 0.2855), c);
    }

    @Test
    void testColourWithAnIntersectionBehindTheRay() {
        World w = World.defaultWorld();
        Shape outer = w.getObjects().get(0);
        Material m = outer.getMaterial();
        m.setAmbient(1);
        outer.setMaterial(m);

        Shape inner = w.getObjects().get(1);
        m = inner.getMaterial();
        m.setAmbient(1);
        inner.setMaterial(m);

        Ray r = new Ray(new Point(0,0,0.75), new Vector(0,0,-1));
        Colour c = w.colourAt(r);

        assertEquals(inner.getMaterial().getColour(), c);
    }

    @Test
    void testDefaultWorldXForm() {
        Point from = new Point(0,0,0);
        Point to = new Point(0,0,-1);
        Vector up = new Vector(0,1,0);
        Matrix t = World.view_transform(from, to, up);
        assertEquals(Matrix.identity(4), t);
    }

    @Test
    void testViewXFormLookingInPositiveZ() {
        Point from = new Point(0,0,0);
        Point to = new Point(0,0,1);
        Vector up = new Vector(0,1,0);
        Matrix t = World.view_transform(from, to, up);
        assertEquals(Matrix.scaling(-1, 1, -1), t);
    }

    @Test
    void testViewTransformMovesTheWorld() {
        Point from = new Point(0,0,8);
        Point to = new Point(0,0,0);
        Vector up = new Vector(0,1,0);
        Matrix t = World.view_transform(from, to, up);
        assertEquals(Matrix.translation(0,0,-8),t);
    }

    @Test
    void testArbitraryViewTransform() {
        double[][] vals = {{-0.50709, 0.50709, 0.67612, -2.36643},
            {0.76772, 0.60609, 0.12122, -2.82843},
            {-0.35857, 0.59761, -0.71714, 0.00000},
            {0.00000, 0.00000, 0.00000, 1.00000}};
        Matrix result = new Matrix(vals);

        Point from = new Point(1,3,2);
        Point to = new Point(4,-2,8);
        Vector up = new Vector(1,1,0);
        Matrix t = World.view_transform(from, to, up);
        assertEquals(result.toString(), t.toString());
    }
}

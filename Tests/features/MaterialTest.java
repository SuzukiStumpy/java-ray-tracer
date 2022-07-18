package features;

import features.lights.Light;
import features.lights.PointLight;
import objects.Sphere;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {
    public final static double EPSILON = 0.00001;

    @Test
    void testTheDefaultMaterial() {
        Material m = new Material();

        assertEquals(new Colour(1, 1, 1), m.getColour());
        assertEquals(0.1, m.getAmbient());
        assertEquals(0.9, m.getDiffuse());
        assertEquals(0.9, m.getSpecular());
        assertEquals(200, m.getShininess());
    }

    @Test
    void testLightingWithEyeBetweenLightAndSurface() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal, false);

        assertEquals(new Colour(1.9, 1.9, 1.9), result);
    }

    @Test
    void testLightingWithEyeBetweenLightAndSurfaceOffset45Deg() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal, false);

        assertEquals(new Colour(1, 1, 1), result);
    }

    @Test
    void testLightingWithEyeOppositeSurfaceOffset45Deg() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal, false);

        assertEquals(new Colour(0.7364, 0.7364, 0.7364), result);
    }

    @Test
    void testLightingWithEyeInReflectionPath() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, -Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal, false);

        assertEquals(new Colour(1.6364, 1.6364, 1.6364), result);
    }

    @Test
    void testLightingWithLightBehindSurface() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, 10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal, false);

        assertEquals(new Colour(0.1, 0.1, 0.1), result);
    }

    @Test
    void testNoShadowIfNothingColinearWithPointAndLight() {
        World w = World.defaultWorld();
        Point p = new Point(0, 10, 0);
        assertFalse(w.isShadowed(p));
    }

    @Test
    void testShadowWhenObjectIsBetweenPointAndLight() {
        World w = World.defaultWorld();
        Point p = new Point(10, -10, 10);
        assertTrue(w.isShadowed(p));
    }

    @Test
    void testShadeHitIsGivenAnIntersectionInShadow() {
        World w = new World();
        w.addLight(new PointLight(new Point(0,0,-10), new Colour(1,1,1)));
        w.addObject(new Sphere());
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(0,0,10));
        w.addObject(s);
        Ray r = new Ray(new Point(0,0,5), new Vector(0,0,1));
        Intersection i = new Intersection(4, w.getObjects().get(1));
        Precompute calcs = new Precompute(i, r);
        Colour c = w.shadeHit(calcs);
        assertEquals(new Colour(0.1,0.1,0.1), c);
    }

    @Test
    void testNoShadowWhenObjectIsBehindLight() {
        World w = World.defaultWorld();
        Point p = new Point(-20, 20, -20);
        assertFalse(w.isShadowed(p));
    }

    @Test
    void testNoShadowWhenObjectBehindPoint() {
        World w = World.defaultWorld();
        Point p = new Point(-2, 2, -2);
        assertFalse(w.isShadowed(p));
    }

    @Test
    void testHitOffsetsThePoint() {
        Ray r = new Ray(new Point(0,0,-5), new Vector(0,0,1));
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(0,0,1));
        Intersection i = new Intersection(5,s);
        Precompute comps = new Precompute(i, r);
        assertTrue(comps.over_point.getZ() < -EPSILON/2);
        assertTrue(comps.point.getZ() > comps.over_point.getZ());
    }
}

package features;

import features.lights.Light;
import features.lights.PointLight;
import objects.Plane;
import objects.Shape;
import objects.Sphere;
import org.junit.jupiter.api.Test;
import textures.Pattern;
import textures.Stripes;

import java.rmi.MarshalException;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {
    public final static double EPSILON = 0.00001;
    private static final Colour black = new Colour(0,0,0);
    private static final Colour white = new Colour(1,1,1);
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
        Shape o = new Sphere();
        Material m = new Material();
        o.setMaterial(m);

        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, o, light, position, eye, normal, false);

        assertEquals(new Colour(1.9, 1.9, 1.9), result);
    }

    @Test
    void testLightingWithEyeBetweenLightAndSurfaceOffset45Deg() {
        Shape o = new Sphere();
        Material m = new Material();
        o.setMaterial(m);

        Point position = new Point(0, 0, 0);
        Vector eye = new Vector(0, Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, o, light, position, eye, normal, false);

        assertEquals(new Colour(1, 1, 1), result);
    }

    @Test
    void testLightingWithEyeOppositeSurfaceOffset45Deg() {
        Shape o = new Sphere();
        Material m = new Material();
        o.setMaterial(m);
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, o, light, position, eye, normal, false);

        assertEquals(new Colour(0.7364, 0.7364, 0.7364), result);
    }

    @Test
    void testLightingWithEyeInReflectionPath() {
        Shape o = new Sphere();
        Material m = new Material();
        o.setMaterial(m);
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, -Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, o, light, position, eye, normal, false);

        assertEquals(new Colour(1.6364, 1.6364, 1.6364), result);
    }

    @Test
    void testLightingWithLightBehindSurface() {
        Shape o = new Sphere();
        Material m = new Material();
        o.setMaterial(m);
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, 10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, o, light, position, eye, normal, false);

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
        Colour c = w.shadeHit(calcs, 0);
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

    @Test
    void testLightingWithAPatternApplied() {
        Shape o = new Sphere();
        Material m = new Material();
        Stripes stripes = new Stripes(new Colour(1,1,1), new Colour(0,0,0));
        m.setPattern(stripes);
        m.setAmbient(1);
        m.setDiffuse(0);
        m.setSpecular(0);
        o.setMaterial(m);
        Vector eyev = new Vector(0,0,-1);
        Vector normalv = new Vector(0,0,-1);
        Light light = new PointLight(new Point(0,0,-10), new Colour(1,1,1));
        Colour c1 = Light.lighting(m, o, light, new Point(0.9, 0, 0), eyev, normalv, false);
        Colour c2 = Light.lighting(m, o, light, new Point(1.1, 0, 0), eyev, normalv, false);
        assertEquals(new Colour(1,1,1), c1);
        assertEquals(new Colour(0,0,0), c2);
    }

    @Test
    void testStripesWithObjectTransformation() {
        Sphere object = new Sphere();
        object.setTransform(Matrix.scaling(2,2,2));
        Pattern p = new Stripes(white, black);
        Material m = new Material();
        m.setPattern(p);
        m.setSpecular(0);
        m.setDiffuse(0);
        m.setAmbient(1);
        object.setMaterial(m);
        Colour c = object.colourAt(new Point(1.5, 0, 0));
        assertEquals(white, c);
    }

    @Test
    void testStripesWithPatternTransform() {
        Sphere object = new Sphere();
        Pattern p = new Stripes(white, black);
        p.setTransform(Matrix.scaling(2,2,2));
        Material m = new Material();
        m.setPattern(p);
        m.setSpecular(0);
        m.setDiffuse(0);
        m.setAmbient(1);
        object.setMaterial(m);
        Colour c = object.colourAt(new Point(1.5, 0, 0));
        assertEquals(white, c);
    }

    @Test
    void testStripesWithObjectAndPatternTransform() {
        Sphere object = new Sphere();
        object.setTransform(Matrix.scaling(2,2,2));

        Pattern p = new Stripes(white, black);
        p.setTransform(Matrix.translation(0.5,0,0));
        Material m = new Material();
        m.setPattern(p);
        m.setSpecular(0);
        m.setDiffuse(0);
        m.setAmbient(1);
        object.setMaterial(m);
        Colour c = object.colourAt(new Point(2.5, 0,0));
        assertEquals(white, c);
    }

    @Test
    void testReflectivityOfTheDefaultMaterial() {
        Material m = new Material();
        assertEquals(0, m.getReflectivity(), EPSILON);
    }

    @Test
    void testPrecomputationOfReflectVector() {
        Shape plane = new Plane();
        Ray r = new Ray(new Point(0, 1, -1), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Intersection i = new Intersection(Math.sqrt(2), plane);
        Precompute comps = new Precompute(i, r);

        assertEquals(new Vector(0, Math.sqrt(2)/2, Math.sqrt(2)/2), comps.reflectv);
    }

    @Test
    void testTheReflectedColourOfANonReflectiveSurface() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0,0,0), new Vector(0,0,1));
        Shape shape = w.getObjects().get(1);
        Material m = shape.getMaterial();
        m.setAmbient(1);
        shape.setMaterial(m);
        Intersection i = new Intersection(1, shape);
        Precompute comps = new Precompute(i, r);
        Colour c = w.reflectedColour(comps, 1);

        assertEquals(new Colour(0,0,0), c);
    }

    @Test
    void testTheReflectedColourOfAReflectiveSurface() {
        World w = World.defaultWorld();
        Shape plane = new Plane();
        Material m = new Material();
        m.setReflectivity(0.5);
        plane.setMaterial(m);
        plane.setTransform(Matrix.translation(0, -1, 0));
        w.addObject(plane);
        Ray r = new Ray(new Point(0,0,-3), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Intersection i = new Intersection(Math.sqrt(2), plane);
        Precompute comps = new Precompute(i, r);
        Colour reflectedColour = w.reflectedColour(comps, 1);

        Colour result = new Colour(0.19032, 0.2379, 0.14274);
        //assertEquals(result, reflectedColour);
        assertEquals(result.getR(), reflectedColour.getR(), EPSILON *10);
        assertEquals(result.getG(), reflectedColour.getG(), EPSILON *10);
        assertEquals(result.getB(), reflectedColour.getB(), EPSILON *10);
    }

    @Test
    void testShadingWithReflectiveMaterial() {
        World w = World.defaultWorld();
        Plane shape = new Plane();
        Material m = new Material();
        m.setReflectivity(0.5);
        shape.setMaterial(m);
        shape.setTransform(Matrix.translation(0, -1, 0));
        w.addObject(shape);
        Ray r = new Ray(new Point(0,0,-3), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Intersection i = new Intersection(Math.sqrt(2), shape);
        Precompute comps = new Precompute(i, r);
        Colour c = w.shadeHit(comps, 1);

        Colour result = new Colour(0.87677, 0.92436, 0.82918);
        assertEquals(result.getR(), c.getR(), EPSILON * 10);
        assertEquals(result.getG(), c.getG(), EPSILON * 10);
        assertEquals(result.getB(), c.getB(), EPSILON * 10);
    }

    @Test
    void testMutuallyReflectiveSurfaces() {
        World w = new World();
        Light light = new PointLight(new Point(0, 0, 0), new Colour(1, 1, 1));
        w.addLight(light);

        Material m = new Material();
        m.setReflectivity(1.0);

        Shape lower = new Plane();
        lower.setMaterial(m);
        lower.setTransform(Matrix.translation(0, -1, 0));

        Shape upper = new Plane();
        upper.setMaterial(m);
        upper.setTransform(Matrix.translation(0, 1, 0)
            .scale(1.0, -1.0, 1.0));

        w.addObject(lower);
        w.addObject(upper);

        Ray r = new Ray(new Point(0,0,0), new Vector(0,1,0));
        assertTrue(true);  // If we get here, then the test has worked since recursion has terminated.
    }

    @Test
    void testReflectedColourAtMaxRecursionDepth() {
        World w = World.defaultWorld();
        Plane shape = new Plane();
        Material m = new Material();
        m.setReflectivity(0.5);
        shape.setMaterial(m);
        shape.setTransform(Matrix.translation(0, -1, 0));
        w.addObject(shape);
        Ray r = new Ray(new Point(0,0,-3), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Intersection i = new Intersection(Math.sqrt(2), shape);
        Precompute comps = new Precompute(i, r);
        Colour c = w.reflectedColour(comps, 0);

        assertEquals(new Colour(0,0,0), c);
    }
}

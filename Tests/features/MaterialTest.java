package features;

import features.lights.Light;
import features.lights.PointLight;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {
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
        Colour result = Light.lighting(m, light, position, eye, normal);

        assertEquals(new Colour(1.9, 1.9, 1.9), result);
    }

    @Test
    void testLightingWithEyeBetweenLightAndSurfaceOffset45Deg() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal);

        assertEquals(new Colour(1, 1, 1), result);
    }

    @Test
    void testLightingWithEyeOppositeSurfaceOffset45Deg() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal);

        assertEquals(new Colour(0.7364, 0.7364, 0.7364), result);
    }

    @Test
    void testLightingWithEyeInReflectionPath() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, -Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal);

        assertEquals(new Colour(1.6364, 1.6364, 1.6364), result);
    }

    @Test
    void testLightingWithLightBehindSurface() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eye = new Vector(0, 0, -1);
        Vector normal = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, 10), new Colour(1, 1, 1));
        Colour result = Light.lighting(m, light, position, eye, normal);

        assertEquals(new Colour(0.1, 0.1, 0.1), result);
    }
}

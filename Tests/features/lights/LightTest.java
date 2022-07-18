package features.lights;

import features.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LightTest {
    @Test
    void testPointLightHasPositionAndIntensity() {
        Colour intensity = new Colour(1,1,1);
        Point position = new Point(0, 0, 0);
        PointLight point = new PointLight(position, intensity);

        assertEquals(position, point.getPosition());
        assertEquals(intensity, point.getIntensity());
    }

    @Test
    void testLightingWithTheSurfaceInShadow() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);

        Vector eyev = new Vector(0,0,-1);
        Vector normalv = new Vector(0,0,-1);
        PointLight light = new PointLight(
            new Point(0,0,-10),
            new Colour(1,1,1));
        boolean in_shadow = true;

        Colour result = Light.lighting(m, light, position, eyev, normalv, in_shadow);

        assertEquals(new Colour(0.1,0.1,0.1), result);
    }
}

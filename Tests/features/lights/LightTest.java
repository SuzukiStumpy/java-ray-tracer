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
}

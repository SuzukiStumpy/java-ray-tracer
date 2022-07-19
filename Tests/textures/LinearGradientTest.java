package textures;

import features.Colour;
import features.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearGradientTest {
    public final static Colour white = new Colour(1,1,1);
    public final static Colour black = new Colour(0,0,0);

    @Test
    void testGradientLinearlyInterpolatesBetweenColours() {
        Pattern p = new LinearGradient();
        assertEquals(white, p.colourAt(new Point(0,0,0)));
        assertEquals(new Colour(0.75, 0.75, 0.75), p.colourAt(new Point(0.25,0,0)));
        assertEquals(new Colour(0.5,0.5,0.5), p.colourAt(new Point(0.5, 0,0)));
        assertEquals(new Colour(0.25,0.25,0.25), p.colourAt(new Point(0.75,0,0)));
    }
}

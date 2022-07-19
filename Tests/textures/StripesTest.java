package textures;

import features.Colour;
import features.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StripesTest {
    private static final Colour black = new Colour(0,0,0);
    private static final Colour white = new Colour(1,1,1);

    @Test
    void testCreatingAStripePattern() {
        Stripes s = new Stripes(white, black);

        assertEquals(white, s.getColour(0));
        assertEquals(black, s.getColour(1));
    }

    @Test
    void testStripePatternIsConstantInY() {
        Stripes s = new Stripes(white, black);
        assertEquals(white, s.colourAt(new Point(0,0,0)));
        assertEquals(white, s.colourAt(new Point( 0, 1, 0)));
        assertEquals(white, s.colourAt(new Point(0, 2, 0)));
    }

    @Test
    void testStripePatternIsConstantInZ() {
        Stripes s = new Stripes(white, black);
        assertEquals(white, s.colourAt(new Point(0, 0, 0)));
        assertEquals(white, s.colourAt(new Point(0, 0, 1)));
        assertEquals(white, s.colourAt(new Point(0, 0, 2)));
    }

    @Test
    void testStripePatternAlternatesInX() {
        Stripes s= new Stripes(white, black);
        assertEquals(white, s.colourAt(new Point(0, 0, 0)));
        assertEquals(white, s.colourAt(new Point(0.9, 0, 0)));
        assertEquals(black, s.colourAt(new Point(1, 0, 0)));
        assertEquals(black, s.colourAt(new Point(-0.1, 0, 0)));
        assertEquals(black, s.colourAt(new Point(-1, 0, 0)));
        assertEquals(white, s.colourAt(new Point(-1.1, 0, 0)));
    }
}

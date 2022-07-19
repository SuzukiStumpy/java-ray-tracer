package textures;

import features.Colour;
import features.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTextureTest {
    public final static Colour white = new Colour(1,1,1);
    public final static Colour black = new Colour(0,0,0);

    @Test
    void testCheckersRepeatInX() {
        Pattern p = new CheckerTexture();
        assertEquals(white, p.colourAt(new Point(0,0,0)));
        assertEquals(white, p.colourAt(new Point(0.99, 0,0)));
        assertEquals(black, p.colourAt(new Point(1.01, 0, 0)));
    }

    @Test
    void testCheckerRepeatInY() {
        Pattern p = new CheckerTexture();
        assertEquals(white, p.colourAt(new Point(0,0,0)));
        assertEquals(white, p.colourAt(new Point(0, 0.99,0)));
        assertEquals(black, p.colourAt(new Point(0, 1.01, 0)));
    }

    @Test
    void testCheckerRepeatInZ() {
        Pattern p = new CheckerTexture();
        assertEquals(white, p.colourAt(new Point(0,0,0)));
        assertEquals(white, p.colourAt(new Point(0, 0,0.99)));
        assertEquals(black, p.colourAt(new Point(0, 0, 1.01)));
    }

}

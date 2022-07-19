package textures;

import features.Colour;
import features.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RingTextureTest {
    public final static Colour white = new Colour(1,1,1);
    public final static Colour black = new Colour(0,0,0);

    @Test
    void testARingExtendsInBothXAndZ() {
        Pattern p = new RingTexture(white, black);
        assertEquals(white, p.colourAt(new Point(0, 0, 0)));
        assertEquals(black, p.colourAt(new Point(1, 0, 0)));
        assertEquals(black, p.colourAt(new Point(0, 0, 1)));
        assertEquals(black, p.colourAt(new Point(0.708,0,0.708)));
    }
}

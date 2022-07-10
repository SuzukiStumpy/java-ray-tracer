package features;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CanvasTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCanvasCreation() {
        Canvas c = new Canvas(10, 20);

        assertEquals(10, c.getWidth());
        assertEquals(20, c.getHeight());

        Colour black = new Colour(0,0,0);

        for(Colour pixel: c.getAllPixels()) {
            assertEquals(black, pixel);
        }
    }

    @Test
    void testWriteToCanvas() {
        Canvas c = new Canvas(10, 20);
        Colour red = new Colour(1,0,0);
        c.setPixel(2, 3, red);

        assertEquals(red, c.getPixel(2,3));
    }

    @Test
    void testGeneratePPMHeader() {
        String validHeader = "P3\n5 3\n255\n";
        Canvas c = new Canvas(5, 3);
        ArrayList<String> ppm = PPMWriter.canvas_to_ppm(c);
        assertTrue(ppm.size() >= 3);  // Make sure that the string is at least generated correctly...

        // Get each of the lines from the PPM ArrayList and drop them into a single
        // String (newline separated)
        StringBuilder ppmHeader = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            ppmHeader.append(ppm.get(i)).append('\n');
        }

        assertEquals(validHeader, ppmHeader.toString());
    }

    @Test
    void testGeneratePPMData() {
        String validData =
            "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0\n" +
            "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255\n";

        Canvas c = new Canvas(5,3);
        Colour c1 = new Colour(1.5, 0, 0);
        Colour c2 = new Colour(0, 0.5, 0);
        Colour c3 = new Colour(-0.5, 0, 1);

        c.setPixel(0, 0, c1);
        c.setPixel(2,1,c2);
        c.setPixel(4,2,c3);

        ArrayList<String> ppm = PPMWriter.canvas_to_ppm(c);
        assertTrue(ppm.size() >= 3);

        // Get the data for each of the lines from the PPM arraylist and drop them
        // into a single string (newline separated)
        StringBuilder ppmData = new StringBuilder();
        for (int i =3; i < ppm.size(); i++) {
            ppmData.append(ppm.get(i)).append("\n");
        }

        assertEquals(validData, ppmData.toString());
    }

    @Test
    void testNoLinesInPPMFileCanBeMoreThan70Characters() {
        Canvas c = new Canvas(10, 2);
        c.setAllPixels(new Colour(1, 0.8, 0.8));
        ArrayList<String> ppm = PPMWriter.canvas_to_ppm(c);
        assertTrue(ppm.size() >= 7);

        for (String line: ppm) {
            assertTrue(line.length() <= 70);
        }
    }
}

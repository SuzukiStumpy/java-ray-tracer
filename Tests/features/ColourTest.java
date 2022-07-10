package features;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColourTest {
    static final double EPSILON = 0.00001;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testColoursAreRegularTuples() {
        Colour c = new Colour(-0.5, 0.4, 1.7);
        assertEquals(-0.5, c.getR(), EPSILON);
        assertEquals(0.4, c.getG(), EPSILON);
        assertEquals(1.7, c.getB(), EPSILON);
    }

    @Test
    void testAddingColours() {
        Colour c1 = new Colour(0.9, 0.6, 0.75);
        Colour c2 = new Colour(0.7,0.1,0.25);
        Colour result = c1.add(c2);

        assertEquals(new Colour(1.6, 0.7, 1.0), result);
    }

    @Test
    void testSubtractingColours() {
        Colour c1 = new Colour(0.9, 0.6, 0.75);
        Colour c2 = new Colour(0.7,0.1,0.25);
        Colour result = c1.subtract(c2);

        assertEquals(new Colour(0.2, 0.5, 0.5), result);
    }

    @Test
    void testScalarMultiplyingColours() {
        Colour c1 = new Colour(0.2, 0.3, 0.4);
        Colour result = c1.multiply(2);

        assertEquals(new Colour(0.4, 0.6, 0.8), result);
    }

    @Test
    void testColourBlending() {
        Colour c1 = new Colour(1, 0.2, 0.4);
        Colour c2 = new Colour(0.9, 1, 0.1);
        Colour result = c1.multiply(c2);

        assertEquals(new Colour(0.9, 0.2, 0.04), result);
    }
}

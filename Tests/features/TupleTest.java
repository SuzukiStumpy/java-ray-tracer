package features;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {
    static final double EPSILON = 0.00001;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testPointComponentValues() {
        Point p = new Point(4.3, -4.2, 3.1);

        assertEquals(4.3, p.getX(), EPSILON);
        assertEquals(-4.2, p.getY(), EPSILON);
        assertEquals(3.1, p.getZ(), EPSILON);
        assertEquals(1.0, p.getW(), EPSILON);
    }

    @Test
    void testVectorComponentValues() {
        Vector v = new Vector(4.3, -4.2, 3.1);

        assertEquals(4.3, v.getX(), EPSILON);
        assertEquals(-4.2, v.getY(), EPSILON);
        assertEquals(3.1, v.getZ(), EPSILON);
        assertEquals(0.0, v.getW(), EPSILON);
    }

    @Test
    void testAddTuples() {
        Tuple a = new Tuple(3, -2, 5, 1);
        Tuple b = new Tuple(-2, 3, 1, 0);
        Tuple result = a.add(b);

        assertEquals(new Tuple(1, 1, 6, 1), result);
    }

    @Test
    void testSubtractPoints() {
        Point p1 = new Point(3,2,1);
        Point p2 = new Point(5,6,7);
        Vector result = p1.subtract(p2);

        assertEquals(new Vector(-2,-4,-6), result);
    }

    @Test
    void testSubtractVectorFromPoint() {
        Point p = new Point(3,2,1);
        Vector v = new Vector(5,6,7);
        Point result = p.subtract(v);

        assertEquals(new Point(-2,-4,-6), result);
    }

    @Test
    void testSubtractTwoVectors() {
        Vector v1 = new Vector(3,2,1);
        Vector v2 = new Vector(5,6,7);
        Vector result = v1.subtract(v2);

        assertEquals(new Vector(-2,-4,-6), result);
    }

    @Test
    void testNegation() {
        Tuple a = new Tuple(1,-2,3,-4);
        assertEquals(a.negate(), new Tuple(-1,2,-3,4));
    }

    @Test
    void testScalarMultiply() {
        Tuple a = new Tuple(1,-2,3,-4);
        assertEquals(a.multiply(3.5), new Tuple(3.5, -7,10.5,-14));
    }

    @Test
    void testScalarFractionMultiply() {
        Tuple a = new Tuple(1,-2,3,-4);
        assertEquals(a.multiply(0.5), new Tuple(0.5,-1,1.5,-2));
    }

    @Test
    void testScalarDivision() {
        Tuple a=  new Tuple(1,-2,3,-4);
        assertEquals(a.divide(2), new Tuple(0.5,-1,1.5,-2));
    }

    @Test
    void testMagnitude() {
        Vector v1 = new Vector(1,0,0);
        assertEquals(1, v1.magnitude(), EPSILON);

        Vector v2 = new Vector(0,1,0);
        assertEquals(1, v2.magnitude(), EPSILON);

        Vector v3 = new Vector(0,0,1);
        assertEquals(1, v3.magnitude(), EPSILON);

        Vector v4 = new Vector(1,2,3);
        assertEquals(Math.sqrt(14), v4.magnitude(), EPSILON);

        Vector v5 = new Vector(-1,-2,-3);
        assertEquals(Math.sqrt(14), v5.magnitude(), EPSILON);
    }

    @Test
    void testNormalizing() {
        Vector v1 = new Vector(4,0,0);
        assertEquals(v1.normalize(), new Vector(1,0,0));

        Vector v2 = new Vector(1,2,3);
        assertEquals(v2.normalize(), new Vector(0.26726, 0.53452, 0.80178));
    }

    @Test
    void testMagnitudeOfNormalizedVector() {
        Vector v = new Vector(1,2,3);
        assertEquals(1.0, v.normalize().magnitude(), EPSILON);
    }

    @Test
    void testDotProduct() {
        Vector a = new Vector(1,2,3);
        Vector b = new Vector(2,3,4);
        assertEquals(20, a.dot(b), EPSILON);
    }

    @Test
    void testCrossProduct() {
        Vector a = new Vector(1,2,3);
        Vector b = new Vector(2,3,4);
        assertEquals(new Vector(-1,2,-1), a.cross(b));
        assertEquals(new Vector(1,-2,1), b.cross(a));
    }

    @Test
    void testReflectingAVectorApproachingAt45Degrees() {
        Vector v = new Vector(1, -1, 0);
        Vector n = new Vector(0, 1, 0);
        Vector r = v.reflect(n);

        assertEquals(new Vector(1, 1, 0), r);
    }

    @Test
    void testReflectingAVectorOffASlantedSurface() {
        Vector v = new Vector(0, -1, 0);
        Vector n = new Vector(Math.sqrt(2)/2, Math.sqrt(2)/2, 0);
        Vector r = v.reflect(n);

        assertEquals(new Vector(1, 0, 0), r);
    }
}

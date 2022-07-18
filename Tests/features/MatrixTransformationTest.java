package features;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.spi.CharsetProvider;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTransformationTest {
    static final double EPSILON = 0.00001;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testTranslation() {
        Matrix t = Matrix.translation(5, -3, 2);
        Point p = new Point(-3, 4, 5);

        Point result = t.multiply(p).toPoint();

        assertEquals(new Point(2,1,7), result);
    }

    @Test
    void testTranslationInverse() {
        Matrix t = Matrix.translation(5, -3, 2);
        Matrix inv = t.inverse();
        Point p = new Point(-3, 4, 5);
        Point result = inv.multiply(p).toPoint();

        assertEquals(new Point(-8,7,3), result);
    }

    @Test
    void testTranslationDoesNotAffectVectors() {
        Matrix t = Matrix.translation(5, -3, 2);
        Vector v = new Vector(-3, 4, 5);
        Vector result = t.multiply(v).toVector();

        assertEquals(v, result);
    }

    @Test
    void testScaleAPoint() {
        Matrix t = Matrix.scaling(2, 3, 4);
        Point p = new Point(-4, 6, 8);
        Point result = t.multiply(p).toPoint();

        assertEquals(new Point(-8, 18, 32), result);
    }

    @Test
    void testScaleAVector() {
        Matrix t = Matrix.scaling(2, 3, 4);
        Vector v = new Vector(-4, 6, 8);
        Vector result = t.multiply(v).toVector();

        assertEquals(new Vector(-8, 18, 32), result);
    }

    @Test
    void testScaleByInverse() {
        Matrix t = Matrix.scaling(2, 3, 4);
        Matrix inv = t.inverse();
        Vector v = new Vector(-4, 6, 8);
        Vector result = inv.multiply(v).toVector();

        assertEquals(new Vector(-2, 2, 2), result);
    }

    @Test
    void testScaleByNegativeValue() {
        Matrix t = Matrix.scaling(-1, 1, 1);
        Point p = new Point(2, 3, 4);
        Point result = t.multiply(p).toPoint();

        assertEquals(new Point(-2, 3, 4), result);
    }

    @Test
    void testRotatePointAroundXAxis() {
        Point p = new Point(0, 1, 0);
        Matrix halfQtr = Matrix.rotation_x(Math.PI / 4);
        Matrix fullQtr = Matrix.rotation_x(Math.PI / 2);

        assertEquals(new Point(0, Math.sqrt(2)/2, Math.sqrt(2)/2), halfQtr.multiply(p).toPoint());
        assertEquals(new Point(0,0,1), fullQtr.multiply(p).toPoint());
    }

    @Test
    void testInverseXRotationRotatesInOppositeDirection() {
        Point p = new Point(0,1,0);
        Matrix halfQtr = Matrix.rotation_x(Math.PI / 4);
        Matrix inv = halfQtr.inverse();

        assertEquals(new Point(0, Math.sqrt(2)/2, -Math.sqrt(2)/2), inv.multiply(p).toPoint());
    }

    @Test
    void testRotatePointAroundYAxis() {
        Point p = new Point(0, 0, 1);
        Matrix halfQtr = Matrix.rotation_y(Math.PI / 4);
        Matrix fullQtr = Matrix.rotation_y(Math.PI / 2);

        assertEquals(new Point(Math.sqrt(2)/2, 0, Math.sqrt(2)/2), halfQtr.multiply(p).toPoint());
        assertEquals(new Point(1,0,0), fullQtr.multiply(p).toPoint());
    }

    @Test
    void testRotatePointAroundZAxis() {
        Point p = new Point(0, 1, 0);
        Matrix halfQtr = Matrix.rotation_z(Math.PI / 4);
        Matrix fullQtr = Matrix.rotation_z(Math.PI / 2);

        assertEquals(new Point(-Math.sqrt(2)/2, Math.sqrt(2)/2, 0), halfQtr.multiply(p).toPoint());
        assertEquals(new Point(-1,0,0), fullQtr.multiply(p).toPoint());
    }

    @Test
    void testShearXInProportionToY() {
        Matrix t = Matrix.shearing(1, 0, 0, 0, 0, 0);
        Point p = new Point(2, 3, 4);
        assertEquals(new Point(5,3,4), t.multiply(p).toPoint());
    }

    @Test
    void testShearXInProportionToZ() {
        Matrix t = Matrix.shearing(0, 1, 0, 0, 0, 0);
        Point p = new Point(2,3,4);
        assertEquals(new Point(6,3,4), t.multiply(p).toPoint());
    }

    @Test
    void testShearYInProportionToX() {
        Matrix t = Matrix.shearing(0, 0, 1, 0, 0, 0);
        Point p = new Point(2,3,4);
        assertEquals(new Point(2,5,4), t.multiply(p).toPoint());
    }

    @Test
    void testShearYInProportionToZ() {
        Matrix t = Matrix.shearing(0, 0, 0, 1, 0, 0);
        Point p = new Point(2,3,4);
        assertEquals(new Point(2,7,4), t.multiply(p).toPoint());
    }

    @Test
    void testShearZInProportionToX() {
        Matrix t = Matrix.shearing(0, 0, 0, 0, 1, 0);
        Point p = new Point(2,3,4);
        assertEquals(new Point(2,3,6), t.multiply(p).toPoint());
    }

    @Test
    void testShearZInProportionToY() {
        Matrix t = Matrix.shearing(0, 0, 0, 0, 0, 1);
        Point p = new Point(2,3,4);
        assertEquals(new Point(2,3,7), t.multiply(p).toPoint());
    }

    @Test
    void testIndividualTransformationsAppliedInSequence() {
        Point p = new Point(1,0,1);
        Matrix A = Matrix.rotation_x(Math.PI / 2);
        Matrix B = Matrix.scaling(5,5,5);
        Matrix C = Matrix.translation(10, 5, 7);

        // Apply individually: Rotate, Scale, Translate
        Point p2 = A.multiply(p).toPoint();
        assertEquals(new Point(1,-1,0), p2);

        Point p3 = B.multiply(p2).toPoint();
        assertEquals(new Point(5, -5, 0), p3);

        Point p4 = C.multiply(p3).toPoint();
        assertEquals(new Point(15, 0, 7), p4);
    }

    @Test
    void testChainedTransformationsAppliedInReverseSequence() {
        Point p = new Point(1, 0, 1);
        Matrix A = Matrix.rotation_x(Math.PI / 2);
        Matrix B = Matrix.scaling(5,5,5);
        Matrix C = Matrix.translation(10, 5, 7);
        Matrix t = C.multiply(B).multiply(A);

        assertEquals(new Point(15,0,7), t.multiply(p).toPoint());
    }

    @Test
    void testChainedTranslationsUsingFullyFluentAPI() {
        Point p = new Point(1, 0, 1);
        Matrix t = Matrix.identity(4)
            .translate(10, 5, 7)
            .scale(5, 5, 5)
            .rotate_x(Math.PI / 2);

        assertEquals(new Point(15, 0, 7), t.multiply(p).toPoint());
    }
}

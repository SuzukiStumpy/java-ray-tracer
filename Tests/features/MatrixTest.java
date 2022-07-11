package features;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.MarshalException;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
    static final double EPSILON = 0.00001;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testConstructionOf4x4Matrix() throws IllegalAccessException {
        double[][] values = {
            {1,2,3,4},
            {5.5,6.5,7.5,8.5},
            {9,10,11,12},
            {13.5,14.5,15.5,16.5}
        };

        Matrix m = new Matrix(values);

        assertEquals(1, m.getElement(0,0), EPSILON);
        assertEquals(4, m.getElement(0,3), EPSILON);
        assertEquals(5.5, m.getElement(1,0), EPSILON);
        assertEquals(7.5, m.getElement(1,2), EPSILON);
        assertEquals(11, m.getElement(2,2), EPSILON);
        assertEquals(13.5, m.getElement(3,0), EPSILON);
        assertEquals(15.5, m.getElement(3,2), EPSILON);
    }

    @Test
    void testConstructionOf2x2Matrix() throws IllegalAccessException {
        double[][] values = {
            {-3, 5},
            {1,-2}
        };

        Matrix m = new Matrix(values);

        assertEquals(-3, m.getElement(0,0), EPSILON);
        assertEquals(5, m.getElement(0,1), EPSILON);
        assertEquals(1, m.getElement(1,0), EPSILON);
        assertEquals(-2, m.getElement(1,1), EPSILON);
    }

    @Test
    void testConstructionOf3x3Matrix() throws IllegalAccessException {
        double[][] values = {
            {-3, 5, 0},
            {1, -2, -7},
            {0, 1, 1}
        };

        Matrix m = new Matrix(values);

        assertEquals(-3, m.getElement(0,0), EPSILON);
        assertEquals(-2, m.getElement(1,1), EPSILON);
        assertEquals(1, m.getElement(2,2), EPSILON);
    }

    @Test
    void testMatrixEqualityWithIdenticalMatrices() {
        double[][] values = {{1,2,3,4},{5,6,7,8},{9,8,7,6},{5,4,3,2}};
        Matrix a = new Matrix(values);
        Matrix b = new Matrix(values);

        assertEquals(b, a);
    }

    @Test
    void testInequalityWithDifferentMatrices() {
        double[][] v1 = {{1,2,3,4},{5,6,7,8},{9,8,7,6},{5,4,3,2}};
        double[][] v2 ={{2,3,4, 5},{6,7,8, 9},{8,7,6,5},{4,3,2,1}};
        Matrix a = new Matrix(v1);
        Matrix b = new Matrix(v2);

        assertNotEquals(a, b);
    }

    @Test
    void testMatrixMultiplication() {
        double[][] v1 = {{1,2,3,4},{5,6,7,8},{9,8,7,6},{5,4,3,2}};
        double[][] v2 = {{-2,1,2,3},{3,2,1,-1},{4,3,6,5},{1,2,7,8}};
        double[][] rv = {{20,22,50,48},{44,54,114,108},{40,58,110,102},{16,26,46,42}};
        Matrix a = new Matrix(v1);
        Matrix b = new Matrix(v2);
        Matrix r = a.multiply(b);

        assertEquals(r, new Matrix(rv));
    }

    @Test
    void testMatrixMultipliedByTuple() {
        double[][] v = {{1,2,3,4},{2,4,4,2},{8,6,4,1},{0,0,0,1}};
        Matrix a = new Matrix(v);
        Tuple b = new Tuple(1,2,3,1);

        Tuple result = a.multiply(b);

        assertEquals(new Tuple(18,24,33,1), result);
    }

    @Test
    void testMultiplyMatrixByIdentity() {
        double[][] v = {{0,1,2,4}, {1, 2, 4, 8}, {2, 4, 8, 16}, {4, 8, 16, 32}};
        Matrix a = new Matrix(v);
        Matrix result = a.multiply(Matrix.identity(4));

        assertEquals(a, result);
    }

    @Test
    void testMultiplyIdentityMatrixByTuple() {
        Matrix i = Matrix.identity(4);
        Tuple a = new Tuple(1,2,3,4);

        assertEquals(a, i.multiply(a));
    }

    @Test
    void testMatrixTransposition() {
        double[][] values = {{0,9,3,0},{9,8,0,8},{1,8,5,3},{0,0,5,8}};
        double[][] trVals = {{0,9,1,0},{9,8,8,0},{3,0,5,5},{0,8,3,8}};

        Matrix a = new Matrix(values).transpose();
        Matrix b = new Matrix(trVals);

        assertEquals(b, a);
    }

    @Test
    void testTransposingIdentityMatrix() {
        Matrix a = Matrix.identity(4);

        assertEquals(a, a.transpose());
    }

    @Test
    void testDeterminantOf2x2Matrix() {
        double[][] vals = {{1,5},{-3,2}};
        Matrix a = new Matrix(vals);

        assertEquals(17, a.det());
    }

    @Test
    void testSubmatrixOf3x3Is2x2() {
        double[][] vals = {{1,5,0}, {-3,2,7},{0,6,-3}};
        double[][] resultVals = {{-3,2}, {0,6}};
        Matrix a = new Matrix(vals);
        Matrix expected = new Matrix(resultVals);
        Matrix result = a.submatrix(0,2);
        assertEquals(expected, result);

    }

    @Test
    void testSubmatrixOf4x4Is3x3() {
        double[][] vals = {{-6,1,1,6},{-8,5,8,6},{-1,0,8,2},{-7,1,-1,1}};
        double[][] resultVals = {{-6,1,6},{-8,8,6},{-7,-1,1}};
        Matrix a = new Matrix(vals);
        Matrix expected = new Matrix(resultVals);
        Matrix result = a.submatrix(2, 1);
        assertEquals(expected, result);
    }

    @Test
    void testCalculationOfMinorOf3x3Matrix() {
        double[][] vals = {{3, 5, 0}, {2, -1, -7}, {6, -1, 5}};
        Matrix a = new Matrix(vals);
        Matrix b = a.submatrix(1, 0);
        double detB = b.det();
        double minA = a.minor(1, 0);

        assertEquals(25, detB);
        assertEquals(25, minA);
    }

    @Test
    void testCalculateCofactorOf3x3Matrix() {
        double[][] vals = {{3, 5, 0}, {2, -1, -7}, {6, -1, 5}};
        Matrix a = new Matrix(vals);
        double minorA = a.minor(0,0);
        double cofA = a.cofactor(0,0);
        double minorA2 = a.minor(1,0);
        double cofA2 = a.cofactor(1,0);

        assertEquals(-12, minorA);
        assertEquals(-12, cofA);
        assertEquals(25, minorA2);
        assertEquals(-25, cofA2);
    }

    @Test
    void testCalculatingDeterminantOf3x3Matrix() {
        double[][] vals = {{1,2,6},{-5,8,-4},{2,6,4}};
        Matrix a = new Matrix(vals);
        assertEquals(56, a.cofactor(0,0));
        assertEquals(12, a.cofactor(0,1));
        assertEquals(-46, a.cofactor(0,2));
        assertEquals(-196, a.det());
    }

    @Test
    void testCalculatingDeterminantOf4x4Matrix() {
        double[][] vals = {{-2,-8,3,5},{-3,1,7,3},{1,2,-9,6},{-6,7,7,-9}};
        Matrix a = new Matrix(vals);
        assertEquals(690, a.cofactor(0,0));
        assertEquals(447, a.cofactor(0,1));
        assertEquals(210, a.cofactor(0,2));
        assertEquals(51, a.cofactor(0,3));
        assertEquals(-4071, a.det());
    }

    @Test
    void testInvertibleMatrixForInvertibility() {
        double[][] vals = {{6,4,4,4},{5,5,7,6},{4,-9,3,-7},{9,1,7,-6}};
        Matrix a = new Matrix(vals);
        assertEquals(-2120, a.det());
        assertTrue(a.isInvertible());
    }

    @Test
    void testNonInvertibleMatrixForInvertibility() {
        double[][] vals = {{-4,2,-2,-3},{9,6,2,6},{0,-5,1,-5},{0,0,0,0}};
        Matrix a = new Matrix(vals);
        assertEquals(0, a.det());
        assertFalse(a.isInvertible());
    }

    @Test
    void testCalculateInverseOfMatrix() {
        try {
            double[][] vals = {{-5, 2, 6, -8}, {1, -5, 1, 8}, {7, 7, -6, -7}, {1, -3, 7, 4}};
            double[][] results = {{0.21805, 0.45113, 0.24060, -0.04511}, {-0.80827, -1.45677, -0.44361, 0.52068},
                {-0.07895,-0.22368,-0.05263,0.19737},{-0.52256,-0.81391,-0.30075,0.30639}};
            Matrix expected = new Matrix(results);
            Matrix a = new Matrix(vals);
            Matrix b = a.inverse();

            assertEquals(532, a.det());
            assertEquals(-160, a.cofactor(2, 3));
            assertEquals(-160.0 / 532.0, b.getElement(3, 2));
            assertEquals(105, a.cofactor(3,2));
            assertEquals(105.0/532.0, b.getElement(2,3));

            // Need to test with toString() here since internal representation is far higher precision
            // than the results values entered above ...
            assertEquals(expected.toString(), b.toString());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testCalculateInverseOfAnotherMatrix() {
        double[][] vals = {{8,-5,9,2},{7,5,6,1},{-6,0,9,6},{-3,0,-9,-4}};
        double[][] results = {{-0.15385,-0.15385,-0.28205,-0.53846},{-0.07692,0.12308,0.02564,0.03077},
            {0.35897,0.35897,0.43590,0.92308},{-0.69231,-0.69231,-0.76923,-1.92308}};

        Matrix expected = new Matrix(results);
        Matrix a = new Matrix(vals);
        Matrix b = a.inverse();

        assertEquals(expected.toString(), b.toString());
    }

    @Test
    void testThirdInversionTest() {
        double[][] vals = {{9,3,0,9},{-5,-2,-6,-3},{-4,9,6,4},{-7,6,6,2}};
        double[][] results = {{-0.04074,-0.07778,0.14444,-0.22222},{-0.07778,0.03333,0.36667,-0.33333},
            {-0.02901,-0.14630,-0.10926,0.12963},{0.17778,0.06667,-0.26667,0.33333}};

        Matrix expected = new Matrix(results);
        Matrix a = new Matrix(vals);
        Matrix b = a.inverse();

        assertEquals(expected.toString(), b.toString());
    }

    @Test
    void testMultiplyAProductByItsInverse() {
        double[][] vals1 = {{3,-9,7,3},{3,-8,2,-9},{-4,4,4,1},{-6,5,-1,1}};
        double[][] vals2 = {{8,2,2,2},{3,-1,7,0},{7,0,5,4},{6,-2,0,5}};
        Matrix a = new Matrix(vals1);
        Matrix b = new Matrix(vals2);
        Matrix c = a.multiply(b);

        assertEquals(a.toString(), c.multiply(b.inverse()).toString());
    }
}

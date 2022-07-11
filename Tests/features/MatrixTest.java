package features;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}

package features;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class to implement a square matrix and the operations upon it.
 *
 * @author Mark Edwards
 * @version July 7th, 2022
 */
public class Matrix {
    // Matrix data is held internally in an array.
    private final double[] matrix;
    private final int degree;

    /**
     * Construct a new, empty matrix (all elements set to 0.0)
     * @param degree The size of the matrix (matrices are square, so matrix of degree
     *               2 is a 2x2 matrix.  2 is the smallest allowable degree.
     * @throws IndexOutOfBoundsException If degree is less than 2, then throws this exception.
     */
    public Matrix(int degree) throws IndexOutOfBoundsException {
        if (degree < 2) {
            throw new IndexOutOfBoundsException("A matrix should have a minimum degree of 2!");
        }

        this.degree = degree;
        this.matrix = new double[degree * degree];
    }

    /**
     * Construct a new matrix with the supplied values
     * @param values The values to set into the matrix.  Should be a square array of doubles.
     * @throws RuntimeException If the values array isn't square, then throws a runtime exception
     */
    public Matrix(double[][] values) throws RuntimeException {
        int rows = values.length;
        int cols = values[0].length;

        if (rows != cols) {
            throw new RuntimeException("Attempt to generate a matrix with incorrect values.\nThere should be equal numbers of elements in row and columns");
        }

        degree = rows;
        matrix = new double[degree * degree];

        for (int row = 0; row < degree; row++) {
            for (int col = 0; col < degree; col++) {
                int idx = getElementIndex(row, col);
                matrix[idx] = values[row][col];
            }
        }
    }

    /**
     * Internal function.  Maps an element position of [row][col] to the internal index.
     * @param x The row of the element we're referencing
     * @param y The column of the element we're referencing
     * @return The index into the internal array.
     * @throws IndexOutOfBoundsException We tried to access a non-existant element
     */
    private int getElementIndex(int x, int y) throws IndexOutOfBoundsException {
        if (x < 0 || x > degree || y < 0 || y > degree) {
            throw new IndexOutOfBoundsException("Degree is of degree "+ degree + ".  Index ("+ x +", "+ y +") is out of bounds");
        }
        return (x * degree) + y;
    }

    /**
     * Sets the element at position [x][y] to the specified value.  If a position
     * is referenced outside of the matrix, then an error is printed to stderr
     * and the operation is ignored
     * @param x The row of the element we wish to set
     * @param y The column of the element we wish to set
     * @param value The value we wish to set
     */
    public void setElement(int x, int y, double value) {
        try {
            int index = getElementIndex(x, y);
            matrix[index] = value;
        }
        catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @return The degree of this matrix (since all matrices are square, then only this one dimension is needed.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * @return The matrix represented as a printable string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat fmt = new DecimalFormat("0.00000");

        for (int row = 0; row < degree; row++){
            sb.append("| ");
            for (int col = 0; col < degree; col++) {
                sb.append(fmt.format(matrix[getElementIndex(row, col)])).append(" | ");
            }
            sb.append("\n");
        }
        return  sb.toString();
    }

    /**
     * Get the value stored in the matrix at position [x][y].  If an attempt is
     * made to access an out of bounds element, then an IllegalAccessException is thrown
     * @param x The row of the element we wish to access
     * @param y The column of the element we wish to access
     * @return The value stored at the position
     * @throws IllegalAccessException An attempt was made to access a non-existent element
     */
    public double getElement(int x, int y) throws IllegalAccessException {
        try {
            int index = getElementIndex(x, y);
            return matrix[index];
        }
        catch (IndexOutOfBoundsException e) {
            String errorMsg = "Illegal operation on Matrix element reference! " + e.getMessage();
            throw new IllegalAccessException(errorMsg);
        }
    }

    /**
     * Test two matrices for equality
     * @param o The other matrix we want to check
     * @return true if both matrices are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix m = (Matrix) o;
        return degree == m.degree && Arrays.equals(matrix, m.matrix);
    }

    /**
     * @return The hash code for this particular matrix
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(degree);
        result = 31 * result + Arrays.hashCode(matrix);
        return result;
    }

    /**
     * Multiply two matrices together.
     * @throws RuntimeException if the two matrices aren't of the same degree (since we only deal with square matrices)
     * @param b The 'other' matrix
     * @return The result of the multiplication - or null if we had an error...
     */
    public Matrix multiply(@NotNull Matrix b) {
        if (degree != b.getDegree()) {
            throw new RuntimeException("Cannot multiply, matrices are of different dimension!");
        }

        try {
            Matrix m = new Matrix(degree);  // Generates a fresh, empty matrix
            // Iterate over the rows and columns of the destination matrix...
            for (int row = 0; row < degree; row++) {
                for (int col = 0; col < degree; col++) {
                    double cellResult = 0;

                    for (int iter = 0; iter < degree; iter++) {
                        double aElement = this.getElement(row, iter);
                        double bElement = b.getElement(iter, col);
                        cellResult += aElement * bElement;
                    }

                    m.setElement(row, col, cellResult);
                }
            }
            return m;
        }
        catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Multiply a matrix by a tuple
     * @param t The tuple we want to multiply by
     * @return The resultant tuple
     * @throws RuntimeException If we've attempted to multiply on an illegal matrix/tuple combination
     */
    public Tuple multiply(@NotNull Tuple t) throws RuntimeException {
        if (this.degree != 4) {
            throw new RuntimeException("Matrix/Tuple multiplication only works on Matrices of degree 4");
        }
        try {
            Tuple result = new Tuple(0,0,0,0);

            for (int row = 0; row < degree; row++) {
                double cellResult = 0;

                for (int col = 0; col < degree; col++) {
                    cellResult += t.getElementByIndex(col) * this.getElement(row, col);
                }

                result.setElementByIndex(row, cellResult);
            }
            return result;
        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException("Illegal access attempted to matrix/tuple element.  "+ ex.getMessage());
        }
    }

    /**
     * @return The transposed matrix (ie: row/col swapped with col/row)
     */
    public Matrix transpose() {
        Matrix m = new Matrix(degree);

        try {
            for (int row = 0; row < degree; row++) {
                for (int col = 0; col < degree; col++) {
                    m.setElement(row, col, this.getElement(col, row));
                }
            }
            return m;
        }
        catch (IllegalAccessException ex) {
            // Shouldn't get here...
            System.err.println("Somehow got an IllegalAccessException during matrix transposition.  This should not be possible!");
            return null;
        }
    }

    /**
     * Calculate the determinant of a 2x2 Matrix
     * @return The calculated determinant
     */
    public double det() {
        try {
            // Determinant for a 2x2 matrix
            if (degree == 2) {
                return (getElement(0, 0) * getElement(1, 1)) - (getElement(0, 1) * getElement(1, 0));
            } else {
                // Determinant for a matrix of degree > 2
                double det = 0;
                for (int col = 0; col < degree; col++) {
                    det += getElement(0, col) * cofactor(0, col);
                }
                return det;
            }
        }
        catch (Exception ignored) {
            return 0; // Shouldn't get here...
        }
    }

    /**
     * Generate the submatrix of A, which is the matrix formed by removing the
     * specified row and column from Matrix A
     * @param row The row index to remove
     * @param col The column index to remove
     * @return The specified submatrix
     */
    public Matrix submatrix(int row, int col) throws RuntimeException {
        if (row < 0 || row > degree || col < 0 || col > degree) {
            throw new RuntimeException("Row/Column parameters are out of bounds for returning submatrix!");
        }

        try {
            Matrix m = new Matrix(degree - 1);

            int mrIdx = 0;
            int mcIdx;

            for (int srcRow = 0; srcRow < degree; srcRow++) {
                mcIdx = 0;  // Reset the column index for the destination cell
                for (int srcCol = 0; srcCol < degree; srcCol++) {
                    if (srcRow != row && srcCol != col) {
                        m.setElement(mrIdx, mcIdx, getElement(srcRow, srcCol));
                        mcIdx++;
                    }
                }
                if (mcIdx > 0) {
                    mrIdx++;
                }
            }
            return m;
        }
        catch (Exception ignore) {
            return null;
        }
    }

    /**
     * Calculate the minor of the submatrix at startRow, startCol
     * @param startRow The row we start at
     * @param startCol The column we start at
     * @return The value of the determinant of that submatrix.
     */
    public double minor(int startRow, int startCol) {
        Matrix sub = submatrix(startRow, startCol);
        return sub.det();
    }

    /**
     * Determine the cofactor of a matrix
     * @param row The row we determine the submatrix from
     * @param col The column we determine the submatrix from
     * @return The computed cofactor value
     */
    public double cofactor(int row, int col) {
        // Sign needs to change if row + col is odd
        int sign = ((row+col)%2) == 0 ? 1 : -1;

        return minor(row, col) * sign;
    }

    /**
     * @return True if the matrix can be inverted
     */
    public boolean isInvertible() {
        return det() != 0;
    }

    public Matrix inverse() throws RuntimeException {
        if (!isInvertible()) {
            throw new RuntimeException("Matrix is not invertible!");
        }

        Matrix m = new Matrix(degree);
        double det = det();

        for (int row = 0; row < degree; row++) {
            for (int col = 0; col < degree; col++) {
                double c = cofactor(row, col);
                m.setElement(col, row, c / det);
            }
        }
        return m;
    }
    /**
     * @param degree The size of the square identity matrix to return
     * @return The identity matrix thus generated
     */
    public static Matrix identity(int degree) {
        Matrix m = new Matrix(degree);
        for (int i = 0; i < degree; i++) {
            m.setElement(i, i, 1);
        }
        return m;
    }
}

package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Basic ordered list of values.  Used as the basis for Vector and
 * Point classes
 *
 * @author Mark Edwards
 * @version July 8th, 2022
 */
public class Tuple {
    private final double x;
    private final double y;
    private final double z;
    private final double w;

    /**
     * Constructor.  Define a tuple with the specified values.
     * @param x The x value
     * @param y The y value
     * @param z The z value
     * @param w The w value
     */
    public Tuple(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Accessor function for the x component
     * @return The x component of the tuple
     */
    public double getX() {
        return x;
    }

    /**
     * Accessor function for the y component
     * @return The y component of the tuple
     */
    public double getY() {
        return y;
    }

    /**
     * Accessor function for the z component
     * @return The z component of the tuple
     */
    public double getZ() {
        return z;
    }

    /**
     * Accessor function for the w component
     * @return The w component of the tuple
     */
    public double getW() {
        return w;
    }

    /**
     * @return a string representation of the tuple
     */
    public String toString() {
        return getClass().getSimpleName() + "("+ x +", "+ y +", "+ z +", "+ w +")";
    }

    /**
     * Test objects for equality
     * @param o Object we want to test against
     * @return true if objects are computationally the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return checkEqual(tuple.x, x) && checkEqual(tuple.y, y) && checkEqual(tuple.z, z) && checkEqual(tuple.w, w);
    }

    /**
     * Method to compare two doubles within a tolerance level rather than
     * testing for direct equality.
     * @param a The first double value we're testing
     * @param b The second double value we're testing
     * @return true if the values are within tolerance
     */
    private boolean checkEqual(double a, double b) {
        final double EPSILON = 0.00001;
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * @return the integer hashcode for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    /**
     * Adds two tuples and returns the result
     * @param t The tuple we wish to add to this one
     * @return The result of the addition
     */
    public Tuple add(@NotNull Tuple t) {
        return new Tuple(this.x + t.getX(),
            this.y + t.getY(),
            this.z + t.getZ(),
            this.w + t.getW());
    }

    /**
     * Subtract two tuples and returns the result
     * @param t The tuple we wish to subtract from this one
     * @return The result of the subtraction
     */
    public Tuple subtract(@NotNull Tuple t) {
        return new Tuple(this.x - t.getX(),
            this.y - t.getY(),
            this.z - t.getZ(),
            this.w - t.getW());
    }

    /**
     * @return The negation of this tuple
     */
    public Tuple negate() {
        return new Tuple(-x, -y, -z, -w);
    }

    /**
     * Scalar multiplication of a tuple.
     * @param m the value we wish to multiply by
     * @return New tuple that results from the multiplication
     */
    public Tuple multiply(double m) {
        return new Tuple(x*m, y*m, z*m, w*m);
    }

    public Tuple multiply(int m) {
        return new Tuple(x*m, y*m, z*m, w*m);
    }

    /**
     * Scalar division of a tuple
     * @param m the value we wish to divide by
     * @return New tuple that results from the division
     */
    public Tuple divide(double m) {
        return new Tuple(x/m, y/m, z/m, w/m);
    }

    public Tuple divide(int m) {
        return new Tuple(x/m, y/m, z/m, w/m);
    }
}

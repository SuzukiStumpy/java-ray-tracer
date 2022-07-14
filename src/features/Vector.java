package features;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a 3D vector
 *
 * @author Mark Edwards
 * @version July 8th, 2022
 */
public class Vector extends Tuple {
    public Vector(double x, double y, double z) {
        super(x, y, z, 0.0);
    }

    /**
     * Copy constructor
     * @param other The vector we wish to duplicate
     */
    public Vector(Vector other) {
        super(other.getX(), other.getY(), other.getZ(), 0.0);
    }

    /**
     * Add a point to a vector
     * @param p The point we're adding to the vector
     * @return The resultant point from the addition
     */
    public Point add(@NotNull Point p) {
        return new Point(getX() + p.getX(),
            getY()+p.getY(),
            getZ()+p.getZ());
    }

    /**
     * Add two vectors
     * @param v the Vector we wish to add
     * @return The resultant vector
     */
    public Vector add(@NotNull Vector v) {
        return new Vector(getX() + v.getX(),
            getY() + v.getY(),
            getZ() + v.getZ());
    }

    /**
     * Multiply a Vector by a scalar amount
     * @param v The value we want to multiply the vector by
     * @return The resultant vector
     */
    public Vector multiply(double v) {
        return new Vector(getX() * v, getY() * v, getZ() * v);
    }

    /**
     * Subtract two vectors
     * @param v the Vector we wish to subtract
     * @return The resultant Vector showing the change in direction between
     * the two
     */
    public Vector subtract(@NotNull Vector v) {
        return new Vector(getX() - v.getX(),
            getY()-v.getY(),
            getZ()-v.getZ());
    }

    /**
     * @return The magnitude of this vector (calculated using Pythagoras)
     */
    public double magnitude() {
        double x = getX();
        double y = getY();
        double z = getZ();
        double w = getW();
        return Math.sqrt(x*x + y*y + z*z + w*w);
    }

    /**
     * Normalise a vector to obtain a unit vector
     */
    public Vector normalize() {
        double mag = magnitude();
        return new Vector(getX() / mag, getY()/mag, getZ()/mag);
    }

    /**
     * Return the dot product of two Vectors
     * @param v2 The other vector that we're calculating with
     * @return The dot product of the two vectors
     */
    public double dot(@NotNull Vector v2) {
        return getX() * v2.getX() +
            getY() * v2.getY() +
            getZ() * v2.getZ() +
            getW() * v2.getW();
    }

    /**
     * Return the cross product of two vectors
     * @param v2 The other vector that we're calculating with
     * @return The cross product of the two vectors
     */
    public Vector cross(@NotNull Vector v2) {
        return new Vector(getY() * v2.getZ() - getZ() * v2.getY(),
            getZ() * v2.getX() - getX() * v2.getZ(),
            getX() * v2.getY() - getY() * v2.getX());
    }

    /**
     * Return the vector derived by reflecting around the given normal
     * @param normal The Normal vector around which we are reflecting
     * @return The reflected vector
     */
    public Vector reflect(@NotNull Vector normal) {
        return new Vector(this)
            .subtract(normal
                .multiply(2)
                .multiply(this.dot(normal))
            )
            .toVector();
    }

}

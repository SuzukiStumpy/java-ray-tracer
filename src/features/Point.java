package features;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of a point in 3D space.
 *
 * @author Mark Edwards
 * @version July 8th, 2022
 */
public class Point extends Tuple {
    /**
     * Generate a point at the given coordinates
     * @param x The x position
     * @param y The y position
     * @param z The z position
     */
    public Point(double x, double y, double z) {
        super(x, y, z, 1.0);
    }

    /**
     * Add a vector to a point
     * @param v The vector we want to add to this point
     * @return The new point we arrive at
     */
    public Point add(@NotNull Vector v) {
        return new Point(getX() + v.getX(),
            getY() + v.getY(), getZ() + v.getZ());
    }

    /**
     * Subtract two points.  Yields the vector between the two points
     * @param p The point we wish to subtract from this
     */
    public Vector subtract(@NotNull Point p) {
        return new Vector(getX() - p.getX(),
            getY() - p.getY(),
            getZ() - p.getZ());
    }

    /**
     * Subtract a vector from a point.
     * @param v The vector we wish to subtract
     * @return The point we end up at.
     */
    public Point subtract(@NotNull Vector v) {
        return new Point(getX() - v.getX(),
            getY() - v.getY(),
            getZ() - v.getZ());
    }
}

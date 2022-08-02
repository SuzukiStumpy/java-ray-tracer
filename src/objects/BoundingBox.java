package objects;

import features.Intersection;
import features.Matrix;
import features.Point;
import features.Ray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static features.Precompute.EPSILON;

/**
 * Simple class which defines an Axis Aligned Bounding Box.  Used to provide
 * a simple intersection text for rays intersecting groups of objects.  Helps
 * to provide a simple speedup of most rendering tasks in more complex scenes.
 *
 * The class is not intended to stand alone, but instead be part of the various
 * shape objects by way of composition.
 *
 * @author Mark Edwards
 * @version August 2nd, 2022
 */
public class BoundingBox {
    private Point min;
    private Point max;

    /**
     * Generates an empty (and thus invalid) box.
     */
    public BoundingBox() {
        min = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        max = new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    /**
     * Generate a bounding box with some volume
     * @param min The lower point encompassing the box
     * @param max The higer point encompassing the box
     */
    public BoundingBox(@NotNull Point min, @NotNull Point max) {
        this.max = new Point(max);
        this.min = new Point(min);
    }

    /**
     * @return Returns a copy of the point defining the minimum extent of the box
     */
    public Point min() {
        return new Point(min);
    }

    /**
     * @return Returns a copy of the point defining the maximum extent of the box
     */
    public Point max() {
        return new Point(max);
    }

    /**
     * Add a point into the box volume.  Causes the min/max values of the box
     * to automatically recompute to encompass the new point
     * @param p The point to add to the volume
     */
    public void add(@NotNull Point p) {
        min.setX(Math.min(min.getX(), p.getX()));
        min.setY(Math.min(min.getY(), p.getY()));
        min.setZ(Math.min(min.getZ(), p.getZ()));

        max.setX(Math.max(max.getX(), p.getX()));
        max.setY(Math.max(max.getY(), p.getY()));
        max.setZ(Math.max(max.getZ(), p.getZ()));
    }

    /**
     * Merge another bounding box with this one.
     * @param b The bounding box whose volume we wish to add to this
     */
    public void add(@NotNull BoundingBox b) {
        add(b.min());
        add(b.max());
    }

    /**
     * Determines whether the given point is contained within the bounding box
     * @param p The point we wish to test
     * @return true if the point is within the bounding box volume, false otherwise
     */
    public boolean contains(@NotNull Point p) {
        return p.getX() >= min.getX() && p.getX() <= max.getX()
            && p.getY() >= min.getY() && p.getY() <= max.getY();
    }

    /**
     * Determines whether the given bounding box is contained within this box.
     * @param b The bounding box we wish to test
     * @return true if the tested bounding box is completely contained within this box, false otherwise
     */
    public boolean contains(@NotNull BoundingBox b) {
        return contains(b.min()) && contains(b.max());
    }

    /**
     * Returns the new bounding box formed by applying the given matrix
     * transformation to this current bounding box.  Note that this method
     * does not update this current bounding box.
     * @param m The transformation matrix we want to apply
     * @return The new bounding box formed by applying the transformation to the
     * current bounding box.
     */
    @Contract(pure = true)
    public BoundingBox transform(@NotNull Matrix m) {
        // Get all the eight points of the cube encompassed by the current box
        Point[] points = {
            new Point(min),
            new Point(min.getX(), min.getY(), max.getZ()),
            new Point(min.getX(), max.getY(), min.getZ()),
            new Point(min.getX(), max.getY(), max.getZ()),
            new Point(max.getX(), min.getY(), min.getZ()),
            new Point(max.getX(), min.getY(), max.getZ()),
            new Point(max.getX(), max.getY(), min.getZ()),
            new Point(max)
        };

        BoundingBox box = new BoundingBox();

        for (Point p: points) {
            box.add(m.multiply(p).toPoint());
        }

        return box;
    }

    /**
     * Determines whether the given ray will intersect this bounding box or not
     * @param r The ray to test
     * @return true if the ray intersects the box at any point, false otherwise
     */
    @Contract(pure = true)
    public boolean intersects(@NotNull Ray r) {
        double[] xt, yt, zt;

        xt = checkAxis(r.getOrigin().getX(), r.getDirection().getX(), min.getX(), max.getX());
        yt = checkAxis(r.getOrigin().getY(), r.getDirection().getY(), min.getY(), max.getY());
        zt = checkAxis(r.getOrigin().getZ(), r.getDirection().getZ(), min.getZ(), max.getZ());

        double tmin = Math.max(xt[0], Math.max(yt[0], zt[0]));
        double tmax = Math.min(xt[1], Math.min(yt[1], zt[1]));

        return tmin <= tmax;
    }

    /**
     * Determines the min/max values for intersect on an axis
     * @param origin ray origin in the affected plane
     * @param direction ray direction in the affected plane
     * @param pmin The minimum extent of the bounding box
     * @param pmax The maximum extent of the bounding box
     * @return Array of two doubles representing tmin and tmax in the affected plane
     */
    @Contract(pure = true)
    private double[] checkAxis(double origin, double direction, double pmin, double pmax) {
        double[] retVals = new double[2];
        double tmin_numerator = (pmin - origin);
        double tmax_numerator = (pmax - origin);

        if (Math.abs(direction) >= EPSILON) {
            retVals[0] = tmin_numerator / direction;
            retVals[1] = tmax_numerator / direction;
        } else {
            retVals[0] = tmin_numerator * Double.POSITIVE_INFINITY;
            retVals[1] = tmax_numerator * Double.POSITIVE_INFINITY;
        }

        if (retVals[0] > retVals[1]) {
            double tmp = retVals[0];
            retVals[0] = retVals[1];
            retVals[1] = tmp;
        }

        return retVals;
    }
}

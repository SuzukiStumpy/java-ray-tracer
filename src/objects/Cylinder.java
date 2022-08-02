package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.List;

import static features.Precompute.EPSILON;

/**
 * Concrete class for modelling a cylinder
 *
 * @author Mark Edwards
 * @version August 1st, 2022
 */
public class Cylinder extends Shape {
    private double miny, maxy;  // Extents for cylinder height
    private boolean closed;

    public Cylinder() {
        super();
        miny = Double.NEGATIVE_INFINITY;
        maxy = Double.POSITIVE_INFINITY;
        closed = false;
    }

    /**
     * Set the current upper truncation point of the cylinder.  Note that
     * miny and maxy are normalised such that miny is always the lesser of the
     * two values
     * @param value The y value at which we want to truncate
     */
    public void maxY(double value) {
        if (value < miny) {
            maxy = miny;
            miny = value;
        } else {
            maxy = value;
        }
    }

    /**
     * @return The current upper truncation point of the cylinder
     */
    public double maxY() {
        return maxy;
    }

    /**
     * Set the lower truncation point of the cylinder
     * miny and maxy are normalised such that miny is always the lesser of the
     * two values
     * @param value The y value at which we want to truncate
     */
    public void minY(double value) {
        if (value > maxy) {
            miny = maxy;
            maxy = value;
        } else {
            miny = value;
        }
    }

    /**
     * @return The current lower truncation point of the cylinder
     */
    public double minY() {
        return miny;
    }

    /**
     * @return Whether the cylinder is capped or not.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Set whether the cylinder is closed or open
     * @param value True closes the cylinder, false opens it.
     */
    public void closed(boolean value) {
        closed = value;
    }

    /**
     * Helper function to reduce duplication.  Checks to see if the intersection
     * at 't' is within a radius of 1 (the radius of the basic cylinder) of
     * the y axis
     * @param r The ray we are testing against
     * @param t The time point of the intersection
     * @return True if the intersection is valid, false otherwise
     */
    private boolean checkCap(@NotNull Ray r, double t) {
        double x = r.getOrigin().getX() + (t * r.getDirection().getX());
        double z = r.getOrigin().getZ() + (t * r.getDirection().getZ());
        return ((x*x) + (z*z)) <= 1;
    }

    /**
     * Checks to see whether the ray intersects with the caps of the cylinder
     * @param r The ray we are testing
     * @param xs The list of intersections (which will be returned either with
     *           new intersections added, or unchanged)
     * @return The ArrayList passed in as xs with any new intersections added to
     *         it
     */
    private ArrayList<Intersection> intersectCaps(@NotNull Ray r, @NotNull ArrayList<Intersection> xs) {
        // Caps only matter if the cylinder is closed and if the ray might possibly pass through them
        // (ie: has a significant Y direction component)
        if (!this.closed || Math.abs(r.getDirection().getY()) < EPSILON) {
            return xs;
        }

        // Check for an intersection with the lower cap by intersecting the ray
        // with the plane at y = miny
        double t = (miny - r.getOrigin().getY()) / r.getDirection().getY();
        if (checkCap(r, t)) {
            xs.add(new Intersection(t, this));
        }

        // Check for an intersection with the upper cap by intersecting the ray
        // with the plane at y = maxy
        t = (maxy - r.getOrigin().getY()) / r.getDirection().getY();
        if (checkCap(r, t)) {
            xs.add(new Intersection(t, this));
        }
        return xs;
    }

    /**
     * Get the points at which a Ray intersects a shape
     * @param ray The ray we wish to test
     * @return The list of intersections with this shape
     */
    @Override
    protected ArrayList<Intersection> local_intersect(@NotNull Ray ray) {
        ArrayList<Intersection> xs = new ArrayList<>();
        double a = Math.pow(ray.getDirection().getX(), 2) + Math.pow(ray.getDirection().getZ(), 2);

        if (Math.abs(a) < EPSILON) {
            // Ray is parallel to y axis, so just check for intersection of caps...
            return intersectCaps(ray, xs);
        } else {
            double b = 2 * ray.getOrigin().getX() * ray.getDirection().getX() + 2 * ray.getOrigin().getZ() * ray.getDirection().getZ();
            double c = Math.pow(ray.getOrigin().getX(), 2) + Math.pow(ray.getOrigin().getZ(), 2) - 1;

            double disc = (b*b) - (4*a*c);

            if (disc < 0) {
                // Ray does not intersect cylinder
                return new ArrayList<>();
            } else {
                double t0 = (-b - Math.sqrt(disc)) / (2*a);
                double t1 = (-b + Math.sqrt(disc)) / (2*a);

                if (t0 > t1) {
                    double tmp = t0;
                    t0 = t1;
                    t1 = tmp;
                }

                double y0 = ray.getOrigin().getY() + (t0 * ray.getDirection().getY());

                if (miny < y0 && y0 < maxy) {
                    xs.add(new Intersection(t0, this));
                }

                double y1 = ray.getOrigin().getY() + (t1 * ray.getDirection().getY());

                if (miny < y1 && y1 < maxy) {
                    xs.add(new Intersection(t1, this));
                }

                // Now, check for any intersection with the caps...
                return intersectCaps(ray, xs);
            }
        }
    }

    /**
     * Computes the surface normal at a given point
     * @param p The point we wish to get the normal at (in object space)
     * @return The computed normal (in object space)
     */
    @Override
    protected Vector local_normal_at(@NotNull Point p) {
        // Compute the square of the distance from the y axis
        double d = Math.pow(p.getX(), 2) + Math.pow(p.getZ(), 2);

        if (d < 1 && p.getY() >= maxy - EPSILON) {
            return new Vector(0, 1, 0);
        } else if (d < 1 && p.getY() <= miny + EPSILON) {
            return new Vector(0,-1,0);
        } else {
            return new Vector(p.getX(), 0, p.getZ());
        }
    }
}

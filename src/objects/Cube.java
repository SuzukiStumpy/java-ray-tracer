package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static features.Precompute.EPSILON;

/**
 * Concrete class for modelling a cube using an Axis-Aligned Bounding Box
 * centered at the origin with sides from +/- 1 in each axis
 *
 * @author Mark Edwards
 * @version August 1st, 2022
 */
public class Cube extends Shape {
    /**
     * Get the points at which a Ray intersects a shape
     * @param ray The ray we wish to test
     * @return The list of intersections with this shape
     */
    @Override
    protected ArrayList<Intersection> local_intersect(@NotNull Ray ray) {
        double[] xt;
        double[] yt;
        double[] zt;

        xt = checkAxis(ray.getOrigin().getX(), ray.getDirection().getX());
        yt = checkAxis(ray.getOrigin().getY(), ray.getDirection().getY());
        zt = checkAxis(ray.getOrigin().getZ(), ray.getDirection().getZ());

        double tmin = Math.max(xt[0], Math.max(yt[0], zt[0]));
        double tmax = Math.min(xt[1], Math.min(yt[1], zt[1]));

        if (tmin > tmax) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(List.of(
                new Intersection(tmin, this),
                new Intersection(tmax, this)
            ));
        }
    }

    /**
     * Determines the min/max values for intersect on an axis
     * @param origin ray origin in the affected plane
     * @param direction ray direction in the affected plane
     * @return Array of two doubles representing tmin and tmax in the affected plane
     */
    private double[] checkAxis(double origin, double direction) {
        double[] retVals = new double[2];
        double tmin_numerator = (-1 - origin);
        double tmax_numerator = (1 - origin);

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

    /**
     * Computes the surface normal at a given point
     * @param p The point we wish to get the normal at (in object space)
     * @return The computed normal (in object space)
     */
    @Override
    protected Vector local_normal_at(@NotNull Point p) {
        double maxc = Math.max(Math.abs(p.getX()), Math.max(Math.abs(p.getY()), Math.abs(p.getZ())));

        if (maxc == Math.abs(p.getX())) {
            return new Vector(p.getX(), 0, 0);
        } else if (maxc == Math.abs(p.getY())) {
            return new Vector(0, p.getY(), 0);
        } else {
            return new Vector(0, 0, p.getZ());
        }
    }

    @Override
    public BoundingBox bounds() {
        return new BoundingBox(new Point(-1,-1,-1), new Point(1,1,1));
    }
}

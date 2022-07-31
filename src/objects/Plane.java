package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static features.Precompute.EPSILON;

/**
 * Describes a planar surface in 3D space.  Plane is defined in the xz plane
 * and can then be transformed into any available orientation.
 *
 * @author Mark Edwards
 * @version July 18th, 2022
 */
public class Plane extends Shape {

    /**
     * Return list of intersections between a ray and this object
     * @param ray The ray we wish to test
     * @return The list of intersections
     */
    @Override
    protected ArrayList<Intersection> local_intersect(@NotNull Ray ray) {
        if (Math.abs(ray.getDirection().getY()) < EPSILON) {
            // parallel ray - no intersection
            return new ArrayList<>();
        } else {
            // ray will intersect...
            double t = -ray.getOrigin().getY() / ray.getDirection().getY();
            Intersection i = new Intersection(t, this);
            ArrayList<Intersection> xs = new ArrayList<>();
            xs.add(i);
            return xs;
        }
    }

    /**
     * Retunrns the normal at the given point
     * @param p The point we wish to get the normal at (in object space)
     * @return The normal to this shape.
     */
    @Override
    protected Vector local_normal_at(@NotNull Point p) {
        return new Vector(0, 1, 0);
    }

    @Override
    public String toString() {
        return "Plane{transform: "+ getTransform() +", Material: "+ getMaterial() +"}";
    }

}

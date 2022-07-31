package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Concrete class for modelling a sphere
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class Sphere extends Shape {
    /**
     * Get the points at which a Ray intersects a shape
     * @param ray The ray we wish to test
     * @return The list of intersections with this shape
     */
    @Override
    protected ArrayList<Intersection> local_intersect(@NotNull Ray ray) {
        // Create a list for storing the intersected points (if any)
        ArrayList<Intersection> xs = new ArrayList<>();

        // Compute the discriminant to determine whether we've intersected or
        // not
        Vector shapeToRay = ray.getOrigin().subtract(new Point(0, 0, 0));
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2 * ray.getDirection().dot(shapeToRay);
        double c = shapeToRay.dot(shapeToRay) - 1;
        double discriminant = b * b - 4 * a * c;  // This is the discriminant

        // If we miss, simply return an empty list
        if (discriminant < 0) {
            return xs;
        }

        // Calculate the root of the discriminant to save having to compute
        // it twice
        double discriminantRoot = Math.sqrt(discriminant);
        xs.add(new Intersection((-b - discriminantRoot) / (2 * a), this));
        xs.add(new Intersection((-b + discriminantRoot) / (2 * a), this));
        return xs;
    }

    /**
     * Computes the surface normal at a given point
     * @param p The point we wish to get the normal at (in object space)
     * @return The computed normal (in object space)
     */
    @Override
    protected Vector local_normal_at(@NotNull Point p) {
        return p.subtract(new Point(0,0,0));
    }

    @Override
    public String toString() {
        return "Sphere{transform: "+ getTransform() +", Material: "+ getMaterial() +"}";
    }
}

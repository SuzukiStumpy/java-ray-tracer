package features;

import objects.Shape;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Models a ray within the raytracer, a ray being, mathematically simply a
 * line with an origin and direction.
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class Ray {
    private final Point origin;
    private final Vector direction;

    /**
     * Creates a new Ray with the given origin and direction
     * @param origin Point from which the ray emanates
     * @param direction Vector describing the direction the ray progresses
     */
    public Ray(@NotNull Point origin, @NotNull Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    /**
     * @return gets the ray's origin
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * @return gets the ray's direction
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Gets the point intersected by the Ray at a given point in time.
     * Computation is:
     *      Position = origin + (direction * t)
     * @param t The point in time that we wish to grab the point for
     * @return The point interested by the ray at time t
     */
    public Point getPosition(double t) {
        return origin.add(direction.multiply(t));
    }

    /**
     * Get the points at which a Ray intersects a shape
     * @param s The shape which we want to get the intersections of
     * @return The list of time units where the ray intersects
     */
    public ArrayList<Intersection> intersect(@NotNull Shape s) {
        // Create a list for storing the intersected points (if any)
        ArrayList<Intersection> xs = new ArrayList<>();

        // Compute the discriminant to determine whether we've intersected or
        // not
        Vector shapeToRay = origin.subtract(new Point(0,0,0));
        double a = direction.dot(direction);
        double b = 2 * direction.dot(shapeToRay);
        double c = shapeToRay.dot(shapeToRay) - 1;
        double discriminant = b*b - 4*a*c;  // This is the discriminant


        // If we miss, simply return an empty list
        if (discriminant < 0) {
            return xs;
        }

        // Calculate the root of the discriminant to save having to compute
        // it twice
        double discriminantRoot = Math.sqrt(discriminant);
        xs.add(new Intersection((-b - discriminantRoot) / 2*a, s));
        xs.add(new Intersection((-b + discriminantRoot) / 2*a, s));
        return xs;
    }
}

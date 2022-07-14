package features;

import objects.Shape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

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
     * Copy constructor, duplicates a Ray
     * @param other The ray we wish to duplicate.
     */
    public Ray(@NotNull Ray other) {
        origin = new Point(other.origin);
        direction = new Vector(other.direction);
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
     * @return The point intersected by the ray at time t
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
        // Transform the ray by the inverse of the shape's transformation
        Ray r2 = new Ray(this).transform(s.getTransform().inverse());

        // Create a list for storing the intersected points (if any)
        ArrayList<Intersection> xs = new ArrayList<>();

        // Compute the discriminant to determine whether we've intersected or
        // not
        Vector shapeToRay = r2.origin.subtract(new Point(0,0,0));
        double a = r2.direction.dot(r2.direction);
        double b = 2 * r2.direction.dot(shapeToRay);
        double c = shapeToRay.dot(shapeToRay) - 1;
        double discriminant = b*b - 4*a*c;  // This is the discriminant


        // If we miss, simply return an empty list
        if (discriminant < 0) {
            return xs;
        }

        // Calculate the root of the discriminant to save having to compute
        // it twice
        double discriminantRoot = Math.sqrt(discriminant);
        xs.add(new Intersection((-b - discriminantRoot) / (2*a), s));
        xs.add(new Intersection((-b + discriminantRoot) / (2*a), s));
        return xs;
    }

    /**
     * Return the (sorted) intersection list for all objects within a World object
     * with this ray
     * @param w The world object that we are iterating through
     * @return The list of intersections sorted into increasing values of T
     */
    public ArrayList<Intersection> intersect(@NotNull World w) {
        ArrayList<Intersection> xs = new ArrayList<>();

        for (Shape object: w.getObjects()) {
            xs.addAll(this.intersect(object));
        }

        Collections.sort(xs);

        return xs;
    }

    /**
     * Apply the supplied transformation matrix to the ray and return the result
     * Note that the transformation is applied to a copy of the current ray rather
     * than performing the transformation in-situ.  This allows us to retain the
     * original data so that we keep the original world-space distances
     * @param t The transformation matrix we wish to apply.
     * @return A new ray with the result of the transformation.
     */
    public Ray transform(@NotNull Matrix t) {
        Point o = t.multiply(origin).toPoint();
        Vector v = t.multiply(direction).toVector();
        return new Ray(o, v);
    }
}

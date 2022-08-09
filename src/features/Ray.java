package features;

import objects.Shape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger log = LogManager.getLogger(Ray.class);
    private final Point origin;
    private final Vector direction;

    /**
     * Creates a new Ray with the given origin and direction
     * @param origin Point from which the ray emanates
     * @param direction Vector describing the direction the ray progresses
     */
    public Ray(@NotNull Point origin, @NotNull Vector direction) {
        log.debug("Generating new Ray: origin: "+ origin +", direction: "+ direction);
        this.origin = origin;
        this.direction = direction;

        Statistics.rays++;
    }

    /**
     * Copy constructor, duplicates a Ray
     * @param other The ray we wish to duplicate.
     */
    public Ray(@NotNull Ray other) {
        origin = new Point(other.origin);
        direction = new Vector(other.direction);

        Statistics.rays++;
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
     * Return the (sorted) intersection list for all objects within a World object
     * with this ray
     * @param w The world object that we are iterating through
     * @return The list of intersections sorted into increasing values of T
     */
    public ArrayList<Intersection> intersect(@NotNull World w) {
        log.debug("Computing ray "+ this +" intersection with world.");
        ArrayList<Intersection> xs = new ArrayList<>();

        for (Shape object: w.getObjects()) {
            xs.addAll(object.intersect(this));
        }

        //Collections.sort(xs);

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

    @Override
    public String toString() {
        return "Ray{" +
            "origin=" + origin +
            ", direction=" + direction +
            '}';
    }
}

package features;

import objects.Shape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Models an intersection between a ray and a shape.  Encapsulates the time
 * of intersection and the object it intersects at that time.
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class Intersection implements Comparable<Intersection> {
    private static final Logger log = LogManager.getLogger(Intersection.class);

    private final double t;
    private final Shape s;

    public Intersection(double time, @NotNull Shape shape) {
        t = time;
        s = shape;

        Statistics.intersections++;
    }

    /**
     * @return Gets the time stored in this intersection
     */
    public double getTime() {
        return t;
    }

    /**
     * @return The shape stored in this intersection
     */
    public Shape getShape() {
        return s;
    }

    /**
     * Implements the Comparable interface, so we can sort lists of intersections
     * @param i the object to be compared.
     * @return The result of the comparison
     */
    @Override
    public int compareTo(@NotNull Intersection i) {
        if (this.t < i.t) {
            return -1;
        } else if (i.t < this.t) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns a single intersection from a list of Intersection objects identifying
     * the visible 'hit' of a ray.  A hit is identified as being the intersection
     * with the lowest positive 't' value.
     * @param intersections A list of intersections to consider
     * @return The identified hit.
     */
    public static @Nullable Intersection hit(@NotNull List<Intersection> intersections) {
        Intersection hit;

        // Sort the list to make iteration easier...
        Collections.sort(intersections);

        for (Intersection intersection : intersections) {
            hit = intersection;

            if (hit.t >= 0) {
                return hit;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Intersection{" +
            "t=" + t +
            ", s=" + s +
            '}';
    }
}

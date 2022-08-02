package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.apache.logging.log4j.core.config.InMemoryAdvertiser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static features.Precompute.EPSILON;

/**
 * Describes an Object which allows for the regrouping of individual objects.
 * A group itself has no renderable surface, although it does have a position
 * and transformation in 3D space.
 *
 * @author Mark Edwards
 * @version August 2nd, 2022
 */
public class Group extends Shape {
    protected ArrayList<Shape> contents;

    public Group() {
        super();
        contents = new ArrayList<>();
    }

    /**
     * @return Gets the contents of this group
     */
    public ArrayList<Shape> contents() {
        return contents;
    }

    /**
     * Add the given shape to this groups object list.  We have a very simplistic
     * test for recursion in this method.  Note however that we don't test for
     * deep circular references...
     * @param s the shape to add to this group.
     */
    public void addObject(@NotNull Shape s) {
        // Prevent recursive addition :)
        if (!s.equals(this)) {
            s.parent(this);
        }
    }

    /**
     * Removes an object from this group and returns the removed object to the
     * caller
     * @param s The shape/group to remove
     */
    public Shape removeObject(@NotNull Shape s) {
        s.unparent();
        return s;
    }

    /**
     * Return list of intersections between a ray and this object
     * @param ray The ray we wish to test
     * @return The list of intersections
     */
    @Override
    protected ArrayList<Intersection> local_intersect(@NotNull Ray ray) {
        ArrayList<Intersection> xs = new ArrayList<>();

        if (bounds().intersects(ray)) {
            for (Shape s : contents) {
                xs.addAll(s.intersect(ray));
            }
            Collections.sort(xs);
        }
        return xs;
    }

    /**
     * Returns the normal at the given point
     * @param p The point we wish to get the normal at (in object space)
     * @return The normal to this shape.
     */
    @Override
    protected Vector local_normal_at(@NotNull Point p) throws RuntimeException {
        throw new RuntimeException("local_normal_at is not valid for Group objects!");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Group group = (Group) o;
        return contents.equals(group.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contents);
    }

    @Override
    public BoundingBox bounds() {
        BoundingBox box = new BoundingBox();

        for (Shape s: contents) {
            BoundingBox sbox = s.parentSpaceBounds();
            box.add(sbox);
        }

        return box;
    }
}

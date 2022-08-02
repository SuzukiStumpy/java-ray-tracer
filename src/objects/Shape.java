package objects;

import features.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Abstract class for all shapes within the world.  All shapes should
 * extend this class.
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public abstract class Shape {
    private static final Logger log = LogManager.getLogger(Shape.class);
    private Matrix transform;
    private Material material;
    private boolean shadowCaster;
    private Group parent;

    /**
     * Constructor: generates a unique ID for each generated shape
     */
    public Shape() {
        transform = Matrix.identity(4);
        material = new Material();
        shadowCaster = true;
        parent = null;
    }

    /**
     * @return The parent group of this shape.  Note can return null if no
     *         parent assigned.
     */
    public Shape parent() {
        return parent;
    }

    /**
     * Set the parent group for this particular shape (or group) and adds this
     * object to the parent group contents.  Note that parent can only be set
     * to a group, and not to any other random shape.  Also, if the shape is
     * already part of an existing group, it will be removed from that group
     * first.
     * @param parentGroup The group we want to parent this shape to.
     */
    protected void parent(@NotNull Group parentGroup) {
        if (parent != null) {
            unparent();
        }
        parent = parentGroup;
        parent.contents.add(this);
    }

    /**
     * Remove the shape from a parent group
     */
    protected void unparent() {
        if (parent != null) {
            parent.contents.remove(this);
            parent = null;
        }
    }

    /**
     * Set the transform matrix for this shape to the supplied Matrix
     * @param m The transformation matrix we wish to apply
     */
    public void setTransform(@NotNull Matrix m) {
        transform = new Matrix(m);
    }

    /**
     * Get (a copy of) the current transformation Matrix for this shape
     * @return The current transformation Matrix
     */
    public Matrix getTransform() {
        return new Matrix(transform);
    }

    /**
     * @return The material currently assigned to the object
     */
    public Material getMaterial() {
        return new Material(material);
    }

    /**
     * Updates the shape with a new material
     * @param m The material to assign
     */
    public void setMaterial(@NotNull Material m) {
        material = new Material(m);
    }

    /**
     * Converts a point from worldspace to object space
     * @param p The world space point we wish to convert
     * @return The point in object space
     */
    public Point worldToObject(@NotNull Point p) {
        Point op = new Point(p);

        if (parent != null) {
            op = parent.worldToObject(p);
        }

        return transform.inverse().multiply(op).toPoint();
    }

    /**
     * Convert a vector from object space to world space
     * @param normal The object space normal vector we want to transform
     * @return The normal converted to world space
     */
    public Vector normalToWorld(@NotNull Vector normal) {
        Vector n = transform.inverse()
                    .transpose()
                    .multiply(normal)
                    .toVector()
                    .normalize();

        if (parent != null) {
            n = parent.normalToWorld(n);
        }

        return n;
    }

    /**
     * Method to intersect a shape with a ray.  This base function is the public
     * method that will be called by the system in general.  The local_intersect
     * method (which is private to this class and subclasses) will then provide
     * the concrete implementation specific to each shape type.
     * @param ray The ray we wish to test for intersection
     * @return The list of intersections between the ray and the shape
     */
    public ArrayList<Intersection> intersect(@NotNull Ray ray) {
        log.debug("Getting intersections for ray "+ ray +" with object "+ this);
        Ray local_ray = ray.transform(this.transform.inverse());
        log.debug("Calling local_intersect with ray inverse: "+ local_ray);
        return local_intersect(local_ray);
    }

    /**
     * This abstract method must be provided by each individual subclass of shape
     * to determine the points at which a ray intersects the shape.
     * @param ray The ray we wish to test
     * @return The list of intersections
     */
    protected abstract ArrayList<Intersection> local_intersect(@NotNull Ray ray);

    /**
     * Returns the normal to the object at the given point
     * @param point The point (in world coordinates) at which we want to return the surface normal
     * @return The computed surface normal
     */
    public Vector normal_at(@NotNull Point point) {
        Point local_point = worldToObject(point);
        Vector local_normal = local_normal_at(local_point);
        return normalToWorld(local_normal);
    }

    /**
     * Local method to compute the surface normal at a given point.
     * @param p The point we wish to get the normal at (in object space)
     * @return The computed normal (in object space).
     */
    protected abstract Vector local_normal_at(@NotNull Point p);

    /**
     * Retrieves the colour of the object at a given point, taking into account
     * both the object and material transformation matrices
     * @param p The world-space point which we want to examine
     * @return The colour subtended at that point
     */
    public Colour colourAt(@NotNull Point p) {
        Point local_point = worldToObject(p);
        return material.colourAt(local_point);
    }

    /**
     * @return Whether the object casts a shadow or not
     */
    public boolean castsShadow() {
        return shadowCaster;
    }

    /**
     * Determines whether the object will cast a shadow or not.
     * @param s True if the object is a shadowcaster, false otherwise
     */
    public void castsShadow(boolean s) {
        shadowCaster = s;
    }

    /**
     * @return Returns the bounding box that encompasses the shape.  Abstract method
     * in the basic Shape class.  Must be overridden in each concrete class
     */
    public abstract BoundingBox bounds();

    /**
     * @return The boundingbox that encompasses this shape in the shapes object space.
     */
    public BoundingBox parentSpaceBounds() {
        return this.bounds().transform(this.transform);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
            "transform=" + transform +
            ", material=" + material +
            ", shadowCaster=" + shadowCaster +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return shadowCaster == shape.shadowCaster && transform.equals(shape.transform) && material.equals(shape.material) && Objects.equals(parent, shape.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transform, material, shadowCaster, parent);
    }
}

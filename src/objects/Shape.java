package objects;

import features.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Constructor: generates a unique ID for each generated shape
     */
    public Shape() {
        transform = Matrix.identity(4);
        material = new Material();
        shadowCaster = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return transform.equals(shape.transform) && material.equals(shape.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transform, material);
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
     * @param point The point at which we want to return the surface normal
     * @return The computed surface normal
     */
    public Vector normal_at(@NotNull Point point) {
        Point local_point = transform.inverse().multiply(point).toPoint();
        Vector local_normal = local_normal_at(local_point);
        Vector world_normal = transform.inverse().transpose().multiply(local_normal).toVector();
        return world_normal.normalize();
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
        Point local_point = transform.inverse().multiply(p).toPoint();
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
            "transform=" + transform +
            ", material=" + material +
            ", shadowCaster=" + shadowCaster +
            '}';
    }
}

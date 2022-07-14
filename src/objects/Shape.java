package objects;

import features.Material;
import features.Matrix;
import features.Point;
import features.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Abstract class for all shapes within the world.  All shapes should
 * extend this class.
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public abstract class Shape {
    private final UUID id;
    private Matrix transform;
    private Material material;

    /**
     * Constructor: generates a unique ID for each generated shape
     */
    public Shape() {
        id = UUID.randomUUID();
        transform = Matrix.identity(4);
        material = new Material();
    }

    /**
     * @return Gets the unique, internal identifier for a shape.
     */
    public UUID getId() {
        return id;
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
     * Initial implementation is specific for returning normal against unit sphere.
     * This will be enhanced for general shapes later.
     * @param world The world point at which we want to calculate the Normal
     * @return The Normal at the given point.
     */
    public Vector normal_at(@NotNull Point world) {
        // Transform the world point to object space
        Point object = transform.inverse().multiply(world).toPoint();
        Vector normalObj = object.subtract(new Point(0, 0, 0));
        Vector normalWld = transform.inverse().transpose().multiply(normalObj).toVector();
        return normalWld.normalize();
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
}

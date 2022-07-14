package objects;

import features.Matrix;
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

    /**
     * Constructor: generates a unique ID for each generated shape
     */
    public Shape() {
        id = UUID.randomUUID();
        transform = Matrix.identity(4);
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
}

package textures;

import features.Colour;
import features.Matrix;
import features.Point;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for a generic pattern.  Individual pattern classes can then
 * override this as required.
 *
 * TODO: Research and implement uv texture mapping for better texture handling
 * TODO: Research and implement noise textures
 *
 * @author Mark Edwards
 * @version July 18th, 2022
 */
public abstract class Pattern {
    private Matrix transform;

    /**
     * Constructor: Sets the default transformation for the pattern to be the
     * identity matrix.
     */
    public Pattern() {
        transform = Matrix.identity(4);
    }

    /**
     * @param m The transform we wish to set on the pattern
     */
    public void setTransform(@NotNull Matrix m) {
        transform = m;
    }

    /**
     * @return Retrieve the current transform for the pattern
     */
    public Matrix getTransform() {
        return transform;
    }
    public abstract Colour getColour();
    public abstract Colour getColour(int index);
    public Colour colourAt(@NotNull Point p) {
        // Convert point to local space before passing to specific pattern functions
        Point local = getTransform().inverse().multiply(p).toPoint();
        return localColourAt(local);
    }
    protected abstract Colour localColourAt(@NotNull Point p);
    public abstract int hashCode();
    public abstract boolean equals(Object obj);
    public abstract String toString();
}

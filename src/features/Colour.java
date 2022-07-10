package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Uses the Tuple class to encapsulate the RGBA nature of a pixel on the
 * canvas.
 */
public class Colour {
    // Colour value is stored internally within a Tuple
    private final Tuple colour;

    /**
     * Construct a colour object.  Ranges are unbounded for the RGB components
     * but valid values actually range from 0.0 to 1.0 (Black to White).  Alpha
     * is always set here at 1.0 (opaque)
     * @param red The red component
     * @param green The green component
     * @param blue The blue component
     */
    public Colour(double red, double green, double blue) {
        colour = new Tuple(red, green, blue, 1.0);
    }

    /**
     * Accessor for the red component
     * @return The decimal value of the red component
     */
    public double getR() {
        return colour.getX();
    }

    /**
     * Accessor for the green component
     * @return The decimal value of the green component
     */
    public double getG() {
        return colour.getY();
    }

    /**
     * Accessor for the blue component
     * @return The decimal value of the blue component
     */
    public double getB() {
        return colour.getZ();
    }

    /**
     * Get integer component clamped to given min/max range.  Only values from
     * 0.0 to 1.0 are considered, anything higher or lower will be clamped to
     * the given range values.  Static method since we simply pass the decimal
     * value into the method call instead of requesting the specific component
     * by name.
     * @param component The colour component we want to clamp
     * @param lowerBound The lower bound of the range to return (equal to 0.0)
     * @param upperBound The upper bound of the range to return (equal to 1.0)
     */
    public static int getClampedComponent(double component, int lowerBound, int upperBound) {
        // Make sure that the bounds are provided in the correct sequence...
        int lBound = Math.min(lowerBound, upperBound);
        int uBound = Math.max(lowerBound, upperBound);

        // Check the clamp values first.
        if (component < 0.0) {
            return lBound;
        } else if (component > 1.0) {
            return uBound;
        }

        // Now we can clamp the value to the range
        int range = uBound - lBound;
        return (int) Math.round((range * component) + lBound);
    }

    /**
     * @return A descriptive string of the colour object
     */
    public String toString() {
        return "colour("+ getR() + ", "+ getG() +", "+ getB() +")";
    }

    /**
     * @return the integer hashcode for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(colour);
    }

    /**
     * Method to compare two doubles within a tolerance level rather than
     * testing for direct equality.
     * @param a The first double value we're testing
     * @param b The second double value we're testing
     * @return true if the values are within tolerance
     */
    private boolean checkEqual(double a, double b) {
        final double EPSILON = 0.00001;
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Test objects for equality
     * @param o Object we want to test against
     * @return true if objects are computationally the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colour colour = (Colour) o;
        return checkEqual(colour.getR(), getR()) && checkEqual(colour.getG(), getG()) && checkEqual(colour.getB(), getB());
    }

    /**
     * Add two colours together
     * @param c The colour we wish to add to this one
     * @return The resultant colour
     */
    public Colour add(@NotNull Colour c) {
        return new Colour(getR() + c.getR(), getG() + c.getG(), getB() + c.getB());
    }

    /**
     * Subtract two colours
     * @param c The colour we wish to subtract
     * @return The resultant colour
     */
    public Colour subtract(@NotNull Colour c) {
        return new Colour(getR() - c.getR(), getG() - c.getG(), getB() - c.getB());
    }

    /**
     * Multiply a colour by a scalar value
     * @param n The scalar multiplicand
     * @return The resultant colour
     */
    public Colour multiply(double n) {
        return new Colour(n * getR(), n * getG(), n * getB());
    }

    /**
     * Multiply two colours (used for colour blending operations)
     * @param c The colour to blend with
     * @return The resultant, blended, colour.
     */
    public Colour multiply(@NotNull Colour c) {
        return new Colour(getR() * c.getR(), getG() * c.getG(), getB() * c.getB());
    }
}

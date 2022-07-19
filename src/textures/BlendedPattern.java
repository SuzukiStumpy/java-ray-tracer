package textures;

import features.Colour;
import features.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Generate a pattern that blends two other patterns together as its output
 *
 * @author Mark Edwards
 * @version July 19th, 2022
 */
public class BlendedPattern extends Pattern {
    private final Pattern a;
    private final Pattern b;
    private final double blend;

    /**
     * Default constructor.  Generates a simple white to black gradient
     */
    public BlendedPattern() {
        this(
            new ConstantColour(new Colour(1,1,1)),
            new ConstantColour(new Colour(0,0,0)), 0.5
        );
    }

    /**
     * Constructor.  Generates a gradient between the two specified colours
     * @param p1 First gradient colour
     * @param p2 Second gradient colour
     * @param blend How the patterns will be blended - 0 for all pattern A, 1 for all pattern B
     */
    public BlendedPattern(@NotNull Pattern p1, @NotNull Pattern p2, double blend) {
        super();
        a = p1;
        b = p2;
        this.blend = blend;
    }

    /**
     * @return The colour at the origin
     */
    @Override
    public Colour getColour() {
        return colourAt(new Point(0,0,0));
    }

    /**
     * Gets the raw colour for either of the patterns
     * @param index The pattern to retrieve the colour for (either 0==a, 1==b)
     * @return The colour of the individual pattern element
     */
    @Override
    public Colour getColour(int index) {
        if (index == 0) {
            return a.getColour();
        } else {
            return b.getColour();
        }
    }

    @Override
    protected Colour localColourAt(@NotNull Point p) {
        Colour a = this.a.colourAt(p);
        Colour b = this.b.colourAt(p);

        return new Colour(
            (a.getR() * (1-blend)) + (b.getR() * blend),
            (a.getG() * (1-blend)) + (b.getG() * blend),
            (a.getB() * (1-blend)) + (b.getB() * blend));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendedPattern that = (BlendedPattern) o;
        return Double.compare(that.blend, blend) == 0 && a.equals(that.a) && b.equals(that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, blend);
    }

    @Override
    public String toString() {
        return "BlendedPattern{" +
            "a=" + a +
            ", b=" + b +
            ", blend=" + blend +
            '}';
    }
}

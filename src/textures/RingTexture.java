package textures;

import features.Colour;
import features.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Simple, two-colour alternating rings
 *
 * @author Mark Edwards
 * @version July 19th, 2022
 */
public class RingTexture extends Pattern {
    private final ArrayList<Colour> colours;

    /**
     * Default constructor.  Generates a simple white to black gradient
     */
    public RingTexture() {
        this(new Colour(1,1,1), new Colour(0,0,0));
    }

    /**
     * Constructor.  Generates a gradient between the two specified colours
     * @param c1 First gradient colour
     * @param c2 Second gradient colour
     */
    public RingTexture(@NotNull Colour c1, @NotNull Colour c2) {
        super();
        colours = new ArrayList<>();
        colours.add(new Colour(c1));
        colours.add(new Colour(c2));
    }

    /**
     * @return The colour at index zero
     */
    @Override
    public Colour getColour() {
        return colours.get(0);
    }

    /**
     * Returns the colour at the given index position.  If outside the valid
     * range, then the nearest colour index will be returned instead
     * @param index The index of the colour to retrieve (Currently only 0 and 1 are valid)
     * @return The retrieved colour
     */
    @Override
    public Colour getColour(int index) {
        return (index <= 0) ? colours.get(0) : colours.get(1);
    }

    @Override
    protected Colour localColourAt(@NotNull Point p) {
        double x = p.getX();
        double z = p.getZ();

        if (Math.floor(Math.sqrt(x*x + z*z)) % 2 == 0) {
            return colours.get(0);
        } else {
            return colours.get(1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RingTexture that = (RingTexture) o;
        return colours.equals(that.colours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colours);
    }

    @Override
    public String toString() {
        return "RingTexture{" +
            "colours=" + colours +
            '}';
    }
}

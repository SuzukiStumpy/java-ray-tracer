package textures;

import features.Colour;
import features.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Generate a series of radial gradients instead of linear
 *
 * @author Mark Edwards
 * @version July 19th, 2022
 */
public class RadialGradient extends Pattern {
    private final ArrayList<Colour> colours;

    /**
     * Default constructor.  Generates a simple white to black gradient
     */
    public RadialGradient() {
        this(new Colour(1,1,1), new Colour(0,0,0));
    }

    /**
     * Constructor.  Generates a gradient between the two specified colours
     * @param c1 First gradient colour
     * @param c2 Second gradient colour
     */
    public RadialGradient(@NotNull Colour c1, @NotNull Colour c2) {
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
        Colour a = colours.get(0);
        Colour b = colours.get(1);
        double x = p.getX();
        double z = p.getZ();
        double hyp = Math.sqrt(x*x + z*z);
        double fraction = hyp - Math.floor(hyp);

        double distanceR = b.getR() - a.getR();
        double distanceG = b.getG() - a.getG();
        double distanceB = b.getB() - a.getB();

        return new Colour(
            a.getR() + (distanceR * fraction),
            a.getG() + (distanceG * fraction),
            a.getB() + (distanceB * fraction));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RadialGradient that = (RadialGradient) o;
        return colours.equals(that.colours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colours);
    }

    @Override
    public String toString() {
        return "RadialGradient{" +
            "colours=" + colours +
            '}';
    }
}

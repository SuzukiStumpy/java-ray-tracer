package textures;

import features.Colour;
import features.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Defines a procedural stripes texture where the pattern alternates between
 * one of two defined colours.
 *
 * @author Mark Edwards
 * @version July 18th, 2022
 */
public class Stripes extends Pattern {
    private ArrayList<Colour> colours;

    /**
     * Default constructor.  Generates a default black & white striped pattern
     */
    public Stripes() {
        this(new Colour(1, 1, 1), new Colour(0, 0, 0));
    }

    /**
     * Constructor allows the generation of any arbitrary two-colour pattern
     * @param a The first colour we want to employ
     * @param b The second colour we want to employ
     */
    public Stripes(@NotNull Colour a, @NotNull Colour b) {
        super();
        colours = new ArrayList<>();
        colours.add(a);
        colours.add(b);
    }

    /**
     * Gets the colour at index zero
     * @return The colour at index zero
     */
    @Override
    public Colour getColour() {
        return getColour(0);
    }

    /**
     * Return the colour at the specified index (currently, valid indices are
     * simply zero and one).  If an invalid index is supplied, then the colour
     * at index zero is returned.
     * @param index The index of the colour we wish to get
     * @return The colour at the specified index (or index 0 if out of bounds)
     */
    @Override
    public Colour getColour(int index) {
        if (index < colours.size() && index >= 0) {
            return colours.get(index);
        } else {
            return colours.get(0);
        }
    }

    /**
     * Returns the colour that should be present at the given point
     * @param p The point which we are testing
     * @return The colour at the given point
     */
    protected Colour localColourAt(@NotNull Point p) {
        int index = (int)Math.floor(p.getX()) % 2;

        if (index == 0) {
            return colours.get(0);
        } else {
            return colours.get(1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stripes stripes = (Stripes) o;
        return colours.equals(stripes.colours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colours);
    }

    @Override
    public String toString() {
        return "Stripes{" +
            "colours=" + colours +
            '}';
    }
}

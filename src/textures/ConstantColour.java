package textures;

import features.Colour;
import features.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Pattern that defines a single colour for use in materials.  Replaces the idea
 * of having a single colour component embedded within the material itself
 *
 * @author Mark Edwards
 * @version July 18th, 2022
 */
public class ConstantColour extends Pattern {
    private Colour colour;

    /**
     * Generic constructor - assigns a white material
     */
    public ConstantColour() {
        this(new Colour(1,1, 1)); // White is the default colour
    }

    /**
     * Constructor allowing user to specify the colour
     * @param c The colour to assign
     */
    public ConstantColour(@NotNull Colour c) {
        super();
        this.colour = new Colour(c);
    }

    /**
     * Copy constructor
     * @param other The other ConstantColour object to duplicate
     */
    public ConstantColour(@NotNull ConstantColour other) {
        this.colour = new Colour(other.colour);
    }

    /**
     * @return The colour of this pattern
     */
    @Override
    public Colour getColour() {
        return new Colour(colour);
    }

    @Override
    public Colour getColour(int index) {
        return getColour();
    }

    /**
     * @param c Update the colour to the new value
     */
    public void setColour(@NotNull Colour c) {
        colour = new Colour(c);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstantColour that = (ConstantColour) o;
        return Objects.equals(colour, that.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour);
    }

    @Override
    public String toString() {
        return "ConstantColour{" +
            "colour=" + colour +
            '}';
    }

    @Override
    public Colour colourAt(@NotNull Point p) {
        return getColour();
    }
}

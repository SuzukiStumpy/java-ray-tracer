package textures;

import features.Colour;
import features.Point;
import org.jetbrains.annotations.NotNull;

public class TestPattern extends Pattern {

    @Override
    public Colour getColour() {
        return null;
    }

    @Override
    public Colour getColour(int index) {
        return null;
    }

    @Override
    protected Colour localColourAt(@NotNull Point p) {
        return new Colour(p.getX(), p.getY(), p.getZ());
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}

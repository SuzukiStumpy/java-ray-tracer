package features.lights;

import features.Colour;
import features.Point;
import org.jetbrains.annotations.NotNull;

/**
 * Class to simulate a point light.  Currently all functionality is contained
 * within this class rather than the superclass.  This will likely change as
 * new light types are added
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class PointLight extends Light {

    /**
     * Creates a new light at the specified position with the specified intensity (colour)
     * @param position Position of the light
     * @param intensity The colour of the light
     */
    public PointLight(@NotNull Point position, @NotNull Colour intensity) {
        super(position, intensity);
    }
}

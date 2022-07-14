package objects;

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

    /**
     * Constructor: generates a unique ID for each generated shape
     */
    public Shape() {
        id = UUID.randomUUID();
    }

    /**
     * @return Gets the unique, internal identifier for a shape.
     */
    public UUID getId() {
        return id;
    }
}

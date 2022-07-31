package objects;

import features.Material;

/**
 * Test object.  Extends the Sphere class to generate a default sphere with
 * a glassy material value.  Useful for testing raytracer code.
 *
 * @author Mark Edwards
 * @version July 23rd, 2022
 */
public class GlassSphere extends Sphere {
    public GlassSphere() {
        super();
        Material m = new Material();
        m.setTransparency(1.0);
        m.setRefractiveIndex(1.5);

        this.setMaterial(m);
    }

    @Override
    public String toString() {
        return "GlassSphere{transform: "+ getTransform() +", Material: "+ getMaterial() +"}";
    }
}

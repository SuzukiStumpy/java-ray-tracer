package features;

import org.jetbrains.annotations.NotNull;
import textures.*;

import java.util.Objects;

/**
 * Implements a material corresponding to the Phong lighting model.
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class Material {
    // The basic attributes of a surface material
    private Pattern pattern;
    //private Colour colour;
    private double ambient;
    private double diffuse;
    private double specular;
    private double shininess;
    private double reflectivity;
    private double transparency;
    private double refIdx;

    /**
     * Default constructor.  Produces a default material with the properties:
     *  Colour:   White (1, 1, 1)
     *  Ambient:             0.1
     *  Diffuse:             0.9
     *  Specular:            0.9
     *  Shininess:         200.0
     *  Reflectivity:        0.0
     *  Transparency:        0.0
     *  Refractive Index:    1.0
     */
    public Material() {
        pattern = new ConstantColour(new Colour(1,1,1));
        ambient = 0.1;
        diffuse = 0.9;
        specular = 0.9;
        shininess = 200;
        reflectivity = 0.0;
        transparency = 0.0;
        refIdx = 1.0;
    }

    /**
     * Copy constructor to duplicate a material
     * @param other The material we wish to duplicate
     */
    public Material(@NotNull Material other) {
        //TODO:  Need to figure out how to actually make a copy of the pattern
        //       in other
        pattern = other.pattern;
        ambient = other.ambient;
        diffuse = other.diffuse;
        specular = other.specular;
        shininess = other.shininess;
        reflectivity = other.reflectivity;
        transparency = other.transparency;
        refIdx = other.refIdx;
    }

    //
    // Getters and setters:
    //
    /**
     * Set a basic colour on the material (included so that older code doesn't
     * break)
     * @param c The colour we wish to assign
     */
    public void setColour(@NotNull Colour c) {
        pattern = new ConstantColour(c);
    }

    /**
     * Included so that older code doesn't break.  If pattern is a constant colour
     * then returns that colour.  Otherwise, returns the colour from the material
     * with index zero.
     * @return The retrieved colour.  See the method description for specifics
     */
    public Colour getColour() {
        return pattern.getColour();
    }

    /**
     * Set the pattern in the material to something else...
     * @param p The pattern we wish to set
     */
    public void setPattern(@NotNull Pattern p) {
        pattern = p;
    }

    /**
     * Gets the material colour at a given point on the object
     * @param p The point at which we wish to return the colour
     * @return The colour at the given point
     */
    public Colour colourAt(@NotNull Point p) {
        return pattern.colourAt(p);
    }

    /**
     * Get the pattern stored in this material
     * @return The pattern assigned to the material
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * @return The current ambient value
     */
    public double getAmbient() {
        return ambient;
    }

    /**
     * @param val Update the ambient value to 'val'
     */
    public void setAmbient(double val) {
        ambient = val;
    }

    /**
     * @return The current diffuse value
     */
    public double getDiffuse() {
        return diffuse;
    }

    /**
     * @param val Set the diffuse value to val
     */
    public void setDiffuse(double val) {
        diffuse = val;
    }

    /**
     * @return The current specular value
     */
    public double getSpecular() {
        return specular;
    }

    /**
     * @param val Set the specular value to 'val'
     */
    public void setSpecular(double val) {
        specular = val;
    }

    /**
     * @return The current shininess value
     */
    public double getShininess() {
        return shininess;
    }

    /**
     * @param val Set the current shininess to 'val'
     */
    public void setShininess(double val) {
        shininess = val;
    }

    /**
     * @return Gets the refelctivity value for this material
     */
    public double getReflectivity() {
        return reflectivity;
    }

    /**
     * Allows us to set the reflectivity of the material.
     * 0.0 is non-reflective (default), 1.0 is 100% mirror.
     * @param value How reflective the material is to be (0.0 -> 1.0)
     */
    public void setReflectivity(double value) {
        reflectivity = value;
    }

    /**
     * Set the material's transparency from 0-100% (0.0-1.0)
     * @param value The transparency percentage we want to set.
     */
    public void setTransparency(double value) { transparency = value; }

    /**
     * @return The material's transparency
     */
    public double getTransparency() { return transparency; }

    /**
     * Set the refractive index of a material (only useful if the object has
     * some level of transparency, otherwise has no effect.
     * @param value The refractive index to set.
     */
    public void setRefractiveIndex(double value) {
        refIdx = value;
    }

    /**
     * @return The refractive index of the material
     */
    public double getRefractiveIndex() {
        return refIdx;
    }
    //
    // Standard methods for equality and console output
    //
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Double.compare(material.ambient, ambient) == 0 && Double.compare(material.diffuse, diffuse) == 0 && Double.compare(material.specular, specular) == 0 && Double.compare(material.shininess, shininess) == 0 && pattern.equals(material.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, ambient, diffuse, specular, shininess);
    }

    @Override
    public String toString() {
        return "Material{" +
            "pattern=" + pattern +
            ", ambient=" + ambient +
            ", diffuse=" + diffuse +
            ", specular=" + specular +
            ", shininess=" + shininess +
            ", reflectivity=" + reflectivity +
            ", transparency=" + transparency +
            ", refIdx=" + refIdx +
            '}';
    }

}

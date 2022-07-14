package features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Implements a material corresponding to the Phong lighting model.
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class Material {
    // The basic attributes of a surface material
    private Colour colour;
    private double ambient;
    private double diffuse;
    private double specular;
    private double shininess;

    /**
     * Default constructor.  Produces a default material with the properties:
     *  Colour:    White (1, 1, 1)
     *  Ambient:   0.1
     *  Diffuse:   0.9
     *  Specular:  0.9
     *  Shininess: 200.0
     */
    public Material() {
        colour = new Colour(1,1,1);
        ambient = 0.1;
        diffuse = 0.9;
        specular = 0.9;
        shininess = 200;
    }

    /**
     * Copy constructor to duplicate a material
     * @param other The material we wish to duplicate
     */
    public Material(@NotNull Material other) {
        colour = new Colour(other.colour);
        ambient = other.ambient;
        diffuse = other.diffuse;
        specular = other.specular;
        shininess = other.shininess;
    }

    //
    // Getters and setters:
    //
    /**
     * @return The current colour of the material
     */
    public Colour getColour() {
        return new Colour(colour);
    }

    /**
     * @param c Update the colour to the new value
     */
    public void setColour(@NotNull Colour c) {
        colour = new Colour(c);
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

    //
    // Standard methods for equality and console output
    //
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Double.compare(material.ambient, ambient) == 0 && Double.compare(material.diffuse, diffuse) == 0 && Double.compare(material.specular, specular) == 0 && Double.compare(material.shininess, shininess) == 0 && colour.equals(material.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour, ambient, diffuse, specular, shininess);
    }

    @Override
    public String toString() {
        return "Material{" +
            "colour=" + colour +
            ", ambient=" + ambient +
            ", diffuse=" + diffuse +
            ", specular=" + specular +
            ", shininess=" + shininess +
            '}';
    }

}

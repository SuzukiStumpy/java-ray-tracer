package features;


import features.lights.Light;
import features.lights.PointLight;
import objects.Shape;
import objects.Sphere;
import org.jetbrains.annotations.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * World object collects all the lights and objects in a scene
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class World {
    // Make use of the logger for output
    private static final Logger log = LogManager.getLogger(World.class);
    private ArrayList<Shape> objects;
    private ArrayList<Light> lights;

    /**
     * Default constructor.  Initialises an empty World
     */
    public World() {
        log.debug("Creating new, empty World");

        objects = new ArrayList<>();
        lights = new ArrayList<>();
    }

    /**
     * @return Number of objects in the World
     */
    public int objectCount() {
        log.debug("World Object count.  There are "+ objects.size() +" objects here.");
        return objects.size();
    }

    /**
     * @return Number of lights in the world
     */
    public int lightCount() {
        log.debug("World light count.  There are "+ lights.size() +" lights here.");
        return lights.size();
    }

    /**
     * Remove all the existing lights
     */
    public void clearLights() {
        log.debug("Clearing all lights from the world.");
        lights.clear();
    }

    /**
     * Remove all the existing objects
     */
    public void clearObjects() {
        log.debug("Clearing all objects from the world.");
        objects.clear();
    }

    /**
     * Adds a light to the world model
     * @param light The light we want to add
     */
    public void addLight(@NotNull Light light) {
        log.debug("Adding light "+ light +" to the world.");
        lights.add(light);
    }

    /**
     * Adds an object to the world model
     * @param object The object we want to add
     */
    public void addObject(@NotNull Shape object) {
        log.debug("Adding object "+ object +" to the world.");
        objects.add(object);
    }

    //TODO: Probably should look at having the below two methods return
    //      an iterator to the collections rather than references to the
    //      collections themselves...
    /**
     * Returns a list of the lights stored in this world
     * @return The list of lights we have
     */
    public ArrayList<Light> getLights() {
        return lights;
    }

    /**
     * Returns a list of the objects stored in this world
     * @return The list of objects we have
     */
    public ArrayList<Shape> getObjects() {
        return objects;
    }

    /**
     * Generates a default world containing two spheres and a single
     * point light.
     * @return The generated World object
     */
    public static @NotNull World defaultWorld() {
        log.debug("Setting up a default World...");

        World w = new World();
        PointLight light = new PointLight(new Point(-10, 10, -10), new Colour(1,1,1));
        w.addLight(light);

        Sphere s1 = new Sphere();
        Material m1 = new Material();
        m1.setColour(new Colour(0.8, 1.0, 0.6));
        m1.setDiffuse(0.7);
        m1.setSpecular(0.2);
        s1.setMaterial(m1);
        w.addObject(s1);

        Shape s2 = new Sphere();
        s2.setTransform(Matrix.scaling(0.5,0.5,0.5));
        w.addObject(s2);

        log.debug("Default World setup complete.");

        return w;
    }

    /**
     * Compute the shading for a given point in the world space
     * @param comps The precomputed ray/intersection vectors
     * @param remaining The number of recursion calls we can still make
     * @return The colour of the canvas at the current point.
     */
    public Colour shadeHit(@NotNull Precompute comps, int remaining) {
        log.debug("Calculating ShadeHit in World.");
        log.debug("Computations:\n" + comps);
        log.debug("Remaining recursion limit: "+ remaining);

        Colour c = new Colour(0,0,0);
        Colour reflect = new Colour(0,0,0);
        Colour refract = new Colour(0,0,0);

        for (Light light: lights) {
            log.debug("Computing colour for light "+ light);

            Colour colourAtPoint = Light.lighting(comps.object.getMaterial(),
                comps.object,
                light,
                comps.over_point,
                comps.eye,
                comps.normal,
                isShadowed(comps.over_point));

            c = c.add(colourAtPoint);
            log.debug("Current aggregate diffuse colour: "+ c);

            reflect = reflect.add(this.reflectedColour(comps, remaining));
            log.debug("Current aggregate reflected colour: "+ reflect);

            refract = refract.add(this.refractedColour(comps, remaining));
            log.debug("Current aggregate refracted colour: "+ refract);
        }

        log.debug("Done calculating shadeHit.  Component colours:");
        log.debug("  -- Diffuse: "+ c);
        log.debug("  -- Reflect: "+ reflect);
        log.debug("  -- Refract: "+ reflect);
        log.debug("  -- Combined: "+ c.add(reflect).add(refract));

        Material m = comps.object.getMaterial();

        // If we have a reflective and transparent surface, apply the fresnel effect to it.
        if (m.getReflectivity() > 0 && m.getTransparency() >0) {
            return c.add(reflect.multiply(comps.reflectance)).add(refract.multiply(1-comps.reflectance));
        } else {
            return c.add(reflect).add(refract);
        }
    }

    /**
     * Determine the colour at the point where a ray intersect the world
     * @param r The ray we are shooting into the world
     * @param remaining The number of recursion calls we can still make
     * @return The colour at the point the ray intersects something
     */
    public Colour colourAt(@NotNull Ray r, int remaining) {
        log.debug("Computing colourAt in World for Ray "+ r);
        log.debug("Recursion depth remaining: "+ remaining);

        ArrayList<Intersection> xs = r.intersect(this);
        Intersection hit = Intersection.hit(xs);
        log.debug("Ray hit: "+ hit);

        if (hit == null) {
            log.debug("No hit, so returning black!");
            return new Colour(0, 0, 0);
        } else {
            // For transparency and refraction, we need to send the list of intersections
            // into precompute...
            Precompute comps = new Precompute(hit, r, xs);
            log.debug("Generating new precompute: "+ comps);

            return shadeHit(comps, remaining);
        }
    }

    /**
     * Compute the view transformation matrix for the world
     * @param from Originating point
     * @param to Point we want to transform the origin to
     * @param up Direction of 'up'
     * @return The computed matrix.
     */
    public static Matrix view_transform(@NotNull Point from, @NotNull Point to, @NotNull Vector up) {
        log.debug("Generating new view transform.");

        Vector forward = to.subtract(from).normalize();
        Vector upn = up.normalize();
        Vector left = forward.cross(upn);
        Vector true_up = left.cross(forward);
        double[][] oVals = new double[][] {
            {left.getX(), left.getY(), left.getZ(), 0},
            {true_up.getX(), true_up.getY(), true_up.getZ(), 0},
            {-forward.getX(), -forward.getY(), -forward.getZ(), 0},
            {0, 0, 0, 1}
        };
        Matrix orientation = new Matrix(oVals);
        Matrix xform = orientation.multiply(Matrix.translation(-from.getX(), -from.getY(), -from.getZ()));
        log.debug("View transform computed as:\n"+ xform);
        return xform;
    }

    /**
     * Determines whether a point in the world is in shadow.  Determined by whether an object lies between the point
     * and any light source
     * @param p The point we wish to test
     * @return True if the point is in shadow, false otherwise.
     */
    public boolean isShadowed(@NotNull Point p) {
        log.debug("Calling isShadowed for point "+ p);
        //TODO: Need to change this to work with multiple lights at some stage
        Vector v = lights.get(0).getPosition().subtract(p);
        double distance = v.magnitude();
        Vector direction = v.normalize();

        Ray r = new Ray(p, direction);
        ArrayList<Intersection> xs = r.intersect(this);
        Intersection hit = Intersection.hit(xs);

        // If the object hit does not cast shadows, then remove the hit from xs and
        // try again
        while (hit != null && !hit.getShape().castsShadow()) {
            xs.remove(hit);
            hit = Intersection.hit(xs);
        }

        log.debug("isShadowed returning "+ (hit != null && hit.getTime() < distance));
        return (hit != null && hit.getTime() < distance);
    }

    /**
     * Determines the colour returned by a reflection ray
     * @param comps The precomputed vectors and objects
     * @param remaining The number of recursion calls we can make
     * @return The colour reflected
     */
    public Colour reflectedColour(@NotNull Precompute comps, int remaining) {
        log.debug("Computing reflectedColour.");
        log.debug("Computations: "+ comps);
        log.debug("Recursion depth remaining: "+ remaining);

        if (comps.object.getMaterial().getReflectivity() == 0 || remaining == 0) {
            log.debug("Material is non-reflective or we have no recursions remaining, returning black");
            return new Colour(0,0,0);
        }

        Ray reflectRay = new Ray(comps.over_point, comps.reflectv);
        Colour c = this.colourAt(reflectRay, remaining-1);
        log.debug("Base reflected colour: "+ c);
        log.debug(" -- material reflectivity is "+ comps.object.getMaterial().getReflectivity());
        log.debug(" -- Returning colour: "+ c.multiply(comps.object.getMaterial().getReflectivity()));

        return c.multiply(comps.object.getMaterial().getReflectivity());
    }

    /**
     * Determines the colour returned by a refracted ray
     * @param comps The precomputed vectors and objects
     * @param remaining The number of recursion calls we can make
     * @return The colour refracted
     */
    public Colour refractedColour(@NotNull Precompute comps, int remaining) {
        log.debug("Computing refractedColour.");
        log.debug("Computations: "+ comps);
        log.debug("Recursion depth remaining: "+ remaining);

        if (comps.object.getMaterial().getTransparency() == 0 || remaining == 0) {
            log.debug("Material is opaque, or no recursion left.  Returning black");
            return new Colour(0,0,0);
        }

        // Check for total internal reflection using Snell's Law.
        // Get the ratio of first IoR to second:
        double n_ratio = comps.n1 / comps.n2;
        double cos_i = comps.eye.dot(comps.normal);
        double sin2_t = (n_ratio*n_ratio) * (1 - (cos_i * cos_i));

        log.debug(" -- n_ratio: "+ n_ratio);
        log.debug(" -- cos_i: "+ cos_i);
        log.debug(" -- sin2_t: "+ sin2_t);

        // If sin2_t > 1 then we have a case of total internal reflection, so return black
        if (sin2_t > 1) {
            log.debug("Returning black due to total internal reflection.");
            return new Colour(0,0,0);
        }

        // Now compute the refracted ray...
        double cos_t = Math.sqrt(1.0 - sin2_t);
        log.debug(" -- cos_t: "+ cos_t);

        // Compute the refracted ray's direction
        Vector direction = comps.normal.multiply((n_ratio * cos_i) - cos_t).subtract(comps.eye.multiply(n_ratio));
        log.debug(" -- direction vector: "+ direction);

        // Create the refracted ray
        Ray refract_ray = new Ray(comps.under_point, direction);
        log.debug(" -- Refracted ray: "+ refract_ray);

        // Find the colour of the refracted ray, multiplying by transparency to account
        // for any opacity
        log.debug(" -- Determining colour of the refracted ray.");
        Colour c = colourAt(refract_ray, remaining-1).multiply(comps.object.getMaterial().getTransparency());
        log.debug(" -- Refracted colour: "+ c);
        return c;
    }
}

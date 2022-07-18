package features;

import features.lights.Light;
import features.lights.PointLight;
import objects.Shape;
import objects.Sphere;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;

/**
 * World object collects all the lights and objects in a scene
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class World {
    private ArrayList<Shape> objects;
    private ArrayList<Light> lights;

    /**
     * Default constructor.  Initialises an empty World
     */
    public World() {
        objects = new ArrayList<>();
        lights = new ArrayList<>();
    }

    /**
     * @return Number of objects in the World
     */
    public int objectCount() {
        return objects.size();
    }

    /**
     * @return Number of lights in the world
     */
    public int lightCount() {
        return lights.size();
    }

    /**
     * Remove all the existing lights
     */
    public void clearLights() {
        lights.clear();
    }

    /**
     * Remove all the existing objects
     */
    public void clearObjects() {
        objects.clear();
    }

    /**
     * Adds a light to the world model
     * @param light The light we want to add
     */
    public void addLight(@NotNull Light light) {
        lights.add(light);
    }

    /**
     * Adds an object to the world model
     * @param object The object we want to add
     */
    public void addObject(@NotNull Shape object) {
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

        return w;
    }

    /**
     * Compute the shading for a given point in the world space
     * @param comps The precomputed ray/intersection vectors
     * @return The colour of the canvas at the current point.
     */
    public Colour shadeHit(@NotNull Precompute comps) {
        Colour c = new Colour(0,0,0);

        for (Light light: lights) {
            Colour colourAtPoint = Light.lighting(comps.object.getMaterial(),
                light,
                comps.point,
                comps.eye,
                comps.normal);

            c = c.add(colourAtPoint);
        }
        return c;
    }

    /**
     * Determine the colour at the point where a ray intersect the world
     * @param r The ray we are shooting into the world
     * @return The colour at the point the ray intersects something
     */
    public Colour colourAt(@NotNull Ray r) {
        ArrayList<Intersection> xs = r.intersect(this);
        Intersection hit = Intersection.hit(xs);

        if (hit == null) {
            return new Colour(0, 0, 0);
        } else {
            Precompute comps = new Precompute(hit, r);
            return shadeHit(comps);
        }
    }

    public static Matrix view_transform(@NotNull Point from, @NotNull Point to, @NotNull Vector up) {
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
        return orientation.multiply(Matrix.translation(-from.getX(), -from.getY(), -from.getZ()));
    }
}

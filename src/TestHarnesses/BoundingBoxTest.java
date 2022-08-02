package TestHarnesses;

import features.*;
import features.lights.PointLight;
import objects.Cylinder;
import objects.Group;
import objects.Plane;
import objects.Sphere;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import textures.CheckerTexture;

public class BoundingBoxTest {
    public static void main(String[] args) {
        // Switch logging off for running full test
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newRootLogger(Level.OFF));
        LoggerContext ctx = Configurator.initialize(builder.build());

        World w = new World();

        PointLight light = new PointLight(new Point(-7, 11, -6), new Colour(1,1,1));
        w.addLight(light);

        Plane floor = new Plane();
        Material m = new Material();
        m.setPattern(new CheckerTexture(new Colour(0.8, 0.6, 0.1), new Colour(0.2, 0.6, 0.15)));
        floor.setMaterial(m);
        floor.setTransform(Matrix.translation(0, -5, 0));
        w.addObject(floor);

        Group[] groups = new Group[10];

        Material metal = new Material();
        metal.setShininess(10);
        metal.setColour(new Colour(0,0,0));
        metal.setSpecular(0.8);
        metal.setReflectivity(0.01);

        Material chrome = new Material();
        chrome.setShininess(300);
        chrome.setReflectivity(0.9);
        chrome.setDiffuse(0.2);
        chrome.setColour(new Colour(0,0,0));

        // Build a 3D grid of spheres and then test rendering times.
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    Sphere s = new Sphere();
                    s.setTransform(Matrix.translation(-5+i, -5+j, -5+k).scale(0.5, 0.5, 0.5));
                    Colour colour = new Colour( 0.1 * i, 0.1 * j, 0.1 * k);
                    if ((i+j+k) % 2 == 0) {
                        chrome.setColour(colour);
                        s.setMaterial(chrome);
                    } else {
                        metal.setColour(colour);
                        s.setMaterial(metal);
                    }

                    if (groups[k] == null) {
                        groups[k] = new Group();
                    }
                    groups[k].addObject(s);
                }
            }
        }

        for (Group g: groups) {
            w.addObject(g);
        }

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(-5, 4, -15),
            new Point(0,0,0),
            new Vector(0,1,0)));

        Canvas canvas = c.render(w);

        PPMWriter.saveCanvasToFile("GridTest.ppm", canvas);
    }
}

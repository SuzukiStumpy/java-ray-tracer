package TestHarnesses;

import features.*;
import features.lights.PointLight;
import objects.Cube;
import objects.Plane;
import objects.Sphere;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import textures.CheckerTexture;

public class CubeTest {
    public static void main(String[] args) {
        // Switch logging off for running full test
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newRootLogger(Level.OFF));
        LoggerContext ctx = Configurator.initialize(builder.build());

        World w = new World();

        PointLight light = new PointLight(new Point(-1, 3, -3), new Colour(1,1,1));
        w.addLight(light);

        Cube cube = new Cube();
        cube.setTransform(Matrix.translation(-1.5, -0.5, 0).scale(0.5, 0.5 ,0.5).rotate_y(Math.PI/3));
        Material m = new Material();
        m.setColour(new Colour(1,0,0));
        m.setReflectivity(0.1);
        m.setDiffuse(0.9);
        m.setShininess(50);
        cube.setMaterial(m);
        w.addObject(cube);

        Sphere sphere = new Sphere();
        m = new Material();
        m.setColour(new Colour(0,0,0));
        m.setReflectivity(1);
        m.setDiffuse(0);
        m.setSpecular(1);
        m.setShininess(500);
        sphere.setMaterial(m);
        w.addObject(sphere);

        Plane floor = new Plane();
        floor.setTransform(Matrix.translation(0, -1, 0));
        m = new Material();
        m.setPattern(new CheckerTexture());
        m.setReflectivity(0.2);
        m.setDiffuse(0.8);
        m.setSpecular(0.9);
        m.setShininess(200);
        floor.setMaterial(m);
        w.addObject(floor);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1, -6),
            new Point(0,0.5,0),
            new Vector(0,1,0)));

        Canvas image = c.render(w);

        PPMWriter.saveCanvasToFile("cubeTest.ppm", image);

    }
}

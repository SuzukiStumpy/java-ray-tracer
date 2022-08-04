package TestHarnesses;

import features.*;
import features.lights.PointLight;
import objects.BoundingBox;
import objects.Cylinder;
import objects.Plane;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import textures.CheckerTexture;

import java.io.IOException;
import java.time.LocalDateTime;

public class TeapotTest {
    public static void main(String[] args) throws IOException {
        // Switch logging off for running full test
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newRootLogger(Level.OFF));
        LoggerContext ctx = Configurator.initialize(builder.build());

        System.out.println("Beginning scene setup");

        World w = new World();

        PointLight light = new PointLight(new Point(-2, 4, -5), new Colour(1,1,1));
        w.addLight(light);

        Plane floor = new Plane();
        Material m = new Material();
        m.setPattern(new CheckerTexture(new Colour(0.8, 0.6, 0.1), new Colour(0.2, 0.6, 0.15)));
        floor.setMaterial(m);
        floor.setTransform(Matrix.translation(0,-1,0));
        w.addObject(floor);

        Plane wall = new Plane();
        m = new Material();
        m.setPattern(new CheckerTexture());
        m.setReflectivity(0.5);
        m.setDiffuse(0.5);
        m.setShininess(100);
        wall.setMaterial(m);
        Matrix transform = Matrix.identity(4).translate(0, 0, 50).rotate_x(Math.PI/2);
        wall.setTransform(transform);
        w.addObject(wall);

        ObjParser parser = new ObjParser();
        parser.load("./teapot.obj");
        parser.parse();
        w.addObject(parser.getModel());

        double fov = Math.PI/3; // Camera field of view

        // Compute the correct camera position based on the total bounding box for the scene
        BoundingBox bb = parser.getModel().bounds();

        // Compute the z offset (b)
        double A = bb.max().getX() - bb.min().getX();
        double a = A/2;
        double b = a / Math.tan(fov/2);
        b += b/4;  // Add an arbitrary amount.

        // Compute the x offset:
        double x = bb.max().getX() - a;

        // Compute the y offset:
        double y = bb.max().getY() - ((bb.max().getY() - bb.min().getY()) / 2);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(x, y, -b),
            bb.center(),
            new Vector(0,1,0)));

        System.out.println("Beginning render at: "+ LocalDateTime.now());

        Canvas canvas = c.render(w);

        System.out.println("Render complete.  Timestamp: "+ LocalDateTime.now());
        System.out.println("Writing output.");

        PPMWriter.saveCanvasToFile("testCylinderScene.ppm", canvas);
    }
}

package TestHarnesses;

import features.*;
import features.lights.PointLight;
import objects.Cone;
import objects.Cylinder;
import objects.Plane;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import textures.CheckerTexture;

public class ConeTest {
    public static void main(String[] args) {
        // Switch logging off for running full test
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newRootLogger(Level.OFF));
        LoggerContext ctx = Configurator.initialize(builder.build());

        World w = new World();

        PointLight light = new PointLight(new Point(-1, 3, -1), new Colour(1,1,1));
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

        Cone cone = new Cone();
        m = new Material();
        m.setReflectivity(0.2);
        m.setColour(new Colour(0.8,0.5,0.4));
        cone.setMaterial(m);
        cone.minY(-1);
        cone.maxY(0);
        cone.setTransform(Matrix.scaling(0.5, 1, 0.5));
        cone.closed(true);
        w.addObject(cone);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1.5, -5),
            new Point(0,0,0),
            new Vector(0,1,0)));

        Canvas canvas = c.render(w);

        PPMWriter.saveCanvasToFile("testConeScene.ppm", canvas);
    }
}

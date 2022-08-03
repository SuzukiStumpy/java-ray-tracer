package TestHarnesses;

import features.*;
import features.lights.PointLight;
import objects.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class TriangleTest {
    public static void main(String[] args) {
        // Switch logging off for running full test
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newRootLogger(Level.OFF));
        LoggerContext ctx = Configurator.initialize(builder.build());

        World w = new World();

        PointLight light = new PointLight(new Point(-2, 2, -2), new Colour(1,1,1));
        w.addLight(light);

        Triangle t = new Triangle(new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 0, 0));
        w.addObject(t);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1, -3),
            new Point(0,0,0),
            new Vector(0,1,0)));

        Canvas canvas = c.render(w);

        PPMWriter.saveCanvasToFile("HexTest.ppm", canvas);
    }
}

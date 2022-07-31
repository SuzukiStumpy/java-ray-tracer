package TestHarnesses;

import features.*;
import features.lights.Light;
import features.lights.PointLight;
import objects.Plane;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class ReflectiveWorld {
    public static void main(String[] args) {
        // Switch logging off for running full test
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newRootLogger(Level.OFF));
        LoggerContext ctx = Configurator.initialize(builder.build());

        World w = World.defaultWorld();

        Material m = new Material();
        m.setReflectivity(0.8);

        Plane p1 = new Plane();
        p1.setTransform(Matrix.translation(0, -1, 0));
        p1.setMaterial(m);
        w.addObject(p1);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 2, -8),
            new Point(0,1,0),
            new Vector(0,1,0)));

        Canvas image = c.render(w);

        PPMWriter.saveCanvasToFile("reflectTest.ppm", image);

    }
}

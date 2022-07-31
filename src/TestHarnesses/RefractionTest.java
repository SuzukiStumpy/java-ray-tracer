package TestHarnesses;

import features.*;
import features.lights.PointLight;
import objects.Plane;
import objects.Sphere;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import textures.CheckerTexture;

import java.util.Collections;
import java.util.List;

public class RefractionTest {
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
        m.setPattern(new CheckerTexture());
        floor.setMaterial(m);
        floor.setTransform(Matrix.translation(0,-1,0));
        w.addObject(floor);

        Sphere s = new Sphere();
        m = s.getMaterial();
        m.setRefractiveIndex(1.5);
        m.setTransparency(1.0);
        m.setReflectivity(0.0);
        m.setDiffuse(0.1);
        s.setMaterial(m);
        w.addObject(s);


        s = new Sphere();
        m = s.getMaterial();
        m.setRefractiveIndex(1.0);
        m.setTransparency(1.0);
        m.setReflectivity(0.0);
        m.setDiffuse(0.1);
        s.setMaterial(m);
        s.setTransform(Matrix.scaling(0.5,0.5,0.5));
        w.addObject(s);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 3, 0),
            new Point(0,1,0),
            new Vector(0,0,1)));

        Canvas canvas = c.render(w);

        PPMWriter.saveCanvasToFile("testRefraction.ppm", canvas);
    }
}

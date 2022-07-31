package TestHarnesses;

import features.*;
import features.lights.PointLight;
import objects.GlassSphere;
import objects.Plane;
import objects.Sphere;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import textures.CheckerTexture;
import textures.Pattern;

public class FresenlSceneTest {
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
        wall.setMaterial(m);
        Matrix transform = Matrix.identity(4).translate(0, 0, 100).rotate_x(Math.PI/2);
        wall.setTransform(transform);
        w.addObject(wall);

        Plane surface = new Plane();
        m = new Material();
        m.setReflectivity(0.5);
        m.setDiffuse(0.2);
        m.setAmbient(0.2);
        m.setRefractiveIndex(1.25);
        m.setTransparency(1.0);
        m.setColour(new Colour(0, 0.01, 0.1));
        surface.setMaterial(m);
        surface.setTransform(Matrix.translation(0, -0.5, 0));
        surface.castsShadow(false);
        w.addObject(surface);

        Sphere s = new GlassSphere();
        m = s.getMaterial();
        m.setRefractiveIndex(1.2);
        m.setTransparency(1.0);
        m.setReflectivity(0.0);
        m.setDiffuse(0.1);
        s.setMaterial(m);
        w.addObject(s);


        s = new Sphere();
        m = s.getMaterial();
        m.setRefractiveIndex(1.0);
        m.setTransparency(0);
        m.setReflectivity(0);
        m.setColour(new Colour(1, 0, 0));
        s.setMaterial(m);
        s.setTransform(Matrix.translation(-1, 0, 3));
        w.addObject(s);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1, -5),
            new Point(0,0,0),
            new Vector(0,1,0)));

        Canvas canvas = c.render(w);

        PPMWriter.saveCanvasToFile("testFresnelScene.ppm", canvas);
    }
}

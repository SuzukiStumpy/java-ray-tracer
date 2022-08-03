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
import textures.CheckerTexture;

public class GroupTest {
    public static void main(String[] args) {
        // Switch logging off for running full test
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newRootLogger(Level.OFF));
        LoggerContext ctx = Configurator.initialize(builder.build());

        World w = new World();

        PointLight light = new PointLight(new Point(-2, 2, -2), new Colour(1,1,1));
        w.addLight(light);

        w.addObject(hexagon());

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1, -3),
            new Point(0,0,0),
            new Vector(0,1,0)));

        Canvas canvas = c.render(w);

        PPMWriter.saveCanvasToFile("HexTest.ppm", canvas);
    }

    private static Shape hexagonCorner() {
        Sphere corner = new Sphere();
        corner.setTransform(Matrix.translation(0,0,-1).scale(0.25,0.25,0.25));
        return corner;
    }

    private static Shape hexagonEdge() {
        Cylinder edge = new Cylinder();
        edge.minY(0);
        edge.maxY(1);
        edge.setTransform(Matrix.translation(0,0,-1).rotate_y(Math.PI/6).rotate_z(Math.PI/2).scale(0.25, 1, 0.25));
        return edge;
    }

    private static Shape hexagonSide() {
        Group side = new Group();
        side.addObject(hexagonCorner());
        side.addObject(hexagonEdge());
        return side;
    }

    private static Shape hexagon() {
        Group hex = new Group();

        for (int n=0; n<=5; n++) {
            Shape side = hexagonSide();
            side.setTransform(Matrix.rotation_y(n * Math.PI/3));
            hex.addObject(side);
        }
        return hex;
    }
}

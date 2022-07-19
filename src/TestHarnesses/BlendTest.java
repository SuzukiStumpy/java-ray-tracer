package TestHarnesses;

import features.*;
import features.lights.Light;
import features.lights.PointLight;
import objects.Plane;
import objects.Shape;
import objects.Sphere;
import textures.*;

public class BlendTest {
    public static void main(String[] args) {
        World w = new World();

        Plane floor = new Plane();
        Pattern s1 = new Stripes(new Colour(0.3, 0.3, 0.7), new Colour(0.7, 0.7, 0.7));
        Pattern s2 = new Stripes(new Colour(0.3, 0.3, 0.7), new Colour(0.7, 0.7, 0.7));
        s2.setTransform(Matrix.rotation_y(Math.PI/2));

        Pattern blend = new BlendedPattern(s1, s2, 0.5);

        Material m = new Material();
        m.setPattern(blend);
        floor.setMaterial(m);

        w.addObject(floor);

        Light light = new PointLight(new Point(-10, 10, -10), new Colour(1,1,1));

        w.addLight(light);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1.5, -5),
            new Point(0,1,0),
            new Vector(0,1,0)));

        Canvas image = c.render(w);

        PPMWriter.saveCanvasToFile("blendTest.ppm", image);
    }
}

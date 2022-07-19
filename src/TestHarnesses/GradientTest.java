package TestHarnesses;

import features.*;
import features.lights.Light;
import features.lights.PointLight;
import objects.Plane;
import objects.Shape;
import objects.Sphere;
import textures.*;

public class GradientTest {
    public static void main(String[] args) {
        World w = new World();

        Plane floor = new Plane();
        //Pattern p = new LinearGradient();
        //Pattern p = new RingTexture();
        Pattern p = new CheckerTexture();

        Material m = new Material();
        m.setPattern(p);
        floor.setMaterial(m);

        p = new CheckerTexture();
        p.setTransform(Matrix.scaling(0.1, 0.1, 0.1));
        m = new Material();
        m.setPattern(p);

        Shape s = new Sphere();
        s.setTransform(Matrix.translation(0, 1, 0));
        s.setMaterial(m);

        w.addObject(floor);
        w.addObject(s);

        Light light = new PointLight(new Point(-10, 10, -10), new Colour(1,1,1));

        w.addLight(light);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1.5, -5),
            new Point(0,1,0),
            new Vector(0,1,0)));

        Canvas image = c.render(w);

        PPMWriter.saveCanvasToFile("gradientTest.ppm", image);
    }
}

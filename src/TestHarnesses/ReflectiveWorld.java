package TestHarnesses;

import features.*;
import features.lights.Light;
import features.lights.PointLight;
import objects.Plane;

public class ReflectiveWorld {
    public static void main(String[] args) {
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

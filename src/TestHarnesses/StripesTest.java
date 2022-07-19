package TestHarnesses;

import features.*;
import features.lights.Light;
import features.lights.PointLight;
import objects.Plane;
import objects.Sphere;
import textures.ConstantColour;
import textures.Stripes;

public class StripesTest {
    public static void main(String[] args) {
        World w = new World();

        Stripes s = new Stripes(new Colour(1, 0.9, 0.9), new Colour(1, 0.3, 0.3));
        s.setTransform(Matrix.rotation_z(Math.PI/4).scale(0.75,0.75,0.75));
        Material m = new Material();
        m.setSpecular(0);
        m.setPattern(s);

        Plane floor = new Plane();
        floor.setTransform(Matrix.scaling(10, 0.01, 10));
        floor.setMaterial(m);

        Sphere middle = new Sphere();
        middle.setTransform(Matrix.identity(4)
            .translate(-0.5, 1, 0.5));
        Material middleMat = new Material();
        middleMat.setPattern(s);
        middleMat.setDiffuse(0.7);
        middleMat.setSpecular(0.3);
        middle.setMaterial(middleMat);

        Sphere right = new Sphere();
        right.setTransform(Matrix.identity(4)
            .translate(1.5,0.5,-0.5)
            .scale(0.5,0.5,0.5));
        Material rightMat = new Material();
        rightMat.setColour(new Colour(0.5, 1, 0.1));
        rightMat.setDiffuse(0.7);
        rightMat.setSpecular(0.3);

        s = new Stripes(new Colour(0.2, 0.9, 0.5), new Colour(0.3, 0.3, 0.4));
        s.setTransform(Matrix.rotation_x(Math.PI/6).rotate_y(Math.PI/4));
        m = new Material();
        m.setPattern(s);
        m.setDiffuse(0.7);
        m.setSpecular(0.7);
        right.setMaterial(m);

        Sphere left = new Sphere();
        left.setTransform(Matrix.identity(4)
            .translate(-1.5,0.33, -0.75)
            .scale(0.33,0.33, 0.33));
        Material leftMat = new Material();
        leftMat.setPattern(new ConstantColour(new Colour(0.7, 0.3, 0.3)));
        leftMat.setDiffuse(0.7);
        leftMat.setSpecular(0.3);
        left.setMaterial(leftMat);

        w.addObject(floor);
        w.addObject(left);
        w.addObject(middle);
        w.addObject(right);

        Light light = new PointLight(new Point(-10, 10, -10), new Colour(1,1,1));

        w.addLight(light);

        Camera c = new Camera(600, 300, Math.PI/3);
        c.setTransform(World.view_transform(
            new Point(0, 1.5, -5),
            new Point(0,1,0),
            new Vector(0,1,0)));

        Canvas image = c.render(w);

        PPMWriter.saveCanvasToFile("stripesTest.ppm", image);
    }
}

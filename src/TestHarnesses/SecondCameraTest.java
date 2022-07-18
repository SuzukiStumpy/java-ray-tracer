package TestHarnesses;

import features.*;
import features.lights.Light;
import features.lights.PointLight;
import objects.Plane;
import objects.Sphere;

public class SecondCameraTest {
    public static void main(String[] args) {
        World w = new World();

        Plane floor = new Plane();
        Material floorMat = new Material();
        floorMat.setColour(new Colour(1, 0.9, 0.9));
        floorMat.setSpecular(0);

        floor.setTransform(Matrix.scaling(10, 0.01, 10));
        floor.setMaterial(floorMat);

/*        Sphere leftWall = new Sphere();
        leftWall.setTransform(Matrix.identity(4)
            .translate(0,0,5)
            .rotate_y(-Math.PI/4)
            .rotate_x(Math.PI/2)
            .scale(10, 0.01, 10));
        leftWall.setMaterial(floorMat);

        Sphere rightWall = new Sphere();
        rightWall.setTransform(Matrix.identity(4)
            .translate(0,0,5)
            .rotate_y(Math.PI/4)
            .rotate_x(Math.PI/2)
            .scale(10,0.01,10));
        rightWall.setMaterial(floorMat);
*/
        Sphere middle = new Sphere();
        middle.setTransform(Matrix.identity(4)
            .translate(-0.5, 1, 0.5));
        Material middleMat = new Material();
        middleMat.setColour(new Colour(0.1, 1, 0.5));
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
        right.setMaterial(rightMat);

        Sphere left = new Sphere();
        left.setTransform(Matrix.identity(4)
            .translate(-1.5,0.33, -0.75)
            .scale(0.33,0.33, 0.33));
        Material leftMat = new Material();
        leftMat.setColour(new Colour(1, 0.8, 0.1));
        leftMat.setDiffuse(0.7);
        leftMat.setSpecular(0.3);
        left.setMaterial(leftMat);

        w.addObject(floor);
  //      w.addObject(leftWall);
  //      w.addObject(rightWall);
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

        PPMWriter.saveCanvasToFile("secondCameraTest.ppm", image);
    }
}

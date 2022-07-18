package TestHarnesses;

import features.*;
import objects.Sphere;

import java.util.ArrayList;

public class FirstRenderTest {
    public static void main(String[] args) {
        Point rayOrigin = new Point(0, 0, -5);
        double wall_z = 10;
        double wall_size = 7;
        int canvas_size = 500;
        double pixel_size = wall_size / canvas_size;
        double half = wall_size / 2;

        Canvas c = new Canvas(canvas_size, canvas_size);
        Colour red = new Colour(1, 0, 0);
        Sphere s = new Sphere();

        for (int row = 0; row < canvas_size; row++) {
            double world_y = half - pixel_size * row;

            for (int col = 0; col < canvas_size; col++) {
                double world_x = -half + pixel_size * col;

                Point pos = new Point(world_x, world_y, wall_z);
                Ray r = new Ray(rayOrigin, pos.subtract(rayOrigin).normalize());
                ArrayList<Intersection> xs = s.intersect(r);

                if (Intersection.hit(xs) != null) {
                    c.setPixel(row, col, red);
                }
            }
        }

        PPMWriter.saveCanvasToFile("firstSphereRender.ppm", c);
    }
}

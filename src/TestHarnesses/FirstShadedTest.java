package TestHarnesses;

import features.*;
import features.lights.Light;
import features.lights.PointLight;
import objects.Sphere;

import java.awt.image.PixelInterleavedSampleModel;
import java.util.ArrayList;

public class FirstShadedTest {
    public static void main(String[] args) {
        Point rayOrigin = new Point(0, 0, -5);
        double wall_z = 10;
        double wall_size = 7;
        int canvas_size = 500;
        double pixel_size = wall_size / canvas_size;
        double half = wall_size / 2;

        Canvas c = new Canvas(canvas_size, canvas_size);

        // Create a sphere and assign a material to it
        Sphere s = new Sphere();
        Material mat = new Material();
        mat.setColour(new Colour(1, 0.2, 1));
        s.setMaterial(mat);

        // Create a bright white point light
        PointLight light = new PointLight(
            new Point(-10, 10, -10),
            new Colour(1, 1, 1)
        );

        for (int row = 0; row < canvas_size; row++) {
            double world_y = half - pixel_size * row;

            for (int col = 0; col < canvas_size; col++) {
                double world_x = -half + pixel_size * col;

                Point pos = new Point(world_x, world_y, wall_z);
                Ray r = new Ray(rayOrigin, pos.subtract(rayOrigin).normalize());
                ArrayList<Intersection> xs = s.intersect(r);

                Intersection hit = Intersection.hit(xs);

                if (hit != null) {
                    // Since we have a hit, compute the shading at that point
                    Point p = r.getPosition(hit.getTime());
                    Vector normal = hit.getShape().normal_at(p);
                    Vector eye = r.getDirection().negate().toVector();

                    Colour colour = Light.lighting(hit.getShape().getMaterial(),
                        light, p, eye, normal, false);

                    c.setPixel(row, col, colour);
                }
            }
        }

        PPMWriter.saveCanvasToFile("firstShadedSphereRender.ppm", c);
    }
}

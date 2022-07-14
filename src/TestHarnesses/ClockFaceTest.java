package TestHarnesses;

import features.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ClockFaceTest {
    public static void main(String[] args) {
        Canvas c = new Canvas(500, 500);
        Point origin = new Point(0, 0, 0);

        double hour = (2 * Math.PI) / 12;

        for (int i = 0; i < 12; i++) {
            Point p = Matrix.identity(4)
                .translate(0, 0, 200)
                .rotate_y(hour * i)
                .multiply(origin)
                .toPoint();

            c.setPixel((int)(p.getX() + (c.getWidth() / 2)),
                (int)(p.getZ() + (c.getWidth() / 2)),
                new Colour(1, 1, 1));
        }

        PPMWriter.saveCanvasToFile("testClock.ppm", c);
    }
}

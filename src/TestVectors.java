import features.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TestVectors {
    public static void main(String[] args) {
        Projectile p = new Projectile(new Point(0,1,0), new Vector(1,1.8,0).normalize().multiply(11.25));
        Environment e = new Environment(new Vector(0,-0.1,0), new Vector(-0.01,0,0));

        Canvas c = new Canvas(900, 550);

        while(p.position.getY() > 0.0) {
            p = tick(e, p);
            int x = (int) Math.round(p.position.getX());
            int y = c.getHeight() - (int) Math.round(p.position.getY());
            c.setPixel(x, y, new Colour(1,0,0));
        }

        // Write the canvas to file...
        ArrayList<String> ppm = PPMWriter.canvas_to_ppm(c);

        try {
            FileWriter f = new FileWriter("testPlot.ppm");
            for (String line: ppm) {
                f.write(line +"\n");
            }
            f.close();
        }
        catch (IOException ex) {
            System.out.println("File IO Exception raised");
            ex.printStackTrace();
        }
    }

    public static Projectile tick(Environment env, Projectile p) {
        Point pos = p.position.add(p.velocity);
        Vector vel = p.velocity.add(env.gravity).add(env.wind);
        return new Projectile(pos, vel);
    }
}

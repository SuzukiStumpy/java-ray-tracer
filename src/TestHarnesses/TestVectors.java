package TestHarnesses;

import features.*;

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

        PPMWriter.saveCanvasToFile("testPlot.ppm", c);
    }

    public static Projectile tick(Environment env, Projectile p) {
        Point pos = p.position.add(p.velocity);
        Vector vel = p.velocity.add(env.gravity).add(env.wind);
        return new Projectile(pos, vel);
    }
}

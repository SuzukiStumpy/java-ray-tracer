package TestHarnesses;

import features.Point;
import features.Vector;

public class Projectile {
    public Point position;
    public Vector velocity;

    public Projectile(Point pos, Vector vel) {
        position = pos;
        velocity = vel;
    }
}

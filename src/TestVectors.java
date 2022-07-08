import features.Point;
import features.Tuple;
import features.Vector;

public class TestVectors {
    public static void main(String[] args) {
        Projectile p = new Projectile(new Point(0,1,0), new Vector(1,1,0).normalize());
        Environment e = new Environment(new Vector(0,-0.1,0), new Vector(-0.01,0,0));

        while(p.position.getY() > 0.0) {
            p = tick(e, p);
            System.out.println("Projectile position: "+ p.position);
            System.out.println("           velocity: "+ p.velocity);
        }
    }

    public static Projectile tick(Environment env, Projectile p) {
        Point pos = p.position.add(p.velocity);
        Vector vel = p.velocity.add(env.gravity).add(env.wind);
        return new Projectile(pos, vel);
    }
}

package features;

import objects.Shape;
import org.jetbrains.annotations.NotNull;

/**
 * Data structure to allow for the storage and precomputation of certain structures
 * so that they can be reused in multiple areas.
 *
 * @author Mark Edwards
 * @version July 14th, 2022
 */
public class Precompute {
    public static final double EPSILON = 0.00001;


    public double t;
    public Shape object;
    public Point point;
    public Point over_point;
    public Vector eye;
    public Vector normal;
    public boolean inside;

    public Precompute(@NotNull Intersection i, @NotNull Ray r) {
        t = i.getTime();
        object = i.getShape();
        point = r.getPosition(t);
        eye = r.getDirection().negate().toVector();
        normal = object.normal_at(point);
        over_point = point.add(normal.multiply(EPSILON));
        if (normal.dot(eye) < 0) {
            inside = true;
            normal = normal.negate().toVector();
        } else {
            inside = false;
        }
    }
}

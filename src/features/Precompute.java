package features;

import objects.Shape;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public Point under_point;
    public Vector eye;
    public Vector normal;
    public Vector reflectv;
    public boolean inside;
    public double n1;
    public double n2;
    public double reflectance;

    public Precompute(@NotNull Intersection hit, @NotNull Ray r, ArrayList<Intersection> intersections) {
        t = hit.getTime();
        object = hit.getShape();
        point = r.getPosition(t);
        eye = r.getDirection().negate().toVector();
        normal = object.normal_at(point);
        if (normal.dot(eye) < 0) {
            inside = true;
            normal = normal.negate().toVector();
        } else {
            inside = false;
        }
        over_point = point.add(normal.multiply(EPSILON));
        under_point = point.subtract(normal.multiply(EPSILON));
        reflectv = r.getDirection().reflect(normal);

        // For transparency and refraction, calculate n1 and n2
        ArrayList<Shape> containers = new ArrayList<>();

        for (Intersection i: intersections) {
            if (i.equals(hit)) {
                if (containers.isEmpty()) {
                    n1 = 1.0;
                } else {
                    n1 = containers.get(containers.size()-1).getMaterial().getRefractiveIndex();
                }
            }

            if (containers.contains(i.getShape())) {
                containers.remove(i.getShape());
            } else {
                containers.add(i.getShape());
            }

            if (i.equals(hit)) {
                if (containers.isEmpty()) {
                    n2 = 1.0;
                } else {
                    n2 = containers.get(containers.size()-1).getMaterial().getRefractiveIndex();
                }
                break; // We can terminate processing at this point...
            }
        }
        reflectance = schlick();
    }

    /**
     * Computes the schlick approximation of the Fresnel computations to determine the reflectance
     * of a surface when struck by a ray
     */
    private double schlick() {
        // Determine the cosine of the angle between the eye and the normal vectors
        double cos = eye.dot(normal);

        // If n1 > n2 then we have a case of total internal reflection
        if (n1 > n2) {
            double n = n1 / n2;
            double sin2_t = (n*n) * (1.0 - (cos*cos));
            if (sin2_t > 1.0) {
                return 1.0;
            }

            // Compute cos of theta_t using trig identity
            double cos_t = Math.sqrt(1.0 - sin2_t);

            // when n1 > n2 use cos(theta_t) instead
            cos = cos_t;
        }

        double r0 = Math.pow((n1 - n2) / (n1 + n2), 2);
        return r0 + (1-r0) * Math.pow(1-cos, 5);
    }

    /**
     * For backward compatability with older code - simply adds the current intersection
     * to the intersections list by default if none is provided.
     * @param i Intersection we are computing against
     * @param r Ray we are computing against
     */
    public Precompute(@NotNull Intersection i, @NotNull Ray r) {
        this(i, r, new ArrayList<>(List.of(i)));
    }

    @Override
    public String toString() {
        return "Precompute{" +
            "t=" + t +
            ", object=" + object +
            ", point=" + point +
            ", over_point=" + over_point +
            ", under_point=" + under_point +
            ", eye=" + eye +
            ", normal=" + normal +
            ", reflectv=" + reflectv +
            ", inside=" + inside +
            ", n1=" + n1 +
            ", n2=" + n2 +
            ", reflectance=" + reflectance +
            '}';
    }
}

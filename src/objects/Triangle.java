package objects;

import features.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static features.Precompute.EPSILON;

/**
 * Class which defines a triangle in 3D space.
 *
 * @author Mark Edwards
 * @version August 3rd, 2022
 */
public class Triangle extends Shape {
    private final Point[] points;
    private final Vector[] edges;
    private final Vector normal;

    /**
     * Constructor for a new triangle, given the three vertex points in 3D space
     * Points should be given in clockwise order.
     *
     * @param p1 The first point
     * @param p2 The second point
     * @param p3 The third point
     */
    public Triangle(@NotNull Point p1, @NotNull Point p2, @NotNull Point p3) {
        points = new Point[3];
        points[0] = new Point(p1);
        points[1] = new Point(p2);
        points[2] = new Point(p3);

        edges = new Vector[2];
        edges[0] = p2.subtract(p1).toVector();
        edges[1] = p3.subtract(p1).toVector();

        normal = edges[1].cross(edges[0]).normalize().toVector();
    }

    /**
     * Get the point at the given index.  Valid index range is [0..2], and
     * values will be clamped to that range.
     * @param index The point index to return
     * @return The point at the given index
     */
    public Point point(int index) {
        // Clamp index to a valid range
        index = Math.min(2, index);
        index = Math.max(0, index);
        return new Point(points[index]);
    }

    /**
     * Returns the edge vector at the given index:
     *  0: Vector between p0 -> p1
     *  1: Vector between p0 -> p2
     *
     * Invalid index values will be clamped.
     *
     * @param index Index of the edge we want to query.
     * @return The edge at the given index.
     */
    public Vector edge(int index) {
        // Clamp index to valid range
        index = Math.min(1, index);
        index = Math.max(0, index);
        return new Vector(edges[index]);
    }

    /**
     * @return The surface normal for the triangle.
     */
    public Vector normal() {
        return new Vector(normal);
    }


    @Override
    protected ArrayList<Intersection> local_intersect(@NotNull Ray ray) {
        ArrayList<Intersection> xs = new ArrayList<>();

        Vector direction = ray.getDirection();
        Vector dirCrossE1 = direction.cross(edges[1]);
        double det = dirCrossE1.dot(edges[0]);

        // If ray is parallel to triangle, return empty intersection list.
        if (Math.abs(det) < EPSILON) {
            return xs;
        }

        // Test for ray congruent with edge P0-P2 (E1)
        double f = 1/det;
        Vector p0ToOrigin = ray.getOrigin().subtract(points[0]).toVector();
        double u = f * p0ToOrigin.dot(dirCrossE1);

        if (u < 0 || u > 1) {
            return xs;
        }

        // Test for ray congruent with edges P0-P1 and P1-P2
        Vector originCrossE0 = p0ToOrigin.cross(edges[0]);
        double v = f * direction.dot(originCrossE0);

        if (v < 0 || (u+v) > 1) {
            return xs;
        }

        double t = f * edges[1].dot(originCrossE0);
        xs.add(new Intersection(t, this));
        return xs;
    }

    @Override
    protected Vector local_normal_at(@NotNull Point p) {
        return new Vector(normal);
    }

    @Override
    public BoundingBox bounds() {
        BoundingBox box = new BoundingBox();
        for (Point p: points) {
            box.add(p);
        }
        return box;
    }
}

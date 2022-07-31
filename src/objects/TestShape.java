package objects;

import features.Intersection;
import features.Point;
import features.Ray;
import features.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TestShape extends Shape {
    public Ray saved_ray;

    /**
     * Implementation for testing the intersection of rays.
     * TODO: Actually build the implementation.  Currently does nothing useful
     * @param ray The ray we wish to test for intersection
     * @return The list of intersections
     */
    @Override
    protected ArrayList<Intersection> local_intersect(@NotNull Ray ray) {
        saved_ray = ray;
        return new ArrayList<>();
    }

    @Override
    protected Vector local_normal_at(@NotNull Point p) {
        return p.toVector();
    }

    @Override
    public String toString() {
        return "TestShape{transform: "+ getTransform() +", Material: "+ getMaterial() +", saved_ray: "+ saved_ray +"}";
    }
}

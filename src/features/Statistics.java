package features;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class to log and report on object creation statistics
 */
public class Statistics {
    public static long intersections;
    public static long matrices;
    public static long points;
    public static long vectors;
    public static long tuples;
    public static long precomputes;
    public static long rays;

    @Contract(pure = true)
    public static String show() {
        return "Rendering statistics:\n" +
            "Tuples: "+ tuples +"\n"+
            "Points: "+ points +"\n"+
            "Vectors: "+ vectors +"\n"+
            "Matrices: "+ matrices +"\n"+
            "Rays: "+ rays +"\n"+
            "Intersections: "+ intersections +"\n"+
            "Precomputes: "+ precomputes;
    }
}

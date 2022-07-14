package TestHarnesses;

import features.Vector;

public class Environment {
    public Vector gravity;
    public Vector wind;

    public Environment(Vector g, Vector w) {
        gravity = g;
        wind = w;
    }
}

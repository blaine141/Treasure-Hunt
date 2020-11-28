package com.example.treasurehunt;

import java.io.Serializable;

public class Score implements Serializable {
    public double startDistance;
    public double endDistance;
    public int hints;
    public Cache cache;

    public Score(boolean found, double startDistance, Cache cache, int hints) {
        this.cache = cache;
        this.startDistance = startDistance;
        this.hints = hints;
        this.endDistance = cache.distance;
        if (found)
            this.endDistance = 0;
    }

    public double getAccuracy() {
        return (startDistance - endDistance) / startDistance;
    }

    public boolean isFound() {
        return endDistance == 0;
    }
}

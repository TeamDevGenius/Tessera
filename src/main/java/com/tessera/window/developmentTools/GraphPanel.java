package com.tessera.window.developmentTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph data panel – rewritten without javax.swing/java.awt for Android compatibility.
 * Data is still tracked; rendering is omitted (no Swing canvas available).
 */
public class GraphPanel {

    private final List<Double> scores = new ArrayList<>();
    private double maxScore = 1.0;

    public GraphPanel() {}

    public void addDataPoint(double score, double max) {
        scores.add(score);
        if (max > maxScore) maxScore = max;
        if (scores.size() > 200) scores.remove(0);
    }

    public void update() {
        // No-op: rendering not available outside a Swing context
    }
}

package com.tessera.window.developmentTools;

import java.util.ArrayList;
import java.util.List;

/** No-op stub replacing the Swing-based GraphPanel for Android/LibGDX. */
public class GraphPanel {

    public final List<Double> dataPoints = new ArrayList<>();

    public void update() {}

    public void setYBounds(double minScore, double maxScore) {}

    public void dontUseYBounds() {}

    public void addDataPoint(double score) {
        dataPoints.add(score);
    }

    public void addDataPoint(double score, int maxDataPoints) {
        if (dataPoints.size() > maxDataPoints) dataPoints.remove(0);
        dataPoints.add(score);
    }

    public void setPreferredSize(Object size) {}

    public void setVisible(boolean visible) {}

    public int getWidth() { return 800; }
    public int getHeight() { return 600; }
}

package com.tessera.window.developmentTools;

/** Memory graph – no-op stub for Android compatibility (javax.swing not available). */
public class MemoryGraph {
    GraphPanel percentPanel;

    public MemoryGraph() {
        percentPanel = new GraphPanel();
    }

    public void update() {
        // No-op on Android
    }

    public void updateMemoryPanel() {
        // No-op on Android
    }
}

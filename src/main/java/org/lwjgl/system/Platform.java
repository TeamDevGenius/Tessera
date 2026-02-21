package org.lwjgl.system;

/** Stub for LWJGL Platform. */
public enum Platform {
    LINUX, MACOSX, WINDOWS;

    public static Platform get() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("mac") || os.contains("darwin")) return MACOSX;
        if (os.contains("win")) return WINDOWS;
        return LINUX;
    }
}

package org.lwjgl.glfw;

/** Stub for LWJGL GLFWVidMode. */
public class GLFWVidMode {
    private final int width, height, redBits, greenBits, blueBits, refreshRate;

    public GLFWVidMode(int width, int height, int redBits, int greenBits, int blueBits, int refreshRate) {
        this.width = width; this.height = height;
        this.redBits = redBits; this.greenBits = greenBits; this.blueBits = blueBits;
        this.refreshRate = refreshRate;
    }

    public int width() { return width; }
    public int height() { return height; }
    public int redBits() { return redBits; }
    public int greenBits() { return greenBits; }
    public int blueBits() { return blueBits; }
    public int refreshRate() { return refreshRate; }
}

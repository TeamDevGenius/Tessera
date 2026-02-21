package org.lwjgl.nuklear;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL NkRect. */
public class NkRect {
    private float x, y, w, h;

    private NkRect() {}

    public static NkRect create() { return new NkRect(); }
    public static NkRect malloc() { return new NkRect(); }
    public static NkRect malloc(MemoryStack stack) { return new NkRect(); }
    public static NkRect calloc(MemoryStack stack) { return new NkRect(); }

    public float x() { return x; }
    public float y() { return y; }
    public float w() { return w; }
    public float h() { return h; }
    public NkRect x(float v) { this.x = v; return this; }
    public NkRect y(float v) { this.y = v; return this; }
    public NkRect w(float v) { this.w = v; return this; }
    public NkRect set(int x, int y, float w, float h) { this.x = x; this.y = y; this.w = w; this.h = h; return this; }
    public NkRect set(float x, float y, float w, float h) { this.x = x; this.y = y; this.w = w; this.h = h; return this; }
    public NkRect set(NkRect other) { this.x = other.x; this.y = other.y; this.w = other.w; this.h = other.h; return this; }
    public NkRect h(float v) { this.h = v; return this; }
}

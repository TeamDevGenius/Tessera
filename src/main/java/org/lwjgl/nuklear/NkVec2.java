package org.lwjgl.nuklear;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL NkVec2. */
public class NkVec2 {
    private float x, y;

    public NkVec2() {}
    public NkVec2(float x, float y) { this.x = x; this.y = y; }

    public static NkVec2 create() { return new NkVec2(); }
    public static NkVec2 malloc() { return new NkVec2(); }
    public static NkVec2 malloc(MemoryStack stack) { return new NkVec2(); }
    public static NkVec2 calloc(MemoryStack stack) { return new NkVec2(); }

    public float x() { return x; }
    public float y() { return y; }
    public NkVec2 x(float v) { this.x = v; return this; }
    public NkVec2 y(float v) { this.y = v; return this; }
    public NkVec2 set(float x, float y) { this.x = x; this.y = y; return this; }
    public NkVec2 set(int x, int y) { this.x = x; this.y = y; return this; }
}

package org.lwjgl.nuklear;

/** Stub for LWJGL NkMouse. */
public class NkMouse {
    private NkVec2 pos = NkVec2.create();
    private NkVec2 prev = NkVec2.create();

    public boolean grab() { return false; }
    public boolean grabbed() { return false; }
    public boolean ungrab() { return false; }
    public NkVec2 pos() { return pos; }
    public NkVec2 prev() { return prev; }
}

package org.lwjgl.nuklear;

/** Stub for LWJGL NkStyleProgress. */
public class NkStyleProgress {
    private NkStyleItem normal = NkStyleItem.create();
    private NkStyleItem hover = NkStyleItem.create();
    private NkStyleItem active = NkStyleItem.create();
    private NkStyleItem cursor_normal = NkStyleItem.create();
    private NkStyleItem cursor_hover = NkStyleItem.create();
    private NkStyleItem cursor_active = NkStyleItem.create();

    public NkStyleItem normal() { return normal; }
    public NkStyleItem hover() { return hover; }
    public NkStyleItem active() { return active; }
    public NkStyleItem cursor_normal() { return cursor_normal; }
    public NkStyleItem cursor_hover() { return cursor_hover; }
    public NkStyleItem cursor_active() { return cursor_active; }
    public NkStyleProgress border(float v) { return this; }
    public NkStyleProgress rounding(float v) { return this; }
}

package org.lwjgl.nuklear;

/** Stub for LWJGL NkStyleWindow. */
public class NkStyleWindow {
    private NkStyleItem fixed_background = NkStyleItem.create();
    private NkVec2 padding = NkVec2.create();
    private NkVec2 spacing = NkVec2.create();
    private NkColor border_color = NkColor.create();
    private NkColor background = NkColor.create();

    public NkStyleItem fixed_background() { return fixed_background; }
    public NkVec2 padding() { return padding; }
    public NkVec2 spacing() { return spacing; }
    public NkColor border_color() { return border_color; }
    public NkColor background() { return background; }
    public NkStyleWindow border(float v) { return this; }
    public NkStyleWindow rounding(float v) { return this; }
}

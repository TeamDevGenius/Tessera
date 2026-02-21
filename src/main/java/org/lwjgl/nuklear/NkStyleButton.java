package org.lwjgl.nuklear;

/** Stub for LWJGL NkStyleButton. */
public class NkStyleButton {
    private NkStyleItem normal = NkStyleItem.create();
    private NkStyleItem hover = NkStyleItem.create();
    private NkStyleItem active = NkStyleItem.create();
    private NkColor border_color = NkColor.create();
    private NkColor text_normal = NkColor.create();
    private NkColor text_hover = NkColor.create();
    private NkColor text_active = NkColor.create();

    public NkStyleItem normal() { return normal; }
    public NkStyleItem hover() { return hover; }
    public NkStyleItem active() { return active; }
    public NkColor border_color() { return border_color; }
    public NkColor text_normal() { return text_normal; }
    public NkColor text_hover() { return text_hover; }
    public NkColor text_active() { return text_active; }
    public NkStyleButton border(float v) { return this; }
    public float border() { return 0; }
    public NkStyleButton rounding(float v) { return this; }
    public NkStyleButton text_alignment(int align) { return this; }
    public NkVec2 padding() { return new NkVec2(0, 0); }}

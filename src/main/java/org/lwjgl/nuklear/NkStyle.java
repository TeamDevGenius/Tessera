package org.lwjgl.nuklear;

/** Stub for LWJGL NkStyle. */
public class NkStyle {
    private NkStyleWindow window = new NkStyleWindow();
    private NkStyleButton button = new NkStyleButton();
    private NkStyleProgress progress = new NkStyleProgress();
    private NkStyleText text = new NkStyleText();
    private NkUserFont font;

    private NkStyle() {}
    public static NkStyle create() { return new NkStyle(); }

    public NkStyleWindow window() { return window; }
    public NkStyleButton button() { return button; }
    public NkStyleProgress progress() { return progress; }
    public NkStyleText text() { return text; }
    public NkUserFont font() { return font; }
    public NkStyle font(NkUserFont f) { this.font = f; return this; }
}

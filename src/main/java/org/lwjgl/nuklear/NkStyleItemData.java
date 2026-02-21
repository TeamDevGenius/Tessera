package org.lwjgl.nuklear;

/** Stub for LWJGL NkStyleItemData. */
public class NkStyleItemData {
    private NkColor color = NkColor.create();
    private NkImage image = NkImage.create();

    public NkColor color() { return color; }
    public NkImage image() { return image; }
    public NkStyleItemData color(NkColor c) { this.color = c; return this; }
}

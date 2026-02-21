package org.lwjgl.nuklear;

/** Stub for LWJGL NkStyleItem. */
public class NkStyleItem {
    private NkStyleItemData data = new NkStyleItemData();

    private NkStyleItem() {}
    public static NkStyleItem create() { return new NkStyleItem(); }

    public NkStyleItemData data() { return data; }

    public static NkStyleItem nk_style_item_color(NkColor color) { return create(); }
    public static NkStyleItem nk_style_item_image(NkImage img) { return create(); }
}

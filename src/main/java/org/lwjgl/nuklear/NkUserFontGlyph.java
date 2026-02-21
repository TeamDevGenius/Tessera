package org.lwjgl.nuklear;

/** Stub for LWJGL NkUserFontGlyph. */
public class NkUserFontGlyph {
    private NkUserFontGlyph() {}
    public static NkUserFontGlyph create() { return new NkUserFontGlyph(); }
    public static NkUserFontGlyph create(long address) { return new NkUserFontGlyph(); }
    public static NkUserFontGlyph malloc(org.lwjgl.system.MemoryStack stack) { return new NkUserFontGlyph(); }

    public NkUserFontGlyph width(float w) { return this; }
    public NkUserFontGlyph height(float h) { return this; }
    public NkUserFontGlyph xadvance(float xadv) { return this; }
    public NkVec2 uv(int index) { return new NkVec2(0, 0); }
    public NkVec2 offset() { return new NkVec2(0, 0); }

    public float width() { return 0; }
    public float height() { return 0; }
    public float xadvance() { return 0; }
    public long address() { return 0L; }
}

package org.lwjgl.nuklear;

/** Stub for LWJGL NkUserFont. */
public class NkUserFont {
    private NkHandle userData = NkHandle.create();
    private NkQueryFontGlyphCallbackI queryFn;
    private float fontHeight;

    private NkUserFont() {}
    public static NkUserFont create() { return new NkUserFont(); }
    public static NkUserFont malloc() { return new NkUserFont(); }

    public NkUserFont width(NkTextWidthCallbackI fn) { return this; }
    public NkUserFont height(float h) { fontHeight = h; return this; }
    public float height() { return fontHeight; }
    public NkUserFont query(NkQueryFontGlyphCallbackI fn) { this.queryFn = fn; return this; }
    public NkQueryFontGlyphCallbackI query() { return queryFn; }
    public NkUserFont texture(NkHandle handle) { return this; }
    public NkUserFont texture(java.util.function.Consumer<NkHandle> consumer) {
        NkHandle h = NkHandle.create();
        if (consumer != null) consumer.accept(h);
        return this;
    }
    public NkHandle userdata() { return userData; }

    public interface NkTextWidthCallbackI { float invoke(long handle, float h, long text, int len); }
    public interface NkQueryFontGlyphCallbackI { void invoke(long handle, float font_height, long glyph, int codepoint, int next_codepoint); }
}


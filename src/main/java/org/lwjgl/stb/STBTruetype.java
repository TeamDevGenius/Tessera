package org.lwjgl.stb;

import java.nio.*;

/** Stub for LWJGL STBTruetype. */
public class STBTruetype {
    public static boolean stbtt_InitFont(STBTTFontinfo info, ByteBuffer data, int offset) { return false; }
    public static boolean stbtt_InitFont(STBTTFontinfo info, ByteBuffer data) { return false; }
    public static int stbtt_FindGlyphIndex(STBTTFontinfo info, int unicode_codepoint) { return 0; }
    public static void stbtt_GetCodepointHMetrics(STBTTFontinfo info, int codepoint, IntBuffer advanceWidth, IntBuffer leftSideBearing) {}
    public static void stbtt_GetCodepointBitmapBox(STBTTFontinfo info, int codepoint, float scale_x, float scale_y, IntBuffer ix0, IntBuffer iy0, IntBuffer ix1, IntBuffer iy1) {}
    public static void stbtt_GetFontVMetrics(STBTTFontinfo info, IntBuffer ascent, IntBuffer descent, IntBuffer lineGap) {}
    public static float stbtt_ScaleForPixelHeight(STBTTFontinfo info, float pixels) { return 1f; }
    public static float stbtt_ScaleForMappingEmToPixels(STBTTFontinfo info, float pixels) { return 1f; }
    public static boolean stbtt_PackBegin(STBTTPackContext spc, ByteBuffer pixels, int width, int height, int stride_in_bytes, int padding, long alloc_context) { return false; }
    public static void stbtt_PackEnd(STBTTPackContext spc) {}
    public static void stbtt_PackSetOversampling(STBTTPackContext spc, int h_oversample, int v_oversample) {}
    public static boolean stbtt_PackFontRange(STBTTPackContext spc, ByteBuffer fontdata, int font_index, float font_size, int first_unicode_char_in_range, STBTTPackedchar.Buffer chardata_for_range) { return false; }
    public static void stbtt_GetPackedQuad(STBTTPackedchar.Buffer chardata, int pw, int ph, int char_index, FloatBuffer xpos, FloatBuffer ypos, STBTTAlignedQuad q, boolean align_to_integer) {}
}

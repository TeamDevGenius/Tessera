package org.lwjgl.nuklear;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL NkConvertConfig. */
public class NkConvertConfig {
    private NkConvertConfig() {}
    public static NkConvertConfig calloc(MemoryStack stack) { return new NkConvertConfig(); }

    public NkConvertConfig vertex_layout(NkDrawVertexLayoutElement.Buffer layout) { return this; }
    public NkConvertConfig vertex_size(int size) { return this; }
    public NkConvertConfig vertex_alignment(int alignment) { return this; }
    public NkConvertConfig tex_null(NkDrawNullTexture tex) { return this; }
    public NkConvertConfig circle_segment_count(int count) { return this; }
    public NkConvertConfig curve_segment_count(int count) { return this; }
    public NkConvertConfig arc_segment_count(int count) { return this; }
    public NkConvertConfig global_alpha(float alpha) { return this; }
    public NkConvertConfig shape_AA(int aa) { return this; }
    public NkConvertConfig line_AA(int aa) { return this; }
}

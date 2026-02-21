package org.lwjgl.nuklear;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL NkDrawNullTexture. */
public class NkDrawNullTexture {
    private NkHandle texture = new NkHandle();
    private NkVec2 uv = NkVec2.create();

    private NkDrawNullTexture() {}
    public static NkDrawNullTexture create() { return new NkDrawNullTexture(); }

    public NkHandle texture() { return texture; }
    public NkVec2 uv() { return uv; }
}

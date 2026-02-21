package org.lwjgl.opengles;

/** Stub for LWJGL GLES20. */
public class GLES20 {
    public static final int GL_BLEND = 0x0BE2;
    public static final int GL_DEPTH_TEST = 0x0B71;
    public static final int GL_CULL_FACE = 0x0B44;
    public static final int GL_SCISSOR_TEST = 0x0C11;
    public static final int GL_TEXTURE_2D = 0x0DE1;
    public static final int GL_COLOR_BUFFER_BIT = 0x4000;
    public static final int GL_DEPTH_BUFFER_BIT = 0x0100;
    public static final int GL_TRIANGLES = 0x0004;
    public static final int GL_FLOAT = 0x1406;
    public static final int GL_UNSIGNED_BYTE = 0x1401;
    public static final int GL_UNSIGNED_SHORT = 0x1403;

    public static void glEnable(int cap) {}
    public static void glDisable(int cap) {}
    public static void glClear(int mask) {}
    public static void glClearColor(float r, float g, float b, float a) {}
    public static void glViewport(int x, int y, int w, int h) {}
    public static void glBlendFunc(int sfactor, int dfactor) {}
}

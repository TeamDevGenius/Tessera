package org.lwjgl.opengl;

/** Stub for LWJGL GL32. */
public class GL32 extends GL30C {
    public static final int GL_GEOMETRY_SHADER              = 0x8DD9;
    public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 0x8DE0;
    public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 0x8DE1;
    public static final int GL_LINES_ADJACENCY              = 0x000A;
    public static final int GL_LINE_STRIP_ADJACENCY         = 0x000B;
    public static final int GL_TRIANGLES_ADJACENCY          = 0x000C;
    public static final int GL_TRIANGLE_STRIP_ADJACENCY     = 0x000D;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8CD4;
    public static final int GL_DEPTH_CLAMP                  = 0x864F;
    public static final int GL_FIRST_VERTEX_CONVENTION      = 0x8E4D;
    public static final int GL_LAST_VERTEX_CONVENTION       = 0x8E4E;

    public static void glFramebufferTexture(int target, int attachment, int texture, int level) {}
}

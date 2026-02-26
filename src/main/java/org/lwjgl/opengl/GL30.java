package org.lwjgl.opengl;

import java.nio.*;

/** Stub for LWJGL GL30. */
public class GL30 extends GL20C {
    public static final int GL_RGBA8                = 0x8058;
    public static final int GL_RGB8                 = 0x8051;
    public static final int GL_RGBA16F              = 0x881A;
    public static final int GL_RGB16F               = 0x881B;
    public static final int GL_RGBA32F              = 0x8814;
    public static final int GL_RGB32F               = 0x8815;
    public static final int GL_DEPTH_COMPONENT32F   = 0x8CAC;
    public static final int GL_DEPTH24_STENCIL8     = 0x88F0;
    public static final int GL_HALF_FLOAT           = 0x140B;
    public static final int GL_TEXTURE_2D_ARRAY     = 0x8C1A;
    public static final int GL_MAX_ARRAY_TEXTURE_LAYERS = 0x88FF;
    public static final int GL_FRAMEBUFFER          = 0x8D40;
    public static final int GL_READ_FRAMEBUFFER     = 0x8CA8;
    public static final int GL_DRAW_FRAMEBUFFER     = 0x8CA9;
    public static final int GL_RENDERBUFFER         = 0x8D41;
    public static final int GL_DEPTH_ATTACHMENT     = 0x8D00;
    public static final int GL_STENCIL_ATTACHMENT   = 0x8D20;
    public static final int GL_COLOR_ATTACHMENT0    = 0x8CE0;
    public static final int GL_COLOR_ATTACHMENT1    = 0x8CE1;
    public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;
    public static final int GL_RG                   = 0x8227;
    public static final int GL_RG8                  = 0x822B;
    public static final int GL_R8                   = 0x8229;
    public static final int GL_RED                  = 0x1903;
    public static final int GL_VERTEX_ARRAY_BINDING = 0x85B5;

    public static int glGenVertexArrays() {
        if (com.badlogic.gdx.Gdx.gl30 == null) return 0;
        java.nio.IntBuffer buf = java.nio.ByteBuffer.allocateDirect(4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
        com.badlogic.gdx.Gdx.gl30.glGenVertexArrays(1, buf); return buf.get(0);
    }
    public static void glGenVertexArrays(IntBuffer arrays) {
        if (com.badlogic.gdx.Gdx.gl30 == null) return;
        com.badlogic.gdx.Gdx.gl30.glGenVertexArrays(arrays.limit(), arrays);
    }
    public static void glBindVertexArray(int array) { if (com.badlogic.gdx.Gdx.gl30 != null) com.badlogic.gdx.Gdx.gl30.glBindVertexArray(array); }
    public static void glDeleteVertexArrays(int array) {
        if (com.badlogic.gdx.Gdx.gl30 == null) return;
        java.nio.IntBuffer buf = java.nio.ByteBuffer.allocateDirect(4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
        buf.put(0, array);
        com.badlogic.gdx.Gdx.gl30.glDeleteVertexArrays(1, buf);
    }
    public static void glDeleteVertexArrays(IntBuffer arrays) {
        if (com.badlogic.gdx.Gdx.gl30 != null) com.badlogic.gdx.Gdx.gl30.glDeleteVertexArrays(arrays.limit(), arrays);
    }
    /** Also wire glVertexAttribIPointer for integer vertex attributes (uvec3). */
    public static void glVertexAttribIPointer(int index, int size, int type, int stride, long pointer) {
        if (com.badlogic.gdx.Gdx.gl30 != null) com.badlogic.gdx.Gdx.gl30.glVertexAttribIPointer(index, size, type, stride, (int)pointer);
    }
    public static int glGenFramebuffers() {
        if (com.badlogic.gdx.Gdx.gl20 == null) return 0;
        return com.badlogic.gdx.Gdx.gl20.glGenFramebuffer();
    }
    public static void glBindFramebuffer(int target, int framebuffer) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBindFramebuffer(target, framebuffer); }
    public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glFramebufferTexture2D(target, attachment, textarget, texture, level); }
    public static void glDeleteFramebuffers(int framebuffer) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glDeleteFramebuffer(framebuffer); }
    public static int glCheckFramebufferStatus(int target) { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glCheckFramebufferStatus(target) : GL_FRAMEBUFFER_COMPLETE; }
    public static int glGenRenderbuffers() {
        if (com.badlogic.gdx.Gdx.gl20 == null) return 0;
        return com.badlogic.gdx.Gdx.gl20.glGenRenderbuffer();
    }
    public static void glBindRenderbuffer(int target, int renderbuffer) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBindRenderbuffer(target, renderbuffer); }
    public static void glRenderbufferStorage(int target, int internalformat, int width, int height) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glRenderbufferStorage(target, internalformat, width, height); }
    public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer); }
    public static void glDeleteRenderbuffers(int renderbuffer) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glDeleteRenderbuffer(renderbuffer); }
    public static void glGenerateMipmap(int target) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glGenerateMipmap(target); }
    public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
        if (com.badlogic.gdx.Gdx.gl30 != null) com.badlogic.gdx.Gdx.gl30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
    }
}

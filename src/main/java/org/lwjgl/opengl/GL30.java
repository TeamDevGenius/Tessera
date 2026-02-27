package org.lwjgl.opengl;

import com.badlogic.gdx.Gdx;
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
        if (Gdx.gl30 == null) return 0;
        int[] arr = new int[1];
        Gdx.gl30.glGenVertexArrays(1, arr, 0);
        return arr[0];
    }
    public static void glGenVertexArrays(IntBuffer arrays) {}
    public static void glBindVertexArray(int array) { if (Gdx.gl30 != null) Gdx.gl30.glBindVertexArray(array); }
    public static void glDeleteVertexArrays(int array) { if (Gdx.gl30 != null) Gdx.gl30.glDeleteVertexArrays(1, new int[]{array}, 0); }
    public static void glDeleteVertexArrays(IntBuffer arrays) {}
    public static int glGenFramebuffers() { return Gdx.gl20 != null ? Gdx.gl20.glGenFramebuffer() : 0; }
    public static void glBindFramebuffer(int target, int framebuffer) { if (Gdx.gl20 != null) Gdx.gl20.glBindFramebuffer(target, framebuffer); }
    public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) { if (Gdx.gl20 != null) Gdx.gl20.glFramebufferTexture2D(target, attachment, textarget, texture, level); }
    public static void glDeleteFramebuffers(int framebuffer) { if (Gdx.gl20 != null) Gdx.gl20.glDeleteFramebuffer(framebuffer); }
    public static int glCheckFramebufferStatus(int target) { return Gdx.gl20 != null ? Gdx.gl20.glCheckFramebufferStatus(target) : GL_FRAMEBUFFER_COMPLETE; }
    public static int glGenRenderbuffers() { return Gdx.gl20 != null ? Gdx.gl20.glGenRenderbuffer() : 0; }
    public static void glBindRenderbuffer(int target, int renderbuffer) { if (Gdx.gl20 != null) Gdx.gl20.glBindRenderbuffer(target, renderbuffer); }
    public static void glRenderbufferStorage(int target, int internalformat, int width, int height) { if (Gdx.gl20 != null) Gdx.gl20.glRenderbufferStorage(target, internalformat, width, height); }
    public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) { if (Gdx.gl20 != null) Gdx.gl20.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer); }
    public static void glDeleteRenderbuffers(int renderbuffer) { if (Gdx.gl20 != null) Gdx.gl20.glDeleteRenderbuffer(renderbuffer); }
    public static void glGenerateMipmap(int target) { if (Gdx.gl20 != null) Gdx.gl20.glGenerateMipmap(target); }
    public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {}
}

package org.lwjgl.opengl;

import com.badlogic.gdx.Gdx;
import java.nio.*;

/** LWJGL GL11 bridge – delegates to LibGDX {@code Gdx.gl}. */
public class GL11 {
    // Core constants
    public static final int GL_FALSE = 0;
    public static final int GL_TRUE  = 1;
    public static final int GL_NO_ERROR = 0;
    public static final int GL_DEPTH_COMPONENT = 0x1902;
    public static final int GL_UNPACK_ALIGNMENT = 0x0CF5;
    public static final int GL_PACK_ALIGNMENT = 0x0D05;

    // Primitives
    public static final int GL_POINTS         = 0x0000;
    public static final int GL_LINES          = 0x0001;
    public static final int GL_LINE_LOOP      = 0x0002;
    public static final int GL_LINE_STRIP     = 0x0003;
    public static final int GL_TRIANGLES      = 0x0004;
    public static final int GL_TRIANGLE_STRIP = 0x0005;
    public static final int GL_TRIANGLE_FAN   = 0x0006;
    public static final int GL_QUADS          = 0x0007;
    public static final int GL_QUAD_STRIP     = 0x0008;
    public static final int GL_POLYGON        = 0x0009;

    // Types
    public static final int GL_BYTE           = 0x1400;
    public static final int GL_UNSIGNED_BYTE  = 0x1401;
    public static final int GL_SHORT          = 0x1402;
    public static final int GL_UNSIGNED_SHORT = 0x1403;
    public static final int GL_INT            = 0x1404;
    public static final int GL_UNSIGNED_INT   = 0x1405;
    public static final int GL_FLOAT          = 0x1406;
    public static final int GL_DOUBLE         = 0x140A;

    // Blend
    public static final int GL_ZERO                  = 0;
    public static final int GL_ONE                   = 1;
    public static final int GL_SRC_COLOR             = 0x0300;
    public static final int GL_ONE_MINUS_SRC_COLOR   = 0x0301;
    public static final int GL_SRC_ALPHA             = 0x0302;
    public static final int GL_ONE_MINUS_SRC_ALPHA   = 0x0303;
    public static final int GL_DST_ALPHA             = 0x0304;
    public static final int GL_ONE_MINUS_DST_ALPHA   = 0x0305;
    public static final int GL_DST_COLOR             = 0x0306;
    public static final int GL_ONE_MINUS_DST_COLOR   = 0x0307;
    public static final int GL_SRC_ALPHA_SATURATE    = 0x0308;

    // Capabilities
    public static final int GL_DEPTH_TEST    = 0x0B71;
    public static final int GL_BLEND         = 0x0BE2;
    public static final int GL_CULL_FACE     = 0x0B44;
    public static final int GL_SCISSOR_TEST  = 0x0C11;
    public static final int GL_TEXTURE_2D    = 0x0DE1;
    public static final int GL_STENCIL_TEST  = 0x0B90;
    public static final int GL_LIGHTING      = 0x0B50;
    public static final int GL_FOG           = 0x0B60;
    public static final int GL_ALPHA_TEST    = 0x0BC0;
    public static final int GL_POLYGON_OFFSET_FILL = 0x8037;

    // Buffer bits
    public static final int GL_DEPTH_BUFFER_BIT   = 0x0100;
    public static final int GL_STENCIL_BUFFER_BIT = 0x0400;
    public static final int GL_COLOR_BUFFER_BIT   = 0x4000;

    // Face
    public static final int GL_CW           = 0x0900;
    public static final int GL_CCW          = 0x0901;
    public static final int GL_FRONT        = 0x0404;
    public static final int GL_BACK         = 0x0405;
    public static final int GL_FRONT_AND_BACK = 0x0408;

    // Depth
    public static final int GL_NEVER    = 0x0200;
    public static final int GL_LESS     = 0x0201;
    public static final int GL_EQUAL    = 0x0202;
    public static final int GL_LEQUAL   = 0x0203;
    public static final int GL_GREATER  = 0x0204;
    public static final int GL_NOTEQUAL = 0x0205;
    public static final int GL_GEQUAL   = 0x0206;
    public static final int GL_ALWAYS   = 0x0207;

    // Polygon mode
    public static final int GL_POINT    = 0x1B00;
    public static final int GL_LINE     = 0x1B01;
    public static final int GL_FILL     = 0x1B02;

    // Shading
    public static final int GL_FLAT   = 0x1D00;
    public static final int GL_SMOOTH = 0x1D01;

    // Texture
    public static final int GL_TEXTURE_WIDTH           = 0x1000;
    public static final int GL_TEXTURE_HEIGHT          = 0x1001;
    public static final int GL_TEXTURE_INTERNAL_FORMAT = 0x1003;
    public static final int GL_TEXTURE_MAG_FILTER      = 0x2800;
    public static final int GL_TEXTURE_MIN_FILTER      = 0x2801;
    public static final int GL_TEXTURE_WRAP_S          = 0x2802;
    public static final int GL_TEXTURE_WRAP_T          = 0x2803;
    public static final int GL_NEAREST                 = 0x2600;
    public static final int GL_LINEAR                  = 0x2601;
    public static final int GL_NEAREST_MIPMAP_NEAREST  = 0x2700;
    public static final int GL_LINEAR_MIPMAP_NEAREST   = 0x2701;
    public static final int GL_NEAREST_MIPMAP_LINEAR   = 0x2702;
    public static final int GL_LINEAR_MIPMAP_LINEAR    = 0x2703;
    public static final int GL_REPEAT                  = 0x2901;
    public static final int GL_CLAMP                   = 0x2900;

    // Pixel formats
    public static final int GL_ALPHA           = 0x1906;
    public static final int GL_RGB             = 0x1907;
    public static final int GL_RGBA            = 0x1908;
    public static final int GL_LUMINANCE       = 0x1909;
    public static final int GL_LUMINANCE_ALPHA = 0x190A;

    // --- Helper for single-int GL calls ---
    private static final IntBuffer tmpBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();

    // Methods – delegate to Gdx.gl
    public static void glEnable(int cap) { if (Gdx.gl != null) Gdx.gl.glEnable(cap); }
    public static void glDisable(int cap) { if (Gdx.gl != null) Gdx.gl.glDisable(cap); }
    public static void glClear(int mask) { if (Gdx.gl != null) Gdx.gl.glClear(mask); }
    public static void glClearColor(float r, float g, float b, float a) { if (Gdx.gl != null) Gdx.gl.glClearColor(r, g, b, a); }
    public static void glClearDepth(double depth) { if (Gdx.gl != null) Gdx.gl.glClearDepthf((float) depth); }
    public static void glViewport(int x, int y, int w, int h) { if (Gdx.gl != null) Gdx.gl.glViewport(x, y, w, h); }
    public static void glDepthFunc(int func) { if (Gdx.gl != null) Gdx.gl.glDepthFunc(func); }
    public static void glDepthMask(boolean flag) { if (Gdx.gl != null) Gdx.gl.glDepthMask(flag); }
    public static void glBlendFunc(int sfactor, int dfactor) { if (Gdx.gl != null) Gdx.gl.glBlendFunc(sfactor, dfactor); }
    public static void glCullFace(int mode) { if (Gdx.gl != null) Gdx.gl.glCullFace(mode); }
    public static void glFrontFace(int mode) { if (Gdx.gl != null) Gdx.gl.glFrontFace(mode); }
    public static void glPolygonMode(int face, int mode) { /* GL ES does not support glPolygonMode */ }
    public static void glLineWidth(float width) { if (Gdx.gl != null) Gdx.gl.glLineWidth(width); }
    public static void glPointSize(float size) { /* GL ES does not support glPointSize */ }
    public static void glScissor(int x, int y, int w, int h) { if (Gdx.gl != null) Gdx.gl.glScissor(x, y, w, h); }

    public static int glGenTextures() {
        if (Gdx.gl == null) return 0;
        tmpBuf.clear();
        Gdx.gl.glGenTextures(1, tmpBuf);
        return tmpBuf.get(0);
    }
    public static void glGenTextures(IntBuffer textures) {
        if (Gdx.gl != null) Gdx.gl.glGenTextures(textures.remaining(), textures);
    }
    public static void glBindTexture(int target, int texture) { if (Gdx.gl != null) Gdx.gl.glBindTexture(target, texture); }
    public static void glDeleteTextures(int texture) {
        if (Gdx.gl == null) return;
        tmpBuf.clear();
        tmpBuf.put(0, texture);
        Gdx.gl.glDeleteTextures(1, tmpBuf);
    }
    public static void glDeleteTextures(IntBuffer textures) {
        if (Gdx.gl != null) Gdx.gl.glDeleteTextures(textures.remaining(), textures);
    }

    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
        if (Gdx.gl != null) Gdx.gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }
    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
        if (Gdx.gl == null) return;
        // Convert IntBuffer to ByteBuffer for LibGDX
        ByteBuffer bb = ByteBuffer.allocateDirect(pixels.remaining() * 4).order(ByteOrder.nativeOrder());
        bb.asIntBuffer().put(pixels);
        bb.rewind();
        Gdx.gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, bb);
    }
    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, int[] pixels) {
        if (Gdx.gl == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(pixels.length * 4).order(ByteOrder.nativeOrder());
        bb.asIntBuffer().put(pixels);
        bb.rewind();
        Gdx.gl.glTexImage2D(target, level, internalformat, width, height, border, format, type, bb);
    }
    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, int pboOffset) {
        /* PBO offset variant - not supported in GL ES */
    }
    public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
        if (Gdx.gl != null) Gdx.gl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
    }
    public static void glTexParameteri(int target, int pname, int param) { if (Gdx.gl != null) Gdx.gl.glTexParameteri(target, pname, param); }
    public static void glTexParameterf(int target, int pname, float param) { if (Gdx.gl != null) Gdx.gl.glTexParameterf(target, pname, param); }
    public static int glGetTexLevelParameteri(int target, int level, int pname) { return 0; /* Not available in GL ES */ }
    public static void glGetTexImage(int target, int level, int format, int type, ByteBuffer pixels) { /* Not available in GL ES */ }
    public static void glGetTexImage(int target, int level, int format, int type, FloatBuffer pixels) { /* Not available in GL ES */ }
    public static void glDrawArrays(int mode, int first, int count) { if (Gdx.gl != null) Gdx.gl.glDrawArrays(mode, first, count); }
    public static void glDrawElements(int mode, int count, int type, long indices) {
        if (Gdx.gl != null) Gdx.gl.glDrawElements(mode, count, type, (int) indices);
    }
    public static void glDrawElements(int mode, int count, int type, ByteBuffer indices) {
        if (Gdx.gl != null) Gdx.gl.glDrawElements(mode, count, type, indices);
    }
    public static void glDrawElements(int mode, IntBuffer indices) {
        if (Gdx.gl == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(indices.remaining() * 4).order(ByteOrder.nativeOrder());
        bb.asIntBuffer().put(indices);
        bb.rewind();
        Gdx.gl.glDrawElements(mode, indices.remaining(), GL_UNSIGNED_INT, bb);
    }
    public static void glShadeModel(int mode) { /* Fixed-function; not in GL ES */ }
    public static int glGetError() { return Gdx.gl != null ? Gdx.gl.glGetError() : 0; }
    public static void glFinish() { if (Gdx.gl != null) Gdx.gl.glFinish(); }
    public static void glFlush() { if (Gdx.gl != null) Gdx.gl.glFlush(); }
    public static void glColorMask(boolean r, boolean g, boolean b, boolean a) { if (Gdx.gl != null) Gdx.gl.glColorMask(r, g, b, a); }
    public static void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
        if (Gdx.gl != null) Gdx.gl.glReadPixels(x, y, width, height, format, type, pixels);
    }
    public static void glReadPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
        /* FloatBuffer variant not in GL ES */
    }
    public static void glAlphaFunc(int func, float ref) { /* Fixed-function; not in GL ES */ }
    public static String glGetString(int name) { return Gdx.gl != null ? Gdx.gl.glGetString(name) : ""; }
    public static int glGetInteger(int pname) {
        if (Gdx.gl == null) return 0;
        tmpBuf.clear();
        Gdx.gl.glGetIntegerv(pname, tmpBuf);
        return tmpBuf.get(0);
    }
    public static void glPixelStorei(int pname, int param) { if (Gdx.gl != null) Gdx.gl.glPixelStorei(pname, param); }
}


package org.lwjgl.opengl;

import com.badlogic.gdx.Gdx;
import java.nio.*;

/** Stub for LWJGL GL12. Delegates to LibGDX GL30 APIs for 3D textures. */
public class GL12 extends GL11 {
    public static final int GL_TEXTURE_3D             = 0x806F;
    public static final int GL_TEXTURE_WRAP_R         = 0x8072;
    public static final int GL_MAX_3D_TEXTURE_SIZE    = 0x8073;
    public static final int GL_UNSIGNED_BYTE_3_3_2    = 0x8032;
    public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033;
    public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034;
    public static final int GL_UNSIGNED_INT_8_8_8_8   = 0x8035;
    public static final int GL_UNSIGNED_INT_10_10_10_2 = 0x8036;
    public static final int GL_CLAMP_TO_EDGE          = 0x812F;

    public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, ByteBuffer pixels) {
        if (Gdx.gl30 != null) Gdx.gl30.glTexImage3D(target, level, internalformat, width, height, depth, border, format, type, pixels);
    }
    public static void glTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int format, int type, FloatBuffer pixels) {}
    public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
        if (Gdx.gl30 != null) Gdx.gl30.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }
}

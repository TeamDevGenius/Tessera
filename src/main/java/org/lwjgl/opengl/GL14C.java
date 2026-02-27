package org.lwjgl.opengl;

import com.badlogic.gdx.Gdx;

/** Stub for LWJGL GL14C. Delegates to LibGDX GL APIs. */
public class GL14C extends GL13C {
    public static final int GL_BLEND_DST_RGB   = 0x80C8;
    public static final int GL_BLEND_SRC_RGB   = 0x80C9;
    public static final int GL_BLEND_DST_ALPHA = 0x80CA;
    public static final int GL_BLEND_SRC_ALPHA = 0x80CB;
    public static final int GL_FUNC_ADD        = 0x8006;
    public static final int GL_FUNC_SUBTRACT   = 0x800A;
    public static final int GL_FUNC_REVERSE_SUBTRACT = 0x800B;
    public static final int GL_MIN             = 0x8007;
    public static final int GL_MAX             = 0x8008;
    public static final int GL_MIRRORED_REPEAT = 0x8370;
    public static final int GL_DEPTH_COMPONENT   = 0x1902;
    public static final int GL_DEPTH_COMPONENT16 = 0x81A5;

    public static void glBlendEquation(int mode) { if (Gdx.gl20 != null) Gdx.gl20.glBlendEquation(mode); }
    public static void glBlendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
        if (Gdx.gl20 != null) Gdx.gl20.glBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
    }
}

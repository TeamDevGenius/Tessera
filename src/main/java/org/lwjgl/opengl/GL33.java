package org.lwjgl.opengl;

/** Stub for LWJGL GL33. */
public class GL33 extends GL32 {
    public static final int GL_SAMPLER_BINDING          = 0x8919;
    public static final int GL_TIME_ELAPSED             = 0x88BF;
    public static final int GL_TIMESTAMP                = 0x8E28;
    public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 0x88FE;

    public static int glGenSamplers() { return 0; }
    public static void glBindSampler(int unit, int sampler) {}
    public static void glSamplerParameteri(int sampler, int pname, int param) {}
    public static void glSamplerParameterf(int sampler, int pname, float param) {}
    public static void glDeleteSamplers(int sampler) {}
    public static void glVertexAttribDivisor(int index, int divisor) { if (com.badlogic.gdx.Gdx.gl30 != null) com.badlogic.gdx.Gdx.gl30.glVertexAttribDivisor(index, divisor); }
    public static void glDrawElementsInstanced(int mode, int count, int type, long indices, int instancecount) {}
    public static void glDrawArraysInstanced(int mode, int first, int count, int instancecount) { if (com.badlogic.gdx.Gdx.gl30 != null) com.badlogic.gdx.Gdx.gl30.glDrawArraysInstanced(mode, first, count, instancecount); }
    public static void glQueryCounter(int id, int target) {}
    public static long glGetQueryObjectui64(int id, int pname) { return 0L; }
}

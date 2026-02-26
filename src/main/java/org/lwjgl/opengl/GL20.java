package org.lwjgl.opengl;

import java.nio.*;

/** Stub for LWJGL GL20. */
public class GL20 extends GL15C {
    public static final int GL_FRAGMENT_SHADER                 = 0x8B30;
    public static final int GL_VERTEX_SHADER                   = 0x8B31;
    public static final int GL_MAX_VERTEX_ATTRIBS              = 0x8869;
    public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
    public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS  = 0x8B4C;
    public static final int GL_MAX_TEXTURE_IMAGE_UNITS         = 0x8872;
    public static final int GL_COMPILE_STATUS                  = 0x8B81;
    public static final int GL_LINK_STATUS                     = 0x8B82;
    public static final int GL_VALIDATE_STATUS                 = 0x8B83;
    public static final int GL_INFO_LOG_LENGTH                 = 0x8B84;
    public static final int GL_ATTACHED_SHADERS                = 0x8B85;
    public static final int GL_ACTIVE_UNIFORMS                 = 0x8B86;
    public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH       = 0x8B87;
    public static final int GL_ACTIVE_ATTRIBUTES               = 0x8B89;
    public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH     = 0x8B8A;
    public static final int GL_SHADING_LANGUAGE_VERSION        = 0x8B8C;
    public static final int GL_CURRENT_PROGRAM                 = 0x8B8D;
    public static final int GL_FLOAT_VEC2                      = 0x8B50;
    public static final int GL_FLOAT_VEC3                      = 0x8B51;
    public static final int GL_FLOAT_VEC4                      = 0x8B52;
    public static final int GL_SAMPLER_2D                      = 0x8B5E;

    public static int glCreateShader(int type) { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glCreateShader(type) : 0; }
    public static void glShaderSource(int shader, CharSequence source) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glShaderSource(shader, source.toString()); }
    public static void glCompileShader(int shader) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glCompileShader(shader); }
    public static int glGetShaderi(int shader, int pname) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return GL_TRUE;
        java.nio.IntBuffer buf = java.nio.ByteBuffer.allocateDirect(4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
        com.badlogic.gdx.Gdx.gl20.glGetShaderiv(shader, pname, buf); return buf.get(0);
    }
    public static String glGetShaderInfoLog(int shader, int maxLength) { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glGetShaderInfoLog(shader) : ""; }
    public static void glDeleteShader(int shader) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glDeleteShader(shader); }
    public static int glCreateProgram() { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glCreateProgram() : 0; }
    public static void glAttachShader(int program, int shader) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glAttachShader(program, shader); }
    public static void glDetachShader(int program, int shader) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glDetachShader(program, shader); }
    public static void glLinkProgram(int program) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glLinkProgram(program); }
    public static void glValidateProgram(int program) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glValidateProgram(program); }
    public static int glGetProgrami(int program, int pname) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return GL_TRUE;
        java.nio.IntBuffer buf = java.nio.ByteBuffer.allocateDirect(4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
        com.badlogic.gdx.Gdx.gl20.glGetProgramiv(program, pname, buf); return buf.get(0);
    }
    public static String glGetProgramInfoLog(int program, int maxLength) { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glGetProgramInfoLog(program) : ""; }
    public static void glDeleteProgram(int program) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glDeleteProgram(program); }
    public static void glUseProgram(int program) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUseProgram(program); }
    public static int glGetUniformLocation(int program, CharSequence name) { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glGetUniformLocation(program, name.toString()) : -1; }
    public static void glUniform1i(int location, int v0) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniform1i(location, v0); }
    public static void glUniform1f(int location, float v0) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniform1f(location, v0); }
    public static void glUniform2f(int location, float v0, float v1) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniform2f(location, v0, v1); }
    public static void glUniform3f(int location, float v0, float v1, float v2) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniform3f(location, v0, v1, v2); }
    public static void glUniform4f(int location, float v0, float v1, float v2, float v3) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniform4f(location, v0, v1, v2, v3); }
    public static void glUniform2i(int location, int v0, int v1) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniform2i(location, v0, v1); }
    public static void glUniform3i(int location, int v0, int v1, int v2) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniform3i(location, v0, v1, v2); }
    public static void glUniform1fv(int location, FloatBuffer value) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        float[] v = new float[value.limit()]; int pos = value.position(); value.get(v); value.position(pos);
        com.badlogic.gdx.Gdx.gl20.glUniform1fv(location, v.length, v, 0);
    }
    public static void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer value) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        float[] v = new float[value.limit()]; int pos = value.position(); value.get(v); value.position(pos);
        com.badlogic.gdx.Gdx.gl20.glUniformMatrix4fv(location, v.length/16, transpose, v, 0);
    }
    public static void glUniformMatrix4fv(int location, boolean transpose, float[] value) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glUniformMatrix4fv(location, value.length/16, transpose, value, 0); }
    public static void glUniformMatrix3fv(int location, boolean transpose, FloatBuffer value) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        float[] v = new float[value.limit()]; int pos = value.position(); value.get(v); value.position(pos);
        com.badlogic.gdx.Gdx.gl20.glUniformMatrix3fv(location, v.length/9, transpose, v, 0);
    }
    public static int glGetAttribLocation(int program, CharSequence name) { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glGetAttribLocation(program, name.toString()) : -1; }
    public static void glBindAttribLocation(int program, int index, CharSequence name) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBindAttribLocation(program, index, name.toString()); }
    public static void glEnableVertexAttribArray(int index)  { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glEnableVertexAttribArray(index); }
    public static void glDisableVertexAttribArray(int index) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glDisableVertexAttribArray(index); }
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer)       { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glVertexAttribPointer(index, size, type, normalized, stride, (int)pointer); }
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, ByteBuffer pointer)  { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glVertexAttribPointer(index, size, type, normalized, stride, pointer); }
    public static void glVertexAttrib3f(int index, float x, float y, float z) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glVertexAttrib3f(index, x, y, z); }
    public static void glVertexAttrib4f(int index, float x, float y, float z, float w) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glVertexAttrib4f(index, x, y, z, w); }
    public static void glDrawBuffers(int buf) {}
    public static void glDrawBuffers(int[] bufs) {}
    public static void glDrawBuffers(java.nio.IntBuffer bufs) {}
}

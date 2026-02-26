package org.lwjgl.opengl;

import com.badlogic.gdx.Gdx;
import java.nio.*;

/** LWJGL GL20 bridge – delegates shader/program operations to LibGDX {@code Gdx.gl20}. */
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

    private static final IntBuffer tmpBuf = java.nio.ByteBuffer.allocateDirect(4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();

    public static int glCreateShader(int type) { return Gdx.gl20 != null ? Gdx.gl20.glCreateShader(type) : 0; }
    public static void glShaderSource(int shader, CharSequence source) { if (Gdx.gl20 != null) Gdx.gl20.glShaderSource(shader, source.toString()); }
    public static void glCompileShader(int shader) { if (Gdx.gl20 != null) Gdx.gl20.glCompileShader(shader); }
    public static int glGetShaderi(int shader, int pname) {
        if (Gdx.gl20 == null) return GL_TRUE;
        tmpBuf.clear();
        Gdx.gl20.glGetShaderiv(shader, pname, tmpBuf);
        return tmpBuf.get(0);
    }
    public static String glGetShaderInfoLog(int shader, int maxLength) { return Gdx.gl20 != null ? Gdx.gl20.glGetShaderInfoLog(shader) : ""; }
    public static void glDeleteShader(int shader) { if (Gdx.gl20 != null) Gdx.gl20.glDeleteShader(shader); }
    public static int glCreateProgram() { return Gdx.gl20 != null ? Gdx.gl20.glCreateProgram() : 0; }
    public static void glAttachShader(int program, int shader) { if (Gdx.gl20 != null) Gdx.gl20.glAttachShader(program, shader); }
    public static void glDetachShader(int program, int shader) { if (Gdx.gl20 != null) Gdx.gl20.glDetachShader(program, shader); }
    public static void glLinkProgram(int program) { if (Gdx.gl20 != null) Gdx.gl20.glLinkProgram(program); }
    public static void glValidateProgram(int program) { if (Gdx.gl20 != null) Gdx.gl20.glValidateProgram(program); }
    public static int glGetProgrami(int program, int pname) {
        if (Gdx.gl20 == null) return GL_TRUE;
        tmpBuf.clear();
        Gdx.gl20.glGetProgramiv(program, pname, tmpBuf);
        return tmpBuf.get(0);
    }
    public static String glGetProgramInfoLog(int program, int maxLength) { return Gdx.gl20 != null ? Gdx.gl20.glGetProgramInfoLog(program) : ""; }
    public static void glDeleteProgram(int program) { if (Gdx.gl20 != null) Gdx.gl20.glDeleteProgram(program); }
    public static void glUseProgram(int program) { if (Gdx.gl20 != null) Gdx.gl20.glUseProgram(program); }
    public static int glGetUniformLocation(int program, CharSequence name) { return Gdx.gl20 != null ? Gdx.gl20.glGetUniformLocation(program, name.toString()) : -1; }
    public static void glUniform1i(int location, int v0) { if (Gdx.gl20 != null) Gdx.gl20.glUniform1i(location, v0); }
    public static void glUniform1f(int location, float v0) { if (Gdx.gl20 != null) Gdx.gl20.glUniform1f(location, v0); }
    public static void glUniform2f(int location, float v0, float v1) { if (Gdx.gl20 != null) Gdx.gl20.glUniform2f(location, v0, v1); }
    public static void glUniform3f(int location, float v0, float v1, float v2) { if (Gdx.gl20 != null) Gdx.gl20.glUniform3f(location, v0, v1, v2); }
    public static void glUniform4f(int location, float v0, float v1, float v2, float v3) { if (Gdx.gl20 != null) Gdx.gl20.glUniform4f(location, v0, v1, v2, v3); }
    public static void glUniform2i(int location, int v0, int v1) { if (Gdx.gl20 != null) Gdx.gl20.glUniform2i(location, v0, v1); }
    public static void glUniform3i(int location, int v0, int v1, int v2) { if (Gdx.gl20 != null) Gdx.gl20.glUniform3i(location, v0, v1, v2); }
    public static void glUniform1fv(int location, FloatBuffer value) {
        if (Gdx.gl20 != null) Gdx.gl20.glUniform1fv(location, value.remaining(), value);
    }
    public static void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer value) {
        if (Gdx.gl20 != null) Gdx.gl20.glUniformMatrix4fv(location, value.remaining() / 16, transpose, value);
    }
    public static void glUniformMatrix4fv(int location, boolean transpose, float[] value) {
        if (Gdx.gl20 != null) Gdx.gl20.glUniformMatrix4fv(location, value.length / 16, transpose, value, 0);
    }
    public static void glUniformMatrix3fv(int location, boolean transpose, FloatBuffer value) {
        if (Gdx.gl20 != null) Gdx.gl20.glUniformMatrix3fv(location, value.remaining() / 9, transpose, value);
    }
    public static int glGetAttribLocation(int program, CharSequence name) { return Gdx.gl20 != null ? Gdx.gl20.glGetAttribLocation(program, name.toString()) : -1; }
    public static void glBindAttribLocation(int program, int index, CharSequence name) { if (Gdx.gl20 != null) Gdx.gl20.glBindAttribLocation(program, index, name.toString()); }
    public static void glEnableVertexAttribArray(int index) { if (Gdx.gl20 != null) Gdx.gl20.glEnableVertexAttribArray(index); }
    public static void glDisableVertexAttribArray(int index) { if (Gdx.gl20 != null) Gdx.gl20.glDisableVertexAttribArray(index); }
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer) {
        if (Gdx.gl20 != null) Gdx.gl20.glVertexAttribPointer(index, size, type, normalized, stride, (int) pointer);
    }
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, ByteBuffer pointer) {
        /* Buffer-pointer variant: offset already bound via VBO */
        if (Gdx.gl20 != null) Gdx.gl20.glVertexAttribPointer(index, size, type, normalized, stride, 0);
    }
    public static void glVertexAttrib3f(int index, float x, float y, float z) { if (Gdx.gl20 != null) Gdx.gl20.glVertexAttrib3f(index, x, y, z); }
    public static void glVertexAttrib4f(int index, float x, float y, float z, float w) { if (Gdx.gl20 != null) Gdx.gl20.glVertexAttrib4f(index, x, y, z, w); }
    public static void glDrawBuffers(int buf) {
        if (Gdx.gl30 == null) return;
        tmpBuf.clear();
        tmpBuf.put(0, buf);
        Gdx.gl30.glDrawBuffers(1, tmpBuf);
    }
    public static void glDrawBuffers(int[] bufs) {
        if (Gdx.gl30 == null) return;
        IntBuffer ib = java.nio.ByteBuffer.allocateDirect(bufs.length * 4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
        ib.put(bufs).flip();
        Gdx.gl30.glDrawBuffers(bufs.length, ib);
    }
    public static void glDrawBuffers(java.nio.IntBuffer bufs) {
        if (Gdx.gl30 != null) Gdx.gl30.glDrawBuffers(bufs.remaining(), bufs);
    }
}

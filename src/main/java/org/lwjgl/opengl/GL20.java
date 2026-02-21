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

    public static int glCreateShader(int type) { return 0; }
    public static void glShaderSource(int shader, CharSequence source) {}
    public static void glCompileShader(int shader) {}
    public static int glGetShaderi(int shader, int pname) { return GL_TRUE; }
    public static String glGetShaderInfoLog(int shader, int maxLength) { return ""; }
    public static void glDeleteShader(int shader) {}
    public static int glCreateProgram() { return 0; }
    public static void glAttachShader(int program, int shader) {}
    public static void glDetachShader(int program, int shader) {}
    public static void glLinkProgram(int program) {}
    public static void glValidateProgram(int program) {}
    public static int glGetProgrami(int program, int pname) { return GL_TRUE; }
    public static String glGetProgramInfoLog(int program, int maxLength) { return ""; }
    public static void glDeleteProgram(int program) {}
    public static void glUseProgram(int program) {}
    public static int glGetUniformLocation(int program, CharSequence name) { return -1; }
    public static void glUniform1i(int location, int v0) {}
    public static void glUniform1f(int location, float v0) {}
    public static void glUniform2f(int location, float v0, float v1) {}
    public static void glUniform3f(int location, float v0, float v1, float v2) {}
    public static void glUniform4f(int location, float v0, float v1, float v2, float v3) {}
    public static void glUniform2i(int location, int v0, int v1) {}
    public static void glUniform3i(int location, int v0, int v1, int v2) {}
    public static void glUniform1fv(int location, FloatBuffer value) {}
    public static void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer value) {}
    public static void glUniformMatrix4fv(int location, boolean transpose, float[] value) {}
    public static void glUniformMatrix3fv(int location, boolean transpose, FloatBuffer value) {}
    public static int glGetAttribLocation(int program, CharSequence name) { return -1; }
    public static void glBindAttribLocation(int program, int index, CharSequence name) {}
    public static void glEnableVertexAttribArray(int index) {}
    public static void glDisableVertexAttribArray(int index) {}
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer) {}
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, ByteBuffer pointer) {}
    public static void glVertexAttrib3f(int index, float x, float y, float z) {}
    public static void glVertexAttrib4f(int index, float x, float y, float z, float w) {}
    public static void glDrawBuffers(int buf) {}
    public static void glDrawBuffers(int[] bufs) {}
    public static void glDrawBuffers(java.nio.IntBuffer bufs) {}
}

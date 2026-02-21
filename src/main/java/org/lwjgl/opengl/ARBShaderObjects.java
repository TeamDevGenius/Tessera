package org.lwjgl.opengl;

import java.nio.IntBuffer;

/** Stub for LWJGL ARBShaderObjects. */
public class ARBShaderObjects {
    public static final int GL_PROGRAM_OBJECT_ARB  = 0x8B40;
    public static final int GL_OBJECT_TYPE_ARB     = 0x8B4E;
    public static final int GL_OBJECT_SUBTYPE_ARB  = 0x8B4F;
    public static final int GL_OBJECT_DELETE_STATUS_ARB = 0x8B80;
    public static final int GL_OBJECT_COMPILE_STATUS_ARB = 0x8B81;
    public static final int GL_OBJECT_LINK_STATUS_ARB    = 0x8B82;
    public static final int GL_VERTEX_SHADER_ARB   = 0x8B31;
    public static final int GL_FRAGMENT_SHADER_ARB = 0x8B30;

    public static int glCreateProgramObjectARB() { return 0; }
    public static int glCreateShaderObjectARB(int shaderType) { return 0; }
    public static void glShaderSourceARB(int shaderObj, CharSequence string) {}
    public static void glCompileShaderARB(int shaderObj) {}
    public static int glGetObjectParameteriARB(int obj, int pname) { return GL11.GL_TRUE; }
    public static String glGetInfoLogARB(int obj, int maxLength) { return ""; }
    public static void glAttachObjectARB(int containerObj, int obj) {}
    public static void glLinkProgramARB(int programObj) {}
    public static void glUseProgramObjectARB(int programObj) {}
    public static void glDeleteObjectARB(int obj) {}
    public static int glGetUniformLocationARB(int programObj, CharSequence name) { return -1; }
    public static void glBindAttribLocationARB(int programObj, int index, CharSequence name) {}
    public static void glUniform1fARB(int location, float v0) {}
    public static void glUniform1iARB(int location, int v0) {}
    public static void glUniform2fARB(int location, float v0, float v1) {}
    public static void glUniform3fARB(int location, float v0, float v1, float v2) {}
    public static void glUniform4fARB(int location, float v0, float v1, float v2, float v3) {}
    public static void glUniform2iARB(int location, int v0, int v1) {}
    public static void glUniform3iARB(int location, int v0, int v1, int v2) {}
    public static void glUniformMatrix4fvARB(int location, boolean transpose, java.nio.FloatBuffer value) {}
    public static void glUniformMatrix3fvARB(int location, boolean transpose, java.nio.FloatBuffer value) {}
    public static void glValidateProgramARB(int programObj) {}
}

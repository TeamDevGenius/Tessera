package org.lwjgl.opengl;

import java.nio.IntBuffer;

/** Stub for LWJGL ARBOcclusionQuery. */
public class ARBOcclusionQuery {
    public static final int GL_SAMPLES_PASSED_ARB           = 0x8914;
    public static final int GL_QUERY_COUNTER_BITS_ARB       = 0x8864;
    public static final int GL_CURRENT_QUERY_ARB            = 0x8865;
    public static final int GL_QUERY_RESULT_ARB             = 0x8866;
    public static final int GL_QUERY_RESULT_AVAILABLE_ARB   = 0x8867;

    public static void glGenQueriesARB(IntBuffer ids) {}
    public static int glGenQueriesARB() { return 0; }
    public static void glDeleteQueriesARB(int id) {}
    public static void glBeginQueryARB(int target, int id) {}
    public static void glEndQueryARB(int target) {}
    public static int glGetQueryObjectiARB(int id, int pname) { return 0; }
    public static long glGetQueryObjectui64ARB(int id, int pname) { return 0L; }
}

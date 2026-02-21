package org.lwjgl.opengl;

import java.nio.*;

/** Stub for LWJGL GL15. */
public class GL15 extends GL14C {
    public static final int GL_ARRAY_BUFFER         = 0x8892;
    public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;
    public static final int GL_ARRAY_BUFFER_BINDING = 0x8894;
    public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
    public static final int GL_STREAM_DRAW          = 0x88E0;
    public static final int GL_STREAM_READ          = 0x88E1;
    public static final int GL_STREAM_COPY          = 0x88E2;
    public static final int GL_STATIC_DRAW          = 0x88B4;
    public static final int GL_STATIC_READ          = 0x88B5;
    public static final int GL_STATIC_COPY          = 0x88B6;
    public static final int GL_DYNAMIC_DRAW         = 0x88E8;
    public static final int GL_DYNAMIC_READ         = 0x88E9;
    public static final int GL_DYNAMIC_COPY         = 0x88EA;
    public static final int GL_READ_ONLY            = 0x88B8;
    public static final int GL_WRITE_ONLY           = 0x88B9;
    public static final int GL_READ_WRITE           = 0x88BA;
    public static final int GL_BUFFER_SIZE          = 0x8764;
    public static final int GL_BUFFER_USAGE         = 0x8765;
    public static final int GL_QUERY_RESULT         = 0x8866;
    public static final int GL_QUERY_RESULT_AVAILABLE = 0x8867;
    public static final int GL_SAMPLES_PASSED       = 0x8914;

    public static int glGenBuffers() { return 0; }
    public static void glGenBuffers(IntBuffer buffers) {}
    public static void glBindBuffer(int target, int buffer) {}
    public void glBufferData(int target, long size, int usage) {}
    public static void glBufferData(int target, ByteBuffer data, int usage) {}
    public static void glBufferData(int target, FloatBuffer data, int usage) {}
    public static void glBufferData(int target, IntBuffer data, int usage) {}
    public static void glBufferData(int target, float[] data, int usage) {}
    public static void glBufferData(int target, int[] data, int usage) {}
    public static void glBufferData(int target, short[] data, int usage) {}
    public static void glBufferSubData(int target, long offset, ByteBuffer data) {}
    public static void glBufferSubData(int target, long offset, FloatBuffer data) {}
    public static void glBufferSubData(int target, long offset, IntBuffer data) {}
    public static void glDeleteBuffers(int buffer) {}
    public static void glDeleteBuffers(IntBuffer buffers) {}
    public static ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer oldBuffer) { return ByteBuffer.allocateDirect((int)length).order(java.nio.ByteOrder.nativeOrder()); }
    public static ByteBuffer glMapBuffer(int target, int access) { return ByteBuffer.allocateDirect(0).order(java.nio.ByteOrder.nativeOrder()); }
    public static boolean glUnmapBuffer(int target) { return true; }
    public static void glGenQueries(IntBuffer ids) {}
    public static int glGenQueries() { return 0; }
    public static void glDeleteQueries(int id) {}
    public static void glBeginQuery(int target, int id) {}
    public static void glEndQuery(int target) {}
    public static int glGetQueryObjecti(int id, int pname) { return 0; }
    public static long glGetQueryObjectui64(int id, int pname) { return 0L; }
}

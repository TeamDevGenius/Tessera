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

    public static int glGenBuffers() { return com.badlogic.gdx.Gdx.gl20 != null ? com.badlogic.gdx.Gdx.gl20.glGenBuffer() : 0; }
    public static void glGenBuffers(IntBuffer buffers) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        for (int i = 0; i < buffers.limit(); i++) buffers.put(i, com.badlogic.gdx.Gdx.gl20.glGenBuffer());
    }
    public static void glBindBuffer(int target, int buffer) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBindBuffer(target, buffer); }
    public void glBufferData(int target, long size, int usage) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBufferData(target, (int)size, null, usage); }
    public static void glBufferData(int target, ByteBuffer data, int usage)  { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBufferData(target, data.limit(), data, usage); }
    public static void glBufferData(int target, FloatBuffer data, int usage) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBufferData(target, data.limit() * 4, data, usage); }
    public static void glBufferData(int target, IntBuffer data, int usage)   { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBufferData(target, data.limit() * 4, data, usage); }
    public static void glBufferData(int target, float[] data, int usage) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        FloatBuffer buf = java.nio.ByteBuffer.allocateDirect(data.length*4).order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        buf.put(data).flip();
        com.badlogic.gdx.Gdx.gl20.glBufferData(target, data.length * 4, buf, usage);
    }
    public static void glBufferData(int target, int[] data, int usage) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        IntBuffer buf = java.nio.ByteBuffer.allocateDirect(data.length*4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
        buf.put(data).flip();
        com.badlogic.gdx.Gdx.gl20.glBufferData(target, data.length * 4, buf, usage);
    }
    public static void glBufferData(int target, short[] data, int usage) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        java.nio.ShortBuffer buf = java.nio.ByteBuffer.allocateDirect(data.length*2).order(java.nio.ByteOrder.nativeOrder()).asShortBuffer();
        buf.put(data).flip();
        com.badlogic.gdx.Gdx.gl20.glBufferData(target, data.length * 2, buf, usage);
    }
    public static void glBufferSubData(int target, long offset, ByteBuffer data)  { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBufferSubData(target,(int)offset,data.limit(),data); }
    public static void glBufferSubData(int target, long offset, FloatBuffer data) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBufferSubData(target,(int)offset,data.limit()*4,data); }
    public static void glBufferSubData(int target, long offset, IntBuffer data)   { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glBufferSubData(target,(int)offset,data.limit()*4,data); }
    public static void glDeleteBuffers(int buffer) { if (com.badlogic.gdx.Gdx.gl20 != null) com.badlogic.gdx.Gdx.gl20.glDeleteBuffer(buffer); }
    public static void glDeleteBuffers(IntBuffer buffers) {
        if (com.badlogic.gdx.Gdx.gl20 == null) return;
        for (int i = 0; i < buffers.limit(); i++) com.badlogic.gdx.Gdx.gl20.glDeleteBuffer(buffers.get(i));
    }
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

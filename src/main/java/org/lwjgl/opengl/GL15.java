package org.lwjgl.opengl;

import com.badlogic.gdx.Gdx;
import java.nio.*;

/** LWJGL GL15 bridge – delegates buffer operations to LibGDX {@code Gdx.gl20}. */
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

    private static final IntBuffer tmpBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();

    public static int glGenBuffers() {
        if (Gdx.gl20 == null) return 0;
        tmpBuf.clear();
        Gdx.gl20.glGenBuffers(1, tmpBuf);
        return tmpBuf.get(0);
    }
    public static void glGenBuffers(IntBuffer buffers) {
        if (Gdx.gl20 != null) Gdx.gl20.glGenBuffers(buffers.remaining(), buffers);
    }
    public static void glBindBuffer(int target, int buffer) { if (Gdx.gl20 != null) Gdx.gl20.glBindBuffer(target, buffer); }
    public void glBufferData(int target, long size, int usage) {
        if (Gdx.gl20 != null) Gdx.gl20.glBufferData(target, (int) size, null, usage);
    }
    public static void glBufferData(int target, ByteBuffer data, int usage) {
        if (Gdx.gl20 != null) Gdx.gl20.glBufferData(target, data.remaining(), data, usage);
    }
    public static void glBufferData(int target, FloatBuffer data, int usage) {
        if (Gdx.gl20 == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.remaining() * 4).order(ByteOrder.nativeOrder());
        bb.asFloatBuffer().put(data);
        bb.rewind();
        Gdx.gl20.glBufferData(target, bb.remaining(), bb, usage);
    }
    public static void glBufferData(int target, IntBuffer data, int usage) {
        if (Gdx.gl20 == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.remaining() * 4).order(ByteOrder.nativeOrder());
        bb.asIntBuffer().put(data);
        bb.rewind();
        Gdx.gl20.glBufferData(target, bb.remaining(), bb, usage);
    }
    public static void glBufferData(int target, float[] data, int usage) {
        if (Gdx.gl20 == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder());
        bb.asFloatBuffer().put(data);
        bb.rewind();
        Gdx.gl20.glBufferData(target, bb.remaining(), bb, usage);
    }
    public static void glBufferData(int target, int[] data, int usage) {
        if (Gdx.gl20 == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder());
        bb.asIntBuffer().put(data);
        bb.rewind();
        Gdx.gl20.glBufferData(target, bb.remaining(), bb, usage);
    }
    public static void glBufferData(int target, short[] data, int usage) {
        if (Gdx.gl20 == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 2).order(ByteOrder.nativeOrder());
        bb.asShortBuffer().put(data);
        bb.rewind();
        Gdx.gl20.glBufferData(target, bb.remaining(), bb, usage);
    }
    public static void glBufferSubData(int target, long offset, ByteBuffer data) {
        if (Gdx.gl20 != null) Gdx.gl20.glBufferSubData(target, (int) offset, data.remaining(), data);
    }
    public static void glBufferSubData(int target, long offset, FloatBuffer data) {
        if (Gdx.gl20 == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.remaining() * 4).order(ByteOrder.nativeOrder());
        bb.asFloatBuffer().put(data);
        bb.rewind();
        Gdx.gl20.glBufferSubData(target, (int) offset, bb.remaining(), bb);
    }
    public static void glBufferSubData(int target, long offset, IntBuffer data) {
        if (Gdx.gl20 == null) return;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.remaining() * 4).order(ByteOrder.nativeOrder());
        bb.asIntBuffer().put(data);
        bb.rewind();
        Gdx.gl20.glBufferSubData(target, (int) offset, bb.remaining(), bb);
    }
    public static void glDeleteBuffers(int buffer) {
        if (Gdx.gl20 == null) return;
        tmpBuf.clear();
        tmpBuf.put(0, buffer);
        Gdx.gl20.glDeleteBuffers(1, tmpBuf);
    }
    public static void glDeleteBuffers(IntBuffer buffers) {
        if (Gdx.gl20 != null) Gdx.gl20.glDeleteBuffers(buffers.remaining(), buffers);
    }
    public static ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer oldBuffer) {
        return ByteBuffer.allocateDirect((int) length).order(ByteOrder.nativeOrder());
    }
    public static ByteBuffer glMapBuffer(int target, int access) {
        return ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());
    }
    public static boolean glUnmapBuffer(int target) { return true; }
    public static void glGenQueries(IntBuffer ids) {}
    public static int glGenQueries() { return 0; }
    public static void glDeleteQueries(int id) {}
    public static void glBeginQuery(int target, int id) {}
    public static void glEndQuery(int target) {}
    public static int glGetQueryObjecti(int id, int pname) { return 0; }
    public static long glGetQueryObjectui64(int id, int pname) { return 0L; }
}

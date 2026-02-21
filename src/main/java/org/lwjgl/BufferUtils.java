package org.lwjgl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.DoubleBuffer;
import java.nio.ShortBuffer;

/** Stub replacement for LWJGL BufferUtils. Uses standard Java NIO. */
public class BufferUtils {
    public static ByteBuffer createByteBuffer(int size) { return ByteBuffer.allocateDirect(size).order(java.nio.ByteOrder.nativeOrder()); }
    public static FloatBuffer createFloatBuffer(int size) { return createByteBuffer(size * 4).asFloatBuffer(); }
    public static IntBuffer createIntBuffer(int size) { return createByteBuffer(size * 4).asIntBuffer(); }
    public static LongBuffer createLongBuffer(int size) { return createByteBuffer(size * 8).asLongBuffer(); }
    public static DoubleBuffer createDoubleBuffer(int size) { return createByteBuffer(size * 8).asDoubleBuffer(); }
    public static ShortBuffer createShortBuffer(int size) { return createByteBuffer(size * 2).asShortBuffer(); }
}

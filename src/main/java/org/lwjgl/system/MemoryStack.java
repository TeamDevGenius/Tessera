package org.lwjgl.system;

import java.nio.*;

/** Stub for LWJGL MemoryStack. Provides standard NIO buffers. */
public class MemoryStack implements AutoCloseable {

    private static final ThreadLocal<MemoryStack> STACK = new ThreadLocal<MemoryStack>() {
        @Override protected MemoryStack initialValue() { return new MemoryStack(); }
    };

    public static MemoryStack stackPush() { return STACK.get(); }
    public static MemoryStack stackGet() { return STACK.get(); }

    public FloatBuffer mallocFloat(int size) { return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer(); }
    public IntBuffer mallocInt(int size) { return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); }
    public LongBuffer mallocLong(int size) { return ByteBuffer.allocateDirect(size * 8).order(ByteOrder.nativeOrder()).asLongBuffer(); }
    public DoubleBuffer mallocDouble(int size) { return ByteBuffer.allocateDirect(size * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer(); }
    public ByteBuffer malloc(int size) { return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()); }
    public ShortBuffer mallocShort(int size) { return ByteBuffer.allocateDirect(size * 2).order(ByteOrder.nativeOrder()).asShortBuffer(); }

    public FloatBuffer floats(float... values) {
        FloatBuffer buf = mallocFloat(values.length);
        buf.put(values).rewind();
        return buf;
    }

    public IntBuffer ints(int... values) {
        IntBuffer buf = mallocInt(values.length);
        buf.put(values).rewind();
        return buf;
    }

    public LongBuffer longs(long... values) {
        LongBuffer buf = mallocLong(values.length);
        buf.put(values).rewind();
        return buf;
    }

    public ByteBuffer bytes(byte... values) {
        ByteBuffer buf = malloc(values.length);
        buf.put(values).rewind();
        return buf;
    }

    public ByteBuffer UTF8(String text) { return MemoryUtil.memUTF8(text); }
    public ByteBuffer UTF8(String text, boolean nullTerminated) { return MemoryUtil.memUTF8(text, nullTerminated); }
    public ByteBuffer ASCII(String text) { return MemoryUtil.memASCII(text); }
    public ByteBuffer calloc(int size) { return malloc(size); }

    @Override
    public void close() {}
}

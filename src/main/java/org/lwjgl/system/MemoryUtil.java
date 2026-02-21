package org.lwjgl.system;

import java.nio.*;

/** Stub for LWJGL MemoryUtil. */
public class MemoryUtil {

    public static final long NULL = 0L;

    public static IntBuffer memAllocInt(int size) { return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); }
    public static FloatBuffer memAllocFloat(int size) { return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer(); }
    public static LongBuffer memAllocLong(int size) { return ByteBuffer.allocateDirect(size * 8).order(ByteOrder.nativeOrder()).asLongBuffer(); }
    public static DoubleBuffer memAllocDouble(int size) { return ByteBuffer.allocateDirect(size * 8).order(ByteOrder.nativeOrder()).asDoubleBuffer(); }
    public static ByteBuffer memAlloc(int size) { return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()); }

    public static void memFree(Buffer buffer) {}

    public static long memAddress(Buffer buffer) { return 0L; }
    public static long memAddress(ByteBuffer buffer) { return 0L; }

    public static String memASCII(ByteBuffer buffer) {
        if (buffer == null) return "";
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, java.nio.charset.StandardCharsets.US_ASCII);
    }

    public static String memASCII(long address) { return ""; }
    public static String memUTF8(long address) { return ""; }
    public static String memUTF16(long address) { return ""; }
    public static ByteBuffer memUTF8(String text) { return ByteBuffer.wrap(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)); }
    public static ByteBuffer memUTF8(String text, boolean nullTerminated) { return ByteBuffer.wrap(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)); }
    public static ByteBuffer memASCII(String text) { return ByteBuffer.wrap(text.getBytes(java.nio.charset.StandardCharsets.US_ASCII)); }
    public static ByteBuffer memSlice(ByteBuffer buffer) { return buffer.slice(); }
    public static IntBuffer memRealloc(IntBuffer old, int newCapacity) { IntBuffer n = ByteBuffer.allocateDirect(newCapacity * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); if (old != null) { old.rewind(); n.put(old); n.rewind(); } return n; }

    public static ByteBuffer UTF8(String text) { return memUTF8(text); }
    public static ByteBuffer UTF8(String text, boolean nullTerminated) { return memUTF8(text, nullTerminated); }

    public static void memCopy(long src, long dst, long bytes) {}
    public static void memCopy(ByteBuffer src, ByteBuffer dst) {}

    public static ByteBuffer memByteBuffer(long address, int capacity) {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    }

    public static long nmemAllocChecked(long size) { return 0L; }
    public static void nmemFree(long ptr) {}

    public static IntBuffer memCallocInt(int size) { return memAllocInt(size); }
}

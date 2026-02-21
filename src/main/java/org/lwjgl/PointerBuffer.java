package org.lwjgl;

import java.nio.LongBuffer;

/** Stub for LWJGL PointerBuffer. */
public class PointerBuffer {
    private final LongBuffer buffer;
    private PointerBuffer(int capacity) { buffer = LongBuffer.allocate(capacity); }
    public static PointerBuffer allocateDirect(int capacity) { return new PointerBuffer(capacity); }
    public long get(int index) { return buffer.get(index); }
    public PointerBuffer put(int index, long value) { buffer.put(index, value); return this; }
    public PointerBuffer put(long value) { buffer.put(value); return this; }
    public int capacity() { return buffer.capacity(); }
    public int limit() { return buffer.limit(); }
    public PointerBuffer rewind() { buffer.rewind(); return this; }
}

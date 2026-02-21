package org.lwjgl.nuklear;

/** Stub for LWJGL NkColor. */
public class NkColor {
    public static final int SIZEOF = 4;

    private byte r, g, b, a;

    private NkColor() {}
    public static NkColor create() { return new NkColor(); }
    public static NkColor malloc() { return new NkColor(); }
    public static NkColor malloc(org.lwjgl.system.MemoryStack stack) { return new NkColor(); }
    public static NkColor calloc(org.lwjgl.system.MemoryStack stack) { return new NkColor(); }
    public static NkColor mallocStack(org.lwjgl.system.MemoryStack stack) { return new NkColor(); }

    public byte r() { return r; }
    public byte g() { return g; }
    public byte b() { return b; }
    public byte a() { return a; }
    public NkColor r(byte v) { this.r = v; return this; }
    public NkColor g(byte v) { this.g = v; return this; }
    public NkColor b(byte v) { this.b = v; return this; }
    public NkColor a(byte v) { this.a = v; return this; }
    public NkColor set(NkColor other) { this.r = other.r; this.g = other.g; this.b = other.b; this.a = other.a; return this; }
    public NkColor set(byte r, byte g, byte b, byte a) { this.r = r; this.g = g; this.b = b; this.a = a; return this; }

    /** Buffer of NkColor structs. */
    public static class Buffer {
        private final int capacity;
        public Buffer(int capacity) { this.capacity = capacity; }
        public Buffer(java.nio.ByteBuffer buf) { this.capacity = buf.capacity() / SIZEOF; }
        public static Buffer create(int capacity) { return new Buffer(capacity); }
        public Buffer put(int index, NkColor color) { return this; }
        public Buffer put(NkColor color) { return this; }
        public Buffer position(int pos) { return this; }
        public int capacity() { return capacity; }
    }
}


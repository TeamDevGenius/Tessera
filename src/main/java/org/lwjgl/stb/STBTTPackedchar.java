package org.lwjgl.stb;

/** Stub for LWJGL STBTTPackedchar. */
public class STBTTPackedchar {
    private STBTTPackedchar() {}

    public float xoff() { return 0; }
    public float yoff() { return 0; }
    public float xadvance() { return 0; }

    public static Buffer create(int capacity) { return new Buffer(capacity); }
    public static Buffer malloc(int capacity) { return new Buffer(capacity); }
    public static Buffer malloc(org.lwjgl.system.MemoryStack stack, int capacity) { return new Buffer(capacity); }

    public static class Buffer {
        private final int capacity;
        public Buffer(int capacity) { this.capacity = capacity; }
        public static Buffer create(int capacity) { return new Buffer(capacity); }
        public static Buffer malloc(int capacity) { return new Buffer(capacity); }

        public Buffer position(int pos) { return this; }
        public Buffer limit(int limit) { return this; }
        public int capacity() { return capacity; }
    }
}

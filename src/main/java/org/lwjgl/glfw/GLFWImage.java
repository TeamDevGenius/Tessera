package org.lwjgl.glfw;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL GLFWImage. */
public class GLFWImage {
    private int width, height;

    private GLFWImage() {}

    public int width() { return width; }
    public int height() { return height; }
    public GLFWImage width(int w) { this.width = w; return this; }
    public GLFWImage height(int h) { this.height = h; return this; }
    public GLFWImage pixels(java.nio.ByteBuffer pixels) { return this; }

    public static class Buffer {
        private final int capacity;
        private int position;

        public Buffer(int capacity) { this.capacity = capacity; }
        public static Buffer malloc(int capacity) { return new Buffer(capacity); }
        public static Buffer malloc(int capacity, MemoryStack stack) { return new Buffer(capacity); }

        public Buffer position(int pos) { this.position = pos; return this; }
        public Buffer width(int w) { return this; }
        public Buffer height(int h) { return this; }
        public Buffer pixels(java.nio.ByteBuffer pixels) { return this; }
        public int capacity() { return capacity; }
    }
}

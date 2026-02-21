package org.lwjgl.nuklear;

/** Stub for LWJGL NkDrawVertexLayoutElement. */
public class NkDrawVertexLayoutElement {
    private NkDrawVertexLayoutElement() {}

    public NkDrawVertexLayoutElement position(int pos) { return this; }
    public NkDrawVertexLayoutElement attribute(int attr) { return this; }
    public NkDrawVertexLayoutElement format(int fmt) { return this; }
    public NkDrawVertexLayoutElement offset(int off) { return this; }

    public static class Buffer {
        private Buffer() {}
        public static Buffer create(int capacity) { return new Buffer(); }
        public Buffer position(int pos) { return this; }
        public Buffer attribute(int attr) { return this; }
        public Buffer format(int fmt) { return this; }
        public Buffer offset(int off) { return this; }
        public Buffer flip() { return this; }
    }
}

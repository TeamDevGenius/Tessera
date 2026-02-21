package org.lwjgl.nuklear;

/** Stub for LWJGL NkHandle. */
public class NkHandle {
    private int id;
    private long ptr;

    public NkHandle() {}
    public static NkHandle create() { return new NkHandle(); }

    public int id() { return id; }
    public NkHandle id(int v) { this.id = v; return this; }
    public long ptr() { return ptr; }
    public NkHandle ptr(long v) { this.ptr = v; return this; }
    public long address() { return ptr; }
}

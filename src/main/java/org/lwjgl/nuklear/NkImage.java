package org.lwjgl.nuklear;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL NkImage. */
public class NkImage {
    private NkHandle handle = new NkHandle();

    private NkImage() {}

    public static NkImage create() { return new NkImage(); }
    public static NkImage malloc() { return new NkImage(); }
    public static NkImage malloc(MemoryStack stack) { return new NkImage(); }
    public static NkImage calloc(MemoryStack stack) { return new NkImage(); }

    public NkHandle handle() { return handle; }
    public NkImage handle(java.util.function.Consumer<NkHandle> fn) { if (fn != null) fn.accept(handle); return this; }
}

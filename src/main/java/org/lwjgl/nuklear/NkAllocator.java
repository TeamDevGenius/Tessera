package org.lwjgl.nuklear;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL NkAllocator. */
public class NkAllocator {
    private NkPluginAllocI alloc;
    private NkPluginFreeI mfree;

    private NkAllocator() {}
    public static NkAllocator create() { return new NkAllocator(); }

    public NkAllocator alloc(NkPluginAllocI fn) { this.alloc = fn; return this; }
    public NkAllocator mfree(NkPluginFreeI fn) { this.mfree = fn; return this; }
    public NkPluginAllocI alloc() { return alloc; }
    public NkPluginFreeI mfree() { return mfree; }

    public interface NkPluginAllocI { long invoke(long handle, long old, long size); default void free() {} }
    public interface NkPluginFreeI { void invoke(long handle, long ptr); default void free() {} }
}

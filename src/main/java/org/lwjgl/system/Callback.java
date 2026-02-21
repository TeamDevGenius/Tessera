package org.lwjgl.system;

/** Stub for LWJGL Callback. */
public abstract class Callback implements AutoCloseable {
    public void free() {}
    @Override public void close() { free(); }
}

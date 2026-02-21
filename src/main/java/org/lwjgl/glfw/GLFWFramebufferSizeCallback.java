package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/** Stub for LWJGL GLFWFramebufferSizeCallback. */
public abstract class GLFWFramebufferSizeCallback extends Callback {
    public abstract void invoke(long window, int width, int height);
    public void free() {}
}

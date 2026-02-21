package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/** Stub for LWJGL GLFWWindowFocusCallback. */
public abstract class GLFWWindowFocusCallback extends Callback {
    public abstract void invoke(long window, boolean focused);
    public void free() {}
}

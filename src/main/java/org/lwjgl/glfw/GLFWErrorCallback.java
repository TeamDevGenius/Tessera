package org.lwjgl.glfw;

import org.lwjgl.system.Callback;

/** Stub for LWJGL GLFWErrorCallback. */
public abstract class GLFWErrorCallback extends Callback {
    public abstract void invoke(int error, long description);
    public void free() {}
}

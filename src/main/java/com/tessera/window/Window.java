package com.tessera.window;

/**
 * Base window class backed by LibGDX instead of GLFW.
 * Maintains API compatibility with existing subclasses.
 */
public abstract class Window extends GLFWWindow {

    @Override
    public void createWindow(String title, int width, int height) {
        super.createWindow(title, width, height);
    }
}

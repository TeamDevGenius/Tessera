package com.tessera.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkVec2;

/**
 * UI window abstraction - Nuklear replaced by LibGDX-compatible stubs.
 * The NkContext is kept as a stub type for API compatibility with existing code.
 */
public abstract class NKWindow extends GLFWWindow {

    public NkContext ctx = NkContext.create();

    public NKWindow() {
        super();
    }

    @Override
    public void createWindow(String title, int width, int height) {
        super.createWindow(title, width, height);
    }

    @Override
    public void startFrame() {
        super.startFrame();
    }

    @Override
    public void endFrame() {
        super.endFrame();
    }

    @Override
    public void destroyWindow() {
        super.destroyWindow();
    }

    /** Called to render the Nuklear UI layer. No-op stub - override in subclasses. */
    public void NKrender() {}

    public abstract void keyEvent(int key, int scancode, int action, int mods);
    public abstract void mouseButtonEvent(int button, int action, int mods);
    public abstract void mouseScrollEvent(NkVec2 scroll, double xoffset, double yoffset);
}

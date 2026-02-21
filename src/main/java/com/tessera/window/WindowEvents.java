package com.tessera.window;

import org.lwjgl.nuklear.NkVec2;

/**
 * Window events interface - maintains API compatibility with existing implementations.
 * NkVec2 kept as stub type; replace with LibGDX types in future migration.
 */
public interface WindowEvents {
    void windowResizeEvent(int width, int height);
    boolean keyEvent(int key, int scancode, int action, int mods);
    boolean mouseButtonEvent(int button, int action, int mods);
    boolean mouseScrollEvent(NkVec2 scroll, double xoffset, double yoffset);
}

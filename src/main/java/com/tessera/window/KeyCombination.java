package com.tessera.window;

/**
 * Key combination - uses GLFW key constants (via our stub) for API compatibility.
 * Future: migrate to LibGDX Input.Keys.
 */
public class KeyCombination {
    public final int key;
    public final int mods;

    public KeyCombination(int key, int mods) {
        this.key = key;
        this.mods = mods;
    }
}

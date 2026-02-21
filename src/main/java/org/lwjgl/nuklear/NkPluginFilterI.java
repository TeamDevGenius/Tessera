package org.lwjgl.nuklear;

/** Stub for LWJGL NkPluginFilterI functional interface. */
@FunctionalInterface
public interface NkPluginFilterI {
    boolean invoke(long edit, int unicode);
}

package org.lwjgl.nuklear;

import org.lwjgl.system.MemoryStack;

/** Stub for LWJGL NkContext. */
public class NkContext {
    private NkInput input = new NkInput();
    private NkClipboard clip = new NkClipboard();
    private NkStyle style = NkStyle.create();

    private NkContext() {}

    public static NkContext create() { return new NkContext(); }
    public static NkContext malloc() { return new NkContext(); }

    public NkInput input() { return input; }
    public NkClipboard clip() { return clip; }
    public NkStyle style() { return style; }
}

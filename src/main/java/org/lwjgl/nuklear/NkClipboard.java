package org.lwjgl.nuklear;

/** Stub for LWJGL NkClipboard. */
public class NkClipboard {
    private NkPluginCopyI copy;
    private NkPluginPasteI paste;

    public NkPluginCopyI copy() { return copy; }
    public NkPluginPasteI paste() { return paste; }
    public NkClipboard copy(NkPluginCopyI fn) { this.copy = fn; return this; }
    public NkClipboard paste(NkPluginPasteI fn) { this.paste = fn; return this; }

    public interface NkPluginCopyI { void invoke(long handle, long text, int len); default void free() {} }
    public interface NkPluginPasteI { void invoke(long handle, long edit); default void free() {} }
}

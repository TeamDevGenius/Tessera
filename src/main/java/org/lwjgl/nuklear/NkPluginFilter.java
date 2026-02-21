package org.lwjgl.nuklear;

/** Stub for LWJGL NkPluginFilter. */
public class NkPluginFilter implements NkPluginFilterI {
    private final NkPluginFilterI delegate;

    private NkPluginFilter(NkPluginFilterI delegate) { this.delegate = delegate; }

    @Override
    public boolean invoke(long edit, int unicode) {
        return delegate == null || delegate.invoke(edit, unicode);
    }

    public static final NkPluginFilter NK_FILTER_DEFAULT = new NkPluginFilter((e, u) -> true);
    public static final NkPluginFilter NK_FILTER_ASCII   = new NkPluginFilter((e, u) -> u >= 0 && u < 128);
    public static final NkPluginFilter NK_FILTER_FLOAT   = new NkPluginFilter((e, u) -> (u >= '0' && u <= '9') || u == '.' || u == '-');
    public static final NkPluginFilter NK_FILTER_DECIMAL = new NkPluginFilter((e, u) -> (u >= '0' && u <= '9') || u == '-');
    public static final NkPluginFilter NK_FILTER_HEX     = new NkPluginFilter((e, u) -> (u >= '0' && u <= '9') || (u >= 'a' && u <= 'f') || (u >= 'A' && u <= 'F'));
    public static final NkPluginFilter NK_FILTER_OCT     = new NkPluginFilter((e, u) -> u >= '0' && u <= '7');
    public static final NkPluginFilter NK_FILTER_BINARY  = new NkPluginFilter((e, u) -> u == '0' || u == '1');

    public static NkPluginFilter create(NkPluginFilterI filter) { return new NkPluginFilter(filter); }
}


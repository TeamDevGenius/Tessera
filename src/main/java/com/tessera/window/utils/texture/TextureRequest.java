package com.tessera.window.utils.texture;

import com.tessera.engine.utils.resource.ResourceLoader;
import com.tessera.window.utils.IOUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TextureRequest {
    ByteBuffer image;
    /** Resource path; used by libGDX texture loading instead of a pre-loaded ByteBuffer. */
    String path;
    int regionX;
    int regionY;
    int regionWidth;
    int regionHeight;

    public TextureRequest(ByteBuffer buffer,
                          int regionX, int regionY, int regionWidth, int regionHeight) {
        image = buffer;
        this.regionX = regionX;
        this.regionY = regionY;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
    }

    public TextureRequest(ByteBuffer buffer) {
        image = buffer;
        regionX = -1;
        regionY = -1;
        regionWidth = -1;
        regionHeight = -1;
    }

    private final static ResourceLoader resourceLoader = new ResourceLoader();

    public TextureRequest(String resource,
                          int regionX, int regionY, int regionWidth, int regionHeight) throws IOException {
        image = IOUtil.inputStreamToByteBuffer(resourceLoader.getResourceAsStream(resource),512);
        this.path = resource;
        this.regionX = regionX;
        this.regionY = regionY;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
    }

    public TextureRequest(String resource) throws IOException {
        image = IOUtil.inputStreamToByteBuffer(resourceLoader.getResourceAsStream(resource),512);
        this.path = resource;
        regionX = -1;
        regionY = -1;
        regionWidth = -1;
        regionHeight = -1;
    }

    /** Private constructor used by gdxRequest factory methods - does NOT load a ByteBuffer. */
    private TextureRequest(String resource, int regionX, int regionY, int regionWidth, int regionHeight, boolean pathOnly) {
        this.path = resource;
        this.image = null;
        this.regionX = regionX;
        this.regionY = regionY;
        this.regionWidth = regionWidth;
        this.regionHeight = regionHeight;
    }

    /**
     * Create a TextureRequest that stores only the resource path, without pre-loading a ByteBuffer.
     * Used by the libGDX/Android texture loading path.
     */
    public static TextureRequest gdxRequest(String resource) {
        return new TextureRequest(resource, -1, -1, -1, -1, true);
    }

    /**
     * Create a TextureRequest for a sub-region that stores only the resource path.
     * Used by the libGDX/Android texture loading path.
     */
    public static TextureRequest gdxRequest(String resource, int regionX, int regionY, int regionWidth, int regionHeight) {
        return new TextureRequest(resource, regionX, regionY, regionWidth, regionHeight, true);
    }

}

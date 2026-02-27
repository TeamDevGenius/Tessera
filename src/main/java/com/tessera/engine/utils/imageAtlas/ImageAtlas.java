package com.tessera.engine.utils.imageAtlas;

import java.io.File;
import java.io.IOException;

/**
 * Texture atlas utility – AWT removed for Android compatibility.
 * This class is not currently used at runtime.
 */
public class ImageAtlas {

    private int imageSize;
    private int individualTextureSize;

    public int getIndividualTextureSize() { return individualTextureSize; }
    public int getImageWidth() { return imageSize; }

    public ImageAtlas(File imagePath, int atlasRows) throws IOException {
        if (!imagePath.exists()) {
            throw new IOException("The image \"" + imagePath.getAbsolutePath() + "\" does not exist!");
        }
        // Read image dimensions via Pixmap (LibGDX) if available, else use file-size heuristic
        try {
            Class<?> pixmapClass = Class.forName("com.badlogic.gdx.graphics.Pixmap");
            byte[] bytes = java.nio.file.Files.readAllBytes(imagePath.toPath());
            Object pixmap = pixmapClass.getDeclaredConstructor(byte[].class, int.class, int.class)
                    .newInstance(bytes, 0, bytes.length);
            imageSize = (int) pixmapClass.getMethod("getWidth").invoke(pixmap);
            pixmapClass.getMethod("dispose").invoke(pixmap);
        } catch (Throwable e) {
            throw new IOException("Cannot determine image dimensions without AWT or LibGDX", e);
        }
        individualTextureSize = imageSize / atlasRows;
    }

    public ImageAtlasPosition getImageIndex(int[] pos) {
        float texturePerRow = (float) getImageWidth() / (float) getIndividualTextureSize();
        float indvTexSize = 1.0f / texturePerRow;
        float pixelSize = 1.0f / (float) getImageWidth();
        float xMin = (pos[0] * indvTexSize) + 0.0f * pixelSize;
        float yMin = (pos[1] * indvTexSize) + 0.0f * pixelSize;
        float xMax = xMin + indvTexSize - 0.0f * pixelSize;
        float yMax = yMin + indvTexSize - 0.0f * pixelSize;
        return new ImageAtlasPosition(xMin, yMin, xMax, yMax);
    }
}

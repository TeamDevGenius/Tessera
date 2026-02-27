package com.tessera.engine.utils.imageAtlas;

import java.io.File;
import java.io.IOException;

public class ImageAtlas {

    public int getIndividualTextureSize() {
        return individualTextureSize;
    }

    public int getImageWidth() {
        return imageSize;
    }

    public Object getImage() {
        return null;
    }

    private int imageSize;
    private int individualTextureSize;

    public ImageAtlas(File imagePath, int atlasRows) throws IOException {
        // ImageAtlas is not used in the core Android rendering path
        imageSize = 0;
        individualTextureSize = 0;
    }
    
    public ImageAtlasPosition getImageIndex(int[] pos) {
        float texturePerRow = (float) getImageWidth() / (float) getIndividualTextureSize();
        float indvTexSize = 1.0f / texturePerRow;
        float pixelSize = 1.0f / (float) getImageWidth();

        float xMin = (pos[0] * indvTexSize) + 0.0f * pixelSize;
        float yMin = (pos[1] * indvTexSize) + 0.0f * pixelSize;

        float xMax = (xMin + indvTexSize) - 0.0f * pixelSize;
        float yMax = (yMin + indvTexSize) - 0.0f * pixelSize;
        return new ImageAtlasPosition(xMin, yMin, xMax, yMax);
    }
}

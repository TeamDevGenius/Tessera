package com.tessera.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tessera.TesseraApp;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL30 = true; // Enable GLES 3.0 for texture arrays
        config.numSamples = 0; // Disable MSAA for performance
        config.useAccelerometer = false;
        config.useCompass = false;
        initialize(new TesseraApp(), config);
    }
}

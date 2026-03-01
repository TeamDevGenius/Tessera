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
        // Use GL30 (OpenGL ES 3.0) on API 18+ devices; fall back to GL20 on older devices
        config.useGL30 = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
        config.numSamples = 2;
        config.useAccelerometer = false;
        config.useCompass = false;
        initialize(new TesseraApp(), config);
    }
}

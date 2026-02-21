package com.tessera.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tessera.TesseraApp;

public class DesktopLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Tessera");
        config.setWindowedMode(920, 720);
        config.setWindowIcon("builtin/icon32.png", "builtin/icon16.png");
        new Lwjgl3Application(new TesseraApp(), config);
    }
}

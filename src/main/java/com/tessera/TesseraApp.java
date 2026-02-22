package com.tessera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.tessera.window.GdxInputAdapter;

/**
 * Main LibGDX {@link ApplicationAdapter} for Tessera.
 * Initializes the actual game engine in {@link #create()} and delegates every
 * lifecycle call to the existing engine classes so that both the desktop
 * (via {@link com.tessera.desktop.DesktopLauncher}) and Android
 * (via {@link com.tessera.android.AndroidLauncher}) platforms run the real game.
 */
public class TesseraApp extends ApplicationAdapter {

    public static final String TITLE   = "Tessera";
    public static final String VERSION = "1.8.0";

    @Override
    public void create() {
        try {
            Main.initialize(new String[0]);
            Gdx.input.setInputProcessor(new GdxInputAdapter(Main.getClient().window));
            Gdx.app.log(TITLE, "Game engine initialized — version " + VERSION);
        } catch (Exception e) {
            Gdx.app.error(TITLE, "Failed to initialize game engine — the game will not run correctly", e);
        }
    }

    @Override
    public void render() {
        if (Main.getClient() != null) {
            Main.getClient().window.renderFrame();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (Main.getClient() != null) {
            Main.getClient().window.framebufferResizeEvent(width, height);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (Main.getClient() != null) {
            Main.getClient().stopGame();
            Main.getClient().window.destroyWindow();
        }
    }
}

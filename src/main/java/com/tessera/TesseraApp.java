package com.tessera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.tessera.content.vanilla.TesseraGame;
import com.tessera.engine.SkinRegistry;
import com.tessera.engine.client.Client;
import com.tessera.engine.utils.resource.ResourceLister;

/**
 * Main LibGDX ApplicationAdapter for Tessera.
 * Works on Android (via AndroidLauncher) and Desktop (via DesktopLauncher).
 */
public class TesseraApp extends ApplicationAdapter {

    public static final String TITLE = "Tessera";
    public static final String VERSION = "1.8.0";

    private boolean initialized = false;

    @Override
    public void create() {
        Gdx.app.log(TITLE, "Starting version " + VERSION);
        try {
            ResourceLister.init();
            Main.skins = new SkinRegistry();
            Main.game = new TesseraGame();
            Main.localClient = new Client(new String[0], VERSION, Main.game);
            initialized = true;
            Gdx.app.log(TITLE, "Game initialized successfully");
        } catch (Exception e) {
            Gdx.app.error(TITLE, "Failed to initialize game", e);
        }
    }

    @Override
    public void render() {
        if (!initialized) {
            Gdx.gl.glClearColor(0.2f, 0.1f, 0.1f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            return;
        }
        try {
            Main.getClient().window.renderFrame();
        } catch (Exception e) {
            Gdx.app.error(TITLE, "Render error", e);
        }
    }

    @Override
    public void resize(int width, int height) {
        if (initialized && Main.getClient() != null) {
            Main.getClient().window.framebufferResizeEvent(width, height);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (initialized && Main.getClient() != null) {
            Main.getClient().window.destroyWindow();
        }
    }
}

package com.tessera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Main LibGDX ApplicationAdapter for Tessera.
 * Replaces the LWJGL-based ClientWindow and Main entry point.
 * Works on Android (via AndroidLauncher) and Desktop (via DesktopLauncher).
 */
public class TesseraApp extends ApplicationAdapter {

    public static final String TITLE = "Tessera";
    public static final String VERSION = "1.8.0";

    private SpriteBatch batch;
    private BitmapFont font;
    private Stage uiStage;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        uiStage = new Stage(new ScreenViewport());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);

        Gdx.app.log(TITLE, "Started version " + VERSION);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();

        batch.begin();
        font.draw(batch, TITLE + " " + VERSION, 10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 30);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        uiStage.dispose();
    }
}

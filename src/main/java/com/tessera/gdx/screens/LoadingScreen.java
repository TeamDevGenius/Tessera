package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;

public class LoadingScreen implements Screen {

    private final TesseraApp app;
    private final String worldName;
    private final int seed;
    private final String terrain;

    private static final float PROGRESS_INCREMENT_RATE = 0.3f;
    private static final float MAX_PROGRESS_BEFORE_COMPLETE = 0.95f;

    private Stage stage;
    private Label statusLabel;
    private Skin skin;
    private Texture whitePixel;
    private TextureRegion whiteRegion;
    private float progress;
    private volatile boolean loadComplete;
    private volatile String statusMessage = "Initializing...";

    public LoadingScreen(TesseraApp app, String worldName, int seed, String terrain) {
        this.app = app;
        this.worldName = worldName;
        this.seed = seed;
        this.terrain = terrain;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        whitePixel = new Texture(pm);
        pm.dispose();
        whiteRegion = new TextureRegion(whitePixel);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", style);

        Table table = new Table();
        table.setFillParent(true);

        Label title = new Label("Loading World...", new Label.LabelStyle(font, Color.CYAN));
        table.add(title).padBottom(20).row();

        statusLabel = new Label(statusMessage, style);
        table.add(statusLabel).padBottom(10).row();

        stage.addActor(table);

        startLoading();
    }

    private void startLoading() {
        new Thread(() -> {
            try {
                statusMessage = "Setting up game...";
                Thread.sleep(100);
                statusMessage = "Loading world data...";
                Thread.sleep(200);
                statusMessage = "Generating terrain...";
                Thread.sleep(300);
                statusMessage = "Finalizing...";
                Thread.sleep(100);
                loadComplete = true;
            } catch (Exception e) {
                statusMessage = "Error: " + e.getMessage();
            }
        }, "WorldLoader").start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (statusLabel != null) {
            statusLabel.setText(statusMessage);
        }

        progress = Math.min(progress + delta * PROGRESS_INCREMENT_RATE, loadComplete ? 1.0f : MAX_PROGRESS_BEFORE_COMPLETE);

        if (loadComplete && progress >= 0.99f) {
            app.setScreen(new GameScreen(app));
            return;
        }

        stage.act(delta);
        stage.draw();

        // Draw progress bar
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        int barW = (int) (w * 0.6f);
        int barH = 20;
        int barX = (w - barW) / 2;
        int barY = h / 2 - 60;

        app.batch.begin();
        app.batch.setColor(0.3f, 0.3f, 0.3f, 1f);
        app.batch.draw(whiteRegion, barX, barY, barW, barH);
        app.batch.setColor(0.2f, 0.8f, 0.2f, 1f);
        app.batch.draw(whiteRegion, barX, barY, (int) (barW * progress), barH);
        app.batch.setColor(Color.WHITE);
        app.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (stage != null) { stage.dispose(); stage = null; }
        if (skin != null) { skin.dispose(); skin = null; }
        if (whitePixel != null) { whitePixel.dispose(); whitePixel = null; }
    }
}

package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.gdx.GdxGameInitializer;
import com.tessera.gdx.input.TouchControls;
import com.tessera.gdx.render.WorldRenderer;
import com.tessera.gdx.ui.GameHUD;

public class GameScreen implements Screen {

    private final TesseraApp app;
    private PerspectiveCamera camera;
    private Stage hudStage;
    private GameHUD hud;
    private TouchControls touchControls;
    private BitmapFont font;
    private WorldRenderer worldRenderer;

    public GameScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 64, 0);
        camera.lookAt(1, 64, 0);
        camera.near = 0.1f;
        camera.far = 512f;
        camera.update();

        font = new BitmapFont();
        hudStage = new Stage(new ScreenViewport());

        touchControls = new TouchControls(camera, GdxGameInitializer.gdxWorld,
                                          GdxGameInitializer.gdxPlayer);

        hud = new GameHUD(hudStage, font, app,
                          GdxGameInitializer.gdxPlayer,
                          touchControls::jump);

        // HUD stage handles pause/jump button taps first; remainder falls to TouchControls
        Gdx.input.setInputProcessor(new InputMultiplexer(hudStage, touchControls));

        if (GdxGameInitializer.gdxWorld != null) {
            worldRenderer = new WorldRenderer(camera, GdxGameInitializer.gdxWorld);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.65f, 0.9f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        // Physics, player-position sync and stat tick happen here
        touchControls.update(delta, camera);
        camera.update();

        if (worldRenderer != null) worldRenderer.render(delta);

        // ── 2-D overlay pass (no depth test) ─────────────────────────────────
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        // Virtual joystick circles
        touchControls.renderOverlay(app.batch);

        // Hotbar, status bars and crosshair drawn via ShapeRenderer
        hud.renderShapes();

        // Stage actors: FPS/coords labels, pause & jump buttons, item name
        hud.update(delta, camera);
        hudStage.act(delta);
        hudStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth  = width;
        camera.viewportHeight = height;
        camera.update();
        hudStage.getViewport().update(width, height, true);
    }

    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (hudStage      != null) { hudStage.dispose();      hudStage      = null; }
        if (font          != null) { font.dispose();          font          = null; }
        if (worldRenderer != null) { worldRenderer.dispose(); worldRenderer = null; }
        if (hud           != null) { hud.dispose();           hud           = null; }
        if (touchControls != null) { touchControls.dispose(); touchControls = null; }
    }
}

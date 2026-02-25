package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.client.Client;
import com.tessera.engine.client.ClientWindow;

/**
 * LibGDX game screen – delegates rendering to the engine's
 * {@link ClientWindow} render pipeline when a world is loaded.
 * Shows an in-game HUD and a pause menu overlay.
 */
public class GameScreen implements Screen {

    private final TesseraApp app;
    private Stage hudStage;
    private SpriteBatch batch;
    private BitmapFont font;
    private Skin skin;
    private boolean paused = false;

    public GameScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        hudStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(hudStage);
        skin = app.getSkin();
        font = skin.getFont("default-font");
        buildHud();
    }

    private void buildHud() {
        hudStage.clear();

        // Pause / back-to-menu button in top-left
        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top().left().pad(10);

        TextButton menuBtn = new TextButton("Menu", skin);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (paused) {
                    returnToMenu();
                } else {
                    paused = true;
                    buildPauseOverlay();
                }
            }
        });
        topBar.add(menuBtn).width(80).height(32);
        hudStage.addActor(topBar);
    }

    private void buildPauseOverlay() {
        hudStage.clear();
        Table root = new Table();
        root.setFillParent(true);

        Label pauseLabel = new Label("PAUSED", skin, "title");
        root.add(pauseLabel).padBottom(20).row();

        TextButton resumeBtn = new TextButton("RESUME", skin);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = false;
                buildHud();
            }
        });
        root.add(resumeBtn).width(250).height(44).padBottom(8).row();

        TextButton quitBtn = new TextButton("QUIT TO MENU", skin);
        quitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnToMenu();
            }
        });
        root.add(quitBtn).width(250).height(44).padBottom(8).row();

        hudStage.addActor(root);
    }

    private void returnToMenu() {
        paused = false;
        try {
            Client client = app.getClient();
            if (client != null) {
                client.stopGame();
            }
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error stopping game", e);
        }
        app.goToMainMenu();
    }

    @Override
    public void render(float delta) {
        // Let the engine's render pipeline handle the scene.
        // GL stubs now delegate to Gdx.gl, so existing OpenGL-based
        // rendering in ClientWindow/GameScene will execute.
        try {
            ClientWindow window = app.getClientWindow();
            if (window != null && ClientWindow.isInGamePage()) {
                // The server update + game scene render runs through the engine
                if (Client.localServer != null) {
                    Client.localServer.update();
                }
                window.gameScene.render();
            } else {
                // Fallback: clear screen with game-sky blue
                Gdx.gl.glClearColor(0.4f, 0.6f, 0.9f, 1f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            }
        } catch (Exception e) {
            // Fallback rendering on error
            Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.app.error("GameScreen", "Render error", e);
        }

        // HUD overlay on top of the 3D scene
        hudStage.act(delta);
        hudStage.draw();

        // Debug info
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10,
                  Gdx.graphics.getHeight() - 10);
        font.draw(batch, TesseraApp.TITLE + " " + TesseraApp.VERSION, 10,
                  Gdx.graphics.getHeight() - 30);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        hudStage.getViewport().update(width, height, true);
        // Notify engine of resize
        ClientWindow window = app.getClientWindow();
        if (window != null) {
            window.framebufferResizeEvent(width, height);
        }
    }

    @Override
    public void pause() {
        paused = true;
        buildPauseOverlay();
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (hudStage != null) hudStage.dispose();
        if (batch != null) batch.dispose();
    }
}

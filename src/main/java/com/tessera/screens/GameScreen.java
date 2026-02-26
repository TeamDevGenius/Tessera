package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.client.Client;
import com.tessera.engine.client.ClientWindow;

import java.io.IOException;

/**
 * The in-game screen.  Drives the existing ClientWindow render/update loop
 * via the LibGDX lifecycle.
 */
public class GameScreen implements Screen {

    private final TesseraApp app;

    /** Overlay stage for the pause menu */
    private Stage pauseStage;
    private Skin  pauseSkin;
    private boolean paused = false;

    public GameScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        pauseSkin  = ScreenUtils.createSkin();
        pauseStage = new Stage(new ScreenViewport());
        buildPauseMenu();

        // Capture mouse for game
        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    togglePause();
                    return true;
                }
                return false;
            }
        });
    }

    private void buildPauseMenu() {
        Table root = new Table();
        root.setFillParent(true);
        pauseStage.addActor(root);
        root.setVisible(false);

        root.add(new Label("PAUSED", pauseSkin, "title")).padBottom(20).row();

        TextButton resumeBtn = new TextButton("RESUME", pauseSkin);
        resumeBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { togglePause(); }
        });
        root.add(resumeBtn).width(250).height(48).padBottom(10).row();

        TextButton quitBtn = new TextButton("QUIT TO MENU", pauseSkin);
        quitBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                quitToMenu();
            }
        });
        root.add(quitBtn).width(250).height(48).row();

        root.setName("pauseRoot");
    }

    private void togglePause() {
        paused = !paused;
        com.badlogic.gdx.scenes.scene2d.Actor root = pauseStage.getRoot().findActor("pauseRoot");
        if (root != null) root.setVisible(paused);
        if (paused) {
            Gdx.input.setInputProcessor(pauseStage);
        } else {
            Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {
                    if (keycode == Input.Keys.ESCAPE) { togglePause(); return true; }
                    return false;
                }
            });
        }
    }

    private void quitToMenu() {
        paused = false;
        Client c = TesseraApp.client;
        if (c != null) {
            c.stopGame();
            c.window.goToMenuPage();
        }
        app.setScreen(new MainMenuScreen(app));
    }

    @Override
    public void render(float delta) {
        // Clear colour buffer
        Gdx.gl.glClearColor(0f, 0.5f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Client c = TesseraApp.client;
        if (c != null && Client.localServer != null) {
            try {
                // Update game logic
                Client.localServer.update();
                // Render game world via ClientWindow
                if (c.window != null && c.window.gameScene != null) {
                    c.window.gameScene.render();
                }
                // Render HUD / game UI
                if (c.window != null && c.window.gameScene != null
                        && c.window.gameScene.ui != null) {
                    // UI drawing happens inside gameScene.render() already
                }
                ClientWindow.frameCount++;
            } catch (Exception e) {
                Gdx.app.error("GameScreen", "Render error: " + e.getMessage(), e);
            }
        }

        if (paused) {
            // Dim overlay
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            pauseStage.act(delta);
            pauseStage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        pauseStage.getViewport().update(width, height, true);
        Client c = TesseraApp.client;
        if (c != null && c.window != null) {
            c.window.framebufferResizeEvent(width, height);
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }

    @Override
    public void dispose() {
        if (pauseStage != null) { pauseStage.dispose(); pauseStage = null; }
        if (pauseSkin  != null) { pauseSkin.dispose();  pauseSkin  = null; }
    }
}

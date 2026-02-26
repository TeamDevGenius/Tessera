package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.client.Client;
import com.tessera.engine.server.world.WorldsHandler;
import com.tessera.engine.server.world.data.WorldData;
import com.tessera.engine.utils.ErrorHandler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Screen listing saved worlds so the player can load one.
 */
public class LoadWorldScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;
    private final ArrayList<WorldData> worlds = new ArrayList<>();
    private WorldData selectedWorld = null;
    private Label statusLabel;

    public LoadWorldScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin  = ScreenUtils.createSkin();

        // Load world list
        try {
            WorldsHandler.listWorlds(worlds);
        } catch (IOException e) {
            Gdx.app.error("LoadWorldScreen", "Could not list worlds: " + e.getMessage());
        }

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("LOAD WORLD", skin, "title")).padBottom(20).row();

        statusLabel = new Label("", skin);
        root.add(statusLabel).padBottom(10).row();

        if (worlds.isEmpty()) {
            root.add(new Label("No worlds found. Create a new world first.", skin)).padBottom(20).row();
        } else {
            ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
            Table worldList = new Table();
            for (WorldData wd : worlds) {
                final WorldData w = wd;
                TextButton btn = new TextButton(wd.getName(), skin);
                btn.addListener(new ClickListener() {
                    @Override public void clicked(InputEvent e, float x, float y) {
                        selectedWorld = w;
                        statusLabel.setText("Selected: " + w.getName());
                    }
                });
                worldList.add(btn).width(400).height(40).padBottom(6).row();
            }
            ScrollPane scroll = new ScrollPane(worldList);
            root.add(scroll).width(420).height(300).padBottom(16).row();
        }

        TextButton loadBtn = new TextButton("LOAD SELECTED", skin);
        loadBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                if (selectedWorld != null) {
                    loadWorld(selectedWorld);
                } else if (!worlds.isEmpty()) {
                    loadWorld(worlds.get(0));
                } else {
                    statusLabel.setText("No world selected!");
                }
            }
        });
        root.add(loadBtn).width(300).height(48).padBottom(10).row();

        TextButton deleteBtn = new TextButton("DELETE SELECTED", skin);
        deleteBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                if (selectedWorld != null) {
                    try {
                        WorldsHandler.deleteWorld(selectedWorld);
                        worlds.remove(selectedWorld);
                        selectedWorld = null;
                        statusLabel.setText("World deleted.");
                        app.setScreen(new LoadWorldScreen(app));
                    } catch (Exception ex) {
                        statusLabel.setText("Error: " + ex.getMessage());
                    }
                }
            }
        });
        root.add(deleteBtn).width(300).height(40).padBottom(10).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        root.add(backBtn).width(300).height(40).row();
    }

    private void loadWorld(WorldData worldData) {
        try {
            if (TesseraApp.client == null) {
                TesseraApp.client = new Client(new String[0], TesseraApp.VERSION, TesseraApp.game);
            }
            app.setScreen(new LoadingScreen(app, worldData));
        } catch (Exception ex) {
            Gdx.app.error("LoadWorldScreen", "Failed: " + ex.getMessage(), ex);
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() {
        if (stage != null) { stage.dispose(); stage = null; }
        if (skin  != null) { skin.dispose();  skin  = null; }
    }
}

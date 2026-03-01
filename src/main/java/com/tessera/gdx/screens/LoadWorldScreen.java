package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.server.world.WorldsHandler;
import com.tessera.engine.server.world.data.WorldData;
import com.tessera.gdx.ui.UiTheme;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Shows all saved worlds and lets the player load or delete one.
 * Mirrors the desktop LoadWorld.java panel.
 */
public class LoadWorldScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    private final ArrayList<WorldData> worlds = new ArrayList<>();
    private WorldData selected = null;

    // Detail panel widgets
    private Label detailLabel;
    private TextButton loadBtn;
    private TextButton deleteBtn;

    public LoadWorldScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        // Load world list (gracefully handles missing dir)
        refreshWorlds();

        Table root = new Table();
        root.setFillParent(true);
        root.top().center();
        root.pad(20);

        Label title = new Label("LOAD WORLD",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(20).colspan(2).row();

        // Left: scrollable world list
        Table worldList = new Table();
        worldList.top().left();
        if (worlds.isEmpty()) {
            worldList.add(new Label("No saved worlds found.", skin)).left().row();
        } else {
            for (final WorldData wd : worlds) {
                TextButton rowBtn = new TextButton(wd.getName(), skin);
                rowBtn.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent e, Actor a) {
                        selected = wd;
                        updateDetails();
                    }
                });
                worldList.add(rowBtn).expandX().fillX()
                        .height(UiTheme.BTN_HEIGHT).padBottom(8).row();
            }
        }
        ScrollPane listScroll = new ScrollPane(worldList, skin);
        root.add(listScroll).width(260).expandY().fillY().padRight(20).top();

        // Right: details + action buttons
        Table details = new Table();
        details.top().left();
        detailLabel = new Label("Select a world", skin);
        detailLabel.setWrap(true);
        details.add(detailLabel).expandX().fillX().padBottom(20).row();

        loadBtn = new TextButton("LOAD WORLD", skin);
        loadBtn.setDisabled(true);
        loadBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                if (selected != null) {
                    app.setScreen(new LoadingScreen(app,
                            selected.getName(),
                            selected.getSeed(),
                            selected.getTerrain()));
                }
            }
        });
        details.add(loadBtn).expandX().fillX().height(UiTheme.BTN_HEIGHT).padBottom(8).row();

        deleteBtn = new TextButton("DELETE WORLD", skin);
        deleteBtn.setDisabled(true);
        deleteBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                if (selected != null) {
                    try {
                        WorldsHandler.deleteWorld(selected);
                    } catch (IOException ex) {
                        Gdx.app.error("LoadWorld", "Delete failed", ex);
                    }
                    selected = null;
                    app.setScreen(new LoadWorldScreen(app));
                }
            }
        });
        details.add(deleteBtn).expandX().fillX().height(UiTheme.BTN_HEIGHT).padBottom(20).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        details.add(backBtn).expandX().fillX().height(UiTheme.BTN_HEIGHT).row();

        root.add(details).expandX().fillX().top();

        stage.addActor(root);
    }

    private void refreshWorlds() {
        try {
            WorldsHandler.listWorlds(worlds);
        } catch (Exception e) {
            Gdx.app.error("LoadWorld", "Cannot list worlds", e);
        }
    }

    private void updateDetails() {
        if (selected == null) {
            detailLabel.setText("Select a world");
            loadBtn.setDisabled(true);
            deleteBtn.setDisabled(true);
        } else {
            detailLabel.setText(selected.getDetails());
            loadBtn.setDisabled(false);
            deleteBtn.setDisabled(false);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                UiTheme.BG_COLOR.r, UiTheme.BG_COLOR.g, UiTheme.BG_COLOR.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        if (skin  != null) { skin.dispose();  skin  = null; }
    }
}

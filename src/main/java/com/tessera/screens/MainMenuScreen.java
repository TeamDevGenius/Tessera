package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;

/**
 * Main menu screen using LibGDX Scene2D.
 * All buttons are wired to the appropriate screens/actions.
 */
public class MainMenuScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = ScreenUtils.createSkin();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Title
        Label title = new Label(TesseraApp.TITLE, skin, "title");
        root.add(title).padBottom(16).row();

        Label version = new Label("v" + TesseraApp.VERSION, skin);
        root.add(version).padBottom(32).row();

        // Buttons
        addButton(root, skin, "NEW WORLD", () -> app.setScreen(new NewWorldScreen(app)));
        addButton(root, skin, "LOAD WORLD", () -> app.setScreen(new LoadWorldScreen(app)));
        addButton(root, skin, "JOIN MULTIPLAYER", () -> app.setScreen(new MultiplayerScreen(app, false)));
        addButton(root, skin, "HOST MULTIPLAYER", () -> app.setScreen(new MultiplayerScreen(app, true)));
        addButton(root, skin, "CUSTOMIZE PLAYER", () -> app.setScreen(new CustomizePlayerScreen(app)));
        addButton(root, skin, "SETTINGS", () -> app.setScreen(new SettingsScreen(app)));
        addButton(root, skin, "QUIT", () -> Gdx.app.exit());
    }

    private void addButton(Table table, Skin skin, String text, Runnable action) {
        TextButton btn = new TextButton(text, skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });
        table.add(btn).width(300).height(48).padBottom(10).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1f);
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

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

/**
 * LibGDX main menu screen – replaces the Nuklear-based TopMenu.
 * Provides buttons for New World, Load World, Join Multiplayer,
 * Customize Player, Settings, and Quit.
 */
public class MainMenuScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private BitmapFont font;

    public MainMenuScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = app.getSkin();
        font = skin.getFont("default-font");

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Title
        Label title = new Label(TesseraApp.TITLE, skin, "title");
        root.add(title).padBottom(10).row();

        // Version
        Label version = new Label("v" + TesseraApp.VERSION, skin);
        root.add(version).padBottom(30).row();

        // Menu buttons
        addButton(root, "NEW WORLD", () -> app.showNewWorldDialog());
        addButton(root, "LOAD WORLD", () -> app.showLoadWorldDialog());
        addButton(root, "JOIN MULTIPLAYER", () -> app.showJoinMultiplayerDialog());
        addButton(root, "CUSTOMIZE PLAYER", () -> app.showCustomizePlayerDialog());
        addSpacer(root);
        addButton(root, "SETTINGS", () -> app.showSettingsDialog());
        addButton(root, "QUIT", () -> Gdx.app.exit());
    }

    private void addButton(Table table, String text, Runnable action) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });
        table.add(button).width(300).height(48).padBottom(8).row();
    }

    private void addSpacer(Table table) {
        table.add().height(16).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0.5f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // FPS overlay
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (batch != null) batch.dispose();
    }
}

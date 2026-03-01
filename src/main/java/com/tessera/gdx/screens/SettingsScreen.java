package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.gdx.ui.UiTheme;

/**
 * Settings screen.
 * Shows a placeholder for game settings with BACK and APPLY buttons.
 */
public class SettingsScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;
    /** Screen to return to (main menu by default). */
    private final Screen returnTo;

    public SettingsScreen(TesseraApp app) {
        this(app, null);
    }

    public SettingsScreen(TesseraApp app, Screen returnTo) {
        this.app = app;
        this.returnTo = returnTo;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(20);

        Label title = new Label("SETTINGS",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(20).colspan(2).row();

        root.add(new Label("Settings coming soon.", skin)).padBottom(30).colspan(2).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                goBack();
            }
        });
        TextButton applyBtn = new TextButton("APPLY", skin);
        applyBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                goBack();
            }
        });

        Table btnRow = new Table();
        btnRow.add(backBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT).padRight(10);
        btnRow.add(applyBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT);
        root.add(btnRow).colspan(2).row();

        stage.addActor(root);
    }

    private void goBack() {
        if (returnTo != null) {
            app.setScreen(returnTo);
        } else {
            app.setScreen(new MainMenuScreen(app));
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

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }

    @Override
    public void dispose() {
        if (stage != null) { stage.dispose(); stage = null; }
        if (skin  != null) { skin.dispose();  skin  = null; }
    }
}

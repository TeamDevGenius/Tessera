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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.gdx.ui.UiTheme;

/**
 * Customize-Player screen.
 * Allows the player to set their display name and cycle through available skins.
 */
public class CustomizePlayerScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    public CustomizePlayerScreen(TesseraApp app) {
        this.app = app;
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

        Label title = new Label("CUSTOMIZE PLAYER",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(20).colspan(2).row();

        root.add(new Label("My Name:", skin)).left().padRight(10);
        TextField nameField = new TextField("Player", skin);
        root.add(nameField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(10).colspan(2).row();

        root.add(new Label("Player Skin:", skin)).left().padRight(10);
        TextButton skinBtn = new TextButton("Default", skin);
        skinBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                // TODO: cycle through available skins when engine player is wired
            }
        });
        root.add(skinBtn).expandX().fillX().height(UiTheme.BTN_HEIGHT).row();

        root.add().height(20).colspan(2).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        root.add(backBtn).colspan(2).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).row();

        stage.addActor(root);
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

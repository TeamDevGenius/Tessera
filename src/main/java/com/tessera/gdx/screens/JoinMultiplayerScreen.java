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
 * Join-Multiplayer screen.
 * Allows the user to enter an IP address and port, then connect.
 */
public class JoinMultiplayerScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    public JoinMultiplayerScreen(TesseraApp app) {
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

        Label title = new Label("JOIN MULTIPLAYER",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(20).colspan(2).row();

        root.add(new Label("IP Address:", skin)).left().padRight(10);
        TextField ipField = new TextField("", skin);
        root.add(ipField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(10).colspan(2).row();

        root.add(new Label("Port:", skin)).left().padRight(10);
        TextField portField = new TextField("8080", skin);
        portField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        root.add(portField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(20).colspan(2).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        // CONTINUE is a placeholder — multiplayer connection not yet wired
        TextButton continueBtn = new TextButton("CONTINUE", skin);
        continueBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                // TODO: initiate network join using ipField/portField values
                app.setScreen(new MainMenuScreen(app));
            }
        });

        Table btnRow = new Table();
        btnRow.add(backBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT).padRight(10);
        btnRow.add(continueBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT);
        root.add(btnRow).colspan(2).row();

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

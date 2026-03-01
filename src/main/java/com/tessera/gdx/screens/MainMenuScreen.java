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

        skin = UiTheme.buildSkin();

        // Dark panel background fills the whole stage; buttons sit inside it
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        // Panel container — dark background with blue-ish border
        Table panel = new Table();
        panel.setBackground(UiTheme.solid(UiTheme.BG_COLOR));

        Label title = new Label(TesseraApp.TITLE,
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        panel.add(title).padBottom(4).padTop(20).row();

        Label version = new Label("v" + TesseraApp.VERSION, skin.get(Label.LabelStyle.class));
        panel.add(version).padBottom(30).row();

        addBtn(panel, "NEW WORLD").addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new NewWorldScreen(app));
            }
        });
        addBtn(panel, "LOAD WORLD").addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new LoadWorldScreen(app));
            }
        });
        addBtn(panel, "JOIN MULTIPLAYER").addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new JoinMultiplayerScreen(app));
            }
        });
        addBtn(panel, "CUSTOMIZE PLAYER").addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new CustomizePlayerScreen(app));
            }
        });

        // Spacer between main actions and bottom actions
        panel.add().height(20).row();

        addBtn(panel, "SETTINGS").addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new SettingsScreen(app));
            }
        });
        addBtn(panel, "QUIT").addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                Gdx.app.exit();
            }
        });

        panel.pad(20, 30, 30, 30);
        root.add(panel).width(380);
        stage.addActor(root);
    }

    private TextButton addBtn(Table parent, String text) {
        TextButton btn = new TextButton(text, skin);
        parent.add(btn).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT)
                .padBottom(12).row();
        return btn;
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
        if (skin != null)  { skin.dispose();  skin  = null; }
    }
}

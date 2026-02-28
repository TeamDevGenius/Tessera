package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;

public class MainMenuScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(TesseraApp app) {
        this.app = app;
    }

    private Texture makePixel(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Button style
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.WHITE;
        Pixmap btnPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        btnPm.setColor(0.2f, 0.4f, 0.8f, 1f);
        btnPm.fill();
        Texture btnTex = new Texture(btnPm);
        btnPm.dispose();
        btnStyle.up = new TextureRegionDrawable(btnTex);
        Pixmap btnDownPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        btnDownPm.setColor(0.1f, 0.25f, 0.6f, 1f);
        btnDownPm.fill();
        Texture btnDownTex = new Texture(btnDownPm);
        btnDownPm.dispose();
        btnStyle.down = new TextureRegionDrawable(btnDownTex);
        btnStyle.over = btnStyle.down;
        skin.add("default", btnStyle);

        Label.LabelStyle lblStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", lblStyle);

        // Window style required by Dialog (which extends Window)
        Pixmap winBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        winBgPm.setColor(0.1f, 0.1f, 0.25f, 0.97f);
        winBgPm.fill();
        Texture winBgTex = new Texture(winBgPm);
        winBgPm.dispose();
        Window.WindowStyle windowStyle = new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(winBgTex));
        skin.add("default", windowStyle);

        Table root = new Table();
        root.setFillParent(true);

        Label title = new Label(TesseraApp.TITLE, new Label.LabelStyle(font, Color.CYAN));
        root.add(title).padBottom(8).row();

        Label version = new Label("v" + TesseraApp.VERSION, lblStyle);
        root.add(version).padBottom(40).row();

        TextButton newWorldBtn = new TextButton("New World", skin);
        newWorldBtn.getLabel().setFontScale(1.5f);
        root.add(newWorldBtn).width(300).height(70).padBottom(15).row();

        TextButton loadWorldBtn = new TextButton("Load World", skin);
        loadWorldBtn.getLabel().setFontScale(1.5f);
        root.add(loadWorldBtn).width(300).height(70).padBottom(15).row();

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.getLabel().setFontScale(1.5f);
        root.add(settingsBtn).width(300).height(70).padBottom(15).row();

        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.getLabel().setFontScale(1.5f);
        root.add(exitBtn).width(300).height(70).row();

        stage.addActor(root);

        newWorldBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showNewWorldDialog();
            }
        });

        loadWorldBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showLoadWorldDialog();
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSettingsDialog();
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    private void showSettingsDialog() {
        Dialog dialog = new Dialog("Settings", skin) {
            @Override
            protected void result(Object object) { }
        };
        dialog.text("Settings coming soon.");
        dialog.button("OK", true);
        dialog.show(stage);
    }

    private void showNewWorldDialog() {
        Dialog dialog = new Dialog("New World", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    app.setScreen(new LoadingScreen(app, "World", 0, "default"));
                }
            }
        };
        dialog.text("Start a new world?");
        dialog.button("Create", true);
        dialog.button("Cancel", false);
        dialog.show(stage);
    }

    private void showLoadWorldDialog() {
        Dialog dialog = new Dialog("Load World", skin) {
            @Override
            protected void result(Object object) {}
        };
        dialog.text("No saved worlds found.");
        dialog.button("OK", true);
        dialog.show(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.15f, 1f);
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
        if (skin != null) { skin.dispose(); skin = null; }
    }
}

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

/**
 * Placeholder customize-player screen.
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
        skin = ScreenUtils.createSkin();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("CUSTOMIZE PLAYER", skin, "title")).padBottom(20).row();
        root.add(new Label("Player customisation coming soon.", skin)).padBottom(20).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        root.add(backBtn).width(300).height(40).row();
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
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

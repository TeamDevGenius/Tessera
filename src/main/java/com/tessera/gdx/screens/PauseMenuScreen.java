package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.tessera.gdx.GdxGameInitializer;
import com.tessera.gdx.ui.UiTheme;

/**
 * In-game pause / resume overlay.
 * Provides RESUME, SETTINGS, and SAVE &amp; QUIT options.
 */
public class PauseMenuScreen implements Screen {

    private final TesseraApp app;
    /** The game screen to return to when RESUME is pressed. May be null (creates a new GameScreen). */
    private final Screen gameScreen;
    private Stage stage;
    private Skin skin;

    public PauseMenuScreen(TesseraApp app) {
        this(app, null);
    }

    /**
     * @param app        the application
     * @param gameScreen the screen to return to when the player resumes; if null a new GameScreen is created
     */
    public PauseMenuScreen(TesseraApp app, Screen gameScreen) {
        this.app        = app;
        this.gameScreen = gameScreen;
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

        // Semi-transparent dark panel
        Table panel = new Table();
        panel.setBackground(UiTheme.solid(new Color(0.1f, 0.1f, 0.1f, 0.92f)));
        panel.pad(30);

        Label title = new Label("PAUSED",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        panel.add(title).padBottom(30).row();

        TextButton resumeBtn = new TextButton("RESUME", skin);
        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                Screen returnTo = gameScreen != null ? gameScreen : new GameScreen(app);
                app.setScreen(returnTo);
            }
        });
        panel.add(resumeBtn).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).padBottom(12).row();

        TextButton settingsBtn = new TextButton("SETTINGS", skin);
        settingsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new SettingsScreen(app, new PauseMenuScreen(app, gameScreen)));
            }
        });
        panel.add(settingsBtn).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).padBottom(12).row();

        TextButton quitBtn = new TextButton("SAVE & QUIT", skin);
        quitBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                saveAndQuit();
            }
        });
        panel.add(quitBtn).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).row();

        root.add(panel);
        stage.addActor(root);
    }

    private void saveAndQuit() {
        // Persist the world before returning to the main menu
        if (GdxGameInitializer.gdxWorld != null && GdxGameInitializer.gdxWorld.data != null) {
            try {
                GdxGameInitializer.gdxWorld.data.save();
            } catch (Exception ex) {
                Gdx.app.error("PauseMenu", "World save failed", ex);
            }
        }
        app.setScreen(new MainMenuScreen(app));
    }

    @Override
    public void render(float delta) {
        // Draw a dark overlay (semi-transparent)
        Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
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

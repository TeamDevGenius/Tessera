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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.common.settings.Settings;
import com.tessera.engine.server.world.World;
import com.tessera.gdx.GdxGameInitializer;
import com.tessera.gdx.GdxPlayer;
import com.tessera.gdx.ui.UiTheme;

/**
 * In-game pause / resume overlay – mirrors GameMenu.java from the desktop.
 * <p>
 * Provides:
 * <ul>
 *   <li>RESUME – returns to the game screen</li>
 *   <li>SETTINGS – opens {@link SettingsScreen} with this pause menu as return target</li>
 *   <li>View Distance TextField – live adjustment of the active world's view distance</li>
 *   <li>SAVE &amp; QUIT – saves chunks + player data, then navigates to the main menu</li>
 * </ul>
 */
public class PauseMenuScreen implements Screen {

    private final TesseraApp app;
    /** The game screen to return to when RESUME is pressed. */
    private final Screen gameScreen;
    private Stage stage;
    private Skin skin;

    public PauseMenuScreen(TesseraApp app) {
        this(app, null);
    }

    /**
     * @param app        the application
     * @param gameScreen the screen to return to on RESUME; a new {@link GameScreen} is
     *                   created if {@code null}
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

        // Semi-transparent dark panel centred on screen
        Table panel = new Table();
        panel.setBackground(UiTheme.solid(new Color(0.08f, 0.08f, 0.15f, 0.93f)));
        panel.pad(30);

        Label title = new Label("PAUSED",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        panel.add(title).padBottom(24).row();

        // ── RESUME ────────────────────────────────────────────────────────────
        TextButton resumeBtn = new TextButton("RESUME", skin);
        resumeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(gameScreen != null ? gameScreen : new GameScreen(app));
            }
        });
        panel.add(resumeBtn).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).padBottom(12).row();

        // ── SETTINGS ──────────────────────────────────────────────────────────
        TextButton settingsBtn = new TextButton("SETTINGS", skin);
        settingsBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new SettingsScreen(app, new PauseMenuScreen(app, gameScreen)));
            }
        });
        panel.add(settingsBtn).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).padBottom(20).row();

        // ── View Distance inline adjustment ───────────────────────────────────
        Table vdRow = new Table();
        vdRow.add(new Label("View Distance:",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR)))
                .padRight(10);

        int currentViewDist = GdxGameInitializer.gdxWorld != null
                ? GdxGameInitializer.gdxWorld.getViewDistance()
                : World.VIEW_DIST_MIN;
        TextField viewDistField = new TextField(String.valueOf(currentViewDist), skin);
        viewDistField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        viewDistField.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                try {
                    int val = Integer.parseInt(viewDistField.getText());
                    World world = GdxGameInitializer.gdxWorld;
                    if (world != null) {
                        val = Math.max(World.VIEW_DIST_MIN, Math.min(World.VIEW_DIST_MAX, val));
                        Settings settings = Settings.load();
                        try {
                            world.setViewDistance(settings, val);
                        } catch (Exception ignored) {
                            // chunkShader may not be initialised in GDX context
                            settings.viewDistance.value = val;
                            settings.save();
                        }
                    }
                } catch (NumberFormatException ignored) { /* user still typing */ }
            }
        });
        vdRow.add(viewDistField).width(100).height(UiTheme.MIN_TOUCH);
        panel.add(vdRow).padBottom(20).row();

        // ── SAVE & QUIT ───────────────────────────────────────────────────────
        TextButton saveQuitBtn = new TextButton("SAVE & QUIT", skin);
        saveQuitBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                saveWorld();
                app.setScreen(new MainMenuScreen(app));
            }
        });
        panel.add(saveQuitBtn).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).row();

        // ── Root ──────────────────────────────────────────────────────────────
        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(panel);
        stage.addActor(root);
    }

    /** Persists world chunks and player data to disk. */
    private void saveWorld() {
        World world = GdxGameInitializer.gdxWorld;
        GdxPlayer player = GdxGameInitializer.gdxPlayer;
        if (world == null || player == null) return;
        try {
            world.chunks.forEach((coords, chunk) -> chunk.save(world.data));
            world.data.setSpawnPoint(player.worldPosition);
            world.data.save();
            player.saveToWorld(world.data);
        } catch (Exception e) {
            Gdx.app.error("PauseMenu", "World save failed", e);
        }
    }

    @Override
    public void render(float delta) {
        // Render a semi-transparent black overlay over whatever was drawn last
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
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

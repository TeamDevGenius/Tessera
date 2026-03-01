package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.common.settings.Settings;
import com.tessera.engine.server.world.World;
import com.tessera.engine.server.world.chunk.Chunk;
import com.tessera.gdx.GdxGameInitializer;
import com.tessera.gdx.input.TouchControls;
import com.tessera.gdx.ui.UiTheme;

/**
 * Settings screen with functional sliders for view distance, simulation distance
 * and camera sensitivity.
 */
public class SettingsScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;
    /** Screen to return to (main menu by default). */
    private final Screen returnTo;
    /** Optional reference to active touch controls for sensitivity adjustment. */
    private final TouchControls touchControls;

    // Pending values — applied on APPLY
    private int   pendingViewDist;
    private int   pendingSimDist;
    private float pendingSensitivity;

    public SettingsScreen(TesseraApp app) {
        this(app, null, null);
    }

    public SettingsScreen(TesseraApp app, Screen returnTo) {
        this(app, returnTo, null);
    }

    public SettingsScreen(TesseraApp app, Screen returnTo, TouchControls touchControls) {
        this.app           = app;
        this.returnTo      = returnTo;
        this.touchControls = touchControls;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        Settings settings = Settings.load();
        pendingViewDist    = GdxGameInitializer.gdxWorld != null
                           ? GdxGameInitializer.gdxWorld.getViewDistance()
                           : settings.viewDistance.value;
        pendingSimDist     = settings.internal_simulationDistance.value;
        pendingSensitivity = touchControls != null ? TouchControls.SENSITIVITY : 0.2f;

        Label.LabelStyle labelStyle =
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR);
        Label.LabelStyle titleStyle =
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(20);

        root.add(new Label("SETTINGS", titleStyle)).padBottom(24).colspan(2).row();

        // --- View Distance ---
        final Label viewDistLabel = new Label("View Distance: " + pendingViewDist, labelStyle);
        Slider viewDistSlider = new Slider(World.VIEW_DIST_MIN, World.VIEW_DIST_MAX,
                Chunk.WIDTH, false, skin, "default-horizontal");
        viewDistSlider.setValue(pendingViewDist);
        viewDistSlider.addListener(new EventListener() {
            @Override public boolean handle(Event event) {
                pendingViewDist = (int) viewDistSlider.getValue();
                viewDistLabel.setText("View Distance: " + pendingViewDist);
                return false;
            }
        });
        root.add(viewDistLabel).left().padBottom(4).colspan(2).row();
        root.add(viewDistSlider).width(320).padBottom(16).colspan(2).row();

        // --- Simulation Distance ---
        final Label simDistLabel = new Label("Simulation Distance: " + pendingSimDist, labelStyle);
        Slider simDistSlider = new Slider(World.VIEW_DIST_MIN / 2, World.VIEW_DIST_MAX,
                Chunk.WIDTH, false, skin, "default-horizontal"); // min is half of view dist min
        simDistSlider.setValue(pendingSimDist);
        simDistSlider.addListener(new EventListener() {
            @Override public boolean handle(Event event) {
                pendingSimDist = (int) simDistSlider.getValue();
                simDistLabel.setText("Simulation Distance: " + pendingSimDist);
                return false;
            }
        });
        root.add(simDistLabel).left().padBottom(4).colspan(2).row();
        root.add(simDistSlider).width(320).padBottom(16).colspan(2).row();

        // --- Sensitivity ---
        final Label sensLabel = new Label(
                String.format("Look Sensitivity: %.2f", pendingSensitivity), labelStyle);
        Slider sensSlider = new Slider(0.05f, 1.0f, 0.05f, false, skin, "default-horizontal");
        sensSlider.setValue(pendingSensitivity);
        sensSlider.addListener(new EventListener() {
            @Override public boolean handle(Event event) {
                pendingSensitivity = sensSlider.getValue();
                sensLabel.setText(String.format("Look Sensitivity: %.2f", pendingSensitivity));
                return false;
            }
        });
        root.add(sensLabel).left().padBottom(4).colspan(2).row();
        root.add(sensSlider).width(320).padBottom(24).colspan(2).row();

        // --- Buttons ---
        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) { goBack(); }
        });
        TextButton applyBtn = new TextButton("APPLY", skin);
        applyBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) { applySettings(); goBack(); }
        });

        Table btnRow = new Table();
        btnRow.add(backBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT).padRight(10);
        btnRow.add(applyBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT);
        root.add(btnRow).colspan(2).row();

        stage.addActor(root);
    }

    private void applySettings() {
        Settings settings = Settings.load();
        if (GdxGameInitializer.gdxWorld != null) {
            GdxGameInitializer.gdxWorld.setViewDistance(settings, pendingViewDist);
        }
        settings.internal_simulationDistance.value = pendingSimDist;
        settings.save();
        if (touchControls != null) {
            TouchControls.SENSITIVITY = pendingSensitivity;
        }
        Gdx.app.log("SettingsScreen", "Settings applied: viewDist=" + pendingViewDist
                + " simDist=" + pendingSimDist + " sensitivity=" + pendingSensitivity);
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

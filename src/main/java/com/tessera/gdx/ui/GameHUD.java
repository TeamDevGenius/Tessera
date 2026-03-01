package com.tessera.gdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tessera.TesseraApp;
import com.tessera.gdx.input.TouchControls;
import com.tessera.gdx.screens.PauseMenuScreen;
import com.tessera.gdx.screens.SettingsScreen;

public class GameHUD {

    private final Stage stage;
    private final TesseraApp app;

    private Label coordsLabel;
    private Label fpsLabel;
    private Label healthLabel;
    private Label hungerLabel;
    private Label airLabel;
    /** Reflects current sprint state for button label update. */
    private TextButton sprintBtn;

    private Skin skin;

    public GameHUD(Stage stage, BitmapFont font, TesseraApp app) {
        this(stage, font, app, null, null);
    }

    public GameHUD(Stage stage, BitmapFont font, TesseraApp app, Runnable jumpCallback) {
        this(stage, font, app, jumpCallback, null);
    }

    /**
     * Full constructor.
     *
     * @param jumpCallback  called when the JUMP button is pressed; may be null
     * @param touchControls the active {@link TouchControls}; used to wire sprint and fly buttons
     */
    public GameHUD(Stage stage, BitmapFont font, TesseraApp app,
                   Runnable jumpCallback, TouchControls touchControls) {
        this.stage = stage;
        this.app   = app;
        skin = UiTheme.buildSkin();

        Label.LabelStyle style    = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle styleGrn = new Label.LabelStyle(font, Color.GREEN);
        Label.LabelStyle styleYel = new Label.LabelStyle(font, Color.YELLOW);
        Label.LabelStyle styleCyn = new Label.LabelStyle(font, Color.CYAN);

        coordsLabel = new Label("0, 0, 0", style);
        fpsLabel    = new Label("FPS: 0",  style);
        healthLabel = new Label("Health: 20", styleGrn);
        hungerLabel = new Label("Hunger: 20", styleYel);
        airLabel    = new Label("Air: 20",    styleCyn);

        // Top-left overlay: FPS, coords, health/hunger/air
        Table topLeft = new Table();
        topLeft.top().left();
        topLeft.setFillParent(true);
        topLeft.pad(8);
        topLeft.add(fpsLabel).left().row();
        topLeft.add(coordsLabel).left().row();
        topLeft.add(healthLabel).left().row();
        topLeft.add(hungerLabel).left().row();
        topLeft.add(airLabel).left().row();
        stage.addActor(topLeft);

        // Pause button — top-right
        Table topRight = new Table();
        topRight.top().right();
        topRight.setFillParent(true);
        topRight.pad(8);
        TextButton pauseBtn = new TextButton("||", skin);
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                if (app != null) app.setScreen(new PauseMenuScreen(app));
            }
        });
        topRight.add(pauseBtn).size(UiTheme.btnHeight(), UiTheme.btnHeight()).row();
        stage.addActor(topRight);

        // Crosshair — centred
        Table crosshairTable = new Table();
        crosshairTable.setFillParent(true);
        Label crosshair = new Label("+", new Label.LabelStyle(font, Color.WHITE));
        crosshairTable.add(crosshair);
        stage.addActor(crosshairTable);

        // Bottom-right area: jump + sprint + fly buttons
        if (jumpCallback != null || touchControls != null) {
            Table bottomRight = new Table();
            bottomRight.bottom().right();
            bottomRight.setFillParent(true);
            bottomRight.pad(20);

            // Fly-up button (only useful in creative/freeplay but shown regardless – invisible when not needed)
            if (touchControls != null) {
                TextButton flyUpBtn = new TextButton("FLY UP", skin);
                flyUpBtn.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent e, Actor a) {
                        // toggle fly mode on first use; then hold handled via touchDown/Up in HUD
                        touchControls.setFlying(true);
                        touchControls.setFlyUp(flyUpBtn.isPressed());
                    }
                });
                bottomRight.add(flyUpBtn).size(UiTheme.btnHeight(), UiTheme.btnHeight()).padBottom(4).row();

                TextButton flyDownBtn = new TextButton("FLY DN", skin);
                flyDownBtn.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent e, Actor a) {
                        touchControls.setFlying(true);
                        touchControls.setFlyDown(flyDownBtn.isPressed());
                    }
                });
                bottomRight.add(flyDownBtn).size(UiTheme.btnHeight(), UiTheme.btnHeight()).padBottom(4).row();
            }

            if (jumpCallback != null) {
                TextButton jumpBtn = new TextButton("JUMP", skin);
                jumpBtn.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent e, Actor a) {
                        jumpCallback.run();
                    }
                });
                bottomRight.add(jumpBtn).size(130, 90).padBottom(4).row();
            }

            if (touchControls != null) {
                sprintBtn = new TextButton("SPRINT", skin);
                sprintBtn.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent e, Actor a) {
                        touchControls.toggleSprint();
                        sprintBtn.setText(touchControls.isSprinting() ? "SPRINT ✓" : "SPRINT");
                    }
                });
                bottomRight.add(sprintBtn).size(130, 90).row();
            }

            stage.addActor(bottomRight);
        }
    }

    /** Legacy constructor retained for compatibility. */
    public GameHUD(Stage stage, BitmapFont font) {
        this(stage, font, null);
    }

    public void update(float delta, PerspectiveCamera camera) {
        boolean showFps = true;
        try {
            showFps = SettingsScreen.prefs().getBoolean(SettingsScreen.KEY_SHOW_FPS,
                                                         SettingsScreen.DEFAULT_SHOW_FPS);
        } catch (Throwable ignored) {}

        if (camera != null) {
            coordsLabel.setText(String.format("%.1f, %.1f, %.1f",
                camera.position.x, camera.position.y, camera.position.z));
        }
        fpsLabel.setVisible(showFps);
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        // Health/hunger/air labels are static placeholders until the engine player is wired
    }

    public void dispose() {
        if (skin != null) { skin.dispose(); skin = null; }
    }
}


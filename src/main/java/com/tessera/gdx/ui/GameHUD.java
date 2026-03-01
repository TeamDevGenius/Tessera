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
import com.tessera.gdx.screens.PauseMenuScreen;

public class GameHUD {

    private final Stage stage;
    private final TesseraApp app;

    private Label coordsLabel;
    private Label fpsLabel;
    private Label healthLabel;
    private Label hungerLabel;
    private Label airLabel;

    private Skin skin;

    public GameHUD(Stage stage, BitmapFont font, TesseraApp app) {
        this(stage, font, app, null);
    }

    public GameHUD(Stage stage, BitmapFont font, TesseraApp app, Runnable jumpCallback) {
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
        topRight.add(pauseBtn).size(72, 72).row();
        stage.addActor(topRight);

        // Crosshair — centred
        Table crosshairTable = new Table();
        crosshairTable.setFillParent(true);
        Label crosshair = new Label("+", new Label.LabelStyle(font, Color.WHITE));
        crosshairTable.add(crosshair);
        stage.addActor(crosshairTable);

        // Jump button — bottom-right (large target for thumb)
        if (jumpCallback != null) {
            Table bottomRight = new Table();
            bottomRight.bottom().right();
            bottomRight.setFillParent(true);
            bottomRight.pad(20);
            TextButton jumpBtn = new TextButton("JUMP", skin);
            jumpBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    jumpCallback.run();
                }
            });
            bottomRight.add(jumpBtn).size(130, 90).row();
            stage.addActor(bottomRight);
        }
    }

    /** Legacy constructor retained for compatibility. */
    public GameHUD(Stage stage, BitmapFont font) {
        this(stage, font, null);
    }

    public void update(float delta, PerspectiveCamera camera) {
        if (camera != null) {
            coordsLabel.setText(String.format("%.1f, %.1f, %.1f",
                camera.position.x, camera.position.y, camera.position.z));
        }
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        // Health/hunger/air labels are static placeholders until the engine player is wired
    }

    public void dispose() {
        if (skin != null) { skin.dispose(); skin = null; }
    }
}


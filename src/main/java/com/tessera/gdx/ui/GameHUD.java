package com.tessera.gdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tessera.gdx.input.TouchControls;

public class GameHUD {

    private static final int HOTBAR_SLOTS = 9;
    private static final float HOTBAR_SLOT_SIZE = 48f;
    private static final float JOYSTICK_RADIUS = 60f;
    private static final int HEALTH_SEGMENTS = 10;

    private final Stage stage;
    private Label coordsLabel;
    private Label fpsLabel;
    private ShapeRenderer shapeRenderer;
    private TouchControls touchControls;
    private Skin btnSkin;

    public GameHUD(Stage stage, BitmapFont font, TouchControls controls) {
        this.stage = stage;
        this.touchControls = controls;
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        coordsLabel = new Label("0, 0, 0", style);
        fpsLabel = new Label("FPS: 0", style);

        Table topLeft = new Table();
        topLeft.top().left();
        topLeft.setFillParent(true);
        topLeft.pad(5);
        topLeft.add(fpsLabel).left().row();
        topLeft.add(coordsLabel).left().row();

        stage.addActor(topLeft);

        // Crosshair - use a centered fill-parent table so it repositions on resize
        Table crosshairTable = new Table();
        crosshairTable.setFillParent(true);
        Label crosshair = new Label("+", new Label.LabelStyle(font, Color.WHITE));
        crosshairTable.add(crosshair);
        stage.addActor(crosshairTable);

        // Jump button (bottom-right)
        btnSkin = new Skin();
        TextButton.TextButtonStyle jumpStyle = new TextButton.TextButtonStyle();
        jumpStyle.font = font;
        jumpStyle.fontColor = Color.WHITE;
        btnSkin.add("default", jumpStyle);

        TextButton jumpBtn = new TextButton("JUMP", btnSkin);
        jumpBtn.setSize(100, 60);
        jumpBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (controls != null) controls.triggerJump();
            }
        });

        Table bottomRight = new Table();
        bottomRight.bottom().right();
        bottomRight.setFillParent(true);
        bottomRight.pad(20);
        bottomRight.add(jumpBtn).width(100).height(60).row();
        stage.addActor(bottomRight);

        shapeRenderer = new ShapeRenderer();
    }

    public void update(float delta, PerspectiveCamera camera) {
        if (camera != null) {
            coordsLabel.setText(String.format("%.1f, %.1f, %.1f",
                camera.position.x, camera.position.y, camera.position.z));
        }
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    /** Draw shape-based HUD elements (joystick, hotbar, health). Must be called outside SpriteBatch. */
    public void drawShapes() {
        int sw = Gdx.graphics.getWidth();
        int sh = Gdx.graphics.getHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // --- Joystick visual (left side) ---
        float jBaseX, jBaseY;
        if (touchControls != null && touchControls.isJoystickActive()) {
            jBaseX = touchControls.getJoystickStartX();
            jBaseY = sh - touchControls.getJoystickStartY(); // touch origin is top-left; ShapeRenderer uses bottom-left
        } else {
            jBaseX = sw * 0.18f;
            jBaseY = sh * 0.22f;
        }

        // Outer ring
        shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0.45f);
        shapeRenderer.circle(jBaseX, jBaseY, JOYSTICK_RADIUS);

        // Thumb (inner circle offset by joystick displacement)
        float thumbX = jBaseX;
        float thumbY = jBaseY;
        if (touchControls != null && touchControls.isJoystickActive()) {
            float dx = touchControls.getJoystickDX();
            float dy = -touchControls.getJoystickDY(); // screen Y is down, world Y is up
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            float maxDisp = JOYSTICK_RADIUS * 0.7f;
            if (dist > maxDisp) {
                dx = dx / dist * maxDisp;
                dy = dy / dist * maxDisp;
            }
            thumbX += dx;
            thumbY += dy;
        }
        shapeRenderer.setColor(0.7f, 0.7f, 0.7f, 0.7f);
        shapeRenderer.circle(thumbX, thumbY, JOYSTICK_RADIUS * 0.45f);

        // --- Hotbar (bottom center) ---
        float hotbarTotalW = HOTBAR_SLOTS * HOTBAR_SLOT_SIZE + (HOTBAR_SLOTS - 1) * 4;
        float hotbarX = (sw - hotbarTotalW) / 2f;
        float hotbarY = 8f;
        for (int i = 0; i < HOTBAR_SLOTS; i++) {
            float sx = hotbarX + i * (HOTBAR_SLOT_SIZE + 4);
            shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.7f);
            shapeRenderer.rect(sx, hotbarY, HOTBAR_SLOT_SIZE, HOTBAR_SLOT_SIZE);
            shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 0.9f);
            shapeRenderer.rect(sx, hotbarY, HOTBAR_SLOT_SIZE, 2);
            shapeRenderer.rect(sx, hotbarY + HOTBAR_SLOT_SIZE - 2, HOTBAR_SLOT_SIZE, 2);
            shapeRenderer.rect(sx, hotbarY, 2, HOTBAR_SLOT_SIZE);
            shapeRenderer.rect(sx + HOTBAR_SLOT_SIZE - 2, hotbarY, 2, HOTBAR_SLOT_SIZE);
        }

        // --- Health bar (top center) ---
        float heartSize = 14f;
        float heartSpacing = 18f;
        float healthBarX = sw / 2f - (HEALTH_SEGMENTS * heartSpacing) / 2f;
        float healthBarY = sh - 28f;
        for (int i = 0; i < HEALTH_SEGMENTS; i++) {
            shapeRenderer.setColor(0.8f, 0.1f, 0.1f, 0.85f);
            shapeRenderer.rect(healthBarX + i * heartSpacing, healthBarY, heartSize, heartSize);
        }

        shapeRenderer.end();
    }

    public void dispose() {
        if (btnSkin != null) { btnSkin.dispose(); btnSkin = null; }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }
}

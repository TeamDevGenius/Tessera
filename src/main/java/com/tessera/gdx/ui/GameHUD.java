package com.tessera.gdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tessera.gdx.input.TouchControls;

public class GameHUD {

    private static final float JOYSTICK_BASE_RADIUS = 60f;
    private static final float JOYSTICK_THUMB_RADIUS = 30f;
    private static final float JOYSTICK_MAX_DIST = JOYSTICK_BASE_RADIUS;

    private final Stage stage;
    private Label coordsLabel;
    private Label fpsLabel;
    private TouchControls touchControls;
    private ShapeRenderer shapeRenderer;

    private final BitmapFont font;

    public GameHUD(Stage stage, BitmapFont font) {
        this.stage = stage;
        this.font = font;
        shapeRenderer = new ShapeRenderer();

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
    }

    public void setTouchControls(TouchControls tc) {
        this.touchControls = tc;
        addJumpButton(stage);
    }

    private void addJumpButton(Stage stage) {
        TextButton.TextButtonStyle jumpStyle = new TextButton.TextButtonStyle();
        jumpStyle.font = font;
        jumpStyle.fontColor = Color.WHITE;

        TextButton jumpBtn = new TextButton("Jump", jumpStyle);
        jumpBtn.getLabel().setFontScale(1.5f);

        Table bottomRight = new Table();
        bottomRight.bottom().right();
        bottomRight.setFillParent(true);
        bottomRight.pad(20);
        bottomRight.add(jumpBtn).width(120).height(60);

        stage.addActor(bottomRight);

        jumpBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (touchControls != null) {
                    touchControls.jump();
                }
            }
        });
    }

    public void update(float delta, PerspectiveCamera camera) {
        if (camera != null) {
            coordsLabel.setText(String.format("%.1f, %.1f, %.1f",
                camera.position.x, camera.position.y, camera.position.z));
        }
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    /** Draw the virtual joystick shapes. Must be called after hudStage.draw(). */
    public void drawShapes() {
        if (touchControls == null) return;

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        // Joystick base center: left 15% of screen, bottom 20%
        float baseX = w * 0.15f;
        float baseY = h * 0.20f;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Base circle (semi-transparent grey)
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.4f);
        shapeRenderer.circle(baseX, baseY, JOYSTICK_BASE_RADIUS);

        // Thumb indicator
        float thumbX = baseX;
        float thumbY = baseY;
        if (touchControls.isJoystickActive()) {
            float dx = touchControls.getJoystickDX();
            float dy = -touchControls.getJoystickDY(); // screen Y is inverted vs OpenGL Y
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist > JOYSTICK_MAX_DIST) {
                dx = dx / dist * JOYSTICK_MAX_DIST;
                dy = dy / dist * JOYSTICK_MAX_DIST;
            }
            thumbX = baseX + dx;
            thumbY = baseY + dy;
        }
        shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.7f);
        shapeRenderer.circle(thumbX, thumbY, JOYSTICK_THUMB_RADIUS);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }
}

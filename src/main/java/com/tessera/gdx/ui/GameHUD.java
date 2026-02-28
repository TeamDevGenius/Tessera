package com.tessera.gdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameHUD {

    private final Stage stage;
    private Label coordsLabel;
    private Label fpsLabel;

    public GameHUD(Stage stage, BitmapFont font) {
        this.stage = stage;
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

        // Crosshair - center it approximately on the screen
        Label crosshair = new Label("+", new Label.LabelStyle(font, Color.WHITE));
        float crosshairOffsetX = crosshair.getMinWidth() / 2f;
        float crosshairOffsetY = crosshair.getMinHeight() / 2f;
        crosshair.setPosition(
            Gdx.graphics.getWidth() / 2f - crosshairOffsetX,
            Gdx.graphics.getHeight() / 2f - crosshairOffsetY
        );
        stage.addActor(crosshair);
    }

    public void update(float delta, PerspectiveCamera camera) {
        if (camera != null) {
            coordsLabel.setText(String.format("%.1f, %.1f, %.1f",
                camera.position.x, camera.position.y, camera.position.z));
        }
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }
}

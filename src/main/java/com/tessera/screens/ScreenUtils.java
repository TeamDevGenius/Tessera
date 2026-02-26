package com.tessera.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Utility class to build a simple programmatic Skin used by all screens,
 * so no external atlas file is required.
 */
public class ScreenUtils {

    /** Call once per screen – the Skin must be disposed with the screen. */
    public static Skin createSkin() {
        Skin skin = new Skin();

        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        // Enlarged font for title label
        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(2.5f);
        skin.add("titleFont", titleFont);

        // Helper: solid-colour 1x1 texture
        skin.add("white", makeTexture(Color.WHITE));
        skin.add("dark",  makeTexture(new Color(0.15f, 0.15f, 0.15f, 1f)));
        skin.add("btn",   makeTexture(new Color(0.25f, 0.25f, 0.35f, 1f)));
        skin.add("btnOver",makeTexture(new Color(0.35f, 0.35f, 0.50f, 1f)));
        skin.add("btnDown",makeTexture(new Color(0.15f, 0.15f, 0.25f, 1f)));
        skin.add("field",  makeTexture(new Color(0.2f, 0.2f, 0.2f, 1f)));

        // Label styles
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.CYAN;
        skin.add("title", titleStyle);

        // TextButton style
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.WHITE;
        btnStyle.up   = skin.newDrawable("btn");
        btnStyle.over = skin.newDrawable("btnOver");
        btnStyle.down = skin.newDrawable("btnDown");
        skin.add("default", btnStyle);

        // TextField style
        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = font;
        tfStyle.fontColor = Color.WHITE;
        tfStyle.background = skin.newDrawable("field");
        tfStyle.cursor = skin.newDrawable("white");
        tfStyle.selection = skin.newDrawable("btnOver");
        skin.add("default", tfStyle);

        return skin;
    }

    private static Texture makeTexture(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }
}

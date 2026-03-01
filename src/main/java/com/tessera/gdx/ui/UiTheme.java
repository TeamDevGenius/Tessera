package com.tessera.gdx.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Shared UI styling constants and helpers for the Tessera Android screens.
 * Matches the desktop dark theme from Theme.java.
 */
public class UiTheme {

    // Colours matching the desktop theme
    public static final Color BG_COLOR        = new Color(0.102f, 0.102f, 0.102f, 1f); // #1a1a1a
    public static final Color PANEL_BORDER    = new Color(0.267f, 0.267f, 0.667f, 1f); // #4444aa
    public static final Color BTN_UP          = new Color(0.15f,  0.15f,  0.35f,  1f);
    public static final Color BTN_DOWN        = new Color(0.08f,  0.08f,  0.22f,  1f);
    public static final Color TITLE_COLOR     = Color.CYAN;
    public static final Color TEXT_COLOR      = Color.WHITE;
    public static final Color BAR_FILL        = new Color(0.2f,   0.4f,   0.8f,   1f); // blue progress bar
    public static final Color BAR_BACKGROUND  = new Color(0.2f,   0.2f,   0.2f,   1f);

    /** Preferred button width on most screens. */
    public static final int BTN_WIDTH  = 340;
    /** Preferred button height on most screens. */
    public static final int BTN_HEIGHT = 60;
    /** Minimum touch-target dimension (48 dp equivalent). */
    public static final int MIN_TOUCH  = 48;

    /**
     * Returns a pixel value scaled by the device's logical display density,
     * ensuring touch targets are comfortably large on high-DPI screens.
     * Falls back to the raw {@code dp} value if a display is not yet available.
     */
    public static int dp(float dp) {
        try {
            float density = com.badlogic.gdx.Gdx.graphics.getDensity();
            if (density > 0f) return Math.round(dp * density);
        } catch (Throwable ignored) {}
        return Math.round(dp);
    }

    /** Button width scaled to display density. */
    public static int btnWidth()  { return dp(BTN_WIDTH);  }
    /** Button height scaled to display density. */
    public static int btnHeight() { return dp(BTN_HEIGHT); }
    /** Minimum touch-target size scaled to display density. */
    public static int minTouch()  { return dp(MIN_TOUCH);  }

    /**
     * Builds a Skin with the Tessera dark theme applied.
     * The font is a scaled-up default BitmapFont so it is readable on mobile.
     */
    public static Skin buildSkin() {
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.5f);

        Skin skin = new Skin();
        skin.add("default-font", font);

        Label.LabelStyle lblStyle = new Label.LabelStyle(font, TEXT_COLOR);
        skin.add("default", lblStyle);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font      = font;
        btnStyle.fontColor = TEXT_COLOR;
        btnStyle.up        = solid(BTN_UP);
        btnStyle.down      = solid(BTN_DOWN);
        btnStyle.over      = btnStyle.down;
        skin.add("default", btnStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle(font, TEXT_COLOR, solid(BTN_UP));
        skin.add("default", windowStyle);

        // TextField style — dark background, white cursor/selection, white text
        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font              = font;
        tfStyle.fontColor         = TEXT_COLOR;
        tfStyle.background        = solid(BTN_DOWN);
        tfStyle.focusedBackground = solid(new Color(0.12f, 0.12f, 0.30f, 1f));
        tfStyle.cursor            = solid(TEXT_COLOR);
        tfStyle.selection         = solid(new Color(0.3f, 0.3f, 0.7f, 0.7f));
        skin.add("default", tfStyle);

        // ScrollPane style — no decorations needed for plain scrolling
        skin.add("default", new ScrollPane.ScrollPaneStyle());

        return skin;
    }

    /**
     * Creates a 1×1 TextureRegionDrawable filled with the given colour.
     * The backing Texture is added to the provided skin for lifecycle management.
     * When no skin is available callers are responsible for disposing the drawable's texture.
     */
    public static TextureRegionDrawable solid(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        TextureRegionDrawable d = new TextureRegionDrawable(new Texture(pm));
        pm.dispose();
        return d;
    }
}

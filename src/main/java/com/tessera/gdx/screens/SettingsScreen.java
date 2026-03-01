package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.gdx.ui.UiTheme;

/**
 * Settings screen with persisted options (view distance, touch sensitivity,
 * FPS display, auto-jump). Settings are stored via LibGDX {@link Preferences}
 * (backed by Android SharedPreferences on device).
 */
public class SettingsScreen implements Screen {

    /** Preference file name shared across the application. */
    public static final String PREFS_NAME = "tessera_settings";

    // Preference keys
    public static final String KEY_VIEW_DIST   = "view_distance";
    public static final String KEY_SENSITIVITY = "touch_sensitivity";
    public static final String KEY_SHOW_FPS    = "show_fps";
    public static final String KEY_AUTO_JUMP   = "auto_jump";

    // Defaults
    public static final int     DEFAULT_VIEW_DIST   = 64;
    public static final float   DEFAULT_SENSITIVITY = 1.0f;
    public static final boolean DEFAULT_SHOW_FPS    = true;
    public static final boolean DEFAULT_AUTO_JUMP   = false;

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;
    /** Screen to return to (main menu by default). */
    private final Screen returnTo;

    // Live widget references so we can read values on Apply
    private Slider   viewDistSlider;
    private Slider   sensitivitySlider;
    private CheckBox showFpsCheck;
    private CheckBox autoJumpCheck;
    private Label    viewDistLabel;
    private Label    sensitivityLabel;

    public SettingsScreen(TesseraApp app) {
        this(app, null);
    }

    public SettingsScreen(TesseraApp app, Screen returnTo) {
        this.app = app;
        this.returnTo = returnTo;
    }

    /** Returns the shared preferences object. */
    public static Preferences prefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        Preferences p = prefs();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(20);

        Label title = new Label("SETTINGS",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(24).colspan(2).row();

        // ── View Distance ─────────────────────────────────────────────────────
        int savedViewDist = p.getInteger(KEY_VIEW_DIST, DEFAULT_VIEW_DIST);
        viewDistLabel = new Label("View Distance: " + savedViewDist, skin);
        root.add(viewDistLabel).left().padRight(10);

        viewDistSlider = new Slider(32, 256, 32, false, buildSliderStyle());
        viewDistSlider.setValue(savedViewDist);
        viewDistSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                viewDistLabel.setText("View Distance: " + (int) viewDistSlider.getValue());
            }
        });
        root.add(viewDistSlider).expandX().fillX().height(UiTheme.minTouch()).padBottom(16).row();

        // ── Touch Sensitivity ─────────────────────────────────────────────────
        float savedSens = p.getFloat(KEY_SENSITIVITY, DEFAULT_SENSITIVITY);
        sensitivityLabel = new Label(String.format("Touch Sensitivity: %.1f", savedSens), skin);
        root.add(sensitivityLabel).left().padRight(10);

        sensitivitySlider = new Slider(0.1f, 3.0f, 0.1f, false, buildSliderStyle());
        sensitivitySlider.setValue(savedSens);
        sensitivitySlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                sensitivityLabel.setText(
                        String.format("Touch Sensitivity: %.1f", sensitivitySlider.getValue()));
            }
        });
        root.add(sensitivitySlider).expandX().fillX().height(UiTheme.minTouch()).padBottom(16).row();

        // ── Show FPS ──────────────────────────────────────────────────────────
        root.add(new Label("Show FPS:", skin)).left().padRight(10);
        showFpsCheck = new CheckBox(" ", buildCheckBoxStyle());
        showFpsCheck.setChecked(p.getBoolean(KEY_SHOW_FPS, DEFAULT_SHOW_FPS));
        root.add(showFpsCheck).left().padBottom(16).row();

        // ── Auto-Jump ─────────────────────────────────────────────────────────
        root.add(new Label("Auto-Jump:", skin)).left().padRight(10);
        autoJumpCheck = new CheckBox(" ", buildCheckBoxStyle());
        autoJumpCheck.setChecked(p.getBoolean(KEY_AUTO_JUMP, DEFAULT_AUTO_JUMP));
        root.add(autoJumpCheck).left().padBottom(24).row();

        // ── Buttons ───────────────────────────────────────────────────────────
        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                goBack();
            }
        });
        TextButton applyBtn = new TextButton("APPLY", skin);
        applyBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                applySettings();
                goBack();
            }
        });

        Table btnRow = new Table();
        btnRow.add(backBtn).width(UiTheme.btnWidth() / 2).height(UiTheme.btnHeight()).padRight(10);
        btnRow.add(applyBtn).width(UiTheme.btnWidth() / 2).height(UiTheme.btnHeight());
        root.add(btnRow).colspan(2).row();

        stage.addActor(root);
    }

    private void applySettings() {
        Preferences p = prefs();
        p.putInteger(KEY_VIEW_DIST,  (int) viewDistSlider.getValue());
        p.putFloat(KEY_SENSITIVITY,  sensitivitySlider.getValue());
        p.putBoolean(KEY_SHOW_FPS,   showFpsCheck.isChecked());
        p.putBoolean(KEY_AUTO_JUMP,  autoJumpCheck.isChecked());
        p.flush();
    }

    private void goBack() {
        if (returnTo != null) {
            app.setScreen(returnTo);
        } else {
            app.setScreen(new MainMenuScreen(app));
        }
    }

    // ── Minimal inline styles (no atlas required) ─────────────────────────────

    private Slider.SliderStyle buildSliderStyle() {
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = UiTheme.solid(UiTheme.BTN_DOWN);
        style.knob       = UiTheme.solid(UiTheme.BTN_UP);
        return style;
    }

    private CheckBox.CheckBoxStyle buildCheckBoxStyle() {
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font        = skin.getFont("default-font");
        style.fontColor   = UiTheme.TEXT_COLOR;
        style.checkboxOff = UiTheme.solid(UiTheme.BTN_DOWN);
        style.checkboxOn  = UiTheme.solid(UiTheme.PANEL_BORDER);
        return style;
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

package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.common.settings.Settings;
import com.tessera.engine.server.world.World;
import com.tessera.gdx.GdxGameInitializer;
import com.tessera.gdx.ui.UiTheme;

/**
 * Settings screen – mirrors SettingsPage.java from the desktop original.
 * Renders all user-facing settings from {@link Settings}: three checkboxes
 * and three sliders. Changes are applied and saved when APPLY is pressed.
 */
public class SettingsScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;
    /** Screen to return to after BACK or APPLY. */
    private final Screen returnTo;

    // Live settings object loaded in show()
    private Settings settings;

    // Widgets that hold the current values
    private CheckBox switchMouseCb;
    private CheckBox autoJumpCb;
    private CheckBox vsyncCb;
    private Slider viewDistSlider;
    private Slider entityDistSlider;
    private Slider fullscreenSizeSlider;

    public SettingsScreen(TesseraApp app) {
        this(app, null);
    }

    public SettingsScreen(TesseraApp app, Screen returnTo) {
        this.app = app;
        this.returnTo = returnTo;
    }

    @Override
    public void show() {
        settings = Settings.load();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        // ── inner scrollable content ──────────────────────────────────────────
        Table content = new Table();
        content.top().left().pad(10);

        Label note = new Label(
                "Note: some changes take effect after restart.",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR));
        note.setWrap(true);
        content.add(note).expandX().fillX().padBottom(16).colspan(2).row();

        // ── checkboxes ────────────────────────────────────────────────────────
        switchMouseCb = addCheckbox(content, "Switch Mouse Buttons",
                settings.game_switchMouseButtons);
        autoJumpCb   = addCheckbox(content, "Auto Jump",
                settings.game_autoJump);
        vsyncCb      = addCheckbox(content, "VSync",
                settings.video_vsync);

        // ── sliders ───────────────────────────────────────────────────────────
        viewDistSlider = addIntSlider(content, "View Distance",
                settings.viewDistance.value,
                settings.viewDistance.min == 0 ? World.VIEW_DIST_MIN : settings.viewDistance.min,
                settings.viewDistance.max == 0 ? World.VIEW_DIST_MAX : settings.viewDistance.max);

        entityDistSlider = addIntSlider(content, "Entity Distance",
                settings.video_entityDistance.value,
                settings.video_entityDistance.min == 0 ? 20 : settings.video_entityDistance.min,
                settings.video_entityDistance.max == 0 ? 100 : settings.video_entityDistance.max);

        fullscreenSizeSlider = addFloatSlider(content, "Fullscreen Size",
                settings.video_fullscreenSize.value,
                settings.video_fullscreenSize.min == 0f ? 0.5f : settings.video_fullscreenSize.min,
                settings.video_fullscreenSize.max == 0f ? 1.0f : settings.video_fullscreenSize.max);

        // ── scroll pane wraps the content ─────────────────────────────────────
        ScrollPane scroll = new ScrollPane(content, skin);
        scroll.setFadeScrollBars(false);

        // ── root layout ───────────────────────────────────────────────────────
        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(20);

        Label title = new Label("SETTINGS",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(12).row();

        root.add(scroll).expand().fill().padBottom(16).row();

        // ── BACK / APPLY buttons ──────────────────────────────────────────────
        Table btnRow = new Table();
        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) { goBack(); }
        });
        TextButton applyBtn = new TextButton("APPLY", skin);
        applyBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) { applyAndSave(); goBack(); }
        });
        btnRow.add(backBtn) .width(UiTheme.BTN_WIDTH / 2f).height(UiTheme.BTN_HEIGHT).padRight(10);
        btnRow.add(applyBtn).width(UiTheme.BTN_WIDTH / 2f).height(UiTheme.BTN_HEIGHT);
        root.add(btnRow).row();

        stage.addActor(root);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private CheckBox addCheckbox(Table parent, String label, boolean initialValue) {
        CheckBox cb = new CheckBox("  " + label, skin);
        cb.setChecked(initialValue);
        parent.add(cb).left().padBottom(14).colspan(2).row();
        return cb;
    }

    private Slider addIntSlider(Table parent, String labelText, int value, int min, int max) {
        final Label valueLabel = new Label(String.valueOf(value),
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR));

        Slider slider = new Slider(min, max, 1, false, skin);
        slider.setValue(value);
        slider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                valueLabel.setText(String.valueOf((int) slider.getValue()));
            }
        });

        parent.add(new Label(labelText + ":",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR)))
                .left().padRight(8);
        parent.add(valueLabel).right().padBottom(4).row();
        parent.add(slider).expandX().fillX().height(UiTheme.MIN_TOUCH).colspan(2).padBottom(14).row();
        return slider;
    }

    private Slider addFloatSlider(Table parent, String labelText, float value, float min, float max) {
        final Label valueLabel = new Label(String.format("%.2f", value),
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR));

        Slider slider = new Slider(min, max, 0.01f, false, skin);
        slider.setValue(value);
        slider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                valueLabel.setText(String.format("%.2f", slider.getValue()));
            }
        });

        parent.add(new Label(labelText + ":",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR)))
                .left().padRight(8);
        parent.add(valueLabel).right().padBottom(4).row();
        parent.add(slider).expandX().fillX().height(UiTheme.MIN_TOUCH).colspan(2).padBottom(14).row();
        return slider;
    }

    /** Reads all widget values into the settings object and persists to disk. */
    private void applyAndSave() {
        settings.game_switchMouseButtons      = switchMouseCb.isChecked();
        settings.game_autoJump               = autoJumpCb.isChecked();
        settings.video_vsync                 = vsyncCb.isChecked();
        settings.viewDistance.value          = (int) viewDistSlider.getValue();
        settings.video_entityDistance.value  = (int) entityDistSlider.getValue();
        settings.video_fullscreenSize.value  = fullscreenSizeSlider.getValue();
        settings.save();

        // Apply view distance to the active world if one is loaded
        World world = GdxGameInitializer.gdxWorld;
        if (world != null) {
            try {
                world.setViewDistance(settings, settings.viewDistance.value);
            } catch (Exception ignored) {
                // chunkShader may not be initialised in the GDX render path
            }
        }
    }

    private void goBack() {
        if (returnTo != null) {
            app.setScreen(returnTo);
        } else {
            app.setScreen(new MainMenuScreen(app));
        }
    }

    // ── Screen lifecycle ─────────────────────────────────────────────────────

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

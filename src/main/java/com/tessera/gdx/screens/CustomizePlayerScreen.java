package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.Main;
import com.tessera.TesseraApp;
import com.tessera.engine.SkinRegistry;
import com.tessera.engine.client.Client;
import com.tessera.gdx.ui.UiTheme;

/**
 * Customize-Player screen – mirrors CustomizePlayer.java from the desktop.
 * <p>
 * Lets the player:
 * <ul>
 *   <li>Edit their display name (saved immediately via {@code userInfo.saveToDisk()})</li>
 *   <li>Cycle through available skins (calls {@code userInfo.setSkin(id)} + save)</li>
 * </ul>
 */
public class CustomizePlayerScreen implements Screen {

    private final TesseraApp app;
    private final Screen returnTo;
    private Stage stage;
    private Skin skin;

    /** Tracks which skin is currently displayed (0-based index into skins registry). */
    private int chosenSkin = 0;
    private Label skinNameLabel;

    public CustomizePlayerScreen(TesseraApp app) {
        this(app, null);
    }

    /**
     * @param app      the application
     * @param returnTo screen to show when BACK is pressed; defaults to {@link MainMenuScreen}
     */
    public CustomizePlayerScreen(TesseraApp app, Screen returnTo) {
        this.app      = app;
        this.returnTo = returnTo;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        // Pre-populate from player info if available
        String currentName  = "Player";
        if (Client.userPlayer != null && Client.userPlayer.userInfo != null
                && Client.userPlayer.userInfo.name != null) {
            currentName = Client.userPlayer.userInfo.name;
        }
        if (Client.userPlayer != null && Client.userPlayer.userInfo != null) {
            chosenSkin = Client.userPlayer.userInfo.getSkinID();
        }

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(20);

        Label title = new Label("CUSTOMIZE PLAYER",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(20).colspan(2).row();

        // ── Name field ────────────────────────────────────────────────────────
        root.add(new Label("My Name:",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR)))
                .left().padRight(10);
        TextField nameField = new TextField(currentName, skin);
        nameField.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                if (Client.userPlayer != null && Client.userPlayer.userInfo != null) {
                    Client.userPlayer.userInfo.name = nameField.getText();
                    Client.userPlayer.userInfo.saveToDisk();
                }
            }
        });
        root.add(nameField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(10).colspan(2).row();

        // ── Skin selector ─────────────────────────────────────────────────────
        root.add(new Label("Player Skin:",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR)))
                .left().padRight(10);

        skinNameLabel = new Label(currentSkinName(),
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR));
        root.add(skinNameLabel).left().row();

        root.add().height(6).colspan(2).row();

        TextButton skinBtn = new TextButton("NEXT SKIN", skin);
        skinBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                goToNextSkin();
            }
        });
        root.add(skinBtn).colspan(2).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT)
                .padBottom(20).row();

        // ── BACK button ───────────────────────────────────────────────────────
        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                goBack();
            }
        });
        root.add(backBtn).colspan(2).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT).row();

        stage.addActor(root);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private void goToNextSkin() {
        SkinRegistry skins = Main.skins;
        if (skins == null || skins.size() == 0) return;
        chosenSkin = (chosenSkin + 1) % skins.size();
        if (Client.userPlayer != null && Client.userPlayer.userInfo != null) {
            Client.userPlayer.userInfo.setSkin(chosenSkin);
            Client.userPlayer.userInfo.saveToDisk();
        }
        if (skinNameLabel != null) skinNameLabel.setText(currentSkinName());
    }

    private String currentSkinName() {
        if (Main.skins == null || Main.skins.size() == 0) return "none";
        com.tessera.engine.server.players.SkinSupplier supplier = Main.skins.get(chosenSkin);
        if (supplier == null) return "none";
        // Derive a human-readable name from the skin class if a player is available
        if (Client.userPlayer != null) {
            com.tessera.engine.client.player.Skin s = supplier.get(Client.userPlayer);
            return s != null ? s.name : "Skin " + chosenSkin;
        }
        return "Skin " + chosenSkin;
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

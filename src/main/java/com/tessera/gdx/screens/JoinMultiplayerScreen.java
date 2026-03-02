package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.client.Client;
import com.tessera.engine.server.multiplayer.NetworkJoinRequest;
import com.tessera.gdx.GdxGameInitializer;
import com.tessera.gdx.ui.UiTheme;

import java.net.InetAddress;

/**
 * Join / Host Multiplayer screen – mirrors Multiplayer.java from the desktop.
 * <p>
 * Features:
 * <ul>
 *   <li>IP Address + Port text fields</li>
 *   <li>HOST toggle – when checked the local IP is shown read-only and the port
 *       is the server bind port</li>
 *   <li>CONNECT button – builds a {@link NetworkJoinRequest} and starts the
 *       loading sequence (stored in {@link GdxGameInitializer#pendingNetworkRequest})</li>
 *   <li>Error label for connection-time feedback</li>
 *   <li>CUSTOMIZE PLAYER shortcut</li>
 * </ul>
 */
public class JoinMultiplayerScreen implements Screen {

    private static final int DEFAULT_PORT = 8080;

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    // Field references updated when HOST toggle changes
    private TextField ipField;
    private TextField portField;
    private Label errorLabel;
    private CheckBox hostToggle;

    /** Cached local IP address (resolved once in show()). */
    private String localIp = "127.0.0.1";

    public JoinMultiplayerScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        localIp = resolveLocalIp();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(20);

        Label title = new Label("MULTIPLAYER",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(16).colspan(2).row();

        // ── HOST toggle ───────────────────────────────────────────────────────
        hostToggle = new CheckBox("  Host a game (others join me)", skin);
        hostToggle.setChecked(false);
        hostToggle.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                refreshForHostMode();
            }
        });
        root.add(hostToggle).left().colspan(2).padBottom(14).row();

        // ── IP Address ────────────────────────────────────────────────────────
        root.add(new Label("IP Address:",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR)))
                .left().padRight(10);
        ipField = new TextField("", skin);
        root.add(ipField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(10).colspan(2).row();

        // ── Port ──────────────────────────────────────────────────────────────
        root.add(new Label("Port:",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TEXT_COLOR)))
                .left().padRight(10);
        portField = new TextField(String.valueOf(DEFAULT_PORT), skin);
        portField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        root.add(portField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(10).colspan(2).row();

        // ── Error label ───────────────────────────────────────────────────────
        errorLabel = new Label("",
                new Label.LabelStyle(skin.getFont("default-font"), Color.RED));
        errorLabel.setWrap(true);
        root.add(errorLabel).colspan(2).expandX().fillX().padBottom(10).row();

        // ── CUSTOMIZE PLAYER ─────────────────────────────────────────────────
        TextButton customizeBtn = new TextButton("CUSTOMIZE PLAYER", skin);
        customizeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new CustomizePlayerScreen(app,
                        new JoinMultiplayerScreen(app)));
            }
        });
        root.add(customizeBtn).colspan(2).width(UiTheme.BTN_WIDTH).height(UiTheme.BTN_HEIGHT)
                .padBottom(14).row();

        // ── BACK / CONNECT buttons ────────────────────────────────────────────
        Table btnRow = new Table();
        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        TextButton connectBtn = new TextButton("CONNECT", skin);
        connectBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                initiateConnection();
            }
        });
        btnRow.add(backBtn)  .width(UiTheme.BTN_WIDTH / 2f).height(UiTheme.BTN_HEIGHT).padRight(10);
        btnRow.add(connectBtn).width(UiTheme.BTN_WIDTH / 2f).height(UiTheme.BTN_HEIGHT);
        root.add(btnRow).colspan(2).row();

        stage.addActor(root);

        // Apply initial HOST state
        refreshForHostMode();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /** Updates IP field editability and content based on current HOST toggle state. */
    private void refreshForHostMode() {
        if (hostToggle == null) return;
        if (hostToggle.isChecked()) {
            ipField.setText(localIp);
            ipField.setDisabled(true);
        } else {
            if (ipField.isDisabled()) ipField.setText("");
            ipField.setDisabled(false);
        }
    }

    private void initiateConnection() {
        errorLabel.setText("");
        boolean hosting = hostToggle.isChecked();
        String ip       = ipField.getText().trim();
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException ex) {
            errorLabel.setText("Invalid port number.");
            return;
        }
        if (!hosting && ip.isEmpty()) {
            errorLabel.setText("Please enter a host IP address.");
            return;
        }

        String playerName = (Client.userPlayer != null && Client.userPlayer.userInfo != null)
                ? Client.userPlayer.userInfo.name : "Player";

        NetworkJoinRequest req = new NetworkJoinRequest(hosting, port, port, playerName, ip);
        GdxGameInitializer.pendingNetworkRequest = req;

        // Navigate to LoadingScreen which will consume the pending request.
        // For JOIN (non-hosting) worldName/seed/terrain are irrelevant at the GDX
        // level; the actual world data arrives from the remote server.
        app.setScreen(new LoadingScreen(app, "multiplayer", 0, ""));
    }

    private static String resolveLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
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

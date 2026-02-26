package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.client.Client;
import com.tessera.engine.server.world.data.WorldData;

import java.net.InetAddress;

/**
 * Multiplayer screen – host or join a game.
 */
public class MultiplayerScreen implements Screen {

    private final TesseraApp app;
    private final boolean hosting;
    private Stage stage;
    private Skin skin;
    private TextField addressField;
    private Label statusLabel;

    public MultiplayerScreen(TesseraApp app, boolean hosting) {
        this.app = app;
        this.hosting = hosting;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin  = ScreenUtils.createSkin();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label(hosting ? "HOST MULTIPLAYER" : "JOIN MULTIPLAYER", skin, "title"))
                .padBottom(20).row();

        statusLabel = new Label("", skin);
        root.add(statusLabel).padBottom(10).row();

        if (!hosting) {
            root.add(new Label("Server Address:", skin)).left().padBottom(4).row();
            addressField = new TextField("localhost", skin);
            root.add(addressField).width(300).padBottom(12).row();
        } else {
            String myIp = "unknown";
            try { myIp = InetAddress.getLocalHost().getHostAddress(); } catch (Exception ignored) {}
            root.add(new Label("Your IP: " + myIp, skin)).padBottom(12).row();
        }

        TextButton actionBtn = new TextButton(hosting ? "START SERVER" : "CONNECT", skin);
        actionBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                if (hosting) startHost();
                else         joinServer();
            }
        });
        root.add(actionBtn).width(300).height(48).padBottom(10).row();

        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        root.add(backBtn).width(300).height(40).row();
    }

    private void startHost() {
        // Hosting: load the first available world and start as multiplayer host
        statusLabel.setText("Not yet implemented – host game");
    }

    private void joinServer() {
        String address = addressField != null ? addressField.getText().trim() : "localhost";
        statusLabel.setText("Connecting to " + address + " … (not yet implemented)");
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta); stage.draw();
    }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() {
        if (stage != null) { stage.dispose(); stage = null; }
        if (skin  != null) { skin.dispose();  skin  = null; }
    }
}

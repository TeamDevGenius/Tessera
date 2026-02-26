package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.Main;
import com.tessera.TesseraApp;
import com.tessera.engine.client.Client;
import com.tessera.engine.server.Server;
import com.tessera.engine.server.multiplayer.NetworkJoinRequest;
import com.tessera.engine.server.players.Player;
import com.tessera.engine.server.world.data.WorldData;
import com.tessera.engine.utils.ErrorHandler;
import com.tessera.engine.utils.progress.ProgressData;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Shows a progress bar while the world is loading.
 * After loading completes it transitions to {@link GameScreen}.
 */
public class LoadingScreen implements Screen {

    private final TesseraApp app;
    private final WorldData worldData;
    private final NetworkJoinRequest joinRequest;

    private Stage stage;
    private Skin skin;
    private Label statusLabel;
    private ProgressBar progressBar;

    private ProgressData progressData;
    private final AtomicBoolean loadingDone = new AtomicBoolean(false);
    private final AtomicBoolean loadingFailed = new AtomicBoolean(false);
    private final AtomicReference<String> errorMsg = new AtomicReference<>("");

    public LoadingScreen(TesseraApp app, WorldData worldData) {
        this(app, worldData, null);
    }

    public LoadingScreen(TesseraApp app, WorldData worldData, NetworkJoinRequest joinRequest) {
        this.app    = app;
        this.worldData   = worldData;
        this.joinRequest = joinRequest;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin  = ScreenUtils.createSkin();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("Loading World…", skin, "title")).padBottom(20).row();

        ProgressBar.ProgressBarStyle pbStyle = new ProgressBar.ProgressBarStyle();
        pbStyle.background = skin.newDrawable("dark");
        pbStyle.knobBefore  = skin.newDrawable("white");
        progressBar = new ProgressBar(0, 100, 1, false, pbStyle);
        progressBar.setValue(0);
        root.add(progressBar).width(400).height(24).padBottom(12).row();

        statusLabel = new Label("Initialising…", skin);
        root.add(statusLabel).row();

        startLoading();
    }

    private void startLoading() {
        // Lazily initialise the game-engine Client (creates UserControlledPlayer, shaders, block registry, etc.)
        if (TesseraApp.client == null) {
            try {
                TesseraApp.client = new Client(new String[0], TesseraApp.VERSION, TesseraApp.game);
                // Register in Main so legacy code paths that call Main.getClient() work correctly.
                Main.setClient(TesseraApp.client);
            } catch (Exception ex) {
                Gdx.app.error("LoadingScreen", "Client init failed: " + ex.getMessage(), ex);
                loadingFailed.set(true);
                errorMsg.set("Game engine failed to initialise: " + ex.getMessage());
                return;
            }
        }

        Client client = TesseraApp.client;

        progressData = new ProgressData("Loading World…");

        Client.localServer = new Server(TesseraApp.game, Client.world, Client.userPlayer, client);

        final com.tessera.engine.server.Server server = Client.localServer;
        final com.tessera.engine.utils.progress.ProgressData prog = progressData;

        Thread loadThread = new Thread(() -> {
            try {
                // startGameUpdateEvent is a per-frame state machine; call in a loop.
                while (!prog.isFinished() && !prog.isAborted()) {
                    server.startGameUpdateEvent(worldData, prog, joinRequest);
                    if (!prog.isFinished()) Thread.sleep(16); // ~60fps pacing
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                com.tessera.engine.utils.ErrorHandler.report(ex);
                prog.abort();
                loadingFailed.set(true);
                errorMsg.set(ex.getMessage() != null ? ex.getMessage() : ex.toString());
            }
        }, "world-loader");
        loadThread.setDaemon(true);
        loadThread.start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle failure that occurred before progressData was created (e.g., Client init failed).
        if (progressData == null && loadingFailed.get() && !loadingDone.getAndSet(true)) {
            final String msg = errorMsg.get();
            loadingFailed.set(false);
            Gdx.app.postRunnable(() -> {
                Gdx.app.error("LoadingScreen", "Load failed: " + msg);
                app.setScreen(new MainMenuScreen(app));
            });
        }

        if (progressData != null) {
            float pct = (float)(progressData.bar.getProgress() * 100.0);
            progressBar.setValue(pct);
            String task = progressData.getTask();
            if (task != null) statusLabel.setText(task);

            if ((progressData.isFinished() || progressData.bar.isComplete()) && !loadingDone.getAndSet(true)) {
                // Transition to game
                Gdx.app.postRunnable(() -> {
                    Client client = TesseraApp.client;
                    if (client != null) {
                        client.window.goToGamePage();
                    }
                    app.setScreen(new GameScreen(app));
                });
            }
            if ((progressData.isAborted() || loadingFailed.get()) && !loadingDone.getAndSet(true)) {
                loadingFailed.set(false);
                final String msg = errorMsg.get();
                Gdx.app.postRunnable(() -> {
                    Gdx.app.error("LoadingScreen", "Load failed: " + msg);
                    app.setScreen(new MainMenuScreen(app));
                });
            }
        }

        stage.act(delta);
        stage.draw();
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

package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.Main;
import com.tessera.engine.client.Client;
import com.tessera.engine.server.Server;
import com.tessera.engine.server.multiplayer.NetworkJoinRequest;
import com.tessera.engine.server.world.data.WorldData;
import com.tessera.engine.utils.ErrorHandler;
import com.tessera.engine.utils.progress.ProgressData;

/**
 * Shows a progress bar while the world is loading.
 * After loading completes it transitions to {@link GameScreen}.
 *
 * <p>World loading is driven from {@link #render(float)} (the LibGDX GL thread) one step
 * per frame, matching the legacy {@code ProgressMenu} callback pattern.  This is required
 * because {@code startGameUpdateEvent()} creates OpenGL objects (VAOs, VBOs) that must be
 * allocated on the GL thread.</p>
 */
public class LoadingScreen implements Screen {

    private final TesseraApp app;
    private final WorldData worldData;
    private final NetworkJoinRequest joinRequest;

    private Stage stage;
    private Skin skin;
    private Label statusLabel;
    private ProgressBar progressBar;

    /** Set on the GL thread in {@link #startLoading()}; driven each frame in {@link #render}. */
    private ProgressData progressData;
    private Server server;

    private boolean loadingDone   = false;
    private boolean loadingFailed = false;
    private String  errorMsg      = "";

    public LoadingScreen(TesseraApp app, WorldData worldData) {
        this(app, worldData, null);
    }

    public LoadingScreen(TesseraApp app, WorldData worldData, NetworkJoinRequest joinRequest) {
        this.app         = app;
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

    /** Creates the Client and Server on the GL thread if not already initialised. */
    private void startLoading() {
        try {
            if (TesseraApp.client == null) {
                Client c = new Client(new String[0], TesseraApp.VERSION, TesseraApp.game);
                Main.setClient(c);      // sets Main.localClient
                TesseraApp.client = c;  // publish last – both fields consistent from here
            }
            Client c = TesseraApp.client;
            progressData = new ProgressData("Loading World…");
            Client.localServer = new Server(TesseraApp.game, Client.world, Client.userPlayer, c);
            server = Client.localServer;
        } catch (Exception ex) {
            loadingFailed = true;
            errorMsg = ex.getMessage() != null ? ex.getMessage() : ex.toString();
            Gdx.app.error("LoadingScreen", "startLoading failed: " + errorMsg, ex);
        }
    }

    /**
     * Called on the LibGDX GL thread every frame.
     *
     * <p>Each call advances the world-loading state machine by one step via
     * {@code startGameUpdateEvent()}.  All OpenGL allocations (VAOs, VBOs, etc.)
     * that occur during chunk preparation therefore happen on the correct thread.</p>
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!loadingDone) {
            if (progressData == null) {
                // Client init failed before progressData was created
                if (loadingFailed) {
                    loadingDone = true;
                    Gdx.app.error("LoadingScreen", "Load failed: " + errorMsg);
                    app.setScreen(new MainMenuScreen(app));
                    return;
                }
            } else {
                // Advance the world-loading state machine one step per frame on the GL thread.
                if (!progressData.isFinished() && !progressData.isAborted()) {
                    try {
                        server.startGameUpdateEvent(worldData, progressData, joinRequest);
                    } catch (Exception ex) {
                        ErrorHandler.report(ex);
                        progressData.abort();
                        loadingFailed = true;
                        errorMsg = errorString(ex);
                    }
                }

                // Update the progress UI.
                float pct = (float)(progressData.bar.getProgress() * 100.0);
                progressBar.setValue(pct);
                String task = progressData.getTask();
                if (task != null) statusLabel.setText(task);

                if (progressData.isFinished() || progressData.bar.isComplete()) {
                    loadingDone = true;
                    Client client = TesseraApp.client;
                    if (client != null) client.window.goToGamePage();
                    app.setScreen(new GameScreen(app));
                    return;
                }
                if (progressData.isAborted() || loadingFailed) {
                    loadingDone = true;
                    Gdx.app.error("LoadingScreen", "Load failed: " + errorMsg);
                    app.setScreen(new MainMenuScreen(app));
                    return;
                }
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

    private static String errorString(Exception ex) {
        return ex.getMessage() != null ? ex.getMessage() : ex.toString();
    }
}

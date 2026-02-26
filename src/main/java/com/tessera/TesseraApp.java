package com.tessera;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.tessera.content.vanilla.TesseraGame;
import com.tessera.engine.SkinRegistry;
import com.tessera.engine.client.Client;
import com.tessera.engine.utils.resource.ResourceLister;
import com.tessera.screens.MainMenuScreen;

/**
 * Main LibGDX Game entry point for Tessera.
 * Manages screens and initialises the game engine.
 * Works on Android (via AndroidLauncher) and Desktop (via DesktopLauncher).
 */
public class TesseraApp extends Game {

    public static final String TITLE = "Tessera";
    public static final String VERSION = "1.8.0";

    /** Shared client instance – set once the user starts/loads a world. */
    public static Client client;
    public static TesseraGame game;
    public static SkinRegistry skins;

    /** Singleton accessor used by screens that need the game instance. */
    private static TesseraApp instance;
    public static TesseraApp getInstance() { return instance; }

    @Override
    public void create() {
        instance = this;
        Gdx.app.log(TITLE, "Starting version " + VERSION);

        // Initialise resource lister (loads classpath resource list)
        try {
            ResourceLister.init();
        } catch (Exception e) {
            Gdx.app.error(TITLE, "ResourceLister init failed: " + e.getMessage());
        }

        skins = new SkinRegistry();
        game  = new TesseraGame();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (client != null) {
            try { client.stopGame(); } catch (Exception ignored) {}
            client = null;
        }
    }
}
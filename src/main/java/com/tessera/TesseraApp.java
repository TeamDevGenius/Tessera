package com.tessera;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tessera.content.vanilla.TesseraGame;
import com.tessera.engine.client.Client;
import com.tessera.engine.client.ClientWindow;
import com.tessera.engine.SkinRegistry;
import com.tessera.engine.utils.resource.ResourceLister;
import com.tessera.screens.GameScreen;
import com.tessera.screens.MainMenuScreen;

/**
 * Main LibGDX {@link Game} for Tessera.
 * Replaces the LWJGL-based {@code ClientWindow} and {@link Main} entry point.
 * Initialises the game engine and manages screen transitions between
 * the main menu and the in-game view.
 */
public class TesseraApp extends Game {

    public static final String TITLE = "Tessera";
    public static final String VERSION = "1.8.0";

    /* ---- engine references ---- */
    private Client client;
    private TesseraGame tesseraGame;

    /* ---- LibGDX UI ---- */
    private Skin skin;

    /* ---- Accessors for screens ---- */
    public Client getClient() { return client; }
    public ClientWindow getClientWindow() { return client != null ? client.window : null; }
    public Skin getSkin() { return skin; }

    // ------------------------------------------------------------------
    //  Lifecycle
    // ------------------------------------------------------------------

    @Override
    public void create() {
        Gdx.app.log(TITLE, "Started version " + VERSION);

        // Build a simple programmatic skin (no external files required)
        skin = createDefaultSkin();

        // Initialise the game engine on a background thread so the UI
        // is responsive while resources load.  For now we do it on the
        // main thread to keep things simple – heavy resource loading
        // (ResourceLister.init()) may be deferred in a later iteration.
        initEngine();

        // Show the main menu
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) skin.dispose();
        if (client != null) {
            try { client.window.destroyWindow(); } catch (Exception ignored) {}
        }
    }

    // ------------------------------------------------------------------
    //  Engine bootstrap (mirrors Main.main logic)
    // ------------------------------------------------------------------

    private void initEngine() {
        try {
            System.out.println("Client started: " + VERSION);

            // Resource lister scans classpath; may be slow first time.
            ResourceLister.init();

            Main.skins = new SkinRegistry();
            tesseraGame = new TesseraGame();
            Main.game = tesseraGame;

            // Client constructor parses args, creates ClientWindow, etc.
            // Pass skipGlfwInit=true so the GLFW window setup is skipped –
            // LibGDX already manages the window via Lwjgl3Application.
            client = new Client(new String[0], VERSION, tesseraGame, true);

            // Expose client through Main so existing code (Main.getClient())
            // continues to work.
            setMainClient(client);

            Gdx.app.log(TITLE, "Engine initialised");
        } catch (Throwable e) {
            // Catch Throwable (not just Exception) to handle NoClassDefFoundError,
            // VerifyError, and other linkage problems that may arise from
            // partially-ported LWJGL stubs.
            Gdx.app.error(TITLE, "Engine init failed – running in menu-only mode", e);
        }
    }

    /** Reflectively set Main.localClient (private field). */
    private static void setMainClient(Client c) {
        try {
            java.lang.reflect.Field f = Main.class.getDeclaredField("localClient");
            f.setAccessible(true);
            f.set(null, c);
        } catch (Exception ignored) { }
    }

    // ------------------------------------------------------------------
    //  Screen helpers (called from screens)
    // ------------------------------------------------------------------

    public void goToMainMenu() {
        setScreen(new MainMenuScreen(this));
    }

    public void goToGame() {
        setScreen(new GameScreen(this));
    }

    // Placeholder actions for menu buttons – these will be wired to full
    // dialogs once the UI is fully ported from Nuklear to Scene2D.
    public void showNewWorldDialog()          { Gdx.app.log(TITLE, "New World (not yet ported)"); }
    public void showLoadWorldDialog()         { Gdx.app.log(TITLE, "Load World (not yet ported)"); }
    public void showJoinMultiplayerDialog()   { Gdx.app.log(TITLE, "Join Multiplayer (not yet ported)"); }
    public void showCustomizePlayerDialog()   { Gdx.app.log(TITLE, "Customize Player (not yet ported)"); }
    public void showSettingsDialog()          { Gdx.app.log(TITLE, "Settings (not yet ported)"); }

    // ------------------------------------------------------------------
    //  Programmatic Skin
    // ------------------------------------------------------------------

    private static Skin createDefaultSkin() {
        Skin s = new Skin();

        // 1-pixel white texture used for all drawable tinting
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.WHITE);
        pix.fill();
        Texture white = new Texture(pix);
        pix.dispose();
        s.add("white", white);

        // Default font
        BitmapFont font = new BitmapFont();
        s.add("default-font", font);

        // Large font for titles (scale up the default bitmap font)
        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(2f);
        s.add("title-font", titleFont);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        s.add("default", labelStyle);

        // Title label style
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.WHITE;
        s.add("title", titleStyle);

        // TextButton style
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.WHITE;
        btnStyle.downFontColor = Color.LIGHT_GRAY;
        btnStyle.overFontColor = new Color(0.8f, 0.9f, 1f, 1f);

        // Drawable backgrounds via NinePatch-like coloured regions
        btnStyle.up   = s.newDrawable("white", new Color(0.25f, 0.35f, 0.5f, 1f));
        btnStyle.over = s.newDrawable("white", new Color(0.3f, 0.45f, 0.6f, 1f));
        btnStyle.down = s.newDrawable("white", new Color(0.15f, 0.25f, 0.4f, 1f));
        s.add("default", btnStyle);

        return s;
    }
}
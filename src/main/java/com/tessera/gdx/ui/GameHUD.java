package com.tessera.gdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tessera.TesseraApp;
import com.tessera.engine.server.item.ItemStack;
import com.tessera.gdx.GdxPlayer;
import com.tessera.gdx.screens.PauseMenuScreen;

/**
 * Heads-up display for the in-game screen.
 *
 * <ul>
 *   <li>Top-left  : FPS counter and camera coordinates</li>
 *   <li>Top-right : Pause button</li>
 *   <li>Centre    : Crosshair drawn via ShapeRenderer</li>
 *   <li>Bottom-left: Health / Food / Oxygen progress bars</li>
 *   <li>Bottom-centre: 9-slot hotbar with selection highlight; selected item
 *       name shown above the bar</li>
 *   <li>Bottom-right: Jump button</li>
 * </ul>
 *
 * Call {@link #renderShapes()} once per frame <em>before</em>
 * {@link Stage#draw()} to paint ShapeRenderer elements.
 * Hotbar slot selection is performed by {@link
 * com.tessera.gdx.input.TouchControls} which shares the same geometry
 * constants exposed here.
 */
public class GameHUD {

    // ── hotbar geometry (also used by TouchControls) ──────────────────────────
    public static final int   SLOTS      = GdxPlayer.HOTBAR_SIZE; // 9
    public static final float SLOT_SIZE  = 52f;
    public static final float SLOT_PAD   = 4f;
    public static final float SLOT_STEP  = SLOT_SIZE + SLOT_PAD;
    public static final float HOTBAR_W   = SLOTS * SLOT_STEP - SLOT_PAD;
    public static final float HOTBAR_Y   = 10f;   // distance from bottom of screen

    // ── status-bar geometry ───────────────────────────────────────────────────
    private static final float BAR_W          = 180f;
    private static final float BAR_H          = 12f;
    private static final float BAR_SPACING    = 5f;
    private static final float BAR_LEFT       = 8f;
    /** Y-offset from the top of the screen to the top of the first bar. */
    private static final float BAR_TOP_INSET  = 68f;

    // ── crosshair geometry ────────────────────────────────────────────────────
    private static final float CROSS_ARM  = 12f;
    private static final float CROSS_GAP  = 3f;
    private static final float CROSS_HALF = 1.5f;

    // ── fields ────────────────────────────────────────────────────────────────
    private final Stage       stage;
    private final TesseraApp  app;
    private final BitmapFont  font;
    private       GdxPlayer   player;
    private final ShapeRenderer sr;
    private       Skin        skin;

    private Label coordsLabel;
    private Label fpsLabel;
    private Label selectedItemLabel;

    // ── constructors ──────────────────────────────────────────────────────────

    /** Legacy – no player wired yet. */
    public GameHUD(Stage stage, BitmapFont font) {
        this(stage, font, null, null, null);
    }

    /** Backward-compatible – no player. */
    public GameHUD(Stage stage, BitmapFont font, TesseraApp app) {
        this(stage, font, app, null, null);
    }

    /** Backward-compatible – no player. */
    public GameHUD(Stage stage, BitmapFont font, TesseraApp app, Runnable jumpCallback) {
        this(stage, font, app, null, jumpCallback);
    }

    /**
     * Full constructor.
     *
     * @param player       GDX player whose stats are read each frame; may be null
     * @param jumpCallback called when the on-screen Jump button is tapped; may be null
     */
    public GameHUD(Stage stage, BitmapFont font, TesseraApp app,
                   GdxPlayer player, Runnable jumpCallback) {
        this.stage  = stage;
        this.app    = app;
        this.font   = font;
        this.player = player;
        this.sr     = new ShapeRenderer();
        this.skin   = UiTheme.buildSkin();

        Label.LabelStyle white  = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle yellow = new Label.LabelStyle(font, Color.YELLOW);

        coordsLabel       = new Label("0, 0, 0", white);
        fpsLabel          = new Label("FPS: 0",  white);
        selectedItemLabel = new Label("",         yellow);

        // ── top-left: FPS + coordinates ──────────────────────────────────────
        Table topLeft = new Table();
        topLeft.top().left().setFillParent(true);
        topLeft.pad(8);
        topLeft.add(fpsLabel).left().row();
        topLeft.add(coordsLabel).left().row();
        stage.addActor(topLeft);

        // ── top-right: pause button ───────────────────────────────────────────
        Table topRight = new Table();
        topRight.top().right().setFillParent(true);
        topRight.pad(8);
        TextButton pauseBtn = new TextButton("||", skin);
        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                if (app != null) app.setScreen(new PauseMenuScreen(app));
            }
        });
        topRight.add(pauseBtn).size(72, 72);
        stage.addActor(topRight);

        // ── bottom-centre: selected item name above hotbar ────────────────────
        Table bottomCenter = new Table();
        bottomCenter.bottom().setFillParent(true);
        bottomCenter.pad(0, 0, HOTBAR_Y + SLOT_SIZE + 6f, 0);
        bottomCenter.add(selectedItemLabel);
        stage.addActor(bottomCenter);

        // ── bottom-right: jump button ─────────────────────────────────────────
        if (jumpCallback != null) {
            Table bottomRight = new Table();
            bottomRight.bottom().right().setFillParent(true);
            bottomRight.pad(20);
            TextButton jumpBtn = new TextButton("JUMP", skin);
            jumpBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent e, Actor a) {
                    jumpCallback.run();
                }
            });
            bottomRight.add(jumpBtn).size(130, 90);
            stage.addActor(bottomRight);
        }
    }

    // ── public API ────────────────────────────────────────────────────────────

    /** Replace (or initially wire) the player instance. */
    public void setPlayer(GdxPlayer player) {
        this.player = player;
    }

    /**
     * Draws hotbar slots, status bars and crosshair with a {@link ShapeRenderer}.
     * Must be called <em>before</em> {@link Stage#draw()} and outside any
     * active SpriteBatch begin/end block.
     */
    public void renderShapes() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.setProjectionMatrix(stage.getCamera().combined);

        drawStatusBars(h);
        drawHotbar(w);
        drawCrosshair(w, h);
    }

    /** Update text labels from the camera and player each frame. */
    public void update(float delta, PerspectiveCamera camera) {
        if (camera != null) {
            coordsLabel.setText(String.format("%.1f, %.1f, %.1f",
                    camera.position.x, camera.position.y, camera.position.z));
        }
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

        if (player != null) {
            ItemStack held = player.getHotbarItem();
            selectedItemLabel.setText(
                    (held != null && held.item != null) ? held.item.name : "");
        }
    }

    public void dispose() {
        if (skin != null) { skin.dispose(); skin = null; }
        sr.dispose();
    }

    // ── private drawing helpers ───────────────────────────────────────────────

    private void drawStatusBars(int h) {
        float hp  = player != null ? player.getHealth() / GdxPlayer.MAX_HEALTH  : 1f;
        float fp  = player != null ? player.getFood()   / GdxPlayer.MAX_FOOD    : 1f;
        float op  = player != null ? player.getOxygen() / GdxPlayer.MAX_OXYGEN  : 1f;

        float y1 = h - BAR_TOP_INSET;
        float y2 = y1 - BAR_H - BAR_SPACING;
        float y3 = y2 - BAR_H - BAR_SPACING;

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // backgrounds
        sr.setColor(0.15f, 0.15f, 0.15f, 0.8f);
        sr.rect(BAR_LEFT, y1, BAR_W, BAR_H);
        sr.rect(BAR_LEFT, y2, BAR_W, BAR_H);
        sr.rect(BAR_LEFT, y3, BAR_W, BAR_H);

        // health (green)
        sr.setColor(0.15f, 0.82f, 0.15f, 0.9f);
        sr.rect(BAR_LEFT, y1, BAR_W * Math.max(0f, Math.min(1f, hp)), BAR_H);

        // food (amber)
        sr.setColor(0.88f, 0.76f, 0.08f, 0.9f);
        sr.rect(BAR_LEFT, y2, BAR_W * Math.max(0f, Math.min(1f, fp)), BAR_H);

        // oxygen (cyan)
        sr.setColor(0.08f, 0.78f, 0.95f, 0.9f);
        sr.rect(BAR_LEFT, y3, BAR_W * Math.max(0f, Math.min(1f, op)), BAR_H);

        sr.end();
    }

    private void drawHotbar(int w) {
        float hx   = (w - HOTBAR_W) / 2f;
        int   sel  = player != null ? player.getHotbarSlot() : 0;

        // ── slot backgrounds ──────────────────────────────────────────────────
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < SLOTS; i++) {
            float sx = hx + i * SLOT_STEP;
            // Slightly brighter background for occupied slots
            boolean hasItem = player != null
                    && player.inventory.get(i) != null;
            if (hasItem) {
                sr.setColor(0.18f, 0.18f, 0.22f, 0.85f);
            } else {
                sr.setColor(0.08f, 0.08f, 0.10f, 0.80f);
            }
            sr.rect(sx, HOTBAR_Y, SLOT_SIZE, SLOT_SIZE);
        }
        sr.end();

        // ── slot borders ──────────────────────────────────────────────────────
        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2f);
        for (int i = 0; i < SLOTS; i++) {
            float sx = hx + i * SLOT_STEP;
            if (i == sel) {
                sr.setColor(1f, 1f, 1f, 1f);           // selected: bright white
            } else {
                sr.setColor(0.28f, 0.28f, 0.60f, 0.75f); // normal: dim blue
            }
            sr.rect(sx, HOTBAR_Y, SLOT_SIZE, SLOT_SIZE);
        }
        sr.end();
        Gdx.gl.glLineWidth(1f);
    }

    private void drawCrosshair(int w, int h) {
        float cx = w * 0.5f;
        float cy = h * 0.5f;

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1f, 1f, 1f, 0.88f);
        // horizontal left & right arms
        sr.rect(cx - CROSS_ARM,           cy - CROSS_HALF, CROSS_ARM - CROSS_GAP, CROSS_HALF * 2);
        sr.rect(cx + CROSS_GAP,           cy - CROSS_HALF, CROSS_ARM - CROSS_GAP, CROSS_HALF * 2);
        // vertical bottom & top arms
        sr.rect(cx - CROSS_HALF, cy - CROSS_ARM,           CROSS_HALF * 2, CROSS_ARM - CROSS_GAP);
        sr.rect(cx - CROSS_HALF, cy + CROSS_GAP,           CROSS_HALF * 2, CROSS_ARM - CROSS_GAP);
        sr.end();
    }
}


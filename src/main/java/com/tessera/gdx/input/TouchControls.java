package com.tessera.gdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.tessera.engine.server.world.World;
import com.tessera.gdx.GdxPlayer;
import com.tessera.gdx.ui.GameHUD;
import org.joml.Vector3f;

/**
 * Touch-based input handler for the in-game screen.
 *
 * <ul>
 *   <li><b>Left half</b>  – virtual joystick for movement</li>
 *   <li><b>Right half</b> – camera look (drag), break block (quick tap),
 *       place block (long-press ≥ 500 ms without significant drag)</li>
 *   <li><b>Hotbar strip</b> (bottom centre) – tap selects a slot</li>
 * </ul>
 *
 * Call {@link #update(float, PerspectiveCamera)} every frame to apply
 * physics, sync the player position, tick stats, and check for long-press
 * block placement.
 */
public class TouchControls implements InputProcessor {

    private static final float SENSITIVITY     = 0.2f;
    private static final float DEAD_ZONE       = 20f;
    private static final float MOVE_SPEED      = 8f;
    private static final float GRAVITY         = -20f;
    private static final float JUMP_VELOCITY   = 8f;
    private static final float PLAYER_HEIGHT   = 1.8f;
    private static final float JOYSTICK_RADIUS = 80f;
    /** Head-clearance offset used for ceiling collision. */
    private static final float HEAD_CLEARANCE  = 0.1f;
    private static final int   JOYSTICK_BASE_SIZE  = 80;
    private static final int   JOYSTICK_THUMB_SIZE = 50;

    /** A right-side touch shorter than this (ms) with minimal movement = break block. */
    private static final long  TAP_MAX_MS         = 250L;
    /** A right-side touch longer than this (ms) with minimal movement = place block. */
    private static final long  LONG_PRESS_MS      = 500L;
    /** Pixel movement on the right side above which the gesture becomes a camera drag. */
    private static final float LOOK_DRAG_THRESHOLD = 15f;

    // ── joystick (left side) ──────────────────────────────────────────────────
    private int   joystickPointer = -1;
    private float joystickStartX, joystickStartY;
    private float joystickDX, joystickDY;

    // ── camera look (right side) ──────────────────────────────────────────────
    private int   lookPointer   = -1;
    private float lookLastX,    lookLastY;
    private float lookDownX,    lookDownY;
    private long  lookDownTime  = 0L;
    private boolean lookHasMoved      = false;
    private boolean longPressHandled  = false;

    // ── camera orientation ────────────────────────────────────────────────────
    private float pan  = 0f;
    private float tilt = 0f;

    // ── player physics ────────────────────────────────────────────────────────
    private float   velocityY  = 0f;
    private boolean isOnGround = false;

    // ── dependencies ──────────────────────────────────────────────────────────
    private final PerspectiveCamera camera;
    private final World             world;
    private       GdxPlayer         player;

    // ── joystick overlay textures ─────────────────────────────────────────────
    private Texture joystickBaseTexture;
    private Texture joystickThumbTexture;

    // ── constructors ──────────────────────────────────────────────────────────

    /**
     * Full constructor.
     *
     * @param camera the perspective camera (direction updated every frame)
     * @param world  active world for collision and block interaction; may be null
     * @param player GDX player for stats and block interaction; may be null
     */
    public TouchControls(PerspectiveCamera camera, World world, GdxPlayer player) {
        this.camera = camera;
        this.world  = world;
        this.player = player;
        joystickBaseTexture  = createCircleTexture(JOYSTICK_BASE_SIZE,  0.8f);
        joystickThumbTexture = createCircleTexture(JOYSTICK_THUMB_SIZE, 1.0f);
    }

    /** Backward-compatible constructor (no player). */
    public TouchControls(PerspectiveCamera camera, World world) {
        this(camera, world, null);
    }

    /** Backward-compatible single-arg constructor (no physics/player). */
    public TouchControls(PerspectiveCamera camera) {
        this(camera, null, null);
    }

    // ── public API ────────────────────────────────────────────────────────────

    /** Replace (or initially wire) the player instance. */
    public void setPlayer(GdxPlayer player) {
        this.player = player;
    }

    /** Make the player jump if currently on the ground. */
    public void jump() {
        if (isOnGround) {
            velocityY  = JUMP_VELOCITY;
            isOnGround = false;
        }
    }

    // ── frame update ──────────────────────────────────────────────────────────

    public void update(float delta, PerspectiveCamera camera) {
        // ── horizontal movement from joystick ─────────────────────────────────
        float moveX = 0, moveZ = 0;
        float dist = (float) Math.sqrt(joystickDX * joystickDX + joystickDY * joystickDY);
        if (dist > DEAD_ZONE) {
            moveX = joystickDX / dist;
            moveZ = joystickDY / dist;
        }

        // Rebuild camera direction from pan/tilt
        camera.direction.set(
            (float) (Math.cos(pan)  * Math.cos(tilt)),
            (float) (-Math.sin(tilt)),
            (float) (Math.sin(pan)  * Math.cos(tilt))
        );
        camera.direction.nor();

        Vector3 right   = new Vector3(camera.direction).crs(camera.up).nor();
        Vector3 forward = new Vector3(camera.direction);
        forward.y = 0;
        forward.nor();

        // Joystick up (negative DY on screen) → forward; right → strafe
        camera.position.mulAdd(forward, -moveZ * MOVE_SPEED * delta);
        camera.position.mulAdd(right,    moveX * MOVE_SPEED * delta);

        // ── vertical physics ──────────────────────────────────────────────────
        velocityY += GRAVITY * delta;
        float newY = camera.position.y + velocityY * delta;

        if (world != null) {
            int camX = (int) Math.floor(camera.position.x);
            int camZ = (int) Math.floor(camera.position.z);

            if (velocityY <= 0) {
                float feetNewY    = newY - PLAYER_HEIGHT;
                int   blockBelowY = (int) Math.floor(feetNewY);
                com.tessera.engine.server.block.Block blockBelow =
                        world.getBlock(camX, blockBelowY, camZ);
                if (blockBelow != null && blockBelow.solid) {
                    camera.position.y = blockBelowY + 1 + PLAYER_HEIGHT;
                    velocityY  = 0f;
                    isOnGround = true;
                } else {
                    camera.position.y = newY;
                    isOnGround = false;
                }
            } else {
                int headNewY = (int) Math.floor(newY + HEAD_CLEARANCE);
                com.tessera.engine.server.block.Block blockAbove =
                        world.getBlock(camX, headNewY, camZ);
                if (blockAbove != null && blockAbove.solid) {
                    velocityY = 0f;
                } else {
                    camera.position.y = newY;
                }
                isOnGround = false;
            }
        } else {
            camera.position.y = newY;
        }

        // ── long-press block placement ────────────────────────────────────────
        if (lookPointer != -1 && !lookHasMoved && !longPressHandled) {
            long elapsed = System.currentTimeMillis() - lookDownTime;
            if (elapsed >= LONG_PRESS_MS && player != null && world != null) {
                doPlaceBlock(camera);
                longPressHandled = true;
            }
        }

        // ── player stats + position sync ──────────────────────────────────────
        if (player != null) {
            player.syncPositionFromCamera(camera);

            boolean isMoving = dist > DEAD_ZONE;
            boolean isUnderwater = false;
            if (world != null) {
                int bx = (int) Math.floor(camera.position.x);
                int by = (int) Math.floor(camera.position.y);
                int bz = (int) Math.floor(camera.position.z);
                com.tessera.engine.server.block.Block camBlock = world.getBlock(bx, by, bz);
                isUnderwater = camBlock != null && camBlock.isLiquid();
            }
            player.tickStats(delta, isMoving, isUnderwater);
        }
    }

    // ── overlay rendering ─────────────────────────────────────────────────────

    /**
     * Draws the on-screen virtual joystick.
     * Must be called outside an active SpriteBatch begin/end block.
     */
    public void renderOverlay(SpriteBatch batch) {
        if (joystickBaseTexture == null) return;
        int   screenH   = Gdx.graphics.getHeight();
        float baseSize  = joystickBaseTexture.getWidth();
        float thumbSize = joystickThumbTexture.getWidth();

        batch.begin();
        if (joystickPointer != -1) {
            float bx = joystickStartX - baseSize  / 2f;
            float by = screenH - joystickStartY - baseSize  / 2f;

            float clampedDX =  MathUtils.clamp(joystickDX, -JOYSTICK_RADIUS, JOYSTICK_RADIUS);
            float clampedDY = -MathUtils.clamp(joystickDY, -JOYSTICK_RADIUS, JOYSTICK_RADIUS);
            float tx = joystickStartX + clampedDX - thumbSize / 2f;
            float ty = screenH - joystickStartY + clampedDY - thumbSize / 2f;

            batch.setColor(1f, 1f, 1f, 0.25f);
            batch.draw(joystickBaseTexture,  bx, by);
            batch.setColor(1f, 1f, 1f, 0.55f);
            batch.draw(joystickThumbTexture, tx, ty);
        } else {
            float bx = 100f - baseSize  / 2f;
            float by = 100f - baseSize  / 2f;
            batch.setColor(1f, 1f, 1f, 0.12f);
            batch.draw(joystickBaseTexture, bx, by);
        }
        batch.setColor(Color.WHITE);
        batch.end();
    }

    // ── InputProcessor ────────────────────────────────────────────────────────

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert to stage/GL coords (screenY is top-down, stage Y is bottom-up)
        float stageY = Gdx.graphics.getHeight() - screenY;

        // ── hotbar slot tap ───────────────────────────────────────────────────
        float hotbarX = (Gdx.graphics.getWidth() - GameHUD.HOTBAR_W) / 2f;
        if (stageY >= GameHUD.HOTBAR_Y && stageY <= GameHUD.HOTBAR_Y + GameHUD.SLOT_SIZE
                && screenX >= hotbarX && screenX <= hotbarX + GameHUD.HOTBAR_W) {
            int slot = (int) ((screenX - hotbarX) / GameHUD.SLOT_STEP);
            slot = Math.max(0, Math.min(GameHUD.SLOTS - 1, slot));
            if (player != null) player.setHotbarSlot(slot);
            return true;
        }

        // ── left side: joystick ───────────────────────────────────────────────
        if (isLeftSide(screenX)) {
            if (joystickPointer == -1) {
                joystickPointer = pointer;
                joystickStartX  = screenX;
                joystickStartY  = screenY;
                joystickDX = 0;
                joystickDY = 0;
            }
        } else {
            // ── right side: look / tap / long-press ───────────────────────────
            if (lookPointer == -1) {
                lookPointer      = pointer;
                lookLastX        = screenX;
                lookLastY        = screenY;
                lookDownX        = screenX;
                lookDownY        = screenY;
                lookDownTime     = System.currentTimeMillis();
                lookHasMoved     = false;
                longPressHandled = false;
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == joystickPointer) {
            joystickPointer = -1;
            joystickDX = 0;
            joystickDY = 0;
        }
        if (pointer == lookPointer) {
            if (!lookHasMoved && !longPressHandled) {
                long elapsed = System.currentTimeMillis() - lookDownTime;
                if (elapsed < TAP_MAX_MS) {
                    // Quick tap → break block
                    if (player != null && world != null) {
                        doBreakBlock(camera);
                    }
                }
            }
            lookPointer      = -1;
            lookHasMoved     = false;
            longPressHandled = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == joystickPointer) {
            joystickDX = screenX - joystickStartX;
            joystickDY = screenY - joystickStartY;
        }
        if (pointer == lookPointer) {
            float dx = screenX - lookLastX;
            float dy = screenY - lookLastY;
            pan  += dx * SENSITIVITY * 0.01f;
            tilt += dy * SENSITIVITY * 0.01f;
            tilt  = Math.max(-1.5f, Math.min(1.5f, tilt));
            lookLastX = screenX;
            lookLastY = screenY;

            // If the finger moved significantly the gesture is a drag, not a tap
            float totalDx = screenX - lookDownX;
            float totalDy = screenY - lookDownY;
            if (!lookHasMoved
                    && (totalDx * totalDx + totalDy * totalDy)
                       > LOOK_DRAG_THRESHOLD * LOOK_DRAG_THRESHOLD) {
                lookHasMoved = true;
            }
        }
        return true;
    }

    @Override public boolean keyDown(int k)               { return false; }
    @Override public boolean keyUp(int k)                 { return false; }
    @Override public boolean keyTyped(char c)             { return false; }
    @Override public boolean mouseMoved(int sx, int sy)   { return false; }
    @Override public boolean scrolled(float ax, float ay) { return false; }
    @Override public boolean touchCancelled(int sx, int sy, int p, int b) {
        // Treat a cancelled touch the same as a touch-up to clean up state
        return touchUp(sx, sy, p, b);
    }

    // ── block interaction helpers ─────────────────────────────────────────────

    private void doBreakBlock(PerspectiveCamera cam) {
        Vector3f origin = new Vector3f(cam.position.x, cam.position.y, cam.position.z);
        Vector3f dir    = new Vector3f(cam.direction.x, cam.direction.y, cam.direction.z);
        player.breakBlock(origin, dir, world);
    }

    private void doPlaceBlock(PerspectiveCamera cam) {
        Vector3f origin = new Vector3f(cam.position.x, cam.position.y, cam.position.z);
        Vector3f dir    = new Vector3f(cam.direction.x, cam.direction.y, cam.direction.z);
        player.placeBlock(origin, dir, world);
    }

    // ── lifecycle ─────────────────────────────────────────────────────────────

    /** Call on the GL thread when the game screen is hidden. */
    public void dispose() {
        if (joystickBaseTexture  != null) { joystickBaseTexture.dispose();  joystickBaseTexture  = null; }
        if (joystickThumbTexture != null) { joystickThumbTexture.dispose(); joystickThumbTexture = null; }
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private static Texture createCircleTexture(int size, float alpha) {
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, alpha);
        pm.fillCircle(size / 2, size / 2, size / 2 - 2);
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    private boolean isLeftSide(float x) {
        return x < Gdx.graphics.getWidth() * 0.5f;
    }
}

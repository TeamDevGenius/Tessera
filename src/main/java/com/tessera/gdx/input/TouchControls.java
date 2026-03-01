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
import com.tessera.gdx.screens.SettingsScreen;

public class TouchControls implements InputProcessor {

    private static final float BASE_SENSITIVITY  = 0.2f;
    private static final float DEAD_ZONE         = 20f;
    private static final float MOVE_SPEED        = 8f;
    private static final float SPRINT_MULTIPLIER = 1.6f;
    private static final float GRAVITY           = -20f;
    private static final float JUMP_VELOCITY     = 8f;
    private static final float PLAYER_HEIGHT     = 1.8f;
    private static final float JOYSTICK_RADIUS   = 80f;
    /** Head-clearance offset used for ceiling collision: slightly above the eye-level position. */
    private static final float HEAD_CLEARANCE    = 0.1f;
    private static final int   JOYSTICK_BASE_SIZE  = 80;
    private static final int   JOYSTICK_THUMB_SIZE = 50;

    // Joystick (left side)
    private int   joystickPointer = -1;
    private float joystickStartX, joystickStartY;
    private float joystickDX, joystickDY;

    // Camera look (right side)
    private int   lookPointer = -1;
    private float lookLastX, lookLastY;
    /** Accumulated time that the right-side pointer has been held (for hold-to-break detection). */
    private float lookHoldTime = 0f;

    private float pan  = 0f;
    private float tilt = 0f;

    // Player physics
    private float   velocityY  = 0f;
    private boolean isOnGround = false;

    // Movement modifiers (set by HUD buttons)
    private boolean sprinting = false;
    private boolean flying    = false;
    private boolean flyUp     = false;
    private boolean flyDown   = false;
    /** Speed for fly-up / fly-down when in creative/freeplay mode. */
    private static final float FLY_VERTICAL_SPEED = 10f;

    private final World world;

    /**
     * Optional callbacks wired by the game screen.
     * {@code onBreakBlock} is called each frame the right-side pointer is held (> TAP_THRESHOLD).
     * {@code onPlaceBlock} is called when the right-side pointer is released within TAP_THRESHOLD.
     */
    private Runnable onBreakBlock;
    private Runnable onPlaceBlock;
    /** Seconds of hold before the touch is treated as "break" rather than "place". */
    private static final float TAP_THRESHOLD = 0.25f;

    // Joystick overlay textures (created in constructor, disposed in dispose)
    private Texture joystickBaseTexture;
    private Texture joystickThumbTexture;

    /**
     * @param camera the perspective camera (held by reference; direction is written every frame)
     * @param world  nullable – used for ground/ceiling collision; pass null if no world is loaded
     */
    public TouchControls(PerspectiveCamera camera, World world) {
        this.world = world;
        joystickBaseTexture  = createCircleTexture(JOYSTICK_BASE_SIZE,  0.8f);
        joystickThumbTexture = createCircleTexture(JOYSTICK_THUMB_SIZE, 1.0f);
    }

    /** Backward-compatible single-arg constructor (no physics). */
    public TouchControls(PerspectiveCamera camera) {
        this(camera, null);
    }

    // -------------------------------------------------------------------------

    private static Texture createCircleTexture(int size, float alpha) {
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, alpha);
        pm.fillCircle(size / 2, size / 2, size / 2 - 2);
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    /** Call on the GL thread when the game screen leaves. */
    public void dispose() {
        if (joystickBaseTexture  != null) { joystickBaseTexture.dispose();  joystickBaseTexture  = null; }
        if (joystickThumbTexture != null) { joystickThumbTexture.dispose(); joystickThumbTexture = null; }
    }

    // ── Callbacks for block interactions ──────────────────────────────────────

    /**
     * Set callbacks for block breaking (hold) and placing (tap).
     * @param onBreakBlock called each frame the player is holding the look side (break)
     * @param onPlaceBlock called once when the player taps the look side (place)
     */
    public void setBlockInteractionCallbacks(Runnable onBreakBlock, Runnable onPlaceBlock) {
        this.onBreakBlock = onBreakBlock;
        this.onPlaceBlock = onPlaceBlock;
    }

    // ── Sprint / Fly controls (toggled by HUD buttons) ─────────────────────────

    /** Toggle sprint mode on/off. */
    public void toggleSprint()    { sprinting = !sprinting; }
    /** Returns whether the player is currently sprinting. */
    public boolean isSprinting()  { return sprinting; }

    /** Enable or disable fly mode (creative/freeplay). */
    public void setFlying(boolean flying) {
        this.flying = flying;
        if (!flying) velocityY = 0f; // reset vertical velocity when exiting fly
    }
    /** Returns whether fly mode is active. */
    public boolean isFlying() { return flying; }

    /** Begin flying upward (call while fly-up button is pressed). */
    public void setFlyUp(boolean up)     { this.flyUp   = up; }
    /** Begin flying downward (call while fly-down button is pressed). */
    public void setFlyDown(boolean down) { this.flyDown = down; }

    // ── Jump ──────────────────────────────────────────────────────────────────

    /**
     * Make the player jump if currently on the ground.
     * Called by the HUD jump button.
     */
    public void jump() {
        if (isOnGround) {
            velocityY  = JUMP_VELOCITY;
            isOnGround = false;
        }
    }

    // ── Main update ───────────────────────────────────────────────────────────

    public void update(float delta, PerspectiveCamera camera) {

        // --- Horizontal movement from left joystick ---
        float moveX = 0, moveZ = 0;
        float dist = (float) Math.sqrt(joystickDX * joystickDX + joystickDY * joystickDY);
        if (dist > DEAD_ZONE) {
            moveX = joystickDX / dist;
            moveZ = joystickDY / dist;
        }
        float speed = MOVE_SPEED * (sprinting ? SPRINT_MULTIPLIER : 1f);

        // Rebuild camera direction from pan/tilt
        camera.direction.set(
            (float) (Math.cos(pan) * Math.cos(tilt)),
            (float) (-Math.sin(tilt)),
            (float) (Math.sin(pan) * Math.cos(tilt))
        );
        camera.direction.nor();

        Vector3 right   = new Vector3(camera.direction).crs(camera.up).nor();
        Vector3 forward = new Vector3(camera.direction);
        forward.y = 0;
        forward.nor();

        // Joystick up (negative DY on screen) → forward; joystick right → strafe right
        camera.position.mulAdd(forward, -moveZ * speed * delta);
        camera.position.mulAdd(right,    moveX * speed * delta);

        // --- Fly mode (creative/freeplay) or gravity ---
        if (flying) {
            // Fly mode: vertical controlled by fly-up / fly-down buttons
            if (flyUp)   camera.position.y += FLY_VERTICAL_SPEED * delta;
            if (flyDown) camera.position.y -= FLY_VERTICAL_SPEED * delta;
            velocityY  = 0f;
            isOnGround = false;
        } else {
            // --- Vertical physics (gravity + collision) ---
            velocityY += GRAVITY * delta;
            float newY = camera.position.y + velocityY * delta;

            if (world != null) {
                int camX = (int) Math.floor(camera.position.x);
                int camZ = (int) Math.floor(camera.position.z);

                if (velocityY <= 0) {
                    // Falling – check ground
                    float feetNewY    = newY - PLAYER_HEIGHT;
                    int   blockBelowY = (int) Math.floor(feetNewY);
                    com.tessera.engine.server.block.Block blockBelow =
                            world.getBlock(camX, blockBelowY, camZ);
                    if (blockBelow != null && blockBelow.solid) {
                        // Land on top of the block
                        camera.position.y = blockBelowY + 1 + PLAYER_HEIGHT;
                        velocityY  = 0f;
                        isOnGround = true;
                    } else {
                        camera.position.y = newY;
                        isOnGround = false;
                    }
                } else {
                    // Rising – check ceiling
                    int headNewY = (int) Math.floor(newY + HEAD_CLEARANCE);
                    com.tessera.engine.server.block.Block blockAbove =
                            world.getBlock(camX, headNewY, camZ);
                    if (blockAbove != null && blockAbove.solid) {
                        velocityY = 0f;
                        // Stay at current Y
                    } else {
                        camera.position.y = newY;
                    }
                    isOnGround = false;
                }
            } else {
                // No world loaded – free-fly mode
                camera.position.y = newY;
            }
        }

        // --- Block break (hold on right side) ---
        if (lookPointer != -1 && lookHoldTime > TAP_THRESHOLD) {
            if (onBreakBlock != null) onBreakBlock.run();
        }
        lookHoldTime = (lookPointer != -1) ? lookHoldTime + delta : 0f;
    }

    /** Returns the current camera look sensitivity used for touch-drag. */
    private float sensitivity() {
        try {
            return BASE_SENSITIVITY
                    * SettingsScreen.prefs().getFloat(SettingsScreen.KEY_SENSITIVITY,
                                                      SettingsScreen.DEFAULT_SENSITIVITY);
        } catch (Throwable ignored) {
            return BASE_SENSITIVITY;
        }
    }

    // ── Overlay rendering ─────────────────────────────────────────────────────

    /**
     * Draws the on-screen virtual joystick indicator.
     * Must be called outside an active batch (opens and closes its own begin/end).
     */
    public void renderOverlay(SpriteBatch batch) {
        if (joystickBaseTexture == null) return;
        int   screenH  = Gdx.graphics.getHeight();
        float baseSize = joystickBaseTexture.getWidth();
        float thumbSize = joystickThumbTexture.getWidth();

        batch.begin();
        if (joystickPointer != -1) {
            // Dynamic: centred on the touch-down position
            float bx = joystickStartX - baseSize  / 2f;
            float by = screenH - joystickStartY - baseSize  / 2f;

            float clampedDX =  MathUtils.clamp(joystickDX, -JOYSTICK_RADIUS, JOYSTICK_RADIUS);
            float clampedDY = -MathUtils.clamp(joystickDY, -JOYSTICK_RADIUS, JOYSTICK_RADIUS); // flip Y
            float tx = joystickStartX + clampedDX - thumbSize / 2f;
            float ty = screenH - joystickStartY + clampedDY - thumbSize / 2f;

            batch.setColor(1f, 1f, 1f, 0.25f);
            batch.draw(joystickBaseTexture,  bx, by);
            batch.setColor(1f, 1f, 1f, 0.55f);
            batch.draw(joystickThumbTexture, tx, ty);
        } else {
            // Idle: faint guide ring at bottom-left
            float bx = 100f - baseSize  / 2f;
            float by = 100f - baseSize  / 2f;
            batch.setColor(1f, 1f, 1f, 0.12f);
            batch.draw(joystickBaseTexture, bx, by);
        }
        batch.setColor(Color.WHITE);
        batch.end();
    }

    // -------------------------------------------------------------------------

    private boolean isLeftSide(float x) {
        return x < Gdx.graphics.getWidth() * 0.5f;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isLeftSide(screenX)) {
            if (joystickPointer == -1) {
                joystickPointer = pointer;
                joystickStartX  = screenX;
                joystickStartY  = screenY;
                joystickDX = 0;
                joystickDY = 0;
            }
        } else {
            if (lookPointer == -1) {
                lookPointer = pointer;
                lookLastX   = screenX;
                lookLastY   = screenY;
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
            // Short tap on the right side → place block
            if (lookHoldTime <= TAP_THRESHOLD && onPlaceBlock != null) {
                onPlaceBlock.run();
            }
            lookPointer  = -1;
            lookHoldTime = 0f;
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
            float sens = sensitivity() * 0.01f;
            pan  += dx * sens; // drag right → turn right
            tilt += dy * sens; // drag down  → look down
            tilt  = Math.max(-1.5f, Math.min(1.5f, tilt));
            lookLastX = screenX;
            lookLastY = screenY;
        }
        return true;
    }

    @Override public boolean keyDown(int keycode)  { return false; }
    @Override public boolean keyUp(int keycode)    { return false; }
    @Override public boolean keyTyped(char c)      { return false; }
    @Override public boolean mouseMoved(int sx, int sy) { return false; }
    @Override public boolean scrolled(float ax, float ay) { return false; }
    @Override public boolean touchCancelled(int sx, int sy, int p, int b) { return false; }
}

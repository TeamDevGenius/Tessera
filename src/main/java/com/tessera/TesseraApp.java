package com.tessera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tessera.content.vanilla.TesseraGame;
import com.tessera.engine.SkinRegistry;
import com.tessera.engine.client.Client;
import com.tessera.engine.utils.resource.ResourceLister;
import com.tessera.window.GLFWWindow;
import org.lwjgl.glfw.GLFW;

/**
 * Main LibGDX ApplicationAdapter for Tessera.
 * Works on Android (via AndroidLauncher) and Desktop (via DesktopLauncher).
 */
public class TesseraApp extends ApplicationAdapter {

    public static final String TITLE = "Tessera";
    public static final String VERSION = "1.8.0";

    private boolean initialized = false;

    // Touch controls (Android)
    private ShapeRenderer shapeRenderer;
    private boolean isTouchDevice;

    // Touch control state
    // Touch control constants
    private static final float JOYSTICK_RADIUS = 80f;
    private static final float JOYSTICK_DEAD_ZONE = 0.2f;
    private static final float DIRECTION_THRESHOLD = 0.3f;
    private static final float JUMP_BUTTON_RADIUS = 55f;
    private int movementPointer = -1; // pointer id for the movement joystick
    private float joystickCenterX, joystickCenterY; // center of virtual joystick
    private float joystickDX, joystickDY; // current joystick delta

    private int lookPointer = -1; // pointer id for camera look (right side)
    private float lookLastX, lookLastY;

    // Camera sensitivity for touch
    private static final float LOOK_SENSITIVITY = 1.5f;

    @Override
    public void create() {
        Gdx.app.log(TITLE, "Starting version " + VERSION);
        isTouchDevice = Gdx.input.isPeripheralAvailable(com.badlogic.gdx.Input.Peripheral.MultitouchScreen);
        try {
            ResourceLister.init();
            Main.skins = new SkinRegistry();
            Main.game = new TesseraGame();
            Main.localClient = new Client(new String[0], VERSION, Main.game);
            initialized = true;
            Gdx.app.log(TITLE, "Game initialized successfully");

            if (isTouchDevice) {
                shapeRenderer = new ShapeRenderer();
                Gdx.input.setInputProcessor(new TouchInputProcessor());
                // Set initial window size from screen
                Main.localClient.window.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        } catch (Exception e) {
            Gdx.app.error(TITLE, "Failed to initialize game", e);
        }
    }

    @Override
    public void render() {
        if (!initialized) {
            Gdx.gl.glClearColor(0.2f, 0.1f, 0.1f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            return;
        }
        try {
            // Update touch movement keys
            if (isTouchDevice) updateTouchMovementKeys();

            Main.getClient().window.renderFrame();

            // Draw touch controls overlay on top
            if (isTouchDevice && shapeRenderer != null) {
                drawTouchControls();
            }
        } catch (Exception e) {
            Gdx.app.error(TITLE, "Render error", e);
        }
    }

    private void updateTouchMovementKeys() {
        float dx = joystickDX;
        float dy = joystickDY;
        float len = (float) Math.sqrt(dx * dx + dy * dy);

        // Remove all movement virtual keys
        GLFWWindow.virtualKeys.remove(GLFW.GLFW_KEY_W);
        GLFWWindow.virtualKeys.remove(GLFW.GLFW_KEY_S);
        GLFWWindow.virtualKeys.remove(GLFW.GLFW_KEY_A);
        GLFWWindow.virtualKeys.remove(GLFW.GLFW_KEY_D);

        if (movementPointer >= 0 && len > JOYSTICK_RADIUS * JOYSTICK_DEAD_ZONE) {
            float nx = dx / len;
            float ny = dy / len;
            if (ny < -DIRECTION_THRESHOLD) GLFWWindow.virtualKeys.add(GLFW.GLFW_KEY_W);
            if (ny > DIRECTION_THRESHOLD)  GLFWWindow.virtualKeys.add(GLFW.GLFW_KEY_S);
            if (nx < -DIRECTION_THRESHOLD) GLFWWindow.virtualKeys.add(GLFW.GLFW_KEY_A);
            if (nx > DIRECTION_THRESHOLD)  GLFWWindow.virtualKeys.add(GLFW.GLFW_KEY_D);
        }
    }

    private void drawTouchControls() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        // Joystick center is in the bottom-left quarter
        float jcx = w * 0.15f;
        float jcy = h * 0.25f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1f, 1f, 1f, 0.4f);

        // Outer circle
        shapeRenderer.circle(jcx, jcy, JOYSTICK_RADIUS);

        // Inner knob
        float knobX = jcx + (movementPointer >= 0 ? Math.max(-JOYSTICK_RADIUS, Math.min(JOYSTICK_RADIUS, joystickDX)) : 0);
        float knobY = jcy + (movementPointer >= 0 ? Math.max(-JOYSTICK_RADIUS, Math.min(JOYSTICK_RADIUS, joystickDY)) : 0);
        shapeRenderer.circle(knobX, knobY, JOYSTICK_RADIUS * 0.4f);

        // Jump button (bottom-right)
        shapeRenderer.circle(w * 0.87f, h * 0.18f, 40f);

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        if (initialized && Main.getClient() != null) {
            Main.getClient().window.framebufferResizeEvent(width, height);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (initialized && Main.getClient() != null) {
            Main.getClient().window.destroyWindow();
        }
    }

    /**
     * Handles touch input for Android, routing to virtual keys and camera control.
     */
    private class TouchInputProcessor implements InputProcessor {
        private static final float LEFT_ZONE_FRACTION = 0.4f; // left 40% of screen = movement

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            int h = Gdx.graphics.getHeight();
            int w = Gdx.graphics.getWidth();
            float gdxY = h - screenY; // flip Y

            // Check jump button (bottom-right circle)
            float jbx = w * 0.87f, jby = h * 0.18f;
            if (Math.sqrt((screenX - jbx) * (screenX - jbx) + (gdxY - jby) * (gdxY - jby)) < JUMP_BUTTON_RADIUS) {
                GLFWWindow.virtualKeys.add(GLFW.GLFW_KEY_SPACE);
                return true;
            }

            if (screenX < w * LEFT_ZONE_FRACTION) {
                // Left side: movement joystick
                if (movementPointer < 0) {
                    movementPointer = pointer;
                    joystickCenterX = screenX;
                    joystickCenterY = gdxY;
                    joystickDX = 0;
                    joystickDY = 0;
                }
            } else {
                // Right side: camera look
                if (lookPointer < 0) {
                    lookPointer = pointer;
                    lookLastX = screenX;
                    lookLastY = screenY;
                }
            }
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            int h = Gdx.graphics.getHeight();
            float gdxY = h - screenY;

            if (pointer == movementPointer) {
                joystickDX = screenX - joystickCenterX;
                joystickDY = gdxY - joystickCenterY;
                // Clamp to joystick radius
                float len = (float) Math.sqrt(joystickDX * joystickDX + joystickDY * joystickDY);
                if (len > JOYSTICK_RADIUS) {
                    joystickDX = joystickDX / len * JOYSTICK_RADIUS;
                    joystickDY = joystickDY / len * JOYSTICK_RADIUS;
                }
            } else if (pointer == lookPointer) {
                // Update camera pan/tilt via touch drag on the right side
                float dx = screenX - lookLastX;
                float dy = screenY - lookLastY;
                lookLastX = screenX;
                lookLastY = screenY;
                // Inject mouse delta into camera via the GLFW cursor position
                if (initialized && Main.getClient() != null) {
                    try {
                        // Apply touch look delta to the camera through the game's camera system
                        Client client = Main.getClient();
                        if (client.window != null && com.tessera.engine.client.ClientWindow.isInGamePage()) {
                            // Access player camera via reflection or direct reference
                            applyTouchLookDelta(dx * LOOK_SENSITIVITY, dy * LOOK_SENSITIVITY);
                        }
                    } catch (Exception ignored) {}
                }
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            int h = Gdx.graphics.getHeight();
            float gdxY = h - screenY;
            // Release jump button
            GLFWWindow.virtualKeys.remove(GLFW.GLFW_KEY_SPACE);

            if (pointer == movementPointer) {
                movementPointer = -1;
                joystickDX = 0;
                joystickDY = 0;
            } else if (pointer == lookPointer) {
                lookPointer = -1;
            }
            return true;
        }

        @Override public boolean keyDown(int keycode) { return false; }
        @Override public boolean keyUp(int keycode) { return false; }
        @Override public boolean keyTyped(char character) { return false; }
        @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
        @Override public boolean scrolled(float amountX, float amountY) { return false; }
        @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return touchUp(screenX, screenY, pointer, button);
        }
    }

    /** Apply touch look delta to the player camera pan/tilt. */
    private void applyTouchLookDelta(float dx, float dy) {
        try {
            Client client = Main.getClient();
            com.tessera.engine.client.visuals.gameScene.GameScene gs =
                    ((com.tessera.engine.client.ClientWindow) client.window).gameScene;
            if (gs != null) {
                gs.applyTouchLookDelta(dx, dy);
            }
        } catch (Exception ignored) {}
    }
}

package com.tessera.gdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.tessera.engine.server.world.World;

public class TouchControls implements InputProcessor {

    private static final float SENSITIVITY = 0.2f;
    private static final float DEAD_ZONE = 20f;
    private static final float MOVE_SPEED = 8f;
    private static final float GRAVITY = 20f;
    private static final float JUMP_VELOCITY = -12f;

    // Joystick (left side)
    int joystickPointer = -1;
    float joystickStartX, joystickStartY;
    float joystickDX, joystickDY;

    // Camera look (right side)
    private int lookPointer = -1;
    private float lookLastX, lookLastY;

    private float pan = 0f;
    private float tilt = 0f;

    // Gravity / vertical movement (engine uses +Y-down, so positive velocity = falling)
    private float velocityY = 0f;
    private boolean onGround = false;
    private World world;

    public TouchControls(PerspectiveCamera camera) {
        // camera reference kept available for future use
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /** Trigger a jump. */
    public void jump() {
        if (onGround) {
            velocityY = JUMP_VELOCITY;
            onGround = false;
        }
    }

    // --- Getters for HUD joystick visualization ---
    public float getJoystickStartX() { return joystickStartX; }
    public float getJoystickStartY() { return joystickStartY; }
    public float getJoystickDX()     { return joystickDX; }
    public float getJoystickDY()     { return joystickDY; }
    public boolean isJoystickActive() { return joystickPointer != -1; }

    public void update(float delta, PerspectiveCamera camera) {
        float moveX = 0, moveZ = 0;
        float dist = (float) Math.sqrt(joystickDX * joystickDX + joystickDY * joystickDY);
        if (dist > DEAD_ZONE) {
            moveX = joystickDX / dist;
            moveZ = joystickDY / dist;
        }

        camera.direction.set(
            (float) (Math.cos(pan) * Math.cos(tilt)),
            (float) Math.sin(tilt),
            (float) (Math.sin(pan) * Math.cos(tilt))
        );
        camera.direction.nor();

        Vector3 right = new Vector3(camera.direction).crs(camera.up).nor();
        Vector3 forward = new Vector3(camera.direction);
        forward.y = 0;
        forward.nor();

        camera.position.mulAdd(forward, -moveZ * MOVE_SPEED * delta);
        camera.position.mulAdd(right, moveX * MOVE_SPEED * delta);

        // Apply gravity (engine +Y-down: positive velocityY = falling downward,
        // so camera.position.y increases as the player falls toward higher block indices)
        if (world != null) {
            velocityY += GRAVITY * delta;
            camera.position.y += velocityY * delta;

            int bx = (int) Math.floor(camera.position.x);
            int bz = (int) Math.floor(camera.position.z);
            // "Below" the player in +Y-down space is Y+1 (higher index = lower world)
            int groundY = (int) Math.floor(camera.position.y) + 1;
            if (velocityY >= 0 && world.getBlock(bx, groundY, bz).solid) {
                camera.position.y = groundY - 1f;
                velocityY = 0f;
                onGround = true;
            } else {
                onGround = false;
            }
        }
    }

    private boolean isLeftSide(float x) {
        return x < Gdx.graphics.getWidth() * 0.5f;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isLeftSide(screenX)) {
            if (joystickPointer == -1) {
                joystickPointer = pointer;
                joystickStartX = screenX;
                joystickStartY = screenY;
                joystickDX = 0;
                joystickDY = 0;
            }
        } else {
            if (lookPointer == -1) {
                lookPointer = pointer;
                lookLastX = screenX;
                lookLastY = screenY;
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
            lookPointer = -1;
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
            pan += dx * SENSITIVITY * 0.01f;
            tilt -= dy * SENSITIVITY * 0.01f;
            tilt = Math.max(-1.5f, Math.min(1.5f, tilt));
            lookLastX = screenX;
            lookLastY = screenY;
        }
        return true;
    }

    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
}

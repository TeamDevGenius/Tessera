package com.tessera.window;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.tessera.engine.client.ClientWindow;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nuklear.NkVec2;

/**
 * Bridges LibGDX input events to the game's GLFW-style event handlers on
 * {@link ClientWindow}. Registered as the active {@link com.badlogic.gdx.InputProcessor}
 * in {@link com.tessera.TesseraApp#create()}.
 */
public class GdxInputAdapter extends InputAdapter {

    private final ClientWindow window;
    // Pre-allocated scroll vector to avoid per-frame allocation.
    // NkVec2 is a LibGDX-stub backed by a plain Java object (no native memory),
    // so it does not require explicit cleanup.
    private final NkVec2 scrollVec = NkVec2.create();

    public GdxInputAdapter(ClientWindow window) {
        this.window = window;
    }

    @Override
    public boolean keyDown(int keycode) {
        window.keyEvent(gdxToGlfw(keycode), 0, GLFW.GLFW_PRESS, 0);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        window.keyEvent(gdxToGlfw(keycode), 0, GLFW.GLFW_RELEASE, 0);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        window.mouseButtonEvent(button, GLFW.GLFW_PRESS, 0);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        window.mouseButtonEvent(button, GLFW.GLFW_RELEASE, 0);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scrollVec.x(amountX);
        scrollVec.y(amountY);
        window.mouseScrollEvent(scrollVec, amountX, amountY);
        return false;
    }

    /** Maps a LibGDX {@link Input.Keys} code to the equivalent GLFW key constant. */
    private static int gdxToGlfw(int key) {
        switch (key) {
            // Letters A–Z
            case Input.Keys.A: return GLFW.GLFW_KEY_A;
            case Input.Keys.B: return GLFW.GLFW_KEY_B;
            case Input.Keys.C: return GLFW.GLFW_KEY_C;
            case Input.Keys.D: return GLFW.GLFW_KEY_D;
            case Input.Keys.E: return GLFW.GLFW_KEY_E;
            case Input.Keys.F: return GLFW.GLFW_KEY_F;
            case Input.Keys.G: return GLFW.GLFW_KEY_G;
            case Input.Keys.H: return GLFW.GLFW_KEY_H;
            case Input.Keys.I: return GLFW.GLFW_KEY_I;
            case Input.Keys.J: return GLFW.GLFW_KEY_J;
            case Input.Keys.K: return GLFW.GLFW_KEY_K;
            case Input.Keys.L: return GLFW.GLFW_KEY_L;
            case Input.Keys.M: return GLFW.GLFW_KEY_M;
            case Input.Keys.N: return GLFW.GLFW_KEY_N;
            case Input.Keys.O: return GLFW.GLFW_KEY_O;
            case Input.Keys.P: return GLFW.GLFW_KEY_P;
            case Input.Keys.Q: return GLFW.GLFW_KEY_Q;
            case Input.Keys.R: return GLFW.GLFW_KEY_R;
            case Input.Keys.S: return GLFW.GLFW_KEY_S;
            case Input.Keys.T: return GLFW.GLFW_KEY_T;
            case Input.Keys.U: return GLFW.GLFW_KEY_U;
            case Input.Keys.V: return GLFW.GLFW_KEY_V;
            case Input.Keys.W: return GLFW.GLFW_KEY_W;
            case Input.Keys.X: return GLFW.GLFW_KEY_X;
            case Input.Keys.Y: return GLFW.GLFW_KEY_Y;
            case Input.Keys.Z: return GLFW.GLFW_KEY_Z;
            // Digit row 0–9
            case Input.Keys.NUM_0: return GLFW.GLFW_KEY_0;
            case Input.Keys.NUM_1: return GLFW.GLFW_KEY_1;
            case Input.Keys.NUM_2: return GLFW.GLFW_KEY_2;
            case Input.Keys.NUM_3: return GLFW.GLFW_KEY_3;
            case Input.Keys.NUM_4: return GLFW.GLFW_KEY_4;
            case Input.Keys.NUM_5: return GLFW.GLFW_KEY_5;
            case Input.Keys.NUM_6: return GLFW.GLFW_KEY_6;
            case Input.Keys.NUM_7: return GLFW.GLFW_KEY_7;
            case Input.Keys.NUM_8: return GLFW.GLFW_KEY_8;
            case Input.Keys.NUM_9: return GLFW.GLFW_KEY_9;
            // Special / navigation keys
            case Input.Keys.SPACE:         return GLFW.GLFW_KEY_SPACE;
            case Input.Keys.ENTER:         return GLFW.GLFW_KEY_ENTER;
            case Input.Keys.ESCAPE:        return GLFW.GLFW_KEY_ESCAPE;
            case Input.Keys.TAB:           return GLFW.GLFW_KEY_TAB;
            case Input.Keys.BACKSPACE:     return GLFW.GLFW_KEY_BACKSPACE;
            case Input.Keys.LEFT:          return GLFW.GLFW_KEY_LEFT;
            case Input.Keys.RIGHT:         return GLFW.GLFW_KEY_RIGHT;
            case Input.Keys.UP:            return GLFW.GLFW_KEY_UP;
            case Input.Keys.DOWN:          return GLFW.GLFW_KEY_DOWN;
            // Modifier keys
            case Input.Keys.SHIFT_LEFT:    return GLFW.GLFW_KEY_LEFT_SHIFT;
            case Input.Keys.SHIFT_RIGHT:   return GLFW.GLFW_KEY_RIGHT_SHIFT;
            case Input.Keys.CONTROL_LEFT:  return GLFW.GLFW_KEY_LEFT_CONTROL;
            case Input.Keys.CONTROL_RIGHT: return GLFW.GLFW_KEY_RIGHT_CONTROL;
            case Input.Keys.ALT_LEFT:      return GLFW.GLFW_KEY_LEFT_ALT;
            case Input.Keys.ALT_RIGHT:     return GLFW.GLFW_KEY_RIGHT_ALT;
            // Function keys F1–F12
            case Input.Keys.F1:  return GLFW.GLFW_KEY_F1;
            case Input.Keys.F2:  return GLFW.GLFW_KEY_F2;
            case Input.Keys.F3:  return GLFW.GLFW_KEY_F3;
            case Input.Keys.F4:  return GLFW.GLFW_KEY_F4;
            case Input.Keys.F5:  return GLFW.GLFW_KEY_F5;
            case Input.Keys.F6:  return GLFW.GLFW_KEY_F6;
            case Input.Keys.F7:  return GLFW.GLFW_KEY_F7;
            case Input.Keys.F8:  return GLFW.GLFW_KEY_F8;
            case Input.Keys.F9:  return GLFW.GLFW_KEY_F9;
            case Input.Keys.F10: return GLFW.GLFW_KEY_F10;
            case Input.Keys.F11: return GLFW.GLFW_KEY_F11;
            case Input.Keys.F12: return GLFW.GLFW_KEY_F12;
            default:             return GLFW.GLFW_KEY_UNKNOWN;
        }
    }
}

package com.tessera.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tessera.window.utils.ValueSmoother;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.awt.image.BufferedImage;

/**
 * Window abstraction layer backed by LibGDX instead of GLFW.
 * Replaces the LWJGL GLFW implementation for Android/LibGDX compatibility.
 */
public abstract class GLFWWindow {

    /** Used by BlockIconRenderer to synchronize window context creation. */
    public static final Object windowCreateLock = new Object();

    protected int width, height, display_width, display_height;
    protected Vector2d cursor = new Vector2d();

    public static void initGLFW() {}
    public static void endGLFW() {}

    public void createWindow(String title, int w, int h) {
        this.width = w; this.height = h;
        this.display_width = w; this.display_height = h;
    }

    public boolean windowShouldClose() {
        return false;
    }

    public void showWindow() {}
    public void hideWindow() {}

    public void setTitle(String title) {
        if (Gdx.graphics != null) Gdx.graphics.setTitle(title);
    }

    public void setWindowPos(int x, int y) {}

    /**
     * Translate a GLFW key code to a LibGDX Input.Keys code and check if it is pressed.
     */
    public boolean isKeyPressed(int glfwKey) {
        if (Gdx.input == null) return false;
        int gdxKey = glfwToGdxKey(glfwKey);
        if (gdxKey < 0) return false;
        return Gdx.input.isKeyPressed(gdxKey);
    }

    public boolean isMouseButtonPressed(int glfwButton) {
        if (Gdx.input == null) return false;
        // GLFW: 0=left, 1=right, 2=middle  →  LibGDX: 0=left, 1=right, 2=middle
        return Gdx.input.isButtonPressed(glfwButton);
    }

    public void destroyWindow() {
        if (Gdx.app != null) Gdx.app.exit();
    }

    public boolean isFullscreen() {
        if (Gdx.graphics == null) return false;
        return Gdx.graphics.isFullscreen();
    }

    public void disableFullscreen() {
        if (Gdx.graphics != null) Gdx.graphics.setWindowedMode(width, height);
    }

    public void enableFullscreen(float resolutionScale) {
        if (Gdx.graphics != null) {
            com.badlogic.gdx.Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(mode);
        }
    }

    public int getWidth()  { return Gdx.graphics != null ? Gdx.graphics.getWidth()  : width;  }
    public int getHeight() { return Gdx.graphics != null ? Gdx.graphics.getHeight() : height; }
    public int getDisplay_width()  { return getWidth(); }
    public int getDisplay_height() { return getHeight(); }
    public long getWindow() { return 1L; }

    public double getCursorPosX() { return Gdx.input != null ? Gdx.input.getX() : 0; }
    public double getCursorPosY() { return Gdx.input != null ? Gdx.input.getY() : 0; }
    public Vector2d getCursorVector() { return cursor; }

    public void centerWindow() {}

    public void startFrame() {}

    public void endFrame() {
        tickMPF();
    }

    private float frameDeltaSec = 1f / 60f;
    public float smoothFrameDeltaSec = 1f / 60f;
    private ValueSmoother smoothed = new ValueSmoother(20);
    private double msPerFrame = 16.6;
    private int nbFrames = 0;
    private double lastTime = 0;
    private double updateIntervalSec = 1.0;
    private int updateIntervalMS = 1000;

    public void setMpfUpdateInterval(int milliseconds) {
        updateIntervalMS = milliseconds;
        updateIntervalSec = (double) updateIntervalMS / 1000;
    }

    public double getMsPerFrame() { return msPerFrame; }
    public void onMPFUpdate() {}

    public float frameDeltaSec() { return frameDeltaSec; }

    protected void tickMPF() {
        if (Gdx.graphics == null) return;
        frameDeltaSec = Gdx.graphics.getDeltaTime();
        smoothed.add(frameDeltaSec);
        smoothFrameDeltaSec = smoothed.getAverage();

        nbFrames++;
        double currentTime = System.nanoTime() / 1e9;
        if (lastTime == 0) lastTime = currentTime;
        if (currentTime - lastTime >= updateIntervalSec) {
            msPerFrame = updateIntervalMS / ((double) nbFrames);
            onMPFUpdate();
            nbFrames = 0;
            lastTime += updateIntervalSec;
        }
    }

    public abstract void framebufferResizeEvent(int width, int height);

    public boolean windowIsFocused() { return true; }

    public static void printDebugsEnabled(boolean enabled) {}

    public void setIcon(java.io.InputStream icon32, java.io.InputStream icon16, java.io.InputStream icon8) {}
    public void setIcon(java.io.InputStream... icons) {}

    public void getWindowPos(java.nio.IntBuffer x, java.nio.IntBuffer y) {
        if (x != null) x.put(0, 0);
        if (y != null) y.put(0, 0);
    }

    public BufferedImage readPixelsOfWindow() { return null; }

    // -----------------------------------------------------------------------
    // GLFW → LibGDX key mapping
    // -----------------------------------------------------------------------
    private static int glfwToGdxKey(int glfw) {
        // Letters A-Z: GLFW 65-90 = LibGDX Input.Keys.A-Z (51-76)
        if (glfw >= GLFW.GLFW_KEY_A && glfw <= GLFW.GLFW_KEY_Z) {
            return Input.Keys.A + (glfw - GLFW.GLFW_KEY_A);
        }
        // Digits 0-9: GLFW 48-57 = LibGDX Input.Keys.NUM_0-9 (7-16)
        if (glfw >= GLFW.GLFW_KEY_0 && glfw <= GLFW.GLFW_KEY_9) {
            return Input.Keys.NUM_0 + (glfw - GLFW.GLFW_KEY_0);
        }
        switch (glfw) {
            case GLFW.GLFW_KEY_SPACE:        return Input.Keys.SPACE;
            case GLFW.GLFW_KEY_ENTER:        return Input.Keys.ENTER;
            case GLFW.GLFW_KEY_ESCAPE:       return Input.Keys.ESCAPE;
            case GLFW.GLFW_KEY_BACKSPACE:    return Input.Keys.BACKSPACE;
            case GLFW.GLFW_KEY_TAB:          return Input.Keys.TAB;
            case GLFW.GLFW_KEY_LEFT_SHIFT:   return Input.Keys.SHIFT_LEFT;
            case GLFW.GLFW_KEY_RIGHT_SHIFT:  return Input.Keys.SHIFT_RIGHT;
            case GLFW.GLFW_KEY_LEFT_CONTROL: return Input.Keys.CONTROL_LEFT;
            case GLFW.GLFW_KEY_RIGHT_CONTROL:return Input.Keys.CONTROL_RIGHT;
            case GLFW.GLFW_KEY_LEFT_ALT:     return Input.Keys.ALT_LEFT;
            case GLFW.GLFW_KEY_RIGHT_ALT:    return Input.Keys.ALT_RIGHT;
            case GLFW.GLFW_KEY_UP:           return Input.Keys.UP;
            case GLFW.GLFW_KEY_DOWN:         return Input.Keys.DOWN;
            case GLFW.GLFW_KEY_LEFT:         return Input.Keys.LEFT;
            case GLFW.GLFW_KEY_RIGHT:        return Input.Keys.RIGHT;
            case GLFW.GLFW_KEY_F1:           return Input.Keys.F1;
            case GLFW.GLFW_KEY_F2:           return Input.Keys.F2;
            case GLFW.GLFW_KEY_F3:           return Input.Keys.F3;
            case GLFW.GLFW_KEY_F4:           return Input.Keys.F4;
            case GLFW.GLFW_KEY_F5:           return Input.Keys.F5;
            case GLFW.GLFW_KEY_F6:           return Input.Keys.F6;
            case GLFW.GLFW_KEY_F7:           return Input.Keys.F7;
            case GLFW.GLFW_KEY_F8:           return Input.Keys.F8;
            case GLFW.GLFW_KEY_F9:           return Input.Keys.F9;
            case GLFW.GLFW_KEY_F10:          return Input.Keys.F10;
            case GLFW.GLFW_KEY_F11:          return Input.Keys.F11;
            case GLFW.GLFW_KEY_F12:          return Input.Keys.F12;
            case GLFW.GLFW_KEY_DELETE:       return Input.Keys.DEL;
            case GLFW.GLFW_KEY_INSERT:       return Input.Keys.INSERT;
            case GLFW.GLFW_KEY_HOME:         return Input.Keys.HOME;
            case GLFW.GLFW_KEY_END:          return Input.Keys.END;
            case GLFW.GLFW_KEY_PAGE_UP:      return Input.Keys.PAGE_UP;
            case GLFW.GLFW_KEY_PAGE_DOWN:    return Input.Keys.PAGE_DOWN;
            case GLFW.GLFW_KEY_MINUS:        return Input.Keys.MINUS;
            case GLFW.GLFW_KEY_EQUAL:        return Input.Keys.EQUALS;
            case GLFW.GLFW_KEY_LEFT_BRACKET: return Input.Keys.LEFT_BRACKET;
            case GLFW.GLFW_KEY_RIGHT_BRACKET:return Input.Keys.RIGHT_BRACKET;
            case GLFW.GLFW_KEY_SLASH:        return Input.Keys.SLASH;
            case GLFW.GLFW_KEY_BACKSLASH:    return Input.Keys.BACKSLASH;
            case GLFW.GLFW_KEY_PERIOD:       return Input.Keys.PERIOD;
            case GLFW.GLFW_KEY_COMMA:        return Input.Keys.COMMA;
            case GLFW.GLFW_KEY_APOSTROPHE:   return Input.Keys.APOSTROPHE;
            case GLFW.GLFW_KEY_SEMICOLON:    return Input.Keys.SEMICOLON;
            default:                         return -1;
        }
    }
}

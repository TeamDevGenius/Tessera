package com.tessera.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.tessera.window.utils.ValueSmoother;
import org.joml.Vector2d;

import java.util.HashSet;
import java.util.Set;

/**
 * Window abstraction layer backed by LibGDX instead of GLFW.
 * Replaces the LWJGL GLFW implementation for Android/LibGDX compatibility.
 */
public abstract class GLFWWindow {

    /** Used by BlockIconRenderer to synchronize window context creation. */
    public static final Object windowCreateLock = new Object();

    /** Virtual keys pressed by touch controls (key codes are GLFW key values). */
    public static final Set<Integer> virtualKeys = new HashSet<>();

    protected int width, height, display_width, display_height;
    protected Vector2d cursor = new Vector2d();

    public static void initGLFW() {}
    public static void endGLFW() {}

    public void createWindow(String title, int w, int h) {
        this.width = w; this.height = h;
        this.display_width = w; this.display_height = h;
    }

    /** Called when the screen is resized (LibGDX resize callback). */
    public void resize(int w, int h) {
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

    public boolean isKeyPressed(int key) {
        // Check virtual touch keys first (Android touch controls)
        if (virtualKeys.contains(key)) return true;
        // Map GLFW key code to LibGDX Keys
        if (Gdx.input == null) return false;
        int gdxKey = glfwKeyToGdx(key);
        return gdxKey != -1 && Gdx.input.isKeyPressed(gdxKey);
    }

    /** Convert GLFW key code to LibGDX Input.Keys code. */
    private static int glfwKeyToGdx(int glfwKey) {
        // Printable ASCII keys (A-Z) - GLFW uses uppercase ASCII
        if (glfwKey >= 65 && glfwKey <= 90) return glfwKey; // A-Z match LibGDX
        if (glfwKey >= 48 && glfwKey <= 57) return glfwKey; // 0-9 match LibGDX
        switch (glfwKey) {
            case 32: return Keys.SPACE;
            case 256: return Keys.ESCAPE;
            case 257: return Keys.ENTER;
            case 258: return Keys.TAB;
            case 259: return Keys.DEL;
            case 262: return Keys.RIGHT;
            case 263: return Keys.LEFT;
            case 264: return Keys.DOWN;
            case 265: return Keys.UP;
            case 290: return Keys.F1;
            case 291: return Keys.F2;
            case 292: return Keys.F3;
            case 293: return Keys.F4;
            case 294: return Keys.F5;
            case 295: return Keys.F6;
            case 296: return Keys.F7;
            case 297: return Keys.F8;
            case 298: return Keys.F9;
            case 299: return Keys.F10;
            case 300: return Keys.F11;
            case 301: return Keys.F12;
            case 340: return Keys.SHIFT_LEFT;
            case 344: return Keys.SHIFT_RIGHT;
            case 341: return Keys.CONTROL_LEFT;
            case 345: return Keys.CONTROL_RIGHT;
            case 342: return Keys.ALT_LEFT;
            case 346: return Keys.ALT_RIGHT;
            default: return -1;
        }
    }

    public boolean isMouseButtonPressed(int button) {
        if (Gdx.input == null) return false;
        // GLFW: 0=left, 1=right, 2=middle → LibGDX: Buttons.LEFT=0, RIGHT=1, MIDDLE=2
        return Gdx.input.isButtonPressed(button);
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

    public int getWidth() { return Gdx.graphics != null ? Gdx.graphics.getWidth() : width; }
    public int getHeight() { return Gdx.graphics != null ? Gdx.graphics.getHeight() : height; }
    public int getDisplay_width() { return getWidth(); }
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

    public Object readPixelsOfWindow() { return null; }
}

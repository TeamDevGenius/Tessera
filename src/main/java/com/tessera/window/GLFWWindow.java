package com.tessera.window;

import com.badlogic.gdx.Gdx;
import com.tessera.window.utils.ValueSmoother;
import org.joml.Vector2d;

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

    public boolean isKeyPressed(int key) {
        return false;
    }

    public boolean isMouseButtonPressed(int button) {
        return false;
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

    public BufferedImage readPixelsOfWindow() { return null; }
}

package com.tessera.engine.client.visuals.skybox;

import com.badlogic.gdx.graphics.Pixmap;
import com.tessera.engine.client.ClientWindow;
import com.tessera.engine.server.entity.Entity;
import com.tessera.engine.server.world.World;
import com.tessera.engine.utils.resource.ResourceUtils;
import com.tessera.window.utils.IOUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SkyBackground {

    private static final double UPDATE_SPEED = 0.0000005f;
    double offset;
    double lightness;
    Vector3f defaultTint = new Vector3f(1, 1, 1);
    Vector3f defaultSkyColor = new Vector3f(0.5f, 0.5f, 0.5f);
    SkyBoxMesh skyBoxMesh;
    SkyBoxShader skyBoxShader;
    /** Sky image pixel data stored as ARGB int[], row-major. */
    private int[] skyPixels;
    private int skyImageWidth;
    private int skyImageHeight;
    ClientWindow mainWindow;
    World world;

    public SkyBackground(ClientWindow mainWindow, World world) throws IOException {
        skyBoxMesh = new SkyBoxMesh();
        this.world = world;
        this.mainWindow = mainWindow;
        skyBoxMesh.loadFromOBJ(ResourceUtils.file("weather\\skybox.obj"));

        File texture = ResourceUtils.file("weather\\skybox.png");
        skyBoxMesh.setTexture(texture);
        loadSkyImagePixels(texture);
        skyBoxShader = new SkyBoxShader();
    }

    private void loadSkyImagePixels(File textureFile) {
        try (FileInputStream fis = new FileInputStream(textureFile)) {
            byte[] data = IOUtil.inputStreamToByteBuffer(fis, 1024).array();
            Pixmap pixmap = new Pixmap(data, 0, data.length);
            // Ensure RGBA8888 format for consistent pixel reading
            Pixmap rgba;
            if (pixmap.getFormat() == Pixmap.Format.RGBA8888) {
                rgba = pixmap;
            } else {
                rgba = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
                rgba.drawPixmap(pixmap, 0, 0);
                pixmap.dispose();
            }
            skyImageWidth = rgba.getWidth();
            skyImageHeight = rgba.getHeight();
            skyPixels = new int[skyImageWidth * skyImageHeight];
            java.nio.ByteBuffer pb = rgba.getPixels();
            pb.rewind();
            for (int i = 0; i < skyPixels.length; i++) {
                int r = pb.get() & 0xFF;
                int g = pb.get() & 0xFF;
                int b = pb.get() & 0xFF;
                int a = pb.get() & 0xFF;
                // Store as ARGB
                skyPixels[i] = (a << 24) | (r << 16) | (g << 8) | b;
            }
            rgba.dispose();
        } catch (Exception e) {
            // Fall back: no sky image, keep defaults
            skyPixels = null;
            skyImageWidth = 1;
            skyImageHeight = 1;
        }
    }

    private int getSkyRGB(int x, int y) {
        if (skyPixels == null) return 0xFF808080;
        x = Math.max(0, Math.min(skyImageWidth - 1, x));
        y = Math.max(0, Math.min(skyImageHeight - 1, y));
        return skyPixels[y * skyImageWidth + x];
    }

    public double getLightness() {
        return lightness;
    }

    private double calculateLightLevel(double x) {
        int rgb = getSkyRGB((int) (skyImageWidth * getSkyTexturePan()), skyImageHeight - 1);
        lightness = (double) (rgb & 0xFF) / 255;
        if (lightness < 0.18) lightness = 0.18;
        return lightness;
    }

    public void update() {
        //Move the sky texture
        if (world.data.data.alwaysDayMode) setSkyTexturePan(0);
        else updateTexturePan();

        //Calculate the light level
        calculateLightLevel(getSkyTexturePan());

        //Calculate the sky color
        int skyColor = getSkyRGB((int) (skyImageWidth * getSkyTexturePan()), skyImageHeight - 2);
        int red = (skyColor >> 16) & 0xFF;
        int green = (skyColor >> 8) & 0xFF;
        int blue = skyColor & 0xFF;
        defaultSkyColor.set(red / 255f, green / 255f, blue / 255f);

        if (defaultSkyColor.x > defaultSkyColor.z) { //If red is more dominant than blue
            float redDifference = (defaultSkyColor.x - defaultSkyColor.z) * 0.3f; //Choose how much % should be tinted red
            defaultTint.set(lightness + redDifference, lightness, lightness);
        } else defaultTint.set(lightness, lightness, lightness);
        world.chunkShader.setTintAndFogColor(defaultSkyColor, defaultTint);
        if (Entity.shader != null) {
            Entity.shader.setTint(defaultTint);
        }
        if (Entity.arrayTextureShader != null) {
            Entity.arrayTextureShader.setTint(defaultTint);
        }
    }

    private void updateTexturePan() {
        double time = System.currentTimeMillis() * UPDATE_SPEED;
        setSkyTexturePan((time + offset) % 1.0);
    }


    public void setTimeOfDay(double start) {
        double time = System.currentTimeMillis() * UPDATE_SPEED;
        //Take the normalized time plus start minus current time
        offset = Math.floor(time) + start - time;
        updateTexturePan();
    }

    public float getTimeOfDay(){
        return (float) getSkyTexturePan();
    }

    private double getSkyTexturePan() {
        return world.data.data.dayTexturePan;
    }

    private void setSkyTexturePan(double val) {
        //By writing and reading directly from world data, we never have to worry about loading/saving
        world.data.data.dayTexturePan = val;
    }

    public void draw(Matrix4f projection, Matrix4f view) {
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        skyBoxShader.bind();
        skyBoxShader.updateMatrix(projection, view);
        skyBoxShader.loadFloat(skyBoxShader.uniform_cycle_value, (float) getSkyTexturePan());
        skyBoxMesh.draw();
        GL30.glEnable(GL30.GL_DEPTH_TEST);
    }

}

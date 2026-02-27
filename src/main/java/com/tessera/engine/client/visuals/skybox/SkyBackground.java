package com.tessera.engine.client.visuals.skybox;

import com.badlogic.gdx.graphics.Pixmap;
import com.tessera.engine.client.ClientWindow;
import com.tessera.engine.server.entity.Entity;
import com.tessera.engine.server.world.World;
import com.tessera.engine.utils.resource.ResourceLoader;
import com.tessera.engine.utils.resource.ResourceUtils;
import com.tessera.window.utils.IOUtil;
import com.tessera.window.utils.obj.OBJLoader;
import com.tessera.window.utils.texture.TextureUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class SkyBackground {

    private static final double UPDATE_SPEED = 0.0000005f;
    double offset;
    double lightness;
    Vector3f defaultTint = new Vector3f(1, 1, 1);
    Vector3f defaultSkyColor = new Vector3f(0.5f, 0.5f, 0.5f);
    SkyBoxMesh skyBoxMesh;
    SkyBoxShader skyBoxShader;
    Pixmap skyImage;
    ClientWindow mainWindow;
    World world;

    public SkyBackground(ClientWindow mainWindow, World world) throws IOException {
        skyBoxMesh = new SkyBoxMesh();
        this.world = world;
        this.mainWindow = mainWindow;

        ResourceLoader loader = new ResourceLoader();

        // Load skybox OBJ from classpath (fallback to filesystem for desktop)
        InputStream objStream = loader.getResourceAsStream("weather/skybox.obj");
        if (objStream == null) {
            File objFile = ResourceUtils.file("weather/skybox.obj");
            objStream = new FileInputStream(objFile);
        }
        skyBoxMesh.loadFromOBJ(OBJLoader.loadModel(objStream));

        // Load skybox texture and Pixmap from classpath
        byte[] pngBytes = null;
        InputStream pngStream = loader.getResourceAsStream("weather/skybox.png");
        if (pngStream != null) {
            pngBytes = IOUtil.inputStreamToBytes(pngStream);
        } else {
            File texFile = ResourceUtils.file("weather/skybox.png");
            try (FileInputStream fis = new FileInputStream(texFile)) {
                pngBytes = IOUtil.inputStreamToBytes(fis);
            }
        }
        ByteBuffer buf = ByteBuffer.allocateDirect(pngBytes.length);
        buf.put(pngBytes).flip();
        com.tessera.window.utils.texture.Texture tex = TextureUtils.loadTexture(buf, true);
        skyBoxMesh.setTextureID(tex != null ? tex.id : 0);
        skyImage = new Pixmap(pngBytes, 0, pngBytes.length);

        skyBoxShader = new SkyBoxShader();
    }

    public double getLightness() {
        return lightness;
    }

    private double calculateLightLevel(double x) {
        // Pixmap.getPixel returns RGBA8888; extract blue channel (bits 8-15) for lightness
        // matching the original code which extracted the blue channel from ARGB BufferedImage.getRGB()
        int pixel = skyImage.getPixel((int) (skyImage.getWidth() * getSkyTexturePan()), skyImage.getHeight() - 1);
        lightness = ((pixel >> 8) & 0xFF) / 255.0;
        if (lightness < 0.18) lightness = 0.18;
        return lightness;
    }

    public void update() {
        //Move the sky texture
        if (world.data.data.alwaysDayMode) setSkyTexturePan(0);
        else updateTexturePan();

        //Calculate the light level
        calculateLightLevel(getSkyTexturePan());

        //Calculate the sky color (Pixmap RGBA8888 → extract R, G, B)
        int skyColor = skyImage.getPixel((int) (skyImage.getWidth() * getSkyTexturePan()), skyImage.getHeight() - 2);
        int red   = (skyColor >> 24) & 0xFF;
        int green = (skyColor >> 16) & 0xFF;
        int blue  = (skyColor >> 8)  & 0xFF;
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

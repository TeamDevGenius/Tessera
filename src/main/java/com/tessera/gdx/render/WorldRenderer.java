package com.tessera.gdx.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.tessera.engine.server.entity.Entity;
import com.tessera.engine.server.players.Player;
import com.tessera.engine.server.world.World;
import com.tessera.engine.server.world.chunk.Chunk;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class WorldRenderer {

    /** Day-cycle speed: matches original SkyBackground UPDATE_SPEED (0.0000005 / ms = 0.0005 / s). */
    private static final float DAY_CYCLE_SPEED = 0.0005f;
    private static final float MIN_LIGHTNESS   = 0.18f;

    /**
     * Key-frame sky colours at t = 0.0 (midnight), 0.25 (dawn), 0.5 (noon), 0.75 (dusk).
     * Layout: [ topR, topG, topB,  botR, botG, botB ]
     */
    private static final float[][] SKY_KEYS = {
        {0.02f, 0.03f, 0.12f,  0.05f, 0.05f, 0.18f},  // midnight
        {0.12f, 0.22f, 0.55f,  1.00f, 0.62f, 0.30f},  // dawn
        {0.10f, 0.32f, 0.78f,  0.52f, 0.74f, 1.00f},  // noon
        {0.18f, 0.08f, 0.35f,  0.90f, 0.35f, 0.15f},  // dusk
    };

    private final PerspectiveCamera camera;
    private final World world;

    // Chunk rendering
    private ShaderProgram chunkShader;
    private int frameCount = 0;
    private int mvpUniform;
    private final Matrix4f jomlProjection = new Matrix4f();
    private final Matrix4f jomlView       = new Matrix4f();

    // Sky rendering
    private ShaderProgram skyShader;
    private Mesh skyMesh;
    private final Matrix4 skyIdentityMVP = new Matrix4(); // permanent identity
    private final float[] skyColorBuf    = new float[6];  // reused each frame

    // Entity / player wireframe rendering
    private ShapeRenderer shapeRenderer;

    public WorldRenderer(PerspectiveCamera camera, World world) {
        this.camera = camera;
        this.world  = world;
        ShaderProgram.pedantic = false;

        // Chunk shader
        String vert = Gdx.files.internal("shaders/chunk.vert").readString();
        String frag = Gdx.files.internal("shaders/chunk.frag").readString();
        chunkShader = new ShaderProgram(vert, frag);
        if (!chunkShader.isCompiled()) {
            Gdx.app.error("WorldRenderer", "Chunk shader error: " + chunkShader.getLog());
        }
        mvpUniform = chunkShader.getUniformLocation("u_MVP");

        // Sky shader + fullscreen NDC quad
        initSkyResources();

        // Wireframe renderer for entities and players
        shapeRenderer = new ShapeRenderer();
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void render(float delta) {
        if (world == null || chunkShader == null || !chunkShader.isCompiled()) return;

        // 1. Advance time of day and compute sky gradient colours
        float timeOfDay = advanceTimeOfDay(delta);
        interpolateSkyColors(timeOfDay, skyColorBuf);

        // 2. Dynamic clear color + clear (so the colour matches sky horizon on load)
        Gdx.gl.glClearColor(skyColorBuf[3], skyColorBuf[4], skyColorBuf[5], 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // 3. Sky background (rendered before chunks so terrain appears in front)
        renderSky();

        // 4. World chunks
        renderChunks();

        // 5. Entity and player wireframes
        renderEntitiesAndPlayers();
    }

    public void dispose() {
        if (chunkShader    != null) { chunkShader.dispose();    chunkShader    = null; }
        if (skyShader      != null) { skyShader.dispose();      skyShader      = null; }
        if (skyMesh        != null) { skyMesh.dispose();        skyMesh        = null; }
        if (shapeRenderer  != null) { shapeRenderer.dispose();  shapeRenderer  = null; }
    }

    // -------------------------------------------------------------------------
    // Sky
    // -------------------------------------------------------------------------

    private void initSkyResources() {
        try {
            String sv = Gdx.files.internal("shaders/sky.vert").readString();
            String sf = Gdx.files.internal("shaders/sky.frag").readString();
            skyShader = new ShaderProgram(sv, sf);
            if (!skyShader.isCompiled()) {
                Gdx.app.error("WorldRenderer", "Sky shader error: " + skyShader.getLog());
                skyShader = null;
                return;
            }
        } catch (Exception e) {
            Gdx.app.error("WorldRenderer", "Could not load sky shaders: " + e.getMessage());
            skyShader = null;
            return;
        }

        // Fullscreen NDC quad — v_dir = a_position so v_dir.y goes −1..+1 for gradient
        skyMesh = new Mesh(true, 6, 0,
                new VertexAttribute(Usage.Position, 3, "a_position"));
        skyMesh.setVertices(new float[]{
            -1f, -1f, 0f,
             1f, -1f, 0f,
             1f,  1f, 0f,
            -1f, -1f, 0f,
             1f,  1f, 0f,
            -1f,  1f, 0f
        });
    }

    private void renderSky() {
        if (skyShader == null || skyMesh == null) return;

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(false);

        skyShader.bind();
        // Identity MVP keeps the quad in NDC space (fills the entire viewport)
        skyShader.setUniformMatrix("u_MVP", skyIdentityMVP);
        skyShader.setUniformf("u_skyTop",    skyColorBuf[0], skyColorBuf[1], skyColorBuf[2], 1f);
        skyShader.setUniformf("u_skyBottom", skyColorBuf[3], skyColorBuf[4], skyColorBuf[5], 1f);
        skyMesh.render(skyShader, GL20.GL_TRIANGLES);

        Gdx.gl.glDepthMask(true);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }

    // -------------------------------------------------------------------------
    // Chunks
    // -------------------------------------------------------------------------

    private void renderChunks() {
        Vector3f playerPos = new Vector3f(camera.position.x, camera.position.y, camera.position.z);
        world.gdxUpdateChunks(playerPos, frameCount++);

        jomlProjection.set(camera.projection.val);
        jomlView.set(camera.view.val);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, world.getBlockTextureID());

        chunkShader.bind();
        chunkShader.setUniformi("u_texture", 0);

        float viewDist = world.getViewDistance();
        chunkShader.setUniformf("u_fogStart", viewDist - 32f);
        chunkShader.setUniformf("u_fogEnd",   viewDist + 32f);
        // Fog colour tracks the sky horizon so distant terrain blends into the sky
        chunkShader.setUniformf("u_fogColor",
                skyColorBuf[3], skyColorBuf[4], skyColorBuf[5], 1f);

        world.chunks.values().forEach(chunk -> {
            if (chunk.getGenerationStatus() == Chunk.GEN_COMPLETE) {
                chunk.updateMVP(jomlProjection, jomlView);
                chunk.mvp.sendToShader(0, mvpUniform);
                chunk.meshes.opaqueMesh.draw(false);
                if (!chunk.meshes.transMesh.isEmpty()) {
                    chunk.meshes.transMesh.draw(false);
                }
            }
        });

        Gdx.gl.glUseProgram(0);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
    }

    // -------------------------------------------------------------------------
    // Entities and players
    // -------------------------------------------------------------------------

    private void renderEntitiesAndPlayers() {
        if (shapeRenderer == null) return;

        float viewDist = world.getViewDistance();
        float camX = camera.position.x;
        float camY = camera.position.y;
        float camZ = camera.position.z;

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);

        // World entities (green)
        shapeRenderer.setColor(0.2f, 0.9f, 0.2f, 1f);
        try {
            for (Entity e : new ArrayList<>(world.entities.values())) {
                Vector3f p = e.worldPosition;
                if (p.distance(camX, camY, camZ) <= viewDist) {
                    drawEntityBox(p.x, p.y, p.z, 0.6f, 1.8f, 0.6f);
                }
            }
        } catch (Exception ignored) { /* skip on ConcurrentModificationException */ }

        // Other players (cyan)
        shapeRenderer.setColor(0.2f, 0.9f, 0.9f, 1f);
        try {
            for (Player player : new ArrayList<>(world.players)) {
                Vector3f p = player.worldPosition;
                if (p.distance(camX, camY, camZ) <= viewDist) {
                    drawEntityBox(p.x, p.y, p.z, 0.6f, 1.8f, 0.6f);
                }
            }
        } catch (Exception ignored) { /* skip on ConcurrentModificationException */ }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    /** Draw a 3-D wireframe box with its minimum corner at (x, y, z). */
    private void drawEntityBox(float x, float y, float z, float w, float h, float d) {
        float x2 = x + w, y2 = y + h, z2 = z + d;
        // bottom face
        shapeRenderer.line(x,  y,  z,  x2, y,  z);
        shapeRenderer.line(x2, y,  z,  x2, y,  z2);
        shapeRenderer.line(x2, y,  z2, x,  y,  z2);
        shapeRenderer.line(x,  y,  z2, x,  y,  z);
        // top face
        shapeRenderer.line(x,  y2, z,  x2, y2, z);
        shapeRenderer.line(x2, y2, z,  x2, y2, z2);
        shapeRenderer.line(x2, y2, z2, x,  y2, z2);
        shapeRenderer.line(x,  y2, z2, x,  y2, z);
        // verticals
        shapeRenderer.line(x,  y,  z,  x,  y2, z);
        shapeRenderer.line(x2, y,  z,  x2, y2, z);
        shapeRenderer.line(x2, y,  z2, x2, y2, z2);
        shapeRenderer.line(x,  y,  z2, x,  y2, z2);
    }

    // -------------------------------------------------------------------------
    // Day / night helpers
    // -------------------------------------------------------------------------

    /**
     * Advances world.data.data.dayTexturePan by delta and returns the current
     * time-of-day value [0, 1).  Falls back to noon (0.5) if world data is unavailable.
     */
    private float advanceTimeOfDay(float delta) {
        try {
            if (world.data != null && world.data.data != null) {
                if (!world.data.data.alwaysDayMode) {
                    world.data.data.dayTexturePan =
                            (world.data.data.dayTexturePan + delta * DAY_CYCLE_SPEED) % 1.0f;
                }
                return (float) world.data.data.dayTexturePan;
            }
        } catch (Exception ignored) {}
        return 0.5f; // default: noon
    }

    /**
     * Linearly interpolates between the four key-frame sky colours and writes the
     * result into {@code out}: [topR, topG, topB, botR, botG, botB].
     */
    private static void interpolateSkyColors(float t, float[] out) {
        float scaled = (t % 1.0f) * 4.0f;
        int   i      = (int) scaled % 4;
        int   j      = (i + 1) % 4;
        float frac   = scaled - (int) scaled;
        float[] a = SKY_KEYS[i];
        float[] b = SKY_KEYS[j];
        for (int k = 0; k < 6; k++) {
            out[k] = a[k] + frac * (b[k] - a[k]);
        }
    }
}

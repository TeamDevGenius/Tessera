package com.tessera.gdx.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.tessera.engine.server.world.World;
import com.tessera.engine.server.world.chunk.Chunk;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WorldRenderer {

    private final PerspectiveCamera camera;
    private final World world;
    private ShaderProgram chunkShader;
    private int frameCount = 0;
    private int mvpUniform;

    private final Matrix4f jomlProjection = new Matrix4f();
    private final Matrix4f jomlView = new Matrix4f();

    public WorldRenderer(PerspectiveCamera camera, World world) {
        this.camera = camera;
        this.world = world;
        ShaderProgram.pedantic = false;
        String vert = Gdx.files.internal("shaders/chunk.vert").readString();
        String frag = Gdx.files.internal("shaders/chunk.frag").readString();
        chunkShader = new ShaderProgram(vert, frag);
        if (!chunkShader.isCompiled()) {
            Gdx.app.error("WorldRenderer", "Chunk shader error: " + chunkShader.getLog());
        }
        mvpUniform = chunkShader.getUniformLocation("u_MVP");
    }

    public void render(float delta) {
        if (world == null || chunkShader == null || !chunkShader.isCompiled()) return;

        Vector3f playerPos = new Vector3f(camera.position.x, camera.position.y, camera.position.z);
        world.gdxUpdateChunks(playerPos, frameCount++);

        // Convert GDX camera matrices to JOML
        jomlProjection.set(camera.projection.val);
        jomlView.set(camera.view.val);
        // Flip Y axis so the engine's +Y-down world maps correctly onto +Y-up OpenGL space
        jomlView.scale(1, -1, 1);

        // Enable depth test and backface culling
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_BACK);

        // Bind texture array
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, world.getBlockTextureID());

        chunkShader.bind();
        chunkShader.setUniformi("u_texture", 0);

        float viewDist = world.getViewDistance();
        chunkShader.setUniformf("u_fogStart", viewDist - 32f);
        chunkShader.setUniformf("u_fogEnd", viewDist + 32f);
        chunkShader.setUniformf("u_fogColor", 0.4f, 0.65f, 0.9f, 1f);

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

    public void dispose() {
        if (chunkShader != null) {
            chunkShader.dispose();
            chunkShader = null;
        }
    }
}

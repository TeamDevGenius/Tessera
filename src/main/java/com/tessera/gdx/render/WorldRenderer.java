package com.tessera.gdx.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class WorldRenderer {

    private final PerspectiveCamera camera;
    private ShaderProgram chunkShader;

    public WorldRenderer(PerspectiveCamera camera) {
        this.camera = camera;
        ShaderProgram.pedantic = false;
        String vert = Gdx.files.internal("shaders/chunk.vert").readString();
        String frag = Gdx.files.internal("shaders/chunk.frag").readString();
        chunkShader = new ShaderProgram(vert, frag);
        if (!chunkShader.isCompiled()) {
            Gdx.app.error("WorldRenderer", "Chunk shader error: " + chunkShader.getLog());
        }
    }

    public void render() {
        // World rendering happens here once game systems are initialized
    }

    public void dispose() {
        if (chunkShader != null) {
            chunkShader.dispose();
            chunkShader = null;
        }
    }
}

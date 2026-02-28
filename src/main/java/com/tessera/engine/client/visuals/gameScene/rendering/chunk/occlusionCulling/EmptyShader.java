package com.tessera.engine.client.visuals.gameScene.rendering.chunk.occlusionCulling;


import com.tessera.window.render.Shader;

import java.io.IOException;

public class EmptyShader extends Shader {

    static final String vertShader =
            "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "attribute vec3 vertexPosition_modelspace;\n" +
            "uniform mat4 MVP;\n" +
            "void main(){\n" +
            "    gl_Position = MVP * vec4(vertexPosition_modelspace, 1.0);\n" +
            "}\n";

    static final String fragShader =
            "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "void main(){\n" +
            "    gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);\n" +
            "}\n";

    public final int mvpUniform;

    public EmptyShader() {
        try {
            init(vertShader, fragShader);
            mvpUniform = getUniformLocation("MVP");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void bindAttributes() {

    }
}

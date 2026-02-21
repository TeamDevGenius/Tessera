package com.tessera.window.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * Shader wrapper using LibGDX ShaderProgram.
 * Replaces the LWJGL-based shader implementation for Android/LibGDX compatibility.
 */
public class Shader {

    private ShaderProgram program;

    public Shader() {}

    public Shader(String vertSrc, String fragSrc) throws IOException {
        init(vertSrc, fragSrc);
    }

    public Shader(File vert, File frag) throws IOException {
        init(readFile(vert), readFile(frag));
    }

    public void init(String vertSrc, String fragSrc) throws IOException {
        ShaderProgram.pedantic = false;
        program = new ShaderProgram(vertSrc, fragSrc);
        if (!program.isCompiled()) {
            throw new IOException("Shader compilation failed: " + program.getLog());
        }
        bindAttributes();
    }

    public void init(File vert, File frag) throws IOException {
        init(readFile(vert), readFile(frag));
    }

    private static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public void bindAttributes() {}

    public void bind() {
        if (program != null) program.bind();
    }

    public static void unbind() {}

    public int getUniformLocation(String uniformName) {
        if (program == null) return -1;
        return program.getUniformLocation(uniformName);
    }

    public void bindAttribute(int attribute, String variableName) {
        if (program == null) return;
        program.bind();
        Gdx.gl20.glBindAttribLocation(program.getHandle(), attribute, variableName);
    }

    public void loadFloat(int location, float value) {
        if (program == null) return;
        program.bind();
        program.setUniformf(location, value);
    }

    public void loadInt(int location, int value) {
        if (program == null) return;
        program.bind();
        program.setUniformi(location, value);
    }

    public void loadVec3f(int location, Vector3f vector) {
        if (program == null) return;
        program.bind();
        program.setUniformf(location, vector.x, vector.y, vector.z);
    }

    public void loadVec4f(int location, Vector4f vector) {
        if (program == null) return;
        program.bind();
        program.setUniformf(location, vector.x, vector.y, vector.z, vector.w);
    }

    public void loadVec2f(int location, Vector2f vector) {
        if (program == null) return;
        program.bind();
        program.setUniformf(location, vector.x, vector.y);
    }

    public void loadVec3i(int location, Vector3i vector) {
        if (program == null) return;
        program.bind();
        program.setUniformi(location, vector.x, vector.y, vector.z);
    }

    public void loadVec2i(int location, Vector2i vector) {
        if (program == null) return;
        program.bind();
        program.setUniformi(location, vector.x, vector.y);
    }

    public void loadMatrix4f(int location, FloatBuffer buffer) {
        if (program == null) return;
        float[] values = new float[16];
        buffer.get(values);
        buffer.rewind();
        program.bind();
        Gdx.gl20.glUniformMatrix4fv(location, 1, false, values, 0);
    }

    public void loadMatrix4f(int location, boolean transpose, FloatBuffer buffer) {
        if (program == null) return;
        float[] values = new float[16];
        buffer.get(values);
        buffer.rewind();
        program.bind();
        Gdx.gl20.glUniformMatrix4fv(location, 1, transpose, values, 0);
    }

    public void delete() {
        if (program != null) {
            program.dispose();
            program = null;
        }
    }

    public int getID() {
        return program != null ? program.getHandle() : 0;
    }

    public ShaderProgram getShaderProgram() {
        return program;
    }
}

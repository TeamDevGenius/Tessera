package com.tessera.engine.client.visuals.gameScene.rendering.chunk.mesh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.tessera.engine.client.visuals.gameScene.rendering.Mesh;
import com.tessera.engine.client.visuals.gameScene.rendering.chunk.ChunkShader;

import java.nio.IntBuffer;

public class CompactMesh extends Mesh {

    protected int vbo = -1;
    protected int textureID;
    protected int vertLength;
    static final int VALUES_PER_VERTEX = 3;

    public boolean isEmpty() {
        return vertLength == 0;
    }

    public void makeEmpty() {
        vertLength = 0;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    private void ensureVBO() {
        if (vbo == -1) {
            IntBuffer tmp = IntBuffer.allocate(1);
            Gdx.gl.glGenBuffers(1, tmp);
            vbo = tmp.get(0);
        }
    }

    public void sendBuffersToGPU(IntBuffer data) {
        vertLength = data.capacity() / VALUES_PER_VERTEX;
        ensureVBO();
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
        Gdx.gl.glBufferData(GL20.GL_ARRAY_BUFFER, data.capacity() * Integer.BYTES, data, GL20.GL_STATIC_DRAW);
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }

    public void sendBuffersToGPU(IntBuffer data, int size) {
        vertLength = size / VALUES_PER_VERTEX;
        ensureVBO();
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
        Gdx.gl.glBufferData(GL20.GL_ARRAY_BUFFER, size * Integer.BYTES, data, GL20.GL_STATIC_DRAW);
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }

    public void sendBuffersToGPU(int[] data, int size) {
        vertLength = size / VALUES_PER_VERTEX;
        ensureVBO();
        IntBuffer buf = IntBuffer.wrap(data, 0, size);
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
        Gdx.gl.glBufferData(GL20.GL_ARRAY_BUFFER, size * Integer.BYTES, buf, GL20.GL_STATIC_DRAW);
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }

    public void delete() {
        if (vbo != -1) {
            IntBuffer tmp = IntBuffer.wrap(new int[]{vbo});
            Gdx.gl.glDeleteBuffers(1, tmp);
            vbo = -1;
        }
    }

    public void draw(boolean wireframe) {
        if (isEmpty() || vbo == -1) return;

        GL30 gl30 = Gdx.gl30;
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, vbo);
        gl30.glVertexAttribIPointer(0, VALUES_PER_VERTEX, GL20.GL_INT, VALUES_PER_VERTEX * Integer.BYTES, 0);
        Gdx.gl.glEnableVertexAttribArray(0);

        if (textureID != 0) {
            Gdx.gl.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, textureID);
        }

        Gdx.gl.glDrawArrays(GL20.GL_TRIANGLES, 0, vertLength);

        Gdx.gl.glDisableVertexAttribArray(0);
        Gdx.gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
    }

    public void draw(ChunkShader shader, boolean wireframe) {
        draw(wireframe);
    }

    @Override
    public String toString() {
        return "CompactMesh{vbo=" + vbo + ", texture=" + textureID + ", vertLength=" + vertLength + '}';
    }
}

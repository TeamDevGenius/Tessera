package com.tessera.window.render;

import com.badlogic.gdx.Gdx;
import org.joml.Matrix4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * MVP (Model-View-Projection) matrix helper.
 * Extends Matrix4f for JOML API compatibility.
 * Uses LibGDX GL20 for shader upload.
 */
public class MVP extends Matrix4f {

    private FloatBuffer buffer;

    public MVP() {
        super();
        buffer = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void update(final Matrix4f projection, final Matrix4f view, final Matrix4f model) {
        identity().mul(projection).mul(view).mul(model);
        get(buffer);
    }

    public void update(final Matrix4f projection, final Matrix4f view) {
        identity().mul(projection).mul(view);
        get(buffer);
    }

    public void update(Matrix4f model) {
        set(model);
        get(buffer);
    }

    public void update() {
        get(buffer);
    }

    public void sendToShader(int shaderID, int uniformID) {
        if (Gdx.gl20 != null) {
            float[] vals = new float[16];
            buffer.rewind();
            buffer.get(vals);
            buffer.rewind();
            Gdx.gl20.glUniformMatrix4fv(uniformID, 1, false, vals, 0);
        }
    }

    public void updateAndSendToShader(int shaderID, int uniformID) {
        update();
        sendToShader(shaderID, uniformID);
    }

    public FloatBuffer getBuffer() {
        return buffer;
    }
}

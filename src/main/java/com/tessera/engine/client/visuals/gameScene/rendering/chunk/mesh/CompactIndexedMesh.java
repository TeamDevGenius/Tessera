/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tessera.engine.client.visuals.gameScene.rendering.chunk.mesh;

import com.tessera.engine.client.visuals.gameScene.rendering.Mesh;
import org.lwjgl.opengl.*;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author zipCoder933
 */
public class CompactIndexedMesh extends Mesh {

    private int vao, vbo, indexVbo, textureID, vertLength;
    public boolean empty;
    final static int VALUES_PER_VERTEX = 3;

    public CompactIndexedMesh() {
        vao = GL30.glGenVertexArrays();//Every chunk gets its own VAO
        vbo = GL30.glGenBuffers();
        indexVbo = GL30.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        // Vertex data is packed as 3 unsigned integers per vertex (uvec3 in shader)
        GL30.glVertexAttribIPointer(0, VALUES_PER_VERTEX, GL11.GL_UNSIGNED_INT, 0, 0);
        GL20.glEnableVertexAttribArray(0); //Enables the vertex attribute array at index 0.
        GL30.glBindVertexArray(0);
    }

    /**
     * @param textureID the textureID to set
     */
    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public void sendBuffersToGPU(int[] vertex, int[] indicies) {
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex, GL15.GL_STATIC_DRAW); //send data to the GPU

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVbo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicies, GL15.GL_STATIC_DRAW);

        vertLength = vertex.length / VALUES_PER_VERTEX;
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void sendBuffersToGPU(IntBuffer vertex, IntBuffer indicies) {
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex, GL15.GL_STATIC_DRAW); //send data to the GPU

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexVbo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicies, GL15.GL_STATIC_DRAW);

        vertLength = vertex.capacity() / VALUES_PER_VERTEX;
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void delete() {
        GL30.glDeleteVertexArrays(vao);
        GL30.glDeleteBuffers(vbo);
    }

    public void draw(boolean wireframe) {
        GL30.glBindVertexArray(vao);

        if (wireframe) {
            GL11.glLineWidth(1); //Set the line width
            GL11.glBindTexture(GL33.GL_TEXTURE_2D_ARRAY, 0);
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // Enable wireframe mode
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertLength); //We have to specify how many verticies we want
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); // Disable wireframe mode
        }

        GL11.glBindTexture(GL33.GL_TEXTURE_2D_ARRAY, textureID);//required to assign texture to mesh
         GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertLength); //We have to specify how many verticies we want
    }
}

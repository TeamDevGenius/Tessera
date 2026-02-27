package com.tessera.content.vanilla.skins;

import com.tessera.engine.server.players.Player;
import com.tessera.engine.client.player.Skin;
import com.tessera.engine.client.visuals.gameScene.rendering.entity.EntityMesh;
import com.tessera.engine.utils.ErrorHandler;
import com.tessera.engine.utils.resource.ResourceLoader;
import com.tessera.engine.utils.resource.ResourceUtils;
import com.tessera.window.utils.texture.TextureUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FoxSkin extends Skin {

    EntityMesh mesh;
    String texture;
    int textureID;

    public FoxSkin(Player position, String texture) {
        super("fox (" + texture + ")", position);
        this.texture = texture;
    }

    public void init() {
        mesh = new EntityMesh();
        try {
            ResourceLoader loader = new ResourceLoader();
            // Load OBJ from classpath (fallback to filesystem)
            InputStream objStream = loader.getResourceAsStream("skins/fox/body.obj");
            if (objStream == null) {
                objStream = new FileInputStream(ResourceUtils.file("skins/fox/body.obj"));
            }
            mesh.loadFromOBJ(objStream);
            // Load texture from classpath (fallback to filesystem)
            String texPath = "skins/fox/" + texture + ".png";
            InputStream texStream = loader.getResourceAsStream(texPath);
            if (texStream != null) {
                textureID = TextureUtils.loadTextureFromResource(texPath, false).id;
            } else {
                textureID = TextureUtils.loadTextureFromFile(
                        ResourceUtils.file("skins/fox/" + texture + ".png"), false).id;
            }
        } catch (IOException e) {
            ErrorHandler.report(e);
        }
    }

    @Override
    public void render() {
        modelMatrix.translate(0, player.aabb.size.y, 0);
        modelMatrix.update();
        modelMatrix.sendToShader(shader.getID(), shader.uniform_modelMatrix);
        mesh.draw(false, textureID);
    }
}

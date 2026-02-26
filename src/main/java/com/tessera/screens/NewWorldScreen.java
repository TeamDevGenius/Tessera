package com.tessera.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.content.vanilla.terrain.DevTerrain;
import com.tessera.content.vanilla.terrain.FlatTerrain;
import com.tessera.content.vanilla.terrain.defaultTerrain.DefaultTerrain;
import com.tessera.engine.client.Client;
import com.tessera.engine.server.GameMode;
import com.tessera.engine.server.world.Terrain;
import com.tessera.engine.server.world.data.WorldData;

import java.util.Random;

/**
 * Screen for creating a new world.
 */
public class NewWorldScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    private TextField nameField;
    private TextField seedField;
    private Label gameModeLabel;
    private Label terrainLabel;
    private GameMode currentMode = GameMode.ADVENTURE;
    private int terrainIndex = 0;

    private static final Terrain[] TERRAINS = {
        new DefaultTerrain(),
        new FlatTerrain(),
        new DevTerrain()
    };

    public NewWorldScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin  = ScreenUtils.createSkin();

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.add(new Label("NEW WORLD", skin, "title")).padBottom(20).row();

        // World name
        root.add(new Label("World Name:", skin)).left().padBottom(4).row();
        nameField = new TextField("My World", skin);
        root.add(nameField).width(300).padBottom(12).row();

        // Seed
        root.add(new Label("Seed (leave blank for random):", skin)).left().padBottom(4).row();
        seedField = new TextField("", skin);
        root.add(seedField).width(300).padBottom(12).row();

        // Game Mode toggle
        root.add(new Label("Game Mode:", skin)).left().padBottom(4).row();
        gameModeLabel = new Label(currentMode.toString(), skin);
        TextButton gameModeBtn = new TextButton("Change: " + currentMode, skin);
        gameModeBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                int next = (currentMode.ordinal() + 1) % GameMode.values().length;
                currentMode = GameMode.values()[next];
                gameModeBtn.setText("Change: " + currentMode);
            }
        });
        root.add(gameModeBtn).width(300).height(40).padBottom(12).row();

        // Terrain toggle
        root.add(new Label("Terrain:", skin)).left().padBottom(4).row();
        TextButton terrainBtn = new TextButton("Terrain: " + TERRAINS[terrainIndex].name, skin);
        terrainBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                terrainIndex = (terrainIndex + 1) % TERRAINS.length;
                terrainBtn.setText("Terrain: " + TERRAINS[terrainIndex].name);
            }
        });
        root.add(terrainBtn).width(300).height(40).padBottom(20).row();

        // Create button
        TextButton createBtn = new TextButton("CREATE", skin);
        createBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                createWorld();
            }
        });
        root.add(createBtn).width(300).height(48).padBottom(10).row();

        // Back button
        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        root.add(backBtn).width(300).height(40).row();
    }

    private void createWorld() {
        String worldName = nameField.getText().trim();
        if (worldName.isEmpty()) worldName = "World";

        String seedText = seedField.getText().trim();
        int seed = seedText.isEmpty() ? new Random().nextInt(Integer.MAX_VALUE) : parseSeed(seedText);

        Terrain terrain = TERRAINS[terrainIndex];
        int worldSize = 5; // Default world size in chunks

        try {
            // Initialize the client if not already done
            if (TesseraApp.client == null) {
                TesseraApp.client = new Client(new String[0], TesseraApp.VERSION, TesseraApp.game);
            }

            boolean created = TesseraApp.client.makeNewWorld(worldName, worldSize, terrain, seed, currentMode);
            if (created) {
                // Load the world
                WorldData worldData = new WorldData();
                worldData.load(com.tessera.engine.server.world.WorldsHandler.worldFile(worldName));
                app.setScreen(new LoadingScreen(app, worldData));
            }
        } catch (Exception ex) {
            Gdx.app.error("NewWorldScreen", "Failed to create world: " + ex.getMessage(), ex);
            // Show error – for now just go back
            app.setScreen(new MainMenuScreen(app));
        }
    }

    private int parseSeed(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return text.hashCode();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() {
        if (stage != null) { stage.dispose(); stage = null; }
        if (skin  != null) { skin.dispose();  skin  = null; }
    }
}

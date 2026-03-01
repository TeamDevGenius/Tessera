package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.server.GameMode;
import com.tessera.engine.server.world.Terrain;
import com.tessera.gdx.GdxTerrainRegistry;
import com.tessera.gdx.ui.UiTheme;

/**
 * Full-screen New World creation UI.
 * Provides world name, game-mode toggle, terrain selector, world options, and seed.
 */
public class NewWorldScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    // Form state
    private GameMode gameMode = GameMode.ADVENTURE;
    private int terrainIndex = 0;
    private TextField nameField;
    private TextField seedField;

    // Labels updated when state changes
    private TextButton gameModeBtn;
    private TextButton terrainBtn;

    public NewWorldScreen(TesseraApp app) {
        this.app = app;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = UiTheme.buildSkin();

        Table root = new Table();
        root.setFillParent(true);
        root.top().center();
        root.pad(20);

        Label title = new Label("NEW WORLD",
                new Label.LabelStyle(skin.getFont("default-font"), UiTheme.TITLE_COLOR));
        root.add(title).padBottom(20).colspan(2).row();

        // World Name
        root.add(lbl("World Name:")).left().padRight(10);
        nameField = new TextField("New World", skin);
        root.add(nameField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(10).colspan(2).row();

        // Game Mode toggle button
        root.add(lbl("Game Mode:")).left().padRight(10);
        gameModeBtn = new TextButton(gameMode.toString(), skin);
        gameModeBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                int next = (gameMode.ordinal() + 1) % GameMode.values().length;
                gameMode = GameMode.values()[next];
                gameModeBtn.setText(gameMode.toString());
            }
        });
        root.add(gameModeBtn).expandX().fillX().height(UiTheme.BTN_HEIGHT).row();

        root.add().height(10).colspan(2).row();

        // Terrain selector
        root.add(lbl("Terrain:")).left().padRight(10);
        terrainBtn = new TextButton(currentTerrainName(), skin);
        terrainBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                if (!GdxTerrainRegistry.terrains.isEmpty()) {
                    terrainIndex = (terrainIndex + 1) % GdxTerrainRegistry.terrains.size();
                }
                terrainBtn.setText(currentTerrainName());
            }
        });
        root.add(terrainBtn).expandX().fillX().height(UiTheme.BTN_HEIGHT).row();

        root.add().height(10).colspan(2).row();

        // Seed input
        root.add(lbl("Seed (0 = random):")).left().padRight(10);
        seedField = new TextField("0", skin);
        seedField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        root.add(seedField).expandX().fillX().height(UiTheme.MIN_TOUCH).row();

        root.add().height(20).colspan(2).row();

        // BACK / CREATE buttons
        TextButton backBtn = new TextButton("BACK", skin);
        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                app.setScreen(new MainMenuScreen(app));
            }
        });
        TextButton createBtn = new TextButton("CREATE", skin);
        createBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                createWorld();
            }
        });

        Table btnRow = new Table();
        btnRow.add(backBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT).padRight(10);
        btnRow.add(createBtn).width(UiTheme.BTN_WIDTH / 2).height(UiTheme.BTN_HEIGHT);
        root.add(btnRow).colspan(2).row();

        ScrollPane scroll = new ScrollPane(root, skin);
        scroll.setFillParent(true);
        stage.addActor(scroll);
    }

    private void createWorld() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) name = "New World";

        int seed = 0;
        try {
            String seedText = seedField.getText().trim();
            if (!seedText.isEmpty()) seed = Integer.parseInt(seedText);
        } catch (NumberFormatException ignored) {
            seed = 0;
        }

        String terrainName = currentTerrainName();
        app.setScreen(new LoadingScreen(app, name, seed, terrainName));
    }

    private String currentTerrainName() {
        if (!GdxTerrainRegistry.terrains.isEmpty() && terrainIndex < GdxTerrainRegistry.terrains.size()) {
            return GdxTerrainRegistry.terrains.get(terrainIndex).name;
        }
        return "Default Terrain";
    }

    private Label lbl(String text) {
        return new Label(text, skin);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                UiTheme.BG_COLOR.r, UiTheme.BG_COLOR.g, UiTheme.BG_COLOR.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (stage != null) { stage.dispose(); stage = null; }
        if (skin  != null) { skin.dispose();  skin  = null; }
    }
}

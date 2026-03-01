package com.tessera.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.server.world.WorldsHandler;
import com.tessera.engine.server.world.data.WorldData;
import com.tessera.engine.utils.resource.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainMenuScreen implements Screen {

    private final TesseraApp app;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(TesseraApp app) {
        this.app = app;
    }

    private Texture makePixel(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Button style
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.WHITE;
        Pixmap btnPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        btnPm.setColor(0.2f, 0.4f, 0.8f, 1f);
        btnPm.fill();
        Texture btnTex = new Texture(btnPm);
        btnPm.dispose();
        btnStyle.up = new TextureRegionDrawable(btnTex);
        Pixmap btnDownPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        btnDownPm.setColor(0.1f, 0.25f, 0.6f, 1f);
        btnDownPm.fill();
        Texture btnDownTex = new Texture(btnDownPm);
        btnDownPm.dispose();
        btnStyle.down = new TextureRegionDrawable(btnDownTex);
        btnStyle.over = btnStyle.down;
        skin.add("default", btnStyle);

        Label.LabelStyle lblStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", lblStyle);

        // Window style required by Dialog (which extends Window)
        Pixmap winBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        winBgPm.setColor(0.1f, 0.1f, 0.25f, 0.97f);
        winBgPm.fill();
        Texture winBgTex = new Texture(winBgPm);
        winBgPm.dispose();
        Window.WindowStyle windowStyle = new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(winBgTex));
        skin.add("default", windowStyle);

        Table root = new Table();
        root.setFillParent(true);

        Label title = new Label(TesseraApp.TITLE, new Label.LabelStyle(font, Color.CYAN));
        root.add(title).padBottom(8).row();

        Label version = new Label("v" + TesseraApp.VERSION, lblStyle);
        root.add(version).padBottom(40).row();

        TextButton newWorldBtn = new TextButton("New World", skin);
        newWorldBtn.getLabel().setFontScale(1.5f);
        root.add(newWorldBtn).width(300).height(70).padBottom(15).row();

        TextButton loadWorldBtn = new TextButton("Load World", skin);
        loadWorldBtn.getLabel().setFontScale(1.5f);
        root.add(loadWorldBtn).width(300).height(70).padBottom(15).row();

        TextButton multiplayerBtn = new TextButton("Multiplayer", skin);
        multiplayerBtn.getLabel().setFontScale(1.5f);
        root.add(multiplayerBtn).width(300).height(70).padBottom(15).row();

        TextButton settingsBtn = new TextButton("Settings", skin);
        settingsBtn.getLabel().setFontScale(1.5f);
        root.add(settingsBtn).width(300).height(70).padBottom(15).row();

        TextButton exitBtn = new TextButton("Exit", skin);
        exitBtn.getLabel().setFontScale(1.5f);
        root.add(exitBtn).width(300).height(70).row();

        stage.addActor(root);

        newWorldBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showNewWorldDialog();
            }
        });

        loadWorldBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showLoadWorldDialog();
            }
        });

        multiplayerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMultiplayerDialog();
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSettingsDialog();
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    private void showSettingsDialog() {
        Dialog dialog = new Dialog("Settings", skin) {
            @Override
            protected void result(Object object) { }
        };
        dialog.text("Settings coming soon.");
        dialog.button("OK", true);
        dialog.show(stage);
    }

    private void showNewWorldDialog() {
        BitmapFont font = skin.getFont("default-font");
        Label.LabelStyle lblStyle = skin.get("default", Label.LabelStyle.class);

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = font;
        tfStyle.fontColor = Color.WHITE;
        Pixmap tfBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        tfBgPm.setColor(0.2f, 0.2f, 0.35f, 1f);
        tfBgPm.fill();
        tfStyle.background = new TextureRegionDrawable(new Texture(tfBgPm));
        tfBgPm.dispose();
        tfStyle.cursor = tfStyle.background;

        SelectBox.SelectBoxStyle sbStyle = new SelectBox.SelectBoxStyle();
        sbStyle.font = font;
        sbStyle.fontColor = Color.WHITE;
        Pixmap sbBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        sbBgPm.setColor(0.2f, 0.2f, 0.35f, 1f);
        sbBgPm.fill();
        sbStyle.background = new TextureRegionDrawable(new Texture(sbBgPm));
        sbBgPm.dispose();
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.YELLOW;
        listStyle.fontColorUnselected = Color.WHITE;
        Pixmap selPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        selPm.setColor(0.3f, 0.3f, 0.6f, 1f);
        selPm.fill();
        listStyle.selection = new TextureRegionDrawable(new Texture(selPm));
        selPm.dispose();
        Pixmap listBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        listBgPm.setColor(0.15f, 0.15f, 0.3f, 1f);
        listBgPm.fill();
        listStyle.background = new TextureRegionDrawable(new Texture(listBgPm));
        listBgPm.dispose();
        ScrollPane.ScrollPaneStyle spStyle = new ScrollPane.ScrollPaneStyle();
        sbStyle.listStyle = listStyle;
        sbStyle.scrollStyle = spStyle;

        final TextField nameField = new TextField("My World", tfStyle);
        final TextField seedField = new TextField("", tfStyle);
        seedField.setMessageText("(random)");

        final SelectBox<String> terrainBox = new SelectBox<>(sbStyle);
        terrainBox.setItems("default", "flat");

        Table content = new Table();
        content.pad(10);
        content.add(new Label("World name:", lblStyle)).left().padBottom(4).row();
        content.add(nameField).width(220).padBottom(10).row();
        content.add(new Label("Seed (optional):", lblStyle)).left().padBottom(4).row();
        content.add(seedField).width(220).padBottom(10).row();
        content.add(new Label("Terrain:", lblStyle)).left().padBottom(4).row();
        content.add(terrainBox).width(220).padBottom(10).row();

        Dialog dialog = new Dialog("New World", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    String worldName = nameField.getText().trim();
                    if (worldName.isEmpty()) worldName = "My World";
                    int seed;
                    try {
                        seed = Integer.parseInt(seedField.getText().trim());
                    } catch (NumberFormatException e) {
                        seed = new Random().nextInt();
                    }
                    String terrain = terrainBox.getSelected();
                    app.setScreen(new LoadingScreen(app, worldName, seed, terrain));
                }
            }
        };
        dialog.getContentTable().add(content).row();
        dialog.button("Create", true);
        dialog.button("Cancel", false);
        dialog.show(stage);
    }

    private void showLoadWorldDialog() {
        // Ensure ResourceUtils is initialized so WorldsHandler can locate the worlds dir
        if (ResourceUtils.WORLDS_DIR == null) {
            try {
                ResourceUtils.initialize(false, null);
            } catch (Exception e) {
                Gdx.app.error("MainMenuScreen", "ResourceUtils init failed", e);
            }
        }

        ArrayList<WorldData> worlds = new ArrayList<>();
        if (ResourceUtils.WORLDS_DIR != null && ResourceUtils.WORLDS_DIR.exists()) {
            try {
                WorldsHandler.listWorlds(worlds);
            } catch (IOException e) {
                Gdx.app.error("MainMenuScreen", "Failed to list worlds", e);
            }
        }

        if (worlds.isEmpty()) {
            Dialog dialog = new Dialog("Load World", skin) {
                @Override
                protected void result(Object object) {}
            };
            dialog.text("No saved worlds found.");
            dialog.button("OK", true);
            dialog.show(stage);
            return;
        }

        BitmapFont font = skin.getFont("default-font");
        Label.LabelStyle lblStyle = skin.get("default", Label.LabelStyle.class);

        Array<String> worldNames = new Array<>();
        for (WorldData wd : worlds) {
            worldNames.add(wd.getName());
        }

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.YELLOW;
        listStyle.fontColorUnselected = Color.WHITE;
        Pixmap selPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        selPm.setColor(0.3f, 0.3f, 0.6f, 1f);
        selPm.fill();
        listStyle.selection = new TextureRegionDrawable(new Texture(selPm));
        selPm.dispose();
        Pixmap listBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        listBgPm.setColor(0.15f, 0.15f, 0.3f, 1f);
        listBgPm.fill();
        listStyle.background = new TextureRegionDrawable(new Texture(listBgPm));
        listBgPm.dispose();

        final List<String> worldList = new List<>(listStyle);
        worldList.setItems(worldNames);
        ScrollPane scrollPane = new ScrollPane(worldList);

        final ArrayList<WorldData> finalWorlds = worlds;
        Dialog dialog = new Dialog("Load World", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    String selected = worldList.getSelected();
                    if (selected != null) {
                        WorldData wd = null;
                        for (WorldData w : finalWorlds) {
                            if (w.getName().equals(selected)) { wd = w; break; }
                        }
                        String terrain = (wd != null && wd.getTerrain() != null) ? wd.getTerrain() : "default";
                        int seed = (wd != null) ? wd.getSeed() : 0;
                        app.setScreen(new LoadingScreen(app, selected, seed, terrain));
                    }
                }
            }
        };
        dialog.getContentTable().add(new Label("Select a world:", lblStyle)).padBottom(6).row();
        dialog.getContentTable().add(scrollPane).width(250).height(180).row();
        dialog.button("Load", true);
        dialog.button("Cancel", false);
        dialog.show(stage);
    }

    private void showMultiplayerDialog() {
        BitmapFont font = skin.getFont("default-font");
        Label.LabelStyle lblStyle = skin.get("default", Label.LabelStyle.class);

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = font;
        tfStyle.fontColor = Color.WHITE;
        Pixmap tfBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        tfBgPm.setColor(0.2f, 0.2f, 0.35f, 1f);
        tfBgPm.fill();
        tfStyle.background = new TextureRegionDrawable(new Texture(tfBgPm));
        tfBgPm.dispose();
        tfStyle.cursor = tfStyle.background;

        final TextField ipField = new TextField("", tfStyle);
        ipField.setMessageText("Server IP address");
        final TextField portField = new TextField("8081", tfStyle);

        Table content = new Table();
        content.pad(10);
        content.add(new Label("Server IP:", lblStyle)).left().padBottom(4).row();
        content.add(ipField).width(220).padBottom(10).row();
        content.add(new Label("Port:", lblStyle)).left().padBottom(4).row();
        content.add(portField).width(220).padBottom(10).row();

        Dialog dialog = new Dialog("Multiplayer", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    // Multiplayer networking not yet fully wired for Android
                    Dialog info = new Dialog("Multiplayer", skin) {
                        @Override
                        protected void result(Object o) {}
                    };
                    info.text("Multiplayer coming soon.");
                    info.button("OK", true);
                    info.show(stage);
                }
            }
        };
        dialog.getContentTable().add(content).row();
        dialog.button("Join", true);
        dialog.button("Cancel", false);
        dialog.show(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.15f, 1f);
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
        if (skin != null) { skin.dispose(); skin = null; }
    }
}

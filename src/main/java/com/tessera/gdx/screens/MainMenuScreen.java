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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.TesseraApp;
import com.tessera.engine.server.world.WorldsHandler;
import com.tessera.engine.server.world.data.WorldData;

import java.io.IOException;
import java.util.ArrayList;

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

        // TextField style for dialogs
        Pixmap tfBgPm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        tfBgPm.setColor(0.15f, 0.15f, 0.35f, 1f);
        tfBgPm.fill();
        Texture tfBgTex = new Texture(tfBgPm);
        tfBgPm.dispose();
        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = font;
        tfStyle.fontColor = Color.WHITE;
        tfStyle.background = new TextureRegionDrawable(tfBgTex);
        tfStyle.cursor = new TextureRegionDrawable(makePixel(Color.WHITE));
        tfStyle.messageFontColor = Color.GRAY;
        tfStyle.messageFont = font;
        skin.add("default", tfStyle);

        // SelectBox style
        SelectBox.SelectBoxStyle sbStyle = new SelectBox.SelectBoxStyle();
        sbStyle.font = font;
        sbStyle.fontColor = Color.WHITE;
        sbStyle.background = new TextureRegionDrawable(btnTex);
        com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle listStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.LIGHT_GRAY;
        listStyle.selection = new TextureRegionDrawable(makePixel(new Color(0.3f, 0.5f, 0.9f, 1f)));
        sbStyle.listStyle = listStyle;
        sbStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        skin.add("default", sbStyle);

        // ScrollPane style for world list
        skin.add("default", new ScrollPane.ScrollPaneStyle());

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
        TextField nameField = new TextField("World", skin);
        TextField seedField = new TextField("", skin);
        seedField.setMessageText("(random)");

        SelectBox<String> terrainBox = new SelectBox<>(skin);
        terrainBox.setItems("default", "flat");

        Dialog dialog = new Dialog("New World", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    String worldName = nameField.getText().trim();
                    if (worldName.isEmpty()) worldName = "World";
                    String seedText = seedField.getText().trim();
                    int seed = 0;
                    if (!seedText.isEmpty()) {
                        try { seed = Integer.parseInt(seedText); } catch (NumberFormatException ignored) {}
                    }
                    String terrain = terrainBox.getSelected();
                    app.setScreen(new LoadingScreen(app, worldName, seed, terrain));
                }
            }
        };
        dialog.text("World name:");
        dialog.getContentTable().row();
        dialog.getContentTable().add(nameField).width(240).padBottom(6).row();
        dialog.getContentTable().add(new Label("Seed (blank = random):", skin)).left().row();
        dialog.getContentTable().add(seedField).width(240).padBottom(6).row();
        dialog.getContentTable().add(new Label("Terrain:", skin)).left().row();
        dialog.getContentTable().add(terrainBox).width(240).padBottom(6).row();
        dialog.button("Create", true);
        dialog.button("Cancel", false);
        dialog.show(stage);
    }

    private void showLoadWorldDialog() {
        ArrayList<WorldData> worlds = new ArrayList<>();
        try {
            WorldsHandler.listWorlds(worlds);
        } catch (IOException | NullPointerException e) {
            worlds.clear();
        }

        Dialog dialog = new Dialog("Load World", skin) {
            @Override
            protected void result(Object object) {}
        };

        if (worlds.isEmpty()) {
            dialog.text("No saved worlds found.");
            dialog.button("OK", true);
        } else {
            dialog.text("Select a world:");
            Table worldList = new Table();
            for (WorldData wd : worlds) {
                String label = wd.getName() + "  [seed:" + wd.getSeed() + "]";
                TextButton worldBtn = new TextButton(label, skin);
                worldBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        dialog.hide();
                        app.setScreen(new LoadingScreen(app, wd.getName(), wd.getSeed(), "default"));
                    }
                });
                worldList.add(worldBtn).width(300).padBottom(4).row();
            }
            ScrollPane scrollPane = new ScrollPane(worldList, skin);
            dialog.getContentTable().row();
            dialog.getContentTable().add(scrollPane).width(320).height(200).row();
            dialog.button("Cancel", false);
        }
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

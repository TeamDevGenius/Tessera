package com.tessera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tessera.content.vanilla.TesseraGame;
import com.tessera.content.vanilla.terrain.defaultTerrain.DefaultTerrain;
import com.tessera.engine.SkinRegistry;
import com.tessera.engine.client.Client;
import com.tessera.engine.client.ClientWindow;
import com.tessera.engine.server.GameMode;
import com.tessera.engine.utils.resource.ResourceLister;
import com.tessera.window.GLFWWindow;
import org.lwjgl.glfw.GLFW;

public class TesseraApp extends ApplicationAdapter {

    public static final String TITLE = "Tessera";
    public static final String VERSION = "1.8.0";
    private static final int MAX_WORLD_SEED = 100_000;

    private SpriteBatch batch;
    private BitmapFont font;
    private Stage uiStage;
    private Skin skin;

    private Client client;
    private boolean initialized = false;
    private String initError = null;
    private Skin fallbackSkin;

    // ──────────────────────────────────────────────────
    // Touch controls – two-zone layout:
    //   Left half  → virtual joystick (WASD movement)
    //   Right half → camera look (drag)
    // ──────────────────────────────────────────────────
    // Track which pointer started in the move zone so touchUp releases correctly
    private final java.util.Set<Integer> movePointers =
            java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    // Centre of the left-stick touch
    private float stickX, stickY;
    // Last look-drag positions
    private float lookLastX, lookLastY;
    private boolean lookActive = false;

    /** InputAdapter installed when the game world is active. */
    private final InputAdapter gameInputAdapter = new InputAdapter() {

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            float halfW = Gdx.graphics.getWidth() * 0.5f;
            if (screenX < halfW) {
                // Left half: start movement stick
                stickX = screenX;
                stickY = screenY;
                movePointers.add(pointer);
            } else {
                // Right half: start look drag
                lookLastX = screenX;
                lookLastY = screenY;
                lookActive = true;
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (movePointers.remove(pointer)) {
                // This pointer started in the move zone – release all movement keys
                GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_W);
                GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_S);
                GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_A);
                GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_D);
            } else {
                lookActive = false;
            }
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (movePointers.contains(pointer)) {
                // Movement: compute angle and distance from stick origin
                float dx = screenX - stickX;
                float dy = screenY - stickY;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                float deadzone = 20f;
                if (dist > deadzone) {
                    float nx = dx / dist;
                    float ny = dy / dist;
                    // Forward/backward (positive dy = down = backward on screen)
                    if (ny < -0.4f) GLFWWindow.setTouchKeyDown(GLFW.GLFW_KEY_W);
                    else            GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_W);
                    if (ny >  0.4f) GLFWWindow.setTouchKeyDown(GLFW.GLFW_KEY_S);
                    else            GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_S);
                    // Strafe left/right
                    if (nx < -0.4f) GLFWWindow.setTouchKeyDown(GLFW.GLFW_KEY_A);
                    else            GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_A);
                    if (nx >  0.4f) GLFWWindow.setTouchKeyDown(GLFW.GLFW_KEY_D);
                    else            GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_D);
                } else {
                    GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_W);
                    GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_S);
                    GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_A);
                    GLFWWindow.setTouchKeyUp(GLFW.GLFW_KEY_D);
                }
            } else if (lookActive) {
                lookLastX = screenX;
                lookLastY = screenY;
            }
            return true;
        }
    };

    private void installMenuInput() {
        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(uiStage);
        Gdx.input.setInputProcessor(mux);
    }

    private void installGameInput() {
        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(gameInputAdapter);
        Gdx.input.setInputProcessor(mux);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        uiStage = new Stage(new ScreenViewport());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);        Gdx.app.log(TITLE, "Starting " + VERSION);

        new Thread(() -> {
            try {
                ResourceLister.init();
                SkinRegistry skins = new SkinRegistry();
                TesseraGame game = new TesseraGame();
                // Save in Main statics (non-GL fields)
                Main.skins = skins;
                Main.game = game;
                // GL initialization must happen on the render thread
                Gdx.app.postRunnable(() -> {
                    try {
                        client = new Client(new String[0], VERSION, game);
                        Main.localClient = client;
                        initialized = true;
                        Gdx.app.log(TITLE, "Game engine initialized");
                        buildMainMenuUI();
                    } catch (Throwable e) {
                        initError = "GL init failed: " + e.getMessage();
                        Gdx.app.error(TITLE, initError, e);
                        buildErrorUI(initError);
                    }
                });
            } catch (Exception e) {
                initError = "Init failed: " + e.getMessage();
                Gdx.app.error(TITLE, initError, e);
                Gdx.app.postRunnable(() -> buildErrorUI(initError));
            }
        }, "tessera-init").start();
    }

    private void buildMainMenuUI() {
        uiStage.clear();
        skin = new Skin();
        skin.add("default-font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        BitmapFont btnFont = new BitmapFont();
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = btnFont;
        btnStyle.fontColor = Color.WHITE;
        btnStyle.overFontColor = Color.YELLOW;
        btnStyle.downFontColor = Color.GRAY;
        skin.add("default", btnStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label(TITLE + " " + VERSION, skin);
        title.setFontScale(2f);
        table.add(title).padBottom(40).row();

        TextButton singlePlayer = new TextButton("SINGLE PLAYER", skin);
        singlePlayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showLoadWorldUI();
            }
        });
        table.add(singlePlayer).width(250).height(50).padBottom(15).row();

        TextButton settings = new TextButton("SETTINGS", skin);
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TITLE, "Settings not yet implemented");
            }
        });
        table.add(settings).width(250).height(50).padBottom(15).row();

        TextButton quit = new TextButton("QUIT", skin);
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        table.add(quit).width(250).height(50).row();

        uiStage.addActor(table);
    }

    private void showLoadWorldUI() {
        uiStage.clear();
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("Load World", skin);
        title.setFontScale(1.5f);
        table.add(title).padBottom(30).row();

        try {
            java.util.ArrayList<com.tessera.engine.server.world.data.WorldData> worlds = new java.util.ArrayList<>();
            com.tessera.engine.server.world.WorldsHandler.listWorlds(worlds);
            if (worlds.isEmpty()) {
                table.add(new Label("No worlds found. Create a new world.", skin)).padBottom(20).row();
            } else {
                for (com.tessera.engine.server.world.data.WorldData world : worlds) {
                    final com.tessera.engine.server.world.data.WorldData w = world;
                    TextButton worldBtn = new TextButton(world.getName(), skin);
                    worldBtn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (client != null) {
                                client.loadWorld(w, null);
                                uiStage.clear();
                                installGameInput();
                            }
                        }
                    });
                    table.add(worldBtn).width(300).height(45).padBottom(10).row();
                }
            }
        } catch (Exception e) {
            table.add(new Label("Error loading worlds: " + e.getMessage(), skin)).padBottom(20).row();
        }

        TextButton newWorld = new TextButton("NEW WORLD", skin);
        newWorld.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showNewWorldUI();
            }
        });
        table.add(newWorld).width(250).height(50).padBottom(15).row();

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buildMainMenuUI();
            }
        });
        table.add(back).width(250).height(50).row();

        uiStage.addActor(table);
    }

    private void showNewWorldUI() {
        uiStage.clear();
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label title = new Label("New World", skin);
        title.setFontScale(1.5f);
        table.add(title).padBottom(30).row();

        final com.badlogic.gdx.scenes.scene2d.ui.TextField nameField =
                new com.badlogic.gdx.scenes.scene2d.ui.TextField("My World", createTextFieldStyle());
        table.add(new Label("World Name:", skin)).padBottom(5).row();
        table.add(nameField).width(250).padBottom(20).row();

        TextButton create = new TextButton("CREATE", skin);
        create.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    if (client != null) {
                        boolean ok = client.makeNewWorld(name, 512,
                                new DefaultTerrain(),
                                (int) (Math.random() * MAX_WORLD_SEED),
                                GameMode.FREEPLAY);
                        if (ok) showLoadWorldUI();
                    }
                }
            }
        });
        table.add(create).width(250).height(50).padBottom(15).row();

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showLoadWorldUI();
            }
        });
        table.add(back).width(250).height(50).row();

        uiStage.addActor(table);
    }

    private com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle createTextFieldStyle() {
        com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle style =
                new com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        com.badlogic.gdx.graphics.Pixmap cursorPixmap =
                new com.badlogic.gdx.graphics.Pixmap(2, 20, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        cursorPixmap.setColor(Color.WHITE);
        cursorPixmap.fill();
        style.cursor = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(
                new com.badlogic.gdx.graphics.g2d.TextureRegion(
                        new com.badlogic.gdx.graphics.Texture(cursorPixmap)));
        cursorPixmap.dispose();
        return style;
    }

    private void buildErrorUI(String error) {
        uiStage.clear();
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        if (skin == null && fallbackSkin == null) {
            fallbackSkin = createFallbackSkin();
        }
        Label lbl = new Label("Error: " + error, skin != null ? skin : fallbackSkin);
        lbl.setWrap(true);
        table.add(lbl).width(400).padBottom(20).row();
        uiStage.addActor(table);
    }

    private Skin createFallbackSkin() {
        Skin s = new Skin();
        s.add("default-font", font);
        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = font;
        ls.fontColor = Color.RED;
        s.add("default", ls);
        return s;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (initialized) {
            if (client != null) {
                try {
                    if (ClientWindow.isInGamePage()) {
                        client.window.startFrame();
                        if (Client.localServer != null) {
                            Client.localServer.update();
                        }
                        client.window.gameScene.render();
                        client.window.endFrame();
                        batch.begin();
                        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
                        batch.end();
                    } else {
                        // Drive Nuklear menu logic (world-loading progress callbacks)
                        try {
                            client.window.topMenu.render();
                        } catch (Exception e) {
                            Gdx.app.error(TITLE, "Menu render: " + e.getMessage(), e);
                            Gdx.gl.glClearColor(0f, 0.3f, 0.6f, 1f);
                            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                        }
                        uiStage.act(delta);
                        uiStage.draw();
                        return;
                    }
                } catch (Exception e) {
                    Gdx.app.error(TITLE, "Render error: " + e.getMessage(), e);
                    Gdx.gl.glClearColor(0.2f, 0.1f, 0.1f, 1f);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                }
            } else {
                Gdx.gl.glClearColor(0f, 0.3f, 0.6f, 1f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }
        } else {
            Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            if (initError != null) {
                batch.begin();
                font.setColor(Color.RED);
                font.draw(batch, "Error: " + initError, 20, Gdx.graphics.getHeight() / 2f);
                font.setColor(Color.WHITE);
                batch.end();
            } else {
                batch.begin();
                font.draw(batch, "Loading " + TITLE + " " + VERSION + "...", 20, Gdx.graphics.getHeight() / 2f);
                batch.end();
            }
        }

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        if (initialized && client != null) {
            client.window.framebufferResizeEvent(width, height);
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (uiStage != null) uiStage.dispose();
        if (skin != null) skin.dispose();
        if (fallbackSkin != null) fallbackSkin.dispose();
    }
}
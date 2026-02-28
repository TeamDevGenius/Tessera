package com.tessera;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tessera.gdx.screens.MainMenuScreen;

public class TesseraApp extends Game {

    public static final String TITLE = "Tessera";
    public static final String VERSION = "1.8.0";

    public SpriteBatch batch;
    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        Gdx.app.log(TITLE, "Started version " + VERSION);
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}
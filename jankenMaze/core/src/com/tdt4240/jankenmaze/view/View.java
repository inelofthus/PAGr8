package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

/**
 * Created by karim on 13/03/2018.
 */

public abstract class View {
    protected FitViewport viewport;
    protected int gameWidth;
    protected int gameHeight;
    protected View(){
        GameSettings gameSettings = GameSettings.getInstance();
        gameWidth = gameSettings.viewPortWidth;
        gameHeight = gameSettings.viewPortHeight;
        viewport = new FitViewport(gameWidth, gameHeight);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
    }
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}

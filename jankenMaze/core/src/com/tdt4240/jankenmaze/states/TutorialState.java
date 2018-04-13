package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

/**
 * Created by jonas on 13/04/18.
 */

public class TutorialState extends State {
    Viewport viewport;
    Stage stage;
    public TutorialState() {
        GameSettings gameSettings = GameSettings.getInstance();
        int gameWidth = gameSettings.viewPortWidth;
        int gameHeight = gameSettings.viewPortHeight;
        viewport = new FitViewport(gameWidth, gameHeight);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Image tutorialImage = new Image(new Texture("tutorial.png"));
        stage.addActor(tutorialImage);
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        if(Gdx.input.justTouched()){
            System.out.println("Pop it like a zit");
            gsm.pop();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}

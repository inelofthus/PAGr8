package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by jonas on 13/04/18.
 */

public class TutorialView extends View {
    Stage stage;

    public TutorialView() {
        stage = new Stage(viewport);
        this.stage = stage;

        Gdx.input.setInputProcessor(stage);

        Image tutorialImage = new Image(new Texture("tutorial.png"));
        stage.addActor(tutorialImage);
    }

    @Override
    public void update(float dt) {

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
        stage.dispose();
    }
}

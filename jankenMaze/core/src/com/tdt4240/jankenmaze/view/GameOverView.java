package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by bartosz on 4/7/18.
 */

public class GameOverView extends View {
    private Stage stage;

    public GameOverView(){
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}

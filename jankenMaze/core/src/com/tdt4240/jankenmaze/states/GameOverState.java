package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.view.GameOverView;

/**
 * Created by bartosz on 4/7/18.
 * This is the state that is activated when the game is over.
 */

public class GameOverState extends State {
    private GameStateManager gms;
    private GameOverView gameOverView;

    public GameOverState(){
        super();
        gms=GameStateManager.getGsm();
        this.gameOverView = new GameOverView();



    }

    @Override
    protected void handleInput() {

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

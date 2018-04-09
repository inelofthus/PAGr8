package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.view.GameOverView;
import com.tdt4240.jankenmaze.view.YouLooseView;
import com.tdt4240.jankenmaze.view.YouWinView;

/**
 * Created by bartosz on 4/7/18.
 * This is the state that is activated when the game is over.
 */

public class GameOverState extends State {
    private GameStateManager gms;
    private GameOverView gameOverView;
    private SpriteBatch batch;

    public GameOverState(){
        super();
        gms=GameStateManager.getGsm();
        this.gameOverView = new YouWinView();



    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        this.batch = sb;
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        gameOverView.render(batch);
        sb.end();

    }

    @Override
    public void dispose() {

    }
}

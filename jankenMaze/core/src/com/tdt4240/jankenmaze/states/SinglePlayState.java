package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;

/**
 * Created by karim on 09/04/2018.
 */

public class SinglePlayState extends PlayState {

    public SinglePlayState(SpriteBatch batch) {
        super(batch);
        //entityManager.createLocalPlayer(PlayerType.ROCK);
    }

    @Override
    protected void handleInput() {
        super.handleInput();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        for(GameEvent gameOver: gameOverQueue.getEvents()){
            gsm.set(new GameOverState());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

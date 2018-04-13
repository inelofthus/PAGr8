package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.systems.HealthSystem;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;

/**
 * Created by karim on 09/04/2018.
 */

public class SinglePlayState extends PlayState {

    public SinglePlayState(SpriteBatch batch) {
        super(batch);
        entityManager.SP_createLocalPlayer(PlayerType.ROCK);
        entityManager.SP_createLocalBot(PlayerType.SCISSORS);
        entityManager.SP_createLocalBot(PlayerType.PAPER);
        //The bots have to be instantiated before the health system is added to the engine, but
        //since the health system is added to the engine when entityManager is instantiated, we
        //have to instantiate entityManager again, lol.
        //entityManager = new com.tdt4240.jankenmaze.gameecs.EntityManager(engine, batch, gameOverSignal);
        //entityManager.createMap(binaryMap, new Texture("redAndWhiteWall.png"));
        //entityManager.createHUDItem();
        System.out.println("Created two bots in SinglePlayerState");
    }

    @Override
    protected void handleInput() {
        super.handleInput();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        for(GameEvent gameOver: gameOverQueue.getEvents()){
            gsm.push(new GameOverState());
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

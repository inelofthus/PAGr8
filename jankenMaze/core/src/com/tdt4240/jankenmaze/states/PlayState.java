package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by jonas on 07/03/2018.
 * TODO: Figure out better way of getting spritebatch
 *
 * Creates Engine and EntityManager, then calls on entityManager to update effectively delegating
 * all in-game code.
 */

public class PlayState extends State {
    Engine engine;
    com.tdt4240.jankenmaze.gameecs.EntityManager entityManager;

    public PlayState(SpriteBatch batch){
        super();
        engine = new Engine();
        entityManager = new com.tdt4240.jankenmaze.gameecs.EntityManager(engine, batch);
    }
    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        entityManager.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        //Rendering is handled by Entity Systems for now
    }

    @Override
    public void dispose() {

    }
}

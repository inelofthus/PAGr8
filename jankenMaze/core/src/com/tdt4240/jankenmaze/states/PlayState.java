package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.ashley.signals.Signal;
import com.tdt4240.jankenmaze.gameecs.components.PlayerInfo;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.gamesettings.Maps;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;

/**
 * Created by jonas on 07/03/2018.
 * TODO: Figure out better way of getting spritebatch
 *
 * Creates Engine and EntityManager, then calls on entityManager to update effectively delegating
 * all in-game code.
 */

public class PlayState extends State {
    Engine engine;
    protected Signal<GameEvent> gameOverSignal;
    protected EventQueue gameOverQueue;
    com.tdt4240.jankenmaze.gameecs.EntityManager entityManager;
    SpriteBatch batch;

    public PlayState(SpriteBatch batch){
        super();
        this.batch = batch;
        engine = new Engine();
        gameOverSignal= new Signal<GameEvent>();
        gameOverQueue = new EventQueue();
        this.gameOverSignal.add(gameOverQueue);
        entityManager = new com.tdt4240.jankenmaze.gameecs.EntityManager(engine, batch, gameOverSignal);

        String chosenMap = GameSettings.getInstance().chosenMap;
        entityManager.createMap(Maps.getINSTANCE().getMap(), new Texture(Maps.getINSTANCE().getTexture(chosenMap)));
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        entityManager.update();



    }

    @Override
    public void render(SpriteBatch sb) {
        //Rendering is handled by Entity Systems
    }

    @Override
    public void dispose() {

    }


}

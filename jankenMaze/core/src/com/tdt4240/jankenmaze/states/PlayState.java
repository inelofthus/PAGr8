package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.ashley.signals.Signal;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

/**
 * Created by jonas on 07/03/2018.
 * TODO: Figure out better way of getting spritebatch
 *
 * Creates Engine and EntityManager, then calls on entityManager to update effectively delegating
 * all in-game code.
 */

public class PlayState extends State {
    Engine engine;
    private Signal<GameEvent> gameOverSignal;
    private EventQueue gameOverQueue;
    com.tdt4240.jankenmaze.gameecs.EntityManager entityManager;
    SpriteBatch batch;
    int[][] binaryMap = {{1 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}}; //Map is instatiated from a binary matrix (A list of columns)

    public PlayState(SpriteBatch batch){
        super();
        this.batch = batch;
        engine = new Engine();
        gameOverSignal= new Signal<GameEvent>();
        gameOverQueue= new EventQueue();
        this.gameOverSignal.add(gameOverQueue);
        entityManager = new com.tdt4240.jankenmaze.gameecs.EntityManager(engine, batch, gameOverSignal);
        entityManager.createMap(binaryMap, new Texture("redAndWhiteWall.png"));
        entityManager.createLocalPlayer("Rock");
       /* entityManager.createPlayer("Paper");//Players have to be created after map.
        entityManager.createPlayer("Scissors");*/
        entityManager.createHUDItem();
    }
    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        entityManager.update();

        for(GameEvent gameOver: gameOverQueue.getEvents()){
            gsm.push(new GameOverState());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        //Rendering is handled by Entity Systems for now
    }

    @Override
    public void dispose() {

    }


}

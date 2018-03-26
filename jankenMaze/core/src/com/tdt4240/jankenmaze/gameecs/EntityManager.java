package com.tdt4240.jankenmaze.gameecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.gameecs.components.LocalPlayer;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Renderable;
import com.tdt4240.jankenmaze.gameecs.components.SpriteComponent;
import com.tdt4240.jankenmaze.gameecs.components.Velocity;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.systems.EntityFactory;
import com.tdt4240.jankenmaze.gameecs.systems.HUDSystem;
import com.tdt4240.jankenmaze.gameecs.systems.InputSystem;
import com.tdt4240.jankenmaze.gameecs.systems.MovementSystem;
import com.tdt4240.jankenmaze.gameecs.systems.EntityFactory;
import com.tdt4240.jankenmaze.gameecs.systems.ReceiveSignalSystemExample;
import com.tdt4240.jankenmaze.gameecs.systems.SendSignalSystemExample;

/**
 * Created by jonas on 07/03/2018.
 * Implements the Entity Manager to use in gameplay.
 * Super-quick how-to-ECS:
 * 1: Have ONE Engine, preferably made in PlayState or something. Just one per ECS.
 * 2: Implement Systems (see Systems-package) and docstring in MovementSystem
 * -- Systems add logic to all entities with given components.
 * -- Systems MUST be added to engine (such that update() etc can be called by it
 * -- RenderSystem currently draws everything.
 * 3: Add entities as shown below. You can string as many .add(EntitySystem) as you like.
 *
 * -- In the playState, where we call draw now, we should have a call to the EnitityManager  like
 *    currently shown in the JankenLabyrinth-class. That is spriteBatch.begin(); entityManger.update();
 *    spriteBatch.end();
 *
 *    Note that entityManger.update() calls the engine.update().
 *
 * -- See also the Position on how-to-Component if you need to.
 */

public class EntityManager {
    private Engine engine;
    SpriteBatch batch;
    OrthographicCamera cam;
    InputSystem inputSystem;
    public EntityFactory entityFactory;
    private Signal<GameEvent> gameEventSignal;

    public EntityManager(Engine e, SpriteBatch sb) {
        this.engine = e;
        this.batch = sb;
        entityFactory = new EntityFactory(engine, batch);
        gameEventSignal = new Signal<GameEvent>();

        MovementSystem cms = new MovementSystem(gameEventSignal);
        engine.addSystem(cms);
        com.tdt4240.jankenmaze.gameecs.systems.RenderSystem rs = new com.tdt4240.jankenmaze.gameecs.systems.RenderSystem(batch);
        engine.addSystem(rs);
        this.inputSystem = new InputSystem(gameEventSignal);
        engine.addSystem(inputSystem);
        HUDSystem hudSystem = new HUDSystem();
        engine.addSystem(hudSystem);

<<<<<<< HEAD
        //TODO: Should entityfactory add entities directly?)
        engine.addEntity(entityFactory.createHUDItem(0, 0, new Texture("button.png"), "playerHealth"));
    }



        //engine.addEntity(
        //        entityFactory.createWall(800, 800, new Texture("testWall.png"))
        //);

    public void createMap(int[][] binaryMap, Texture texture) {
        for (int i = 0; i < binaryMap.length; i++) { //Iterates over rows
            for (int j = 0; j < binaryMap[i].length; j++) { //Iterates over columns
                if (binaryMap[i][j] == 1) {
                    engine.addEntity(entityFactory.createWall(i * 32, j * 32, new Texture("greyWall.png"))); //200 here represents the width of a block
                }
            }
        }
    }
=======
        SendSignalSystemExample sendEx = new SendSignalSystemExample(gameEventSignal);
        engine.addSystem(sendEx);
        ReceiveSignalSystemExample recEx = new ReceiveSignalSystemExample(gameEventSignal);
        engine.addSystem(recEx);

        //TODO: Should entityfactory add entities directly?
        engine.addEntity(
            entityFactory.createPlayer("rock", 0, 0, 3, new Texture("badlogic.jpg"))
        );
        engine.addEntity(
                entityFactory.createHUDItem(0, 0, new Texture("button.png"), "playerHealth")
        );
        engine.addEntity(
                entityFactory.createWall(800, 800, new Texture("testWall.png")
        ));
>>>>>>> b794e9c6249ca1b0471529b5d4f06ef539ff0d0f
        /*
        Entity testImageEntity = new Entity();
        testImageEntity.add(new Position(0,0))
                .add(new Velocity(300,300))
                .add(new SpriteComponent((new Texture("badlogic.jpg"))))
                .add(new LocalPlayer())
                .add(new Renderable());

        engine.addEntity(testImageEntity);*/

    public void update(){
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public void draw(SpriteBatch batch){

    }
    public void setBatch(SpriteBatch batch){

    }

    public boolean hasNoSpriteBatch(){
        return (this.batch == null);
    }
}

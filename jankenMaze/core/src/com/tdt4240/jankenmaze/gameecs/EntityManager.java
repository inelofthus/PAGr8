package com.tdt4240.jankenmaze.gameecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
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
import com.tdt4240.jankenmaze.gameecs.components.Unoccupied;
import com.tdt4240.jankenmaze.gameecs.systems.CollisionSystem;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.systems.EntityFactory;
import com.tdt4240.jankenmaze.gameecs.systems.HUDSystem;
import com.tdt4240.jankenmaze.gameecs.systems.HealthSystem;
import com.tdt4240.jankenmaze.gameecs.systems.InputSystem;
import com.tdt4240.jankenmaze.gameecs.systems.MovementSystem;
import com.tdt4240.jankenmaze.gameecs.systems.EntityFactory;
import com.tdt4240.jankenmaze.gameecs.systems.ReceiveSignalSystemExample;
import com.tdt4240.jankenmaze.gameecs.systems.RenderSystem;
import com.tdt4240.jankenmaze.gameecs.systems.SendSignalSystemExample;
import com.badlogic.ashley.utils.ImmutableArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    public EntityFactory entityFactory;
    private Signal<GameEvent> gameEventSignal;

    private Signal<GameEvent> playerCollisionSignal;
    Random rand = new Random();
    private ImmutableArray<Entity> spawnPositions;
    private HashMap<String, Texture> textureMap;


    public EntityManager(Engine e, SpriteBatch sb) {
        this.engine = e;
        this.batch = sb;
        entityFactory = new EntityFactory(engine, batch);
        gameEventSignal = new Signal<GameEvent>();
        playerCollisionSignal = new Signal<GameEvent>();
        //TODO: This should be an Enum
        this.textureMap = new HashMap<String, Texture>();
        textureMap.put("Rock", new Texture("singleRock.png"));
        textureMap.put("Paper", new Texture("singlePaper.png"));
        textureMap.put("Scissors", new Texture("Scissors.png"));

        addSystemsToEngine();
    }

    //TODO: Should entityfactory add entities directly? It's currently done in playstate
    public void createPlayer(String type) {
        com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(randomSpawnPosition());
        engine.addEntity(
                entityFactory.createPlayer(type, playerPosition.x, playerPosition.y, 3, textureMap.get(type))
        );
    }

    public void createLocalPlayer(String type) {

        com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(randomSpawnPosition());
        engine.addEntity(
                entityFactory.createLocalPlayer(type, playerPosition.x, playerPosition.y, 3, textureMap.get(type))
        );
    }

    public void createHUDItem() {
        engine.addEntity(
                entityFactory.createHUDItem(100, 100, new Texture("button.png"), "playerHealth")
        );
        //engine.addEntity(
        //        entityFactory.createWall(200, 200, new Texture("testWall.png")
        //        ));


    }

    //Returns a random spawnposition Entity.
    public Entity randomSpawnPosition() {
        spawnPositions = engine.getEntitiesFor(Family.all(Unoccupied.class).get());
        int randomNumber = rand.nextInt(spawnPositions.size());
        return spawnPositions.get(randomNumber);
    }

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

    public void createMap(int[][] binaryMap, Texture texture) {
        for (int i = 0; i < binaryMap.length; i++) { //Iterates over rows
            for (int j = 0; j < binaryMap[i].length; j++) { //Iterates over columns
                if (binaryMap[i][j] == 1) {
                    engine.addEntity(entityFactory.createWall(i * 32, j * 32, texture)); //200 here represents the width of a block
                }
                else {
                    engine.addEntity(entityFactory.createSpawnPosition(i*32, j*32));
                }
            }
        }
    }

    void addSystemsToEngine(){
        MovementSystem cms = new MovementSystem(gameEventSignal);
        engine.addSystem(cms);

        RenderSystem rs = new RenderSystem(batch);
        engine.addSystem(rs);

        InputSystem is = new InputSystem(gameEventSignal);
        engine.addSystem(is);

        HUDSystem hudSystem = new HUDSystem();
        engine.addSystem(hudSystem);

        CollisionSystem cs = new CollisionSystem(playerCollisionSignal);
        engine.addSystem(cs);

        HealthSystem hs = new HealthSystem(playerCollisionSignal);
        engine.addSystem(hs);

        SendSignalSystemExample sendEx = new SendSignalSystemExample(gameEventSignal);
        engine.addSystem(sendEx);

        ReceiveSignalSystemExample recEx = new ReceiveSignalSystemExample(gameEventSignal);
        engine.addSystem(recEx);
    }

}

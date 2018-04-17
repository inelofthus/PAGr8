package com.tdt4240.jankenmaze.gameecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gameecs.components.Unoccupied;
import com.tdt4240.jankenmaze.gameecs.events.GameVariable;

import com.tdt4240.jankenmaze.gameecs.systems.CollisionSystem;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.systems.HUDSystem;
import com.tdt4240.jankenmaze.gameecs.systems.HealthBroadcastSystem;
import com.tdt4240.jankenmaze.gameecs.systems.HealthSystem;
import com.tdt4240.jankenmaze.gameecs.systems.InputSystem;
import com.tdt4240.jankenmaze.gameecs.systems.MovementSystem;
import com.tdt4240.jankenmaze.gameecs.systems.PositionBroadcastSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.gamesettings.Maps;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;
import com.tdt4240.jankenmaze.gamesettings.PlayerTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    InputSystem inputSystem;
    public EntityFactory entityFactory;
    private Signal<GameEvent> gameEventSignal;
    private Signal<GameEvent> gameOverSignal;
    private Signal<GameEvent> playerCollisionSignal;
    private Signal<GameEvent> decreaseHealthSignal;
    private Signal<GameVariable> playerPositionSignal;

    private Random rand = new Random();
    private ImmutableArray<Entity> spawnPositions;
    private HashMap<PlayerType, Texture> playerTextureMap;

    public EntityManager(Engine e, SpriteBatch sb, Signal<GameEvent> gameOverSignal) {
        this.engine = e;
        this.batch = sb;
        entityFactory = new EntityFactory(engine, batch);
        gameEventSignal = new Signal<GameEvent>();
        playerCollisionSignal = new Signal<GameEvent>();
        decreaseHealthSignal = new Signal<GameEvent>();
        playerPositionSignal = new Signal<GameVariable>();
        playerTextureMap = PlayerTypes.getPlayerTextures();

        this.gameOverSignal = gameOverSignal;

        addSystemsToEngine();
    }

    //TODO: Should entityfactory add entities directly? It's currently done in playstate
    public void createRemotePlayer(PlayerType type, PlayerNetworkData networkData) {
        System.out.println("Create player " + PlayerTypes.getPlayerTextures().get(type));
        com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(randomSpawnPosition());
        engine.addEntity(
                entityFactory.createRemotePlayer(type, playerPosition.x, playerPosition.y, 3, playerTextureMap.get(type), networkData)
        );
    }

    public void createLocalPlayer(PlayerType type, PlayerNetworkData networkData) {
        System.out.println("Create local player " + PlayerTypes.getPlayerTextures().get(type));
        com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(randomSpawnPosition());
        engine.addEntity(
                entityFactory.createLocalPlayer(type, playerPosition.x, playerPosition.y, 3, playerTextureMap.get(type), networkData)
        );
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

    public void createMap(int[][] binaryMap, Texture texture) {
        for (int i = 0; i < binaryMap.length; i++) { //Iterates over rows
            for (int j = 0; j < binaryMap[i].length; j++) { //Iterates over columns
                if (binaryMap[i][j] == 1) {
                    engine.addEntity(entityFactory.createWall(j * 32, (binaryMap.length - i - 1) * 32, texture)); //200 here represents the width of a block
                }
                else {
                    engine.addEntity(entityFactory.createSpawnPosition(j*32, (binaryMap.length - i - 1)*32));
                }
            }
        }
    }

    void addSystemsToEngine(){
        MovementSystem cms = new MovementSystem(gameEventSignal);
        engine.addSystem(cms);
        com.tdt4240.jankenmaze.gameecs.systems.RenderSystem rs = new com.tdt4240.jankenmaze.gameecs.systems.RenderSystem(batch);
        engine.addSystem(rs);
        this.inputSystem = new InputSystem(gameEventSignal);
        engine.addSystem(inputSystem);
        HUDSystem hudSystem = new HUDSystem();
        engine.addSystem(hudSystem);
        CollisionSystem cs = new CollisionSystem(playerCollisionSignal, playerPositionSignal);
        engine.addSystem(cs);

        HealthSystem hs=new HealthSystem(playerCollisionSignal, gameOverSignal, decreaseHealthSignal);
        engine.addSystem(hs);
    }

    public void addMPSystemsToEngine(PlayServices playServices){
       PositionBroadcastSystem positionBroadcastSystem = new PositionBroadcastSystem(playerPositionSignal,playServices);
        engine.addSystem(positionBroadcastSystem);
        HealthBroadcastSystem healthBroadcastSystem= new HealthBroadcastSystem(playServices,decreaseHealthSignal);
        engine.addSystem(healthBroadcastSystem);
    }


    public void createMPEntities (){
        createMPPlayers();
    }

    private void createMPPlayers() {
        List<PlayerNetworkData> players = GameSettings.getInstance().getPlayers();

        //Creating player entities
        ArrayList<PlayerType> playerTypes = PlayerTypes.getPlayerTypes();
        for(int i = 0; i < players.size(); i++){
            if (players.get(i).isLocalPlayer) {
                createLocalPlayer(playerTypes.get(i % playerTypes.size()), players.get(i));
            }
            else{
                createRemotePlayer(playerTypes.get(i % playerTypes.size()), players.get(i));
            }
        }
    }

    public void createEntities(){
        String chosenMap = GameSettings.getInstance().chosenMap;
        createMap(Maps.getINSTANCE().getMap(), new Texture(Maps.getINSTANCE().getTexture(chosenMap)));
    }
}

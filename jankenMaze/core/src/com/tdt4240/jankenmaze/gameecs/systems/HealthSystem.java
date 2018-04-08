package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.EntityManager;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.Unoccupied;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

import java.util.Random;

/**
 * Created by jonas on 18/03/2018.
 */

public class HealthSystem extends EntitySystem {
    //TODO: Componentmapper and values
    private Signal<GameEvent> playerCollisionSignal;
    private EventQueue collisionQueue;
    private Signal<GameEvent> gameOverSignal;
    private EventQueue gameOverQueue;
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.Health> healthComponentMapper =ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Health.class);
    ImmutableArray<Entity> localPlayer;
    Random rand = new Random();
    private ImmutableArray<Entity> spawnPositions;




    public HealthSystem(Signal<GameEvent> playerCollisionSignal, Signal<GameEvent> gameOverSignal) {
        //creates the CollisionSignal
        this.playerCollisionSignal = playerCollisionSignal;
        collisionQueue = new EventQueue();
        this.playerCollisionSignal.add(collisionQueue);
        //creates the GameOverSignal
        this.gameOverSignal=gameOverSignal;
        gameOverQueue= new EventQueue();
        this.gameOverSignal.add(gameOverQueue);
    }

    public void increaseHealth(Entity entity, int delta){
        //TODO: Logic

    }
  public void decreaseHealth(Entity player, int delta){

        //get the healthComponent for player
        Health health=healthComponentMapper.get(player);
        System.out.println(health.health);
        //decrease health
        health.health=health.health-Math.abs(delta);
      System.out.println(health.health);
        if (health.health<=0){
            // GAME OVER
            gameOverSignal.dispatch(GameEvent.GAME_OVER);
        }else{
            //Go to fucking spawn.
            //workaround the problem with static context
            int randomNumber = rand.nextInt(spawnPositions.size());
            Entity spawn= spawnPositions.get(randomNumber);
            com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(player);
            com.tdt4240.jankenmaze.gameecs.components.Position spawnPos
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(spawn);

            //TODO the player should loose all velocity when spawns?
            playerPosition.x=spawnPos.x;
            playerPosition.y=spawnPos.y;

        }

    }

    @Override
    public void addedToEngine(Engine engine) {
        //get localPLayer
        localPlayer = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.LocalPlayer.class).get());
        //get all spawnPositions
        spawnPositions = engine.getEntitiesFor(Family.all(Unoccupied.class).get());

    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
      // decreases Health when local player has been eaten.
        for (GameEvent event: collisionQueue.getEvents()){
           decreaseHealth(localPlayer.get(0),1);


        }





    }
}


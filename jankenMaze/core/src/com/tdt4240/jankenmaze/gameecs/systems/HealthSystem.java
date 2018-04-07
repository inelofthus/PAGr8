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
    private EventQueue eventQueue;
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.Health> healthComponentMapper =ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Health.class);
    ImmutableArray<Entity> localPlayer;
    Random rand = new Random();
    private ImmutableArray<Entity> spawnPositions;




    public HealthSystem(Signal<GameEvent> playerCollisionSignal) {
        this.playerCollisionSignal = playerCollisionSignal;

        eventQueue = new EventQueue();
        playerCollisionSignal.add(eventQueue);
    }

    public void increaseHealth(Entity entity, int delta){
        //TODO: Logic

    }
  public void decreaseHealth(Entity player, int delta){
        //TODO: Move to spawnposition

        Health health=healthComponentMapper.get(player);
        System.out.println(health.health);
        health.health=health.health-Math.abs(delta);
      System.out.println(health.health);
        if (health.health<=0){
            // GAME OVER
        }else{
            //Go to fucking spawn.
            //workaround the problem with static context
            int randomNumber = rand.nextInt(spawnPositions.size());
            Entity spawn= spawnPositions.get(randomNumber);
            com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(player);
            com.tdt4240.jankenmaze.gameecs.components.Position spawnPos
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(spawn);
            playerPosition.x=spawnPos.x;
            playerPosition.y=spawnPos.y;

        }

    }

    @Override
    public void addedToEngine(Engine engine) {
        //get localPLayer
         localPlayer = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.LocalPlayer.class).get());
        spawnPositions = engine.getEntitiesFor(Family.all(Unoccupied.class).get());

    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        for (GameEvent event: eventQueue.getEvents()){
           decreaseHealth(localPlayer.get(0),1);


        }




        //super.update(deltaTime);
    }
}


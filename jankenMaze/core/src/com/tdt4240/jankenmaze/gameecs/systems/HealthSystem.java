package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

/**
 * Created by jonas on 18/03/2018.
 */

public class HealthSystem extends EntitySystem {
    //TODO: Componentmapper and values
    private Signal<GameEvent> playerCollisionSignal;
    private EventQueue eventQueue;
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.Health> healthComponentMapper =ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Health.class);
    private ImmutableArray<Entity> localPlayer;

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
    }

    @Override
    public void addedToEngine(Engine engine) {
        //get localPLayer
        localPlayer = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.LocalPlayer.class).get());

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


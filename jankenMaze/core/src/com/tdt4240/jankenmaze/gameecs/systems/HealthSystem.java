package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

/**
 * Created by jonas on 18/03/2018.
 */

public class HealthSystem extends EntitySystem {
    //TODO: Componentmapper and values
    private Signal<GameEvent> playerCollisionSignal;
    private EventQueue eventQueue;
    public HealthSystem(Signal<GameEvent> playerCollisionSignal) {
        this.playerCollisionSignal = playerCollisionSignal;

        eventQueue = new EventQueue();
        playerCollisionSignal.add(eventQueue);
    }

    public void increaseHealth(Entity entity, int delta){
        //TODO: Logic

    }
    public void decreaseHealth(Entity entity, int delta){
        //TODO: Logic
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        for (GameEvent event: eventQueue.getEvents()){
            System.out.println("You've been eaten");
        }




        //super.update(deltaTime);
    }
}


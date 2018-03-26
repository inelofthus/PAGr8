package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.tdt4240.jankenmaze.gameecs.components.LocalPlayer;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

/**
 * Created by Ine on 22.03.2018.
 * This system is an example on how to receive messages from other systems.
 * On update, it will iterate through and handle events in the queue.
 * In this case, the system will print "collision" upon receiving a
 * WALL_COLLISION event.
 */

public class ReceiveSignalSystemExample extends EntitySystem{
    private Signal<GameEvent> gameEventSignal;
    private EventQueue eventQueue;

    public ReceiveSignalSystemExample(Signal<GameEvent> gameEventSignal){
        this.gameEventSignal = gameEventSignal;

        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);

    }

    public void update(float dt){
        for(GameEvent event : eventQueue.getEvents()){
            switch(event){
                case WALL_COLLISION:
                    System.out.println("collision");
                    break;
            }
        }
    }
}

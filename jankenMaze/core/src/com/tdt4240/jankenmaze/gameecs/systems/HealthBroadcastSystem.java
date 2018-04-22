package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameMessages.MessageCodes;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import java.nio.ByteBuffer;

/**
 * The sole purpose of this system is to broadcast the amount of lives localplayer has to other devices
 * using Google Play Services
 */

public class HealthBroadcastSystem extends EntitySystem {

    private PlayServices playServices;
    private com.tdt4240.jankenmaze.gameecs.components.Health health;
    private ImmutableArray<Entity> localPlayer;
    private Signal<GameEvent> decreaseHealthSignal;
    private EventQueue healthQueue;

    public HealthBroadcastSystem(PlayServices playServices, Signal<GameEvent> decreaseHealthSignal){
        //assignes playServices
        this.playServices=playServices;
        //creates decreaseHealthSignal
        this.decreaseHealthSignal=decreaseHealthSignal;
        healthQueue=new EventQueue();
        this.decreaseHealthSignal.add(healthQueue);
    }

    @Override
    public void addedToEngine(Engine engine) {
        //get localPLayer
        localPlayer = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.LocalPlayer.class).get());
        //get health
        health= ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Health.class).get(localPlayer.first());
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        //for each decreaseHealth event
        for (GameEvent event: healthQueue.getEvents()){
            //broadcast new health of localplayer
            broadcastHealth();
        }
        super.update(deltaTime);
    }
    private  void broadcastHealth(){
        //allocates memory for ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(1*4+1);
        //puts the Message Code in the byteBuffer
        buffer.put(MessageCodes.HEALTH);
        //puts the amount of health localplayer has in the byteBuffer
        buffer.putInt(health.health);
        //sends the message to other devices.
        playServices.sendReliableMessageToOthers(buffer.array());
    }
}

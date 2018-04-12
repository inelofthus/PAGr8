package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

public class HealthBroadcastSystem extends EntitySystem {

    PlayServices playServices;

    Signal<GameEvent> decreaseHealthSignal;
    EventQueue healthQueue;

    public HealthBroadcastSystem(PlayServices playServices, Signal<GameEvent> decreaseHealthSignal){
        this.playServices=playServices;
        this.decreaseHealthSignal=decreaseHealthSignal;
        healthQueue=new EventQueue();
        this.decreaseHealthSignal.add(healthQueue);
    }
}

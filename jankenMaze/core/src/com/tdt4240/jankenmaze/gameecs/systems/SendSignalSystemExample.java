package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

/**
 * Created by Ine on 22.03.2018.
 * This class is an example of how to send messages to other systems.
 * On update, this class will dispatch a WALL_COLLISION event,
 * which will be received by systems listening on this event
 * (in this case the ReceiveSignalSystemExample)
 */

public class SendSignalSystemExample extends EntitySystem{
    private Signal<GameEvent> gameEventSignal;
    private EventQueue eventQueue;

    public SendSignalSystemExample(Signal<GameEvent> gameEventSignal){
        this.gameEventSignal = gameEventSignal;

        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);

    }

    public void update(float dt){
        gameEventSignal.dispatch(GameEvent.WALL_COLLISION);
    }
}



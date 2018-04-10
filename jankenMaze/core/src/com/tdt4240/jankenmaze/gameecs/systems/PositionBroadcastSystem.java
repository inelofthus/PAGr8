package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Signal;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.events.GameVariable;
import com.tdt4240.jankenmaze.gameecs.events.VariableQueue;

public class PositionBroadcastSystem extends EntitySystem {
    private PlayServices playServices;
    private Signal<GameVariable> positionSignal;
    private VariableQueue variableQueue;
    public PositionBroadcastSystem(Signal<GameVariable> positionSignal, PlayServices playServices){
        this.playServices = playServices;
        this.positionSignal = positionSignal;
        variableQueue = new VariableQueue();
        positionSignal.add(variableQueue);
    }


    private void broadCastPosition(){

    }

    public void update(float dt) {
    for (GameVariable position : variableQueue.getVariables()){
         broadCastPosition();
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }
}

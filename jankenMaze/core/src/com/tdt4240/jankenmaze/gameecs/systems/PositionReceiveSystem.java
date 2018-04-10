package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.components.BoundsBox;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.events.RemoteQueue;
import com.tdt4240.jankenmaze.gameecs.events.RemoteVariable;

public class PositionReceiveSystem extends EntitySystem {
    Signal<RemoteVariable> remotePositionSignal;
    RemoteQueue remoteQueue;
    private ImmutableArray<Entity> players;
    private ComponentMapper<BoundsBox> bb= ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.BoundsBox.class);
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.Position> position= ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class);
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData> playerNetworkData= ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData.class);

    public PositionReceiveSystem(Signal<RemoteVariable> remotePositionSignal){
        this.remotePositionSignal = remotePositionSignal;
        remoteQueue = new RemoteQueue();
        remotePositionSignal.add(remoteQueue);

    }
    @Override
    public void update(float dt){
        for(RemoteVariable variable : remoteQueue.getRemoteVariable()){
            updatePosition(variable);
        }
    }

    private void updatePosition(RemoteVariable remoteVariable){

        for (Entity player:players){
            if(playerNetworkData.get(player).participantId.equals(remoteVariable.getPlayerID())){
                Position pos = position.get(player);
                BoundsBox bounds = bb.get(player);
                pos.x=remoteVariable.getX();
                pos.y=remoteVariable.getY();
                bounds.boundsBox.x=remoteVariable.getX();
                bounds.boundsBox.y=remoteVariable.getY();
                break;
            }
        }
    }


    @Override
    public void addedToEngine(Engine engine) {
        //get remote players in GameState
        players = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.Remote.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

}

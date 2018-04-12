
    package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.events.GameVariable;
import com.tdt4240.jankenmaze.gameecs.events.VariableQueue;
import java.nio.ByteBuffer;

    public class PositionBroadcastSystem extends EntitySystem {
        private PlayServices playServices;
        private Signal<GameVariable> positionSignal;
        private VariableQueue variableQueue;
        private ImmutableArray<Entity> localPlayer;
        private com.tdt4240.jankenmaze.gameecs.components.Position playerPosition;
        private static final byte  POSITION = 1;
        private float timeSincePositionSent = 0.0f;



        public PositionBroadcastSystem(Signal<GameVariable> positionSignal, PlayServices playServices){
            this.playServices = playServices;
            this.positionSignal = positionSignal;
            variableQueue = new VariableQueue();
            positionSignal.add(variableQueue);
        }


        private void broadcastPosition(){
            ByteBuffer buffer = ByteBuffer.allocate(2 * 4 + 1);
            buffer.put(POSITION);
            buffer.putFloat(playerPosition.x);
            buffer.putFloat(playerPosition.y);
            playServices.sendUnreliableMessageToOthers(buffer.array());

        }

        public void update(float dt) {
            // Want to send new postion every x seconds
            timeSincePositionSent += dt;

            if (timeSincePositionSent > 0.1f){
                broadcastPosition();
                timeSincePositionSent = 0.0f;
            }

        }

        @Override
        public void addedToEngine(Engine engine) {
            //get localPLayer
            localPlayer = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.LocalPlayer.class).get());
            //get playerPosition
            playerPosition = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(localPlayer.first());

        }

        @Override
        public void removedFromEngine(Engine engine) {
            super.removedFromEngine(engine);
        }

}

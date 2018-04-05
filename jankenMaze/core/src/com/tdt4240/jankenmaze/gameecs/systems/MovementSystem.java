package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240.jankenmaze.gameecs.components.*;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Velocity;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

/**
 * Created by jonas on 07/03/2018.
 *
 * Super-quick How-to-System:
 * 1: Implement the appropiate logic (i.e. move the hudEntities who're supposed to be moved by device input or draw drawable stuff)
 * 2: Make an Array of relevant entites in addedToEnginge
 * -- Important: Here the array of hudEntities is null until addedToEngine is called.
 * -- Note that addedToEngine will be called automatically by the engine.
 * -- Note also that it's in addedToEngine the entity-selection takes place. (Family.all(...))
 * 3: In update(float dt), apply logic to the hudEntities (like movement).
 * 4: The ComponentMappers makes it easier to select the right entity-components (and gives good performance)
 * -- See line 30 and 42 for example.
 */

public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> localPlayers;
    private ImmutableArray<Entity> walls;


    private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
    private ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);
    private ComponentMapper<BoundsBox> boundsBoxMapper =ComponentMapper.getFor(BoundsBox.class);
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.BoundsBox> bb= ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.BoundsBox.class);

    private Signal<GameEvent> gameEventSignal;
    private EventQueue eventQueue;

    public MovementSystem (Signal<GameEvent> gameEventSignal){
        this.gameEventSignal = gameEventSignal;
        eventQueue = new EventQueue();
        gameEventSignal.add(eventQueue);}

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(Position.class, Velocity.class).get());
        localPlayers = engine.getEntitiesFor(Family.all(LocalPlayer.class).get());
        walls = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.BoundsBox.class).exclude(com.tdt4240.jankenmaze.gameecs.components.PlayerInfo.class, com.tdt4240.jankenmaze.gameecs.components.PowerUpInfo.class).get());

    }

    public void update(float dt){
        if(entities != null){
            for (int i = 0; i < entities.size(); i++){
                Entity entity = entities.get(i);
                Position position = positionMapper.get(entity);
                Velocity velocity = velocityMapper.get(entity);
                BoundsBox bounds = boundsBoxMapper.get(entity);
                if (entity == localPlayers.get(0)) {
                    System.out.println("Before checking whether future velocity will be needed");
                    System.out.println("futureVelocityX = " + velocity.futureX);
                    System.out.println("futureVelocityY = " + velocity.futureY);
                    System.out.println("currentVelocityX = " + velocity.currentX);
                    System.out.println("currentVelocityY = " + velocity.currentY);
                    System.out.println("if(velocity.futureX != 0 || velocity.futureY != 0) ");
                    if(velocity.futureX != 0 || velocity.futureY != 0) {
                        bounds.boundsBox.setX(position.x+velocity.futureX*dt);
                        bounds.boundsBox.setY(position.y+velocity.futureY*dt);
                        System.out.println("Updated Position to test position from future velocity"); //DEBUGGING
                        boolean collision = false;
                        for(int k=0; k < walls.size(); k++) {
                            Rectangle wallBox = bb.get(walls.get(k)).boundsBox;
                            //If future velocity causes a collision
                            if (bounds.boundsBox.overlaps(wallBox)) {
                                collision = true;
                            }
                        }
                        if(collision){
                                System.out.println("Position from future velocity caused a collision, so position was not updated"); //DEBUGGING
                                bounds.boundsBox.setX(position.x += velocity.currentX * dt);
                                bounds.boundsBox.setY(position.y += velocity.currentY * dt);
                        }
                        else {
                            System.out.println("The position from future velocity worked well, so current velocity has inherited the values from future velocity.");
                            velocity.currentX = velocity.futureX;
                            velocity.currentY = velocity.futureY;
                            velocity.futureY = 0;
                            velocity.futureX = 0;
                            System.out.println("futureVelocityX = " + velocity.futureX);
                            System.out.println("futureVelocityY = " + velocity.futureY);
                            System.out.println("currentVelocityX = " + velocity.currentX);
                            System.out.println("currentVelocityY = " + velocity.currentY);
                        }
                    }
                    else {
                        bounds.boundsBox.setX(position.x += velocity.currentX * dt);
                        bounds.boundsBox.setY(position.y += velocity.currentY * dt);
                    }
                }
                else {
                    position.x += velocity.currentX * dt;
                    position.y += velocity.currentY * dt;
                    bounds.boundsBox.setX(position.x);
                    bounds.boundsBox.setY(position.y);
                }
            }
        }
    }
}

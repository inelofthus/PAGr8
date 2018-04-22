package com.tdt4240.jankenmaze.gameecs.systems;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.events.GameVariable;
import com.tdt4240.jankenmaze.gameecs.events.VariableQueue;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;

/**
 * Created by bartosz on 3/15/18.
 * *
 * Super-quick How-to-System:
 * 1: Implement the appropiate logic (i.e. move the entities who're supposed to be moved by device input or draw drawable stuff)
 * 2: Make an Array of relevant entites in addedToEnginge
 * -- Important: Here the array of entities is null until addedToEngine is called.
 * -- Note that addedToEngine will be called automatically by the engine.
 * -- Note also that it's in addedToEngine the entity-selection takes place. (Family.all(...))
 * 3: In update(float dt), apply logic to the entities (like movement).
 * 4: The ComponentMappers makes it easier to select the right entity-components (and gives good performance)
 * -- See line 30 and 42 for example.
 */

/**
 You have to check for three types of entity. Players, PowerUps and walls. Players have PlayerInfo,
 and wall have BoundingBox but not other "infoComponent". So, you have to have three arrays, one for each entityType,
 then you have to check if a player entity collides with any other player, powerUp or wall and apply the right method.
 Remember, who eats who is in PlayerInfo and PowerUpType is in PowerUpInfo. The wall just sets the position outside the wall.

 ADDITIONAL NOTE: The powerUps are not implemented as in the latest release due too short deadlines.
 */

public class CollisionSystem extends EntitySystem {
    private Signal<GameEvent> playerCollisionSignal;
    private EventQueue eventQueue;
    private ImmutableArray<Entity> powerUps;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> localPlayer;
    private ImmutableArray<Entity> walls;
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.BoundsBox> bb= ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.BoundsBox.class);
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.PlayerInfo> pi = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.PlayerInfo.class);

    public CollisionSystem(Signal<GameEvent> playerCollisionSignal){
        //creates playerCollisionSignal
        this.playerCollisionSignal = playerCollisionSignal;
        eventQueue = new EventQueue();
        playerCollisionSignal.add(eventQueue);
    }


    //gets all the entities with given component(s).
    public void addedToEngine(Engine engine){
        //get all entities with PowerUpInfo
        powerUps = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.PowerUpInfo.class).get());
        //get remote players in GameState
        players = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.Remote.class).get());
        //get localPLayer
        localPlayer = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.LocalPlayer.class).get());
        //get all walls
        walls = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.BoundsBox.class).exclude(com.tdt4240.jankenmaze.gameecs.components.PlayerInfo.class, com.tdt4240.jankenmaze.gameecs.components.PowerUpInfo.class).get());

    }

    //call PowerUpSystem on powerups.
    public void collisionWithWall(Entity player, Rectangle wall){
        //gets the velocity of player
        com.tdt4240.jankenmaze.gameecs.components.Velocity playerVelocity
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Velocity.class).get(player);
        //gets the position of player
        com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(player);
        //checks if player is moving horizontally
        if (playerVelocity.currentX != 0){
            if(playerVelocity.currentX > 0){
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.x = playerPosition.x + (wall.getX() - playerPosition.x) - bb.get(player).boundsBox.getWidth();
                //zeroes the playerVelocity
                playerVelocity.currentX = 0;
            }else{
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.x = playerPosition.x +(wall.getX() - playerPosition.x) + wall.getWidth();
                //zeroes the playerVelocity
                playerVelocity.currentX = 0;

            }
        }else {
            if(playerVelocity.currentY > 0){
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.y = playerPosition.y + (wall.getY() - playerPosition.y) - bb.get(player).boundsBox.getHeight();
                //zeros the playerVelocity
                playerVelocity.currentY = 0;
            }else if (playerVelocity.currentY < 0){
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.y = playerPosition.y + (wall.getY() - playerPosition.y) + wall.getHeight();
                //zeros the playerVelocity
                playerVelocity.currentY = 0;
            }

        }
    }

    //updates the system
    public void update(float dt){
        //checks it there is anything that can collide

        /**
         * This is unnecessarily time consuming as you will be ok with checking just 50% of the entities.
         * Or maybe not as it has to collide with powerUp && wall
         * There has to be a smarter way to implement it, sadly it was not found.
         */
        //checks if a localplayer exists and in there are walls in the game.
        if (walls != null && players != null && localPlayer!=null){
            //check if there is any collision
            for (int i=0; i < localPlayer.size(); i++){
                //checks if player collide
                Entity player1 = localPlayer.get(i);
                for (int k=0; k < players.size(); k++){
                    //checks if looking at the same entity
                    Entity player2 = players.get(k);
                    //checks if player1 collides with player2
                    if(bb.get(player1).boundsBox.overlaps(bb.get(player2).boundsBox)){

                        for (PlayerType targetedBy: pi.get(player1).targetedBy){
                            if (targetedBy.equals(pi.get(player2).type)) {
                                //sends a playerCollisionSignal
                                playerCollisionSignal.dispatch(GameEvent.PLAYER_COLLISION);
                            }
                        }

                    }
                }
                // if there are powerUps in the game
                if (powerUps != null){
                    //for every powerUp
                    for (int k=0; k < powerUps.size(); k++){
                        Entity powerUp = powerUps.get(k);
                        //checks if player1 collides with powerUp
                        if(bb.get(player1).boundsBox.contains(bb.get(powerUp).boundsBox)){
                            //call powerUp system it it collides.
                            //TODO Implement in later releases.
                        }
                    }
                }
                //for every wall in the game
                for(int k=0; k < walls.size(); k++){
                    //get the boundary box
                    Rectangle wallBox = bb.get(walls.get(k)).boundsBox;
                    //check if the player collides with the wall
                    if(bb.get(player1).boundsBox.overlaps(wallBox)){
                        //we want player to be outside wall
                        collisionWithWall(player1,wallBox);
                    }
                }
            }
        }
    }
}

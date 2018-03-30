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
 */

public class CollisionSystem extends EntitySystem {
    //not sure it an immutable array is 100% suited for the purpose of this system.
    //TODO check it in later stage of developement.
    private ImmutableArray<Entity> powerUps;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> walls;
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.BoundsBox> bb= ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.BoundsBox.class);
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.PlayerInfo> pi = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.PlayerInfo.class);

    public CollisionSystem(){}


    //gets all the entities with given component(s).
    public void addedToEngine(Engine engine){
        //get all entities with PowerUpInfo
        powerUps = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.PowerUpInfo.class).get());
        //get all players in GameState
        players = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.PlayerInfo.class).get());
        //get all walls
        walls = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.BoundsBox.class).exclude(com.tdt4240.jankenmaze.gameecs.components.PlayerInfo.class, com.tdt4240.jankenmaze.gameecs.components.PowerUpInfo.class).get());

    }

    //call PowerUpSystem on powerups.
    public void collisionWithWall(Entity player, Rectangle wall){
        com.tdt4240.jankenmaze.gameecs.components.Velocity playerVelocity
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Velocity.class).get(player);
        com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(player);
        //checks if player is moving horizontally
        if (playerVelocity.x != 0){
            if(playerVelocity.x > 0){
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.x = playerPosition.x + (wall.getX() - playerPosition.x) - bb.get(player).boundsBox.getWidth();
                playerVelocity.x = 0;
            }else{
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.x = playerPosition.x +(wall.getX() - playerPosition.x) + wall.getWidth();
                playerVelocity.x = 0;

            }
        }else {
            if(playerVelocity.y > 0){
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.y = playerPosition.y + (wall.getX()- playerPosition.y) - bb.get(player).boundsBox.getHeight();
                playerVelocity.y = 0;
            }else{
                // gets the difference between the position of wal and position of player and moves player outside the wall.
                playerPosition.y = playerPosition.y + (wall.getX() - playerPosition.y) + wall.getHeight();
                playerVelocity.y = 0;
            }

        }

    }

    //updates the system
    public void update(float dt){
        //checks it there is anything that can collide

        /**
         * This is unnecessarily time consuming as you will be ok with checking just 50% of the entities.
         * Or maybe not as it has to collide with powerUp && wall
         */
        if (walls != null && players != null){
            //check if there is any collision
            for (int i=0; i < players.size(); i++){
                //checks if player collide
                Entity player1 = players.get(i);
                for (int k=0; k < players.size(); k++){
                    //checks if looking at the same entity
                    Entity player2 = players.get(k);
                    if(!player1.equals(player2)){
                        //checks if player1 collides with player2
                        if(bb.get(player1).boundsBox.contains(bb.get(player2).boundsBox)){
                            if(pi.get(player1).target.equals(pi.get(player2).type)){
                                //player1 kills player2
                                //call healthsystem with some argument
                                //  decreaseHealth(player2,1);
                            }else if(pi.get(player2).target.equals(pi.get(player1).type)){
                                //player2 kills player1
                                //call healthsystem with some argument
                                // decreaseHealth(player1,1);
                            }
                        }
                    }
                }
                if (powerUps != null){
                    for (int k=0; k < powerUps.size(); k++){
                        Entity powerUp = powerUps.get(k);
                        //checks if player1 collides with powerUp
                        if(bb.get(player1).boundsBox.contains(bb.get(powerUp).boundsBox)){
                            //call powerUp system
                        }
                    }
                }
                for(int k=0; k < walls.size(); k++){

                    Rectangle wallBox = bb.get(walls.get(k)).boundsBox;

                    if(bb.get(player1).boundsBox.overlaps(wallBox)){

                        //we want player to be outside wall
                        collisionWithWall(player1,wallBox);
                    }
                }
            }


        }
    }
}

package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameMessages.HealthMessage;
import com.tdt4240.jankenmaze.gameecs.EntityManager;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Unoccupied;
import com.tdt4240.jankenmaze.gameecs.components.Velocity;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.events.GameVariable;
import com.tdt4240.jankenmaze.gameecs.events.VariableQueue;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

import java.util.Random;

/**
 * Created by jonas on 18/03/2018.
 * This system's purpose is to decrease/increase or set the health of players.
 */

public class HealthSystem extends EntitySystem {
    //TODO: Componentmapper and values
    private Signal<GameEvent> playerCollisionSignal;
    private EventQueue collisionQueue;
    private Signal<GameEvent> gameOverSignal;
    private EventQueue gameOverQueue;
    private Signal<GameEvent> decreaseHealthSignal;
    private EventQueue healthQueue;
    private ComponentMapper<com.tdt4240.jankenmaze.gameecs.components.Health> healthComponentMapper =ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Health.class);
    private ComponentMapper<Velocity> velocityComponentMapper = ComponentMapper.getFor(Velocity.class);
    private ComponentMapper<PlayerNetworkData> playerDataCompMapper = ComponentMapper.getFor(PlayerNetworkData.class);
    ImmutableArray<Entity> localPlayer;
    Random rand = new Random();
    private ImmutableArray<Entity> spawnPositions;
    private ImmutableArray<Entity> remotePlayers;




    public HealthSystem(Signal<GameEvent> playerCollisionSignal, Signal<GameEvent> gameOverSignal, Signal<GameEvent> decreaseHealthSignal) {
        //creates the CollisionSignal
        this.playerCollisionSignal = playerCollisionSignal;
        collisionQueue = new EventQueue();
        this.playerCollisionSignal.add(collisionQueue);
        //creates the GameOverSignal
        this.gameOverSignal=gameOverSignal;
        gameOverQueue= new EventQueue();
        this.gameOverSignal.add(gameOverQueue);
        //creates the decreaseHealthSignal
        this.decreaseHealthSignal=decreaseHealthSignal;
        healthQueue=new EventQueue();
        this.decreaseHealthSignal.add(healthQueue);
    }

    public void increaseHealth(Entity entity, int delta){
        //TODO: Logic
        //this method is not implemented in the latest release

    }

    //this method decreases the health of a player
    //it is currently optimized for decreasing the health of localplayer
  public void decreaseHealth(Entity player, int delta){

        //get the healthComponent for player
        Health health=healthComponentMapper.get(player);
        //get the velocityComponent for player
        Velocity velocity = velocityComponentMapper.get(player);
        velocity.currentX = 0;
        velocity.currentY = 0;
        velocity.futureX = 0;
        velocity.futureY = 0;
        //decrease health
        health.health=health.health-Math.abs(delta);
        //dispatch a DECREASE_HEALTH signal
        decreaseHealthSignal.dispatch(GameEvent.DECREASE_HEALTH);
        //update the health of player in HealthMessage
        HealthMessage.getInstance().updatePlayerHealth(ComponentMapper.getFor(PlayerNetworkData.class).get(localPlayer.first()).participantId, new Health(health.health));

        //check if players health is zero or less
      if (health.health<=0){
            // GAME OVER
          try {
              //give other devices some time to calculate the messages they receive.
              Thread.sleep(40);
          }catch (Exception e){

          }
            //dispatch a GAME_OVER signal
            gameOverSignal.dispatch(GameEvent.GAME_OVER);
      //
      }else{
            //The player has to go a random place on the map
            //get a random int smaller or equal to the amount of spawnPosition on the map
            int randomNumber = rand.nextInt(spawnPositions.size());
            //get the random spawnPosition
            Entity spawn= spawnPositions.get(randomNumber);
            //get player's positionComponent
            com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(player);
            //get spawnPosition's positionComponent
            com.tdt4240.jankenmaze.gameecs.components.Position spawnPos
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(spawn);

            //set playerPosition equal spawnPosition
            playerPosition.x=spawnPos.x;
            playerPosition.y=spawnPos.y;

        }

    }

    @Override
    public void addedToEngine(Engine engine) {
        //get localPLayer
        localPlayer = engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.LocalPlayer.class).get());
        //get all spawnPositions
        spawnPositions = engine.getEntitiesFor(Family.all(Unoccupied.class).get());
        //get remotePlayers
        remotePlayers=engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.Remote.class).get());



    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
      // decreases Health when local player has been eaten.
        for (GameEvent event: collisionQueue.getEvents()){
           decreaseHealth(localPlayer.get(0),1);
        }
        // if the healthHashMap in HealthMessage has been altered
        if (HealthMessage.getInstance().hasChanged){
            //call the updateRemotePlayerHealth
                updateRemotePlayerHealth();
        }




    }

    private void updateRemotePlayerHealth() {
        //change the hasChange variable from true to false
      HealthMessage.getInstance().hasChanged = false;
      //for every remotePlayer
      for (Entity remotePlayer : remotePlayers){
          //get networkData
          PlayerNetworkData netData = playerDataCompMapper.get(remotePlayer);
          //get player's health from HealtMessage
          Health newHealth=HealthMessage.getInstance().getPlayerHealth().get(netData.participantId);
          //get player's healthComponent
          Health healthComp = healthComponentMapper.get(remotePlayer);
          //update health
          healthComp.health=newHealth.health;

      }

    }
}


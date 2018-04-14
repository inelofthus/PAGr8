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
import com.tdt4240.jankenmaze.gameecs.components.PlayerInfo;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gameecs.events.GameVariable;
import com.tdt4240.jankenmaze.gameecs.events.VariableQueue;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;
import com.tdt4240.jankenmaze.gamesettings.PlayerTypes;

import java.util.Random;

/**
 * Created by jonas on 18/03/2018.
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
    private ComponentMapper<PlayerInfo> playerInfoCompMapper = ComponentMapper.getFor(PlayerInfo.class);
    ImmutableArray<Entity> localPlayer;
    Random rand = new Random();
    private ImmutableArray<Entity> spawnPositions;
    private ImmutableArray<Entity> remotePlayers;
    private ImmutableArray<Entity> bots;
    private Entity paper;
    private Entity scissors;



    public HealthSystem(Signal<GameEvent> playerCollisionSignal, Signal<GameEvent> gameOverSignal, Signal<GameEvent> decreaseHealthSignal) {
        //creates the CollisionSignal
        this.playerCollisionSignal = playerCollisionSignal;
        collisionQueue = new EventQueue();
        this.playerCollisionSignal.add(collisionQueue);
        //creates the GameOverSignal
        this.gameOverSignal=gameOverSignal;
        gameOverQueue= new EventQueue();
        this.gameOverSignal.add(gameOverQueue);
        this.decreaseHealthSignal=decreaseHealthSignal;
        healthQueue=new EventQueue();
        this.decreaseHealthSignal.add(healthQueue);
    }

    public void increaseHealth(Entity player, int delta){
        Health health=healthComponentMapper.get(player);
        Velocity velocity = velocityComponentMapper.get(player);
        //increase health
        health.health=health.health+Math.abs(delta);
        decreaseHealthSignal.dispatch(GameEvent.DECREASE_HEALTH);

    }
  public void decreaseHealth(Entity player, int delta){

        //get the healthComponent for player
        Health health=healthComponentMapper.get(player);
        Velocity velocity = velocityComponentMapper.get(player);
        velocity.currentX = 0;
        velocity.currentY = 0;
        velocity.futureX = 0;
        velocity.futureY = 0;
        //decrease health
        health.health=health.health-Math.abs(delta);
        decreaseHealthSignal.dispatch(GameEvent.DECREASE_HEALTH);
        if (health.health<=0){
            // GAME OVER
            gameOverSignal.dispatch(GameEvent.GAME_OVER);
        }else{
            //Go to fucking spawn.
            //workaround the problem with static context
            int randomNumber = rand.nextInt(spawnPositions.size());
            Entity spawn= spawnPositions.get(randomNumber);
            com.tdt4240.jankenmaze.gameecs.components.Position playerPosition
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(player);
            com.tdt4240.jankenmaze.gameecs.components.Position spawnPos
                    = ComponentMapper.getFor(com.tdt4240.jankenmaze.gameecs.components.Position.class).get(spawn);
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
        //get bots
        bots=engine.getEntitiesFor(Family.all(com.tdt4240.jankenmaze.gameecs.components.Bot.class).get());


    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        if (!GameSettings.getInstance().isMultiplayerGame) {
            for (Entity bot:bots) {
                if (playerInfoCompMapper.get(bot).type == PlayerType.PAPER) {
                    paper = bot;
                }
                else if (playerInfoCompMapper.get(bot).type == PlayerType.SCISSORS) {
                    scissors = bot;
                }
            }
        }
      // decreases Health when local player has been eaten.
        for (GameEvent event: collisionQueue.getEvents()){
            if (event == GameEvent.PLAYER_COLLISION) { //Player gets killed by a remote paper
                decreaseHealth(localPlayer.get(0),1);
            }
            else if (event == GameEvent.PLAYER_PAPER_COLLISION) { //Player gets killed by a local paper bot
                decreaseHealth(localPlayer.get(0),1);
                increaseHealth(paper, 1);
            }
            else if (event == GameEvent.PLAYER_SCISSORS_COLLISION) { //Player kills a local scissors bot
                increaseHealth(localPlayer.get(0),1);
                decreaseHealth(scissors, 1);
            }
            else if (event == GameEvent.PAPER_SCISSORS_COLLISION) {
                increaseHealth(scissors, 1);
                decreaseHealth(paper, 1);
            }
        }


        if (GameSettings.getInstance().isMultiplayerGame){
            if (HealthMessage.getInstance().hasChanged){
                updateRemotePlayerHealth();
            }
        }




    }

    private void updateRemotePlayerHealth() {
      HealthMessage.getInstance().hasChanged = false;
      for (Entity remotePlayer : remotePlayers){
          PlayerNetworkData netData = playerDataCompMapper.get(remotePlayer);
          Health newHealth=HealthMessage.getInstance().getRemotePlayerHealth().get(netData.participantId);
          Health healthComp = healthComponentMapper.get(remotePlayer);
          healthComp.health=newHealth.health;

      }

    }
}


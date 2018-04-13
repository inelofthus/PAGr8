package com.tdt4240.jankenmaze.gameMessages;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by karim on 12/04/2018.
 */

public class HealthMessage {
    public static final HealthMessage INSTANCE = new HealthMessage();

    private HashMap<String, Health> playerHealth ;
    private String results;
    public boolean hasChanged = false;

    public HealthMessage() {
        List<PlayerNetworkData> players = GameSettings.getInstance().getPlayers();
        this.playerHealth= new HashMap<String, Health>();
        for (PlayerNetworkData player : players){
            playerHealth.put(player.participantId, new Health(3));
        }
    }

    public static HealthMessage getInstance(){
        return INSTANCE;
    }

    public void updatePlayerHealth(String playerId, Health health){
        playerHealth.put(playerId, health);
    }

    public HashMap<String, Health> getPlayerHealth() {
        return playerHealth;
    }


    public void gameOver(Engine engine){
        SortedMap<Integer,String> map = Collections.synchronizedSortedMap(new TreeMap<Integer, String>(Collections.<Integer>reverseOrder()));
        ImmutableArray<Entity> players = engine.getEntitiesFor(Family.all(PlayerNetworkData.class).get());
        ComponentMapper<PlayerNetworkData> playerNetworkDataMapper=ComponentMapper.getFor(PlayerNetworkData.class);
        for(Entity player: players){
            PlayerNetworkData playerNetworkData=playerNetworkDataMapper.get(player);
            map.put(playerHealth.get(playerNetworkData.participantId).health,playerNetworkData.displayName);
        }
        results="";
        for(Map.Entry<Integer,String> entry : map.entrySet()){
          results=results+entry.getValue()+" has " + entry.getKey()+" lives.\n";

        }
    }

    public String getResults() {
        return results;
    }
}

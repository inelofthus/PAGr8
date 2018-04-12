package com.tdt4240.jankenmaze.gameMessages;

import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

import java.util.HashMap;
import java.util.List;

/**
 * Created by karim on 12/04/2018.
 */

public class HealthMessage {
    public static final HealthMessage INSTANCE = new HealthMessage();

    private HashMap<String, Health> remotePlayerHealth ;

    public HealthMessage() {
        List<PlayerNetworkData> players = GameSettings.getInstance().getPlayers();
        this.remotePlayerHealth= new HashMap<String, Health>();
        for (PlayerNetworkData player : players){
            remotePlayerHealth.put(player.participantId, new Health(3));
        }
    }

    public static HealthMessage getInstance(){
        return INSTANCE;
    }

    public void updateRemotePlayerHealth(String playerId, Health health){
        remotePlayerHealth.put(playerId, health);
    }

    public HashMap<String, Health> getRemotePlayerHealth() {
        return remotePlayerHealth;
    }
}

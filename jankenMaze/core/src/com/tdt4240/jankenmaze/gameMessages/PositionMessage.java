package com.tdt4240.jankenmaze.gameMessages;

import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

import java.util.HashMap;
import java.util.List;

/**
 * Created by karim on 12/04/2018.
 */

public class PositionMessage {
    private static final PositionMessage INSTANCE = new PositionMessage();
    private HashMap<String, Position> remotePlayerPositions ;


    public PositionMessage() {
        List<PlayerNetworkData> players = GameSettings.getInstance().getPlayers();
        this.remotePlayerPositions= new HashMap<String, Position>();
        for (PlayerNetworkData player : players){
            remotePlayerPositions.put(player.participantId, new Position(0, 0));
        }

    }


    public static PositionMessage getInstance(){
        return INSTANCE;
    }

    public void updateRemotePlayerPostion(String playerId, Position position) {
        remotePlayerPositions.put(playerId,position);
    }

    public HashMap<String, Position> getRemotePlayerPositions() {
        return remotePlayerPositions;
    }

    public void reset() {
        List<PlayerNetworkData> players = GameSettings.getInstance().getPlayers();
        this.remotePlayerPositions= new HashMap<String, Position>();
        for (PlayerNetworkData player : players){
            remotePlayerPositions.put(player.participantId, new Position(0, 0));
        }
    }
}

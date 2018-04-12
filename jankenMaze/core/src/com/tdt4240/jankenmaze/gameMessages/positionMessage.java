package com.tdt4240.jankenmaze.gameMessages;

import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

import java.util.HashMap;
import java.util.List;

/**
 * Created by karim on 12/04/2018.
 */

public class positionMessage {
    private static final positionMessage INSTANCE = new positionMessage();
    private HashMap<String, Position> remotePlayerPositions ;


    public positionMessage() {
        List<PlayerNetworkData> players = GameSettings.getInstance().getPlayers();
        this.remotePlayerPositions= new HashMap<String, Position>();
        for (PlayerNetworkData player : players){
            remotePlayerPositions.put(player.participantId, new Position(0, 0));
        }

    }


    public static positionMessage getInstance(){
        return INSTANCE;
    }

    public void updateRemotePlayerPostion(String playerId, Position position) {
        remotePlayerPositions.put(playerId,position);
    }

    public HashMap<String, Position> getRemotePlayerPositions() {
        return remotePlayerPositions;
    }

}

package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by karim on 05/04/2018.
 */

public class PlayerNetworkData implements Component {
    public String participantId = "";
    public String playerId = "";
    public String displayName = "";
    public boolean isSelf = false;

    public PlayerNetworkData(String playerId, String participantId, String displayName) {
        this.playerId = playerId;
        this.participantId = participantId;
        this.displayName = displayName;
    }

}

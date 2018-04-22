package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by karim on 05/04/2018.
 *This component consists of 3 strings and 1 boolean. The strings are:
 * 1: participantId that stores the players ID with respect to the gameRoom.
 * 2: playerId that stores the players ID with respect to the Google Play account.
 * 3: displayName that stores the name of players Google Play account.
 * The boolean stores if the player is local player on the device.
 */

public class PlayerNetworkData implements Component {
    public String participantId = "";
    public String playerId = "";
    public String displayName = "";
    public boolean isLocalPlayer = false;

    public PlayerNetworkData(String playerId, String participantId, String displayName) {
        this.playerId = playerId;
        this.participantId = participantId;
        this.displayName = displayName;
    }

}

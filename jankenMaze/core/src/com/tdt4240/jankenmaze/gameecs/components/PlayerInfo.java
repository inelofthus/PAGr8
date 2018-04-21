package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;
import com.tdt4240.jankenmaze.gamesettings.PlayerTypes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonas on 07/03/2018.
 * This component consists of two PlayerTypes that provide the info what type of
 * playable character a player is and by what type of playable character he is targeted by.
 */

public class PlayerInfo implements Component {
    public ArrayList<PlayerType> targetetBy;
    public PlayerType type;

    public PlayerInfo(PlayerType type){
        this.targetetBy = PlayerTypes.getPlayerTargetedBy(type);
        this.type = type;
    }
}

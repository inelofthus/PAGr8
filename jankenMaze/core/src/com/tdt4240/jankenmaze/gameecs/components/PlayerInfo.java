package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;
import com.tdt4240.jankenmaze.gamesettings.PlayerTypes;

import java.util.HashMap;

/**
 * Created by jonas on 07/03/2018.
 * This component consists of two PlayerTypes that provide the info what type 
 */

public class PlayerInfo implements Component {
    public PlayerType targetetBy;
    public PlayerType type;

    public PlayerInfo(PlayerType type){
        this.targetetBy = PlayerTypes.getPlayerTargetedBy(type);
        this.type = type;
    }
}

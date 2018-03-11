package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jonas on 07/03/2018.
 */

public class PlayerInfo implements Component {
    public String target;
    public String targetetBy;
    public String type;

    public PlayerInfo(String target, String targetetBy, String type){
        this.target = target;
        this.targetetBy = targetetBy;
        this.type = type;
    }
}

package com.tdt4240.jankenlabyrinth.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Ine on 07.03.2018.
 */

public class PowerUpInfo implements Component {
    public String powerUpType;
    public int duration;

    public PowerUpInfo(String powerUpType, int duration){
        this.powerUpType = powerUpType;
        this.duration = duration;
    }
}

package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Ine on 07.03.2018.
 * This component is not used in the game as powerUps aren't implemented.
 * It consist of a String that stores the type of powerUp and of an int that stores
 * the duration of powerup in seconds.
 */

public class PowerUpInfo implements Component {
    public String powerUpType;
    public int duration;

    public PowerUpInfo(String powerUpType, int duration){
        this.powerUpType = powerUpType;
        this.duration = duration;
    }
}

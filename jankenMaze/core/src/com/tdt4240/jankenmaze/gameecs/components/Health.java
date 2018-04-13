package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jonas on 07/03/2018.
 * This component consists of an int with the healthValue of playerEntity
 */

public class Health implements Component {
    public int health;
    public Health(int hp){
        this.health = hp;
    }
}

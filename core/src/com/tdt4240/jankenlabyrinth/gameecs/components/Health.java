package com.tdt4240.jankenlabyrinth.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jonas on 07/03/2018.
 */

public class Health implements Component {
    public int health;
    public Health(int hp){
        this.health = hp;
    }
}

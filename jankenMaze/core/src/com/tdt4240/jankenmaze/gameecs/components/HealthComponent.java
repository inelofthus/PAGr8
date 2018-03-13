package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jonas on 07/03/2018.
 */

public class HealthComponent implements Component {
    public int health;
    public HealthComponent(int hp){
        this.health = hp;
    }
}

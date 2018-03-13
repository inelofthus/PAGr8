package com.tdt4240.jankenlabyrinth.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jonas on 07/03/2018.
 */

public class Velocity implements Component {
    public float x = 0;
    public float y = 0;

    public Velocity(float x, float y){
        this.x = x;
        this.y = y;
    }
}

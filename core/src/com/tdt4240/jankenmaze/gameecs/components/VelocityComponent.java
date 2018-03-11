package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jonas on 07/03/2018.
 */

public class VelocityComponent implements Component {
    public float x = 0;
    public float y = 0;

    public VelocityComponent(float x, float y){
        this.x = x;
        this.y = y;
    }
}

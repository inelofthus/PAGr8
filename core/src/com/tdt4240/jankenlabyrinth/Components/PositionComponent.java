package com.tdt4240.jankenlabyrinth.Components;

import com.badlogic.ashley.core.Component;
/**
 * Created by jonas on 06/03/2018.
 */

public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent(float inX, float inY){
        this.x = inX;
        this.y = inY;
    }
}

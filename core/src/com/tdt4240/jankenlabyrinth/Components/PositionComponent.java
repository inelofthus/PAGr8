package com.tdt4240.jankenlabyrinth.Components;

import com.badlogic.ashley.core.Component;
/**
 * Created by jonas on 06/03/2018.
 *
 * Super-quick How-to-Component:
 * 1: Public variables (or getters/setters but that's a bit overkill)
 * 2: NO logic! Keep all the logic in the Systems.
 * 3: Just store data (as implied by #2), and make a simple constructor.
 */

public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent(float inX, float inY){
        this.x = inX;
        this.y = inY;
    }
}

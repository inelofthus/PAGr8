package com.tdt4240.jankenlabyrinth.gameecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by jonas on 07/03/2018.
 */

public class BoundsBox implements Component {
    public Rectangle boundsBox;
    public BoundsBox(Rectangle bounds){
        this.boundsBox = bounds;
    }
    public BoundsBox(float x, float y, float width, float height){
        this.boundsBox = new Rectangle(x, y, width, height);
    }
}

package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jonas on 07/03/2018.
 * This component consists of 4 float values. Two of those stores the current velocity of entity
 * in x and y direction, and the other two stores the future velocity of entity in x and y direction.
 */

public class Velocity implements Component {
    public float currentX = 0;
    public float currentY = 0;
    public float futureX = 0;
    public float futureY = 0;

    public Velocity(float currentVelocityX, float currentVelocityY){
        this.currentX = currentVelocityX;
        this.currentY = currentVelocityY;
        this.futureX = 0;
        this.futureY = 0;
    }
}

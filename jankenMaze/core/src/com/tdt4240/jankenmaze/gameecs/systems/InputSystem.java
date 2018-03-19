package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.gdx.Gdx;
import com.tdt4240.jankenmaze.gameecs.components.*;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class InputSystem extends EntitySystem{
    private ImmutableArray<Entity> entities;
    private ComponentMapper<Velocity> velocityComponents = ComponentMapper.getFor(Velocity.class);

    //Note: Min X and Y are 0
    private float maxX;
    private float maxY;
    private float centerX;
    private float centerY;

    public InputSystem(){
        this.maxX = Gdx.graphics.getWidth();
        this.maxY = Gdx.graphics.getHeight();
        this.centerX = Gdx.graphics.getWidth() /2;
        this.centerY = Gdx.graphics.getHeight() /2;

        System.out.print("InputSystem created");
    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(LocalPlayer.class).get());
    }

    public void update(float dt){
        //TODO: Fix hardcoded value
        float velX = 250;
        float velY = 250;

        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();

        if(Gdx.input.isTouched()){
            if(pointInTriangle(touchX, touchY, maxX, 0, centerX, centerY, 0, 0)){
                velX = 0;
            }
            else if(pointInTriangle(touchX, touchY, maxX, maxY, centerX, centerY, maxX, 0)){
                velY = 0;
            }
            else if(pointInTriangle(touchX, touchY, 0, maxY, centerX, centerY, maxX, maxY)){
                velY = velY*-1;
                velX = 0;
            }
            else if(pointInTriangle(touchX, touchY, 0, 0, centerX, centerY, 0, maxY)){
                velX = velX*-1;
                velY = 0;
            }
        }
        else{
            //velX = 0;
            //velY = 0;
        }


        for(Entity e: entities){
            Velocity velocityComponent = velocityComponents.get(e);
            velocityComponent.x = velX;
            velocityComponent.y = velY;
        }
    }

    /*
    * Checks if point is within triangle defined by (t0, t1, and t2) using baryocentric coordinates.
    * Input-coords for triangle must be given counter-clockwise.
    * See https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle (andreasdr)
    */
    private boolean pointInTriangle(
            float pointX, float pointY, float t0x, float t0y, float t1x, float t1y, float t2x, float t2y){

        float area = (float) (0.5 *(-t1y*t2x + t0y*(-t1x + t2x) + t0x*(t1y - t2y) + t1x*t2y));
        area = area > 0 ? area : area*-1; //Formula above may sometimes make the area (correct absolute value). This ensures a positive area.
        float s = 1/(2*area)*(t0y*t2x - t0x*t2y + (t2y - t0y)*pointX + (t0x - t2x)*pointY);
        float t = 1/(2*area)*(t0x*t1y - t0y*t1x + (t0y - t1y)*pointX + (t1x - t0x)*pointY);


        return (s>0 && t>0 && 1-s-t>0);
    }
}
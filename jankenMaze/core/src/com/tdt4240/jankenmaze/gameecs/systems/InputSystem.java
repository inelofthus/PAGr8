package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.tdt4240.jankenmaze.gameecs.components.*;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.events.EventQueue;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;

public class InputSystem extends EntitySystem{
    private ImmutableArray<Entity> entities;
    private Entity player;
    private ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);

    //Note: Min X and Y are 0


    float velX = 0;
    float velY = 0;
    float vel = 150;
    private float maxX;
    private float maxY;
    private float centerX;
    private float centerY;

    public InputSystem(Signal<GameEvent> gameEventSignal){
        this.maxX = Gdx.graphics.getWidth();
        this.maxY = Gdx.graphics.getHeight();
        this.centerX = Gdx.graphics.getWidth() /2;
        this.centerY = Gdx.graphics.getHeight() /2;

        //System.out.print("InputSystem created");
    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(LocalPlayer.class).get());
    }

    public boolean canPerformMove(Entity player, int xMove, int yMove) {

        return true; //TODO Check whether a collision occures with a given move.
    }

    public void update(float dt){
        //TODO: Fix hardcoded value


        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();

        if(Gdx.input.isTouched()) {

            if (pointInTriangle(touchX, touchY, maxX, 0, centerX, centerY, 0, 0)) {
                //player moves up,
                System.out.println("Player decided to move up");
                System.out.println("Before input sytem has done its thing:");
                Velocity velocity = velocityMapper.get(entities.get(0));
                System.out.println("velocity.currentX = " + velocity.currentX);
                System.out.println("velocity.currentY = " + velocity.currentY);
                System.out.println("velocity.futureX = " + velocity.futureX);
                System.out.println("velocity.futureY = " + velocity.futureY);
                velocity.futureX = 0;
                velocity.futureY = vel;
                System.out.println("After input sytem has done its thing:");
                System.out.println("velocity.currentX = " + velocity.currentX);
                System.out.println("velocity.currentY = " + velocity.currentY);
                System.out.println("velocity.futureX = " + velocity.futureX);
                System.out.println("velocity.futureY = " + velocity.futureY);
                //for (Entity e : entities) {
                //    Velocity velocity = velocityMapper.get(e);
                //    velocity.futureX = 0;
                //    velocity.futureY = vel;
                //    System.out.println("velocity.currentX = " + velocity.currentX);
                //    System.out.println("velocity.currentY = " + velocity.currentY);
                //    System.out.println("velocity.futureX = " + velocity.futureX);
                //    System.out.println("velocity.futureY = " + velocity.futureY);
                //}
            } else if (pointInTriangle(touchX, touchY, maxX, maxY, centerX, centerY, maxX, 0)) {
                //player moves right
                System.out.println("Player decided to move right");
                for (Entity e : entities) {
                    Velocity velocity = velocityMapper.get(e);
                    velocity.futureX = vel;
                    velocity.futureY = 0;
                }
            } else if (pointInTriangle(touchX, touchY, 0, maxY, centerX, centerY, maxX, maxY)) {
                //player moves down
                System.out.println("Player decided to move down");
                for (Entity e : entities) {
                    Velocity velocity = velocityMapper.get(e);
                    velocity.futureX = 0;
                    velocity.futureY = -vel;
                }
            } else if (pointInTriangle(touchX, touchY, 0, 0, centerX, centerY, 0, maxY)) {
                //player moves to the left
                System.out.println("Player decided to move left");
                for (Entity e : entities) {
                    Velocity velocity = velocityMapper.get(e);
                    velocity.futureX = -vel;
                    velocity.futureY = 0;
                }
            }
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
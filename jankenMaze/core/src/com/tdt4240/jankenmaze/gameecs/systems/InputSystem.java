package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
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
    private final float SPEED = 150;
    private Rectangle upRectangle ;
    private Rectangle downRectangle;
    private Rectangle leftRectangle;
    private Rectangle rightRectangle;

    public InputSystem(Signal<GameEvent> gameEventSignal){
        initializeInputGrid(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(LocalPlayer.class).get());
    }

    public void update(float dt){
        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();

        if(Gdx.input.isTouched()) {
            Velocity velocity = velocityMapper.get(entities.get(0));

            if (pointInRectangle(touchX, touchY, upRectangle)) {
                //player moves up,
                if (velocity.currentY != SPEED) {
                    velocity.futureX = 0;
                    velocity.futureY = SPEED;
                }
            } else if (pointInRectangle(touchX, touchY, rightRectangle)) {
                //player moves right
                if (velocity.currentX != SPEED) {
                    velocity.futureX = SPEED;
                    velocity.futureY = 0;
                }
            } else if (pointInRectangle(touchX, touchY, downRectangle)) {
                //player moves down
                if (velocity.currentY != -SPEED) {
                    velocity.futureX = 0;
                    velocity.futureY = -SPEED;
                }
            } else if (pointInRectangle(touchX, touchY, leftRectangle)) {
                //player moves to the left
                if (velocity.currentX != -SPEED) {
                    velocity.futureX = -SPEED;
                    velocity.futureY = 0;
                }
            }
        }
    }

    private boolean pointInRectangle(float pointX, float pointY, Rectangle rectangle){
        return rectangle.contains(pointX, pointY);
    }

    private void initializeInputGrid(float gameWidth, float gameHeight){
        float rectangleHeight = gameHeight / 3; //Grid is divided into 3 rectangles vertically
        float leftRightRectangleWidth = gameWidth / 3;
        this.upRectangle = new Rectangle(0, 0, gameWidth, rectangleHeight);
        this.downRectangle = new Rectangle(0, rectangleHeight * 2, gameWidth, rectangleHeight);
        this.leftRectangle = new Rectangle(0, rectangleHeight, leftRightRectangleWidth, rectangleHeight);
        this.rightRectangle = new Rectangle(gameWidth - leftRightRectangleWidth, rectangleHeight, leftRightRectangleWidth, rectangleHeight);
    }

    /*
    * Checks if point is within triangle defined by (t0, t1, and t2) using baryocentric coordinates.
    * Input-coords for triangle must be given counter-clockwise.
    * See https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle (andreasdr)
    */
    @Deprecated
    private boolean pointInTriangle(float pointX, float pointY, float t0x, float t0y, float t1x, float t1y, float t2x, float t2y){
        float area = (float) (0.5 *(-t1y*t2x + t0y*(-t1x + t2x) + t0x*(t1y - t2y) + t1x*t2y));
        area = area > 0 ? area : area*-1; //Formula above may sometimes make the area (correct absolute value). This ensures a positive area.
        float s = 1/(2*area)*(t0y*t2x - t0x*t2y + (t2y - t0y)*pointX + (t0x - t2x)*pointY);
        float t = 1/(2*area)*(t0x*t1y - t0y*t1x + (t0y - t1y)*pointX + (t1x - t0x)*pointY);

        return (s>0 && t>0 && 1-s-t>0);
    }
}
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
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;

public class InputSystem extends EntitySystem{
    private ImmutableArray<Entity> entities;
    private Entity localPlayer;
    private ImmutableArray<Entity> bots;
    private Entity player;
    private ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);
    private ComponentMapper<PlayerInfo> playerInfoCompMapper = ComponentMapper.getFor(PlayerInfo.class);
    private ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
    private Entity paper;
    private Entity scissors;

    private Position localPlayerPosition;
    private Position paperPosition;
    private Velocity paperVelocity;
    private Position scissorsPosition;
    private Velocity scissorsVelocity;


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
        if (!GameSettings.getInstance().isMultiplayerGame) {
            vel = 100;
        }
    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(LocalPlayer.class).get());
        bots = engine.getEntitiesFor(Family.all(Bot.class).get());
    }

    public void update(float dt){
        //TODO: Fix hardcoded value
        localPlayer = entities.get(0);
        if (!GameSettings.getInstance().isMultiplayerGame) {
            for (Entity bot:bots) {
                if (playerInfoCompMapper.get(bot).type == PlayerType.PAPER) {
                    paper = bot;
                }
                else if (playerInfoCompMapper.get(bot).type == PlayerType.SCISSORS) {
                    scissors = bot;
                }
            }
            localPlayerPosition = positionMapper.get(localPlayer);
            paperPosition = positionMapper.get(paper);
            paperVelocity = velocityMapper.get(paper);
            scissorsPosition = positionMapper.get(scissors);
            scissorsVelocity = velocityMapper.get(scissors);
        }

        float touchX = Gdx.input.getX();
        float touchY = Gdx.input.getY();

        if (!GameSettings.getInstance().isMultiplayerGame) {

            if (paperVelocity.futureX == 0 && paperVelocity.futureY == 0) {
                if (paperVelocity.currentX == 0 && paperVelocity.lastMove) {
                    if (paperPosition.x < localPlayerPosition.x) {
                        paperVelocity.futureX = vel;
                    } else if (paperPosition.x > localPlayerPosition.x) {
                        paperVelocity.futureX = -vel;
                    }
                    else {
                        paperVelocity.futureX = 0;
                    }
                }
                if (paperVelocity.currentY == 0 && !paperVelocity.lastMove) {
                    if (paperPosition.y < localPlayerPosition.y) {
                        paperVelocity.futureY = vel;
                    } else if (paperPosition.y > localPlayerPosition.y) {
                        paperVelocity.futureY = -vel;
                    }
                    else {
                        paperVelocity.futureY = 0;
                    }
                }
            }
            if (paperVelocity.lastMove) {
                paperVelocity.lastMove = false;
            }
            else {
                paperVelocity.lastMove = true;
            }

            if (scissorsVelocity.futureX == 0 && scissorsVelocity.futureY == 0) {
                if (scissorsVelocity.currentX == 0 && scissorsVelocity.lastMove) {
                    if (scissorsPosition.x < paperPosition.x) {
                        scissorsVelocity.futureX = vel;
                    } else if (scissorsPosition.x > paperPosition.x) {
                        scissorsVelocity.futureX = -vel;
                    }
                    else {
                        scissorsVelocity.futureX = 0;
                    }
                }
                if (scissorsVelocity.currentY == 0 && !scissorsVelocity.lastMove) {
                    if (scissorsPosition.y < paperPosition.y) {
                        scissorsVelocity.futureY = vel;
                    } else if (scissorsPosition.y > paperPosition.y) {
                        scissorsVelocity.futureY = -vel;
                    }
                    else {
                        scissorsVelocity.futureY = 0;
                    }
                }
            }
            if (scissorsVelocity.lastMove) {
                scissorsVelocity.lastMove = false;
            }
            else {
                scissorsVelocity.lastMove = true;
            }




            //Decides scissors position
            if (scissorsVelocity.currentX == 0 && scissorsVelocity.currentY == 0) {
                if (scissorsPosition.x < paperPosition.x && scissorsPosition.y < paperPosition.y) {
                    if (scissorsVelocity.lastMove) {
                        //if (scissorsVelocity.currentX == 0) {
                        scissorsVelocity.futureX = vel;
                        scissorsVelocity.futureY = 0;
                        //}
                    } else {
                        //if (scissorsVelocity.currentY == 0) {
                        scissorsVelocity.futureX = 0;
                        scissorsVelocity.futureY = vel;
                        //}
                    }
                } else if (scissorsPosition.x < paperPosition.x && scissorsPosition.y > paperPosition.y) {
                    if (scissorsVelocity.lastMove) {
                        //if (scissorsVelocity.currentX == 0) {
                        scissorsVelocity.futureX = vel;
                        scissorsVelocity.futureY = 0;
                        //}
                    } else {
                        //if (scissorsVelocity.currentY == 0) {
                        scissorsVelocity.futureX = 0;
                        scissorsVelocity.futureY = -vel;
                        //}
                    }
                } else if (scissorsPosition.x > paperPosition.x && scissorsPosition.y < paperPosition.y) {
                    if (scissorsVelocity.lastMove) {
                        //if (scissorsVelocity.currentX == 0) {
                        scissorsVelocity.futureX = -vel;
                        scissorsVelocity.futureY = 0;
                        //}
                    } else {
                        //if (scissorsVelocity.currentY == 0) {
                        scissorsVelocity.futureX = 0;
                        scissorsVelocity.futureY = vel;
                        //}
                    }
                } else if (scissorsPosition.x > paperPosition.x && scissorsPosition.y > paperPosition.y) {
                    if (scissorsVelocity.lastMove) {
                        //if (scissorsVelocity.currentX == 0) {
                        scissorsVelocity.futureX = -vel;
                        scissorsVelocity.futureY = 0;
                        //}
                    } else {
                        //if (scissorsVelocity.currentY == 0) {
                        scissorsVelocity.futureX = 0;
                        scissorsVelocity.futureY = -vel;
                        //}
                    }
                }
            }
        }


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
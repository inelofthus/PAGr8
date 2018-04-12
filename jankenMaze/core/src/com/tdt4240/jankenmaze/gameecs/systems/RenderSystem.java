package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Renderable;
import com.tdt4240.jankenmaze.gameecs.components.SpriteComponent;

/**
 * Created by jonas on 07/03/2018.
 * Draws sprites when in-game.
 */

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private SpriteBatch batch;
    OrthographicCamera camera;
    Viewport viewport;

    private ComponentMapper<Position> pm = ComponentMapper.getFor(Position.class);

    public RenderSystem(SpriteBatch sb){
        this.batch = sb;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        viewport = new FitViewport(800, 480, camera);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(
                Renderable.class, SpriteComponent.class, Position.class).get());
    }
    public void update(float dt){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Entity e: entities){
            SpriteComponent sCom = e.getComponent(SpriteComponent.class);
            Position pCom = e.getComponent(Position.class);

            //Sees if spritecomp. has been resized, in which case it's drawn to scale
            if(sCom.height != null && sCom.width != null){
                batch.draw(sCom.sprite, pCom.x, pCom.y, sCom.width, sCom.height);
            }
            else{
                batch.draw(sCom.sprite.getTexture(), pCom.x, pCom.y);
            }
        }
        batch.end();
    }
}


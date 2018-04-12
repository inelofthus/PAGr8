package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Renderable;
import com.tdt4240.jankenmaze.gameecs.components.SpriteComponent;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

/**
 * Created by jonas on 07/03/2018.
 * Draws sprites when in-game.
 */

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    public RenderSystem(SpriteBatch sb){
        GameSettings gameSettings = GameSettings.getInstance();
        this.batch = sb;
        camera = new OrthographicCamera();
        int viewportWidth = gameSettings.viewPortWidth;
        int viewportHeight = gameSettings.viewPortHeight;
        camera.setToOrtho(false, viewportWidth, viewportHeight);

        //Calculate screen bounds:
        Viewport viewport = new FitViewport(viewportWidth, viewportHeight, camera);
        float viewPortRatio = viewportWidth/viewportHeight;
        float screenRatio = Gdx.graphics.getWidth()/Gdx.graphics.getHeight();
        float screenBoundWidth;
        float screenBoundHeight;
        if(screenRatio > viewPortRatio){ //Phone has a wider screen than viewport
            screenBoundHeight = Gdx.graphics.getHeight();
            //If height == n * viewport height, we want width to be viewportWidth * n.
            screenBoundWidth = viewPortRatio * (screenBoundHeight/viewportHeight);
            float boundWidthDelta = (Gdx.graphics.getWidth() - screenBoundHeight) / 2;

            viewport.setScreenBounds((int) (0 + boundWidthDelta), 0,
                    (int) (screenBoundWidth - boundWidthDelta), (int) screenBoundHeight);
        }
        else if (screenRatio == viewPortRatio){
            //Screen has same aspect ratio as viewport, we can fill the whole screen
            viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        else{
            //Cropping the height of screen like we did to width
            screenBoundWidth = Gdx.graphics.getWidth();
            screenBoundHeight = (1/viewPortRatio) * (screenBoundWidth / viewportWidth);
            int  boundHeightDelta = (Gdx.graphics.getHeight() - viewportHeight) / 2;
            viewport.setScreenBounds(0, boundHeightDelta,
                    (int) screenBoundWidth, (int) screenBoundHeight - boundHeightDelta);
        }

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

            //Sees if spritecomponent has been resized, in which case it's drawn to scale
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


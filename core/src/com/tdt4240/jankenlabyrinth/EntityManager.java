package com.tdt4240.jankenlabyrinth;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenlabyrinth.Components.PositionComponent;
import com.tdt4240.jankenlabyrinth.Components.RenderableComponent;
import com.tdt4240.jankenlabyrinth.Components.SpriteComponent;
import com.tdt4240.jankenlabyrinth.Components.VelocityComponent;
import com.tdt4240.jankenlabyrinth.Systems.ControlledMovementSystem;
import com.tdt4240.jankenlabyrinth.Systems.RenderSystem;

/**
 * Created by jonas on 07/03/2018.
 */

public class EntityManager {
    private Engine engine;
    SpriteBatch batch;

    public EntityManager(Engine e, SpriteBatch sb){
        this.engine = e;
        this.batch = sb;

        ControlledMovementSystem cms = new ControlledMovementSystem();
        engine.addSystem(cms);
        RenderSystem rs = new RenderSystem(batch);
        engine.addSystem(rs);

        Entity testImageEntity = new Entity();
        testImageEntity.add(new PositionComponent(0,0))
                .add(new VelocityComponent(300,300))
                .add(new SpriteComponent((new Texture("badlogic.jpg"))))
                .add(new RenderableComponent());

        engine.addEntity(testImageEntity);
    }

    public void update(){
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public void draw(SpriteBatch batch){

    }
}

package com.tdt4240.jankenlabyrinth.gameecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenlabyrinth.gameecs.components.Position;
import com.tdt4240.jankenlabyrinth.gameecs.components.Renderable;
import com.tdt4240.jankenlabyrinth.gameecs.components.Token;
import com.tdt4240.jankenlabyrinth.gameecs.components.Velocity;
import com.tdt4240.jankenlabyrinth.gameecs.systems.ControlledMovement;
import com.tdt4240.jankenlabyrinth.gameecs.systems.Render;

/**
 * Created by jonas on 07/03/2018.
 * Implements the Entity Manager to use in gameplay.
 * Super-quick how-to-ECS:
 * 1: Have ONE Engine, preferably made in PlayState or something. Just one per ECS.
 * 2: Implement Systems (see Systems-package) and docstring in ControlledMovement
 * -- Systems add logic to all entities with given components.
 * -- Systems MUST be added to engine (such that update() etc can be called by it
 * -- Render currently draws everything.
 * 3: Add entities as shown below. You can string as many .add(EntitySystem) as you like.
 *
 * -- In the playState, where we call draw now, we should have a call to the EnitityManager  like
 *    currently shown in the JankenLabyrinth-class. That is spriteBatch.begin(); entityManger.update();
 *    spriteBatch.end();
 *
 *    Note that entityManger.update() calls the engine.update().
 *
 * -- See also the Position on how-to-Component if you need to.
 */

public class EntityManager {
    private Engine engine;
    SpriteBatch batch;

    public EntityManager(Engine e, SpriteBatch sb){
        this.engine = e;
        this.batch = sb;

        ControlledMovement cms = new ControlledMovement();
        engine.addSystem(cms);
        Render rs = new Render(batch);
        engine.addSystem(rs);
        Entity testImageEntity = new Entity();
        testImageEntity.add(new Position(0,0))
                .add(new Velocity(300,300))
                .add(new Token((new Texture("badlogic.jpg"))))
                .add(new Renderable());

        engine.addEntity(testImageEntity);
    }

    public void update(){
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public void draw(SpriteBatch batch){

    }
    public void setBatch(SpriteBatch batch){

    }

    public boolean hasNoSpriteBatch(){
        return (this.batch == null);
    }
}

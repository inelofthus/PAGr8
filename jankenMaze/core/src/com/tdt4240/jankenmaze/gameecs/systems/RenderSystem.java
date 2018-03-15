package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private ComponentMapper<Position> pm = ComponentMapper.getFor(Position.class);

    public RenderSystem(SpriteBatch sb){
        this.batch = sb;
    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(
                Renderable.class, SpriteComponent.class, Position.class).get());
    }
    public void update(float dt){
        batch.begin();
        for(Entity e: entities){
            SpriteComponent sCom = e.getComponent(SpriteComponent.class);
            Position pCom = e.getComponent(Position.class);
            batch.draw(sCom.sprite.getTexture(), pCom.x, pCom.y);
        }
        batch.end();
    }
}

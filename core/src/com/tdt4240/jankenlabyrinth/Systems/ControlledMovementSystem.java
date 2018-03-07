package com.tdt4240.jankenlabyrinth.Systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.tdt4240.jankenlabyrinth.Components.PositionComponent;
import com.tdt4240.jankenlabyrinth.Components.VelocityComponent;

/**
 * Created by jonas on 07/03/2018.
 */

public class ControlledMovementSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    public ControlledMovementSystem () {}

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    public void update(float dt){
        if(entities != null){
            for (int i = 0; i < entities.size(); i++){
                Entity entity = entities.get(i);
                PositionComponent pos = pm.get(entity);
                VelocityComponent vel = vm.get(entity);

                int screenTouched = Gdx.input.isTouched() ? 1 : 0;
                pos.x += vel.x * dt;
                //System.out.println("Updated");
            }
        }

    }
}

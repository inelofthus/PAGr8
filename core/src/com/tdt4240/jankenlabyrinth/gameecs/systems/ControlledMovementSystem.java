package com.tdt4240.jankenlabyrinth.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenlabyrinth.gameecs.components.PositionComponent;
import com.tdt4240.jankenlabyrinth.gameecs.components.VelocityComponent;

/**
 * Created by jonas on 07/03/2018.
 *
 * Super-quick How-to-System:
 * 1: Implement the appropiate logic (i.e. move the entities who're supposed to be moved by device input or draw drawable stuff)
 * 2: Make an Array of relevant entites in addedToEnginge
 * -- Important: Here the array of entities is null until addedToEngine is called.
 * -- Note that addedToEngine will be called automatically by the engine.
 * -- Note also that it's in addedToEngine the entity-selection takes place. (Family.all(...))
 * 3: In update(float dt), apply logic to the entities (like movement).
 * 4: The ComponentMappers makes it easier to select the right entity-components (and gives good performance)
 * -- See line 30 and 42 for example.
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

                pos.x += vel.x * dt;
            }
        }

    }
}

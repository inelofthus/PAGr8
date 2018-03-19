package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.components.BoundsBox;
import com.tdt4240.jankenmaze.gameecs.components.Velocity;

/**
 * Created by jonas on 18/03/2018.
 */

public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ComponentMapper<BoundsBox> boundsBoxComponentMapper = ComponentMapper.getFor(BoundsBox.class);

    public CollisionSystem(){}

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(BoundsBox.class).get());
    }

    @Override
    public void update(float deltaTime) {

    }
}

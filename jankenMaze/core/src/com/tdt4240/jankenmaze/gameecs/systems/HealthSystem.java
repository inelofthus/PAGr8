package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Created by jonas on 18/03/2018.
 */

public class HealthSystem extends EntitySystem {
    //TODO: Componentmapper and values
    public HealthSystem() {
        //TODO: Logic
    }

    public void increaseHealth(Entity entity, int delta){
        //TODO: Logic

    }
    public void decreaseHealth(Entity entity, int delta){
        //TODO: Logic
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}

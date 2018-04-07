package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.tdt4240.jankenmaze.gameecs.components.HUDItemInfo;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.LocalPlayer;
import com.tdt4240.jankenmaze.gameecs.components.PlayerInfo;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Remote;
import com.tdt4240.jankenmaze.gameecs.components.Renderable;
import com.tdt4240.jankenmaze.gameecs.components.SpriteComponent;

/**
 * Created by jonas on 18/03/2018.
 * Adds HUD-display to game
 */

public class HUDSystem extends EntitySystem {
    ImmutableArray<Entity> hudEntities;
    ImmutableArray<Entity> remotePlayerEntities;
    ImmutableArray<Entity> localPlayerEntities;
    ComponentMapper<Health> healthComponentMapper;
    ComponentMapper<HUDItemInfo> hudItemInfoComponentMapper;
    ComponentMapper<SpriteComponent> spriteComponentComponentMapper;

    public HUDSystem(){
        healthComponentMapper = ComponentMapper.getFor(Health.class);
        hudItemInfoComponentMapper = ComponentMapper.getFor(HUDItemInfo.class);
        spriteComponentComponentMapper = ComponentMapper.getFor(SpriteComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        hudEntities = engine.getEntitiesFor(Family.all(
                HUDItemInfo.class, Renderable.class, Position.class).get());
        remotePlayerEntities = engine.getEntitiesFor(Family.all(
                Health.class, PlayerInfo.class, Remote.class).get());
        localPlayerEntities = engine.getEntitiesFor(Family.one(LocalPlayer.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        hudEntities = engine.getEntitiesFor(Family.all(
                HUDItemInfo.class, Renderable.class, Position.class).get());
    }

    @Override
    public void update(float deltaTime) {
        int numOfHealthSprites = 0;
        int localPlayerHealth = healthComponentMapper.get(localPlayerEntities.get(0)).health;

        for (Entity e: hudEntities){
            String type = hudItemInfoComponentMapper.get(e).itemType;
            if(type.equals("playerHealth")){
                numOfHealthSprites++;
            }
        }
        if(numOfHealthSprites != localPlayerHealth){
            //TODO: Add/remove sprites based on player health
        }
    }
}

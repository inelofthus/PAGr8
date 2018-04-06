package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.tdt4240.jankenmaze.gameecs.components.HUDItemInfo;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.LocalPlayer;
import com.tdt4240.jankenmaze.gameecs.components.PlayerInfo;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Remote;
import com.tdt4240.jankenmaze.gameecs.components.Renderable;
import com.tdt4240.jankenmaze.gameecs.components.SpriteComponent;

import java.util.ArrayList;

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
    ComponentMapper<SpriteComponent> spriteComponentMapper;
    ComponentMapper<Position> positionComponentMapper;
    ComponentMapper<PlayerInfo> playerTypeMapper;
    private int numOfHealthSprites;
    private int maxHealthSpriteX;
    private boolean typeSpritesNotMade;

    int playerHealth;
    ArrayList<Entity> playerHearts;

    public HUDSystem() {
        healthComponentMapper = ComponentMapper.getFor(Health.class);
        hudItemInfoComponentMapper = ComponentMapper.getFor(HUDItemInfo.class);
        spriteComponentMapper = ComponentMapper.getFor(SpriteComponent.class);
        positionComponentMapper = ComponentMapper.getFor(Position.class);
        playerTypeMapper = ComponentMapper.getFor(PlayerInfo.class);

        playerHearts = new ArrayList<Entity>();
        numOfHealthSprites = 0;
        maxHealthSpriteX = 0;

        typeSpritesNotMade = true;
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
        //Health-display
        int localPlayerHealth = healthComponentMapper.get(localPlayerEntities.get(0)).health;

        if (numOfHealthSprites != localPlayerHealth) {
            Texture healthTexture = new Texture("testHeart.png");
            //  Adding healthsprites:
            while (numOfHealthSprites < localPlayerHealth) {
                Entity heartEntity = new Entity()
                        .add(new HUDItemInfo("playerHealth"))
                        .add(new SpriteComponent(healthTexture))
                        .add(new Position(maxHealthSpriteX, Gdx.graphics.getHeight()-healthTexture.getHeight()))
                        .add(new Renderable());

                getEngine().addEntity(heartEntity);
                playerHearts.add(heartEntity);
                maxHealthSpriteX += healthTexture.getWidth();
                numOfHealthSprites++;
            }

            //Removing healthsprites:
            while (numOfHealthSprites > localPlayerHealth) {
                if (playerHearts.size() >= 1) {
                    Entity lastHeart = playerHearts.get(playerHearts.size() - 1);
                    getEngine().removeEntity(lastHeart);
                    playerHearts.remove(lastHeart);
                    maxHealthSpriteX -= healthTexture.getWidth();

                }
                numOfHealthSprites --;
            }
        }

        //Type display:
        if(typeSpritesNotMade){
            Sprite localPlayerSprite = spriteComponentMapper.get(localPlayerEntities.get(0)).sprite;
            
        }
    }
}

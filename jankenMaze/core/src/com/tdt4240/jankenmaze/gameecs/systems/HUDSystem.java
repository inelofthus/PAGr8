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
    ComponentMapper<PlayerInfo> playerInfoMapper;
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
        playerInfoMapper = ComponentMapper.getFor(PlayerInfo.class);

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

        //Type display. Wrapped in if-clause to only run once (uses created sprites so shouldn't be in constructor)
        if(typeSpritesNotMade) {
            //Check who eats who:
            Sprite localPlayerSprite = spriteComponentMapper.get(localPlayerEntities.get(0)).sprite;
            ArrayList<Sprite> targetSprites = new ArrayList<Sprite>();
            ArrayList<Sprite> targetedBySprites = new ArrayList<Sprite>();

            String localPlayerType = playerInfoMapper.get(localPlayerEntities.get(0)).type;
            for (Entity player : remotePlayerEntities) {
                String playerTarget = playerInfoMapper.get(player).target;
                String playerTargetedBy = playerInfoMapper.get(player).targetetBy;
                if (playerTarget.equals(localPlayerType)) {
                    targetedBySprites.add(spriteComponentMapper.get(player).sprite);
                } else if (playerTargetedBy.equals("localPlayerType")) {
                    targetSprites.add(spriteComponentMapper.get(player).sprite);
                }
            }

            //Create the sprites:
            int leftMostX = Math.round((
                    Gdx.graphics.getWidth() / 2) - (localPlayerSprite.getWidth() / 2));
            int rightMostX = Math.round((
                    Gdx.graphics.getWidth() / 2) + (localPlayerSprite.getWidth() / 2));

            getEngine().addEntity(new Entity()
                    .add(new HUDItemInfo("playerType"))
                    .add(new SpriteComponent(localPlayerSprite.getTexture()))
                    .add(new Position(leftMostX, 0))
                    .add(new Renderable()));

            Texture eats = new Texture("eatsToRight.png");
            leftMostX -= eats.getWidth();
            getEngine().addEntity(new Entity()
                    .add(new HUDItemInfo("eats"))
                    .add(new SpriteComponent(eats))
                    .add(new Position(leftMostX, 0))
                    .add(new Renderable()));
            getEngine().addEntity(new Entity()
                    .add(new HUDItemInfo("eats"))
                    .add(new SpriteComponent(eats))
                    .add(new Position(rightMostX, 0))
                    .add(new Renderable()));
            rightMostX += eats.getWidth();
            leftMostX -= eats.getWidth();

            int highestY = 0;
            for (Sprite playerSprite : targetedBySprites) {
                getEngine().addEntity(new Entity()
                        .add(new HUDItemInfo("targetedBy"))
                        .add(new SpriteComponent(playerSprite))
                        .add(new Position(leftMostX, highestY))
                        .add(new Renderable())
                );
                highestY += playerSprite.getHeight();
            }
            highestY = 0;
            for (Sprite playerSprite : targetSprites) {
                getEngine().addEntity(new Entity()
                        .add(new HUDItemInfo("target"))
                        .add(new SpriteComponent(playerSprite))
                        .add(new Position(rightMostX, highestY))
                        .add(new Renderable())
                );
                highestY += playerSprite.getHeight();
            }
            typeSpritesNotMade = false;
        }
    }
}

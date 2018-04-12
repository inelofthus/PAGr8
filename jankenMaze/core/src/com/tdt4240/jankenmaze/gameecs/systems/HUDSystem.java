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
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

import java.util.ArrayList;

/**
 * Created by jonas on 18/03/2018.
 * Adds HUD-display to game
 */

public class HUDSystem extends EntitySystem {
    GameSettings gameSettings;
    ImmutableArray<Entity> hudEntities;
    ImmutableArray<Entity> localPlayerEntities;
    ComponentMapper<Health> healthComponentMapper;
    ComponentMapper<HUDItemInfo> hudItemInfoComponentMapper;
    ComponentMapper<SpriteComponent> spriteComponentMapper;
    ComponentMapper<Position> positionComponentMapper;
    ComponentMapper<PlayerInfo> playerInfoMapper;
    private int numOfHealthSprites;
    private int maxHealthSpriteX;
    private boolean typeSpritesNotMade;

    private int typeSpriteWidth;
    private int typepriteHeight;

    int playerHealth;
    ArrayList<Entity> playerHearts;

    public HUDSystem() {
        healthComponentMapper = ComponentMapper.getFor(Health.class);
        hudItemInfoComponentMapper = ComponentMapper.getFor(HUDItemInfo.class);
        spriteComponentMapper = ComponentMapper.getFor(SpriteComponent.class);
        positionComponentMapper = ComponentMapper.getFor(Position.class);
        playerInfoMapper = ComponentMapper.getFor(PlayerInfo.class);

        gameSettings = GameSettings.getInstance();
        typepriteHeight = 160;
        typeSpriteWidth = 160;

        playerHearts = new ArrayList<Entity>();
        numOfHealthSprites = 0;
        typeSpritesNotMade = true;
        maxHealthSpriteX = gameSettings.viewPortWidth - typeSpriteWidth;
    }

    @Override
    public void addedToEngine(Engine engine) {
        hudEntities = engine.getEntitiesFor(Family.all(
                HUDItemInfo.class, Renderable.class, Position.class).get());
        localPlayerEntities = engine.getEntitiesFor(Family.one(LocalPlayer.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        hudEntities = engine.getEntitiesFor(Family.all(
                HUDItemInfo.class, Renderable.class, Position.class).get());
        localPlayerEntities = engine.getEntitiesFor(Family.one(LocalPlayer.class).get());
    }

    @Override
    public void update(float deltaTime) {
        //Health-display
        Entity localPlayer = localPlayerEntities.first();

        int localPlayerHealth = healthComponentMapper.get(localPlayer).health;

        if (numOfHealthSprites != localPlayerHealth) {
            //Gets the texture of the local player with a long-ass line of code
            Texture healthTexture = new Texture("greenSquare.png");
            //  Adding healthsprites:
            int margin = 8;
            while (numOfHealthSprites < localPlayerHealth) {
                Entity heartEntity = new Entity()
                        .add(new HUDItemInfo("playerHealth"))
                        .add(new SpriteComponent(healthTexture))
                        .add(new Position(maxHealthSpriteX,
                                gameSettings.viewPortHeight-(typepriteHeight+healthTexture.getHeight()) - margin))
                        .add(new Renderable());

                getEngine().addEntity(heartEntity);
                playerHearts.add(heartEntity);
                maxHealthSpriteX += healthTexture.getWidth() + margin;
                numOfHealthSprites++;
            }

            //Removing healthsprites:
            while (numOfHealthSprites > localPlayerHealth) {
                if (playerHearts.size() >= 1) {
                    Entity lastHeart = playerHearts.get(playerHearts.size() - 1);
                    getEngine().removeEntity(lastHeart);
                    playerHearts.remove(lastHeart);
                    maxHealthSpriteX -= healthTexture.getWidth() + margin;

                }
                numOfHealthSprites --;
            }
        }

        //Type display. Wrapped in if-clause to only run once (uses created sprites so shouldn't be in constructor)
        if(typeSpritesNotMade) {
            Engine engine = getEngine();

            //Make big localplayer-sprite
            Texture playerTexture = spriteComponentMapper.get(localPlayer).sprite.getTexture();
            float bigPlayerSpriteX = (gameSettings.viewPortWidth - typeSpriteWidth);
            float bigPlayerSpriteY = (gameSettings.viewPortHeight - typepriteHeight);

            engine.addEntity(new Entity()
                    .add(new SpriteComponent(playerTexture, 160, 160))
                    .add(new Position(bigPlayerSpriteX, bigPlayerSpriteY))
                    .add(new Renderable())
                    .add(new HUDItemInfo("playerType")));

            typeSpritesNotMade = false;
        }
    }
}

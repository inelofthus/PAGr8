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
        maxHealthSpriteX = 800-160;

        typeSpritesNotMade = true;


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
            Texture healthTexture = spriteComponentMapper.get(localPlayer).sprite.getTexture();
            //  Adding healthsprites:

            while (numOfHealthSprites < localPlayerHealth) {
                Entity heartEntity = new Entity()
                        .add(new HUDItemInfo("playerHealth"))
                        .add(new SpriteComponent(healthTexture))
                        .add(new Position(maxHealthSpriteX, 480-healthTexture.getHeight()))
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
            Engine engine = getEngine();

            //Make big localplayer-sprite
            Texture playerTexture = spriteComponentMapper.get(localPlayer).sprite.getTexture();
            String playerType = playerInfoMapper.get(localPlayer).type;
            /*f (playerType.equals("Rock")) { //TODO: This shouldn't be hardcoded
                playerTexture = new Texture("bigRock.png");
            } else if (playerType.equals("Paper")) {
                playerTexture = new Texture("bigPaper.png");
            } else if (playerType.equals("Scissors")) {
                playerTexture = new Texture("bigScissors.png");
            } else {
                playerTexture = new Texture("testHeart.png"); //In case if-loop fail otherwise
            }*/
            float bigPlayerSpriteX = (800 - 160);
            float bigPlayerSpriteY = (480 - 160);

            engine.addEntity(new Entity()
                    .add(new SpriteComponent(playerTexture, 160, 160))
                    .add(new Position(bigPlayerSpriteX, bigPlayerSpriteY))
                    .add(new Renderable())
                    .add(new HUDItemInfo("playerType")));
        }
    }
}

package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.gameecs.components.Position;
import com.tdt4240.jankenmaze.gameecs.components.Velocity;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.BoundsBox;
import com.tdt4240.jankenmaze.gameecs.components.PlayerInfo;
import com.tdt4240.jankenmaze.gameecs.components.Spawnable;
import com.tdt4240.jankenmaze.gameecs.components.LocalPlayer;
import com.tdt4240.jankenmaze.gameecs.components.Renderable;
import com.tdt4240.jankenmaze.gameecs.components.SpriteComponent;
import com.tdt4240.jankenmaze.gameecs.components.Remote;
import com.tdt4240.jankenmaze.gameecs.components.Occupied;
import com.tdt4240.jankenmaze.gameecs.components.HUDItemInfo;

/**
 * Created by Oyvind Sabo on 14.03.2018.
 */

public class EntityFactory {
    private Engine engine;
    SpriteBatch spriteBatch;

    public EntityFactory(Engine engine, SpriteBatch spriteBatch) {
        this.engine = engine;
        this.spriteBatch = spriteBatch;
    }

    public Entity createPlayer(String type, int xPosition, int yPosition, int health, Texture texture) {
        Entity player = new Entity();
        player.add(new Health(health));
        player.add(new Position(0,0)); //TODO: Consider whether the startposition should be given by some function which finds an unoccupied spot or just be taken as an input to the factory.
        player.add(new Velocity(0,0));
        player.add(new BoundsBox(0,0,0,0)); //TODO: Gjør x og y identiske med Position.x og Position.y
        String[] typeList = {"Rock", "Paper", "Scissors"};
        String target;
        String targetBy;
        if (type.equals("Rock")) {
            player.add(new PlayerInfo("Scissors", "Paper", "Rock"));
        }
        else if (type.equals("Rock")) {
            player.add(new PlayerInfo("Rock", "Scissor", "Paper"));
        }
        else {
            player.add(new PlayerInfo("Paper", "Rock", "Scissor"));
        }
        player.add(new Spawnable());
        player.add(new LocalPlayer());
        player.add(new Renderable());
        player.add(new SpriteComponent(texture));
        player.add(new Remote());
        return player;
    }

    public Entity createPowerUp(int xPosition, int yPosition, Texture texture) {
        Entity powerUp = new Entity();
        powerUp.add(new Position(0,0)); //TODO: Consider if the startposition should be given by some function which finds an unoccupied spot instead of being taken as an input argument.
        powerUp.add(new BoundsBox(0,0,0,0)); //TODO: Gjør x og y identiske med Position.x og Position.y
        powerUp.add(new Spawnable());
        powerUp.add(new Renderable());
        powerUp.add(new SpriteComponent(texture));
        return powerUp;
    }

    public Entity createWall(int xPosition, int yPosition, Texture texture) {
        Entity wall = new Entity();
        wall.add(new Position(0,0));
        wall.add(new BoundsBox(0,0,0,0)); //TODO: Gjør x og y identiske med Position.x og Position.y
        wall.add(new Spawnable());
        wall.add(new Renderable());
        wall.add(new SpriteComponent(texture));
        return wall;
    }

    public Entity createSpawnPosition(int xPosition, int yPosition, Texture texture) {
        Entity spawnPosition = new Entity();
        spawnPosition.add(new Position(0,0));
        spawnPosition.add(new Occupied());
        return spawnPosition;
    }

    public Entity createHUDItem(int xPosition, int yPosition, Texture texture, String itemType) {
        Entity HUDItem = new Entity();
        HUDItem.add(new Position(xPosition, yPosition));
        HUDItem.add(new Renderable());
        HUDItem.add(new SpriteComponent(texture));
        HUDItem.add(new HUDItemInfo(itemType));
        return HUDItem;
    }

    public Entity createBackground(int xPosition, int yPosition, Texture texture) {
        Entity background = new Entity();
        background.add(new Position(xPosition, yPosition));
        background.add(new Renderable());
        background.add(new SpriteComponent(texture));
        return background;
    }
}

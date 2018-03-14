package com.tdt4240.jankenmaze.gameecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.gameecs.components.PositionComponent;
import com.tdt4240.jankenmaze.gameecs.components.VelocityComponent;
import com.tdt4240.jankenmaze.gameecs.components.HealthComponent;
import com.tdt4240.jankenmaze.gameecs.components.BoundsBox;
import com.tdt4240.jankenmaze.gameecs.components.PlayerInfo;
import com.tdt4240.jankenmaze.gameecs.components.Spawnable;
import com.tdt4240.jankenmaze.gameecs.components.LocalPlayer;
import com.tdt4240.jankenmaze.gameecs.components.RenderableComponent;
import com.tdt4240.jankenmaze.gameecs.components.SpriteComponent;
import com.tdt4240.jankenmaze.gameecs.components.Remote;

/**
 * Created by Oyvind Sabo on 14.03.2018.
 */

public class EntityFactory {
    private Engine engine;
    SpriteBatch batch;

    public EntityFactory(Engine engine, SpriteBatch spriteBatch) {
        this.engine = engine;
        this.batch = spriteBatch;
    }

    public Entity createPlayer(String type, int xPosition, int yPosition, int health, Texture texture) {
        Entity player = new Entity();
        player.add(new HealthComponent(health));
        player.add(new PositionComponent(0,0)); //TODO: The startposition should be given by some function which finds an unoccupied spot or taken as an input to the factory.
        player.add(new VelocityComponent(0,0));
        player.add(new BoundsBox(0,0,0,0)); //TODO: Gjør x og y identiske med PositionComponent.x og PositionComponent.y
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
        player.add(new RenderableComponent());
        player.add(new SpriteComponent(texture));
        player.add(new Remote());
        return player;
    }
}

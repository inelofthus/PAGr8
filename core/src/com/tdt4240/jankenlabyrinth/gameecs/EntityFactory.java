package com.tdt4240.jankenlabyrinth.gameecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.tdt4240.jankenlabyrinth.gameecs.components.Health;
import com.tdt4240.jankenlabyrinth.gameecs.components.LocalPlayer;
import com.tdt4240.jankenlabyrinth.gameecs.components.PlayerInfo;
import com.tdt4240.jankenlabyrinth.gameecs.components.Position;
import com.tdt4240.jankenlabyrinth.gameecs.components.RemotePlayer;
import com.tdt4240.jankenlabyrinth.gameecs.components.Spawnable;
import com.tdt4240.jankenlabyrinth.gameecs.components.Token;
import com.tdt4240.jankenlabyrinth.gameecs.components.Velocity;

/**
 * Created by jonas on 08/03/2018.
 */

public class EntityFactory {
    /*
    * TODO: Better logic on createPlayer
    * */
    public void createPlayer(boolean isLocal){
        Entity player = new Entity();
        player.add(new Token(new Texture("playerTexture.png")))
        .add(new Spawnable())
        .add(new Position(200, 200)) //Todo: Logic to determine spawning-position
        .add(new PlayerInfo("rock", "scissor", "paper")) //TODO: Determine types somehow;
        .add(new Health(3))
        .add(new Velocity(30, 30)); //TODO: Again better way to determine arguments

        player.add(isLocal ? (new LocalPlayer()) : (new RemotePlayer()));
    }


}

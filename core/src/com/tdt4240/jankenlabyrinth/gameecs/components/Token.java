package com.tdt4240.jankenlabyrinth.gameecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by jonas on 07/03/2018.
 */

public class Token implements Component {
    public com.badlogic.gdx.graphics.g2d.Sprite sprite;

    public Token(Texture texture){
        this.sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);
    }
}

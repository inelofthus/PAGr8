package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by jonas on 07/03/2018.
 */

public class Sprite implements Component {
    public com.badlogic.gdx.graphics.g2d.Sprite sprite;

    public Sprite(Texture texture){
        this.sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);
    }
}

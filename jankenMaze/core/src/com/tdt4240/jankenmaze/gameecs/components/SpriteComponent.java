package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by jonas on 07/03/2018.
 */

public class SpriteComponent implements Component {
    public Sprite sprite;

    public SpriteComponent(Texture texture){
        this.sprite = new Sprite(texture);
    }
}

package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by jonas on 07/03/2018.
 */

public class SpriteComponent implements Component {
    public com.badlogic.gdx.graphics.g2d.Sprite sprite;
    public Integer height;
    public Integer width;

    public SpriteComponent(Texture texture){
        this.sprite = new com.badlogic.gdx.graphics.g2d.Sprite(texture);
    }
    public SpriteComponent(Sprite sprite){
        this.sprite = sprite;
    }
    public SpriteComponent(Texture texture, int width, int height){
        this.sprite = new Sprite(texture);
        this.width = width;
        this.height = height;
    }
    public SpriteComponent(Sprite sprite, int width, int height){
        this.sprite = sprite;
        this.width = width;
        this.height = height;
    }
}

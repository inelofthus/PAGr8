package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by karim on 13/03/2018.
 */

public abstract class View {

    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}

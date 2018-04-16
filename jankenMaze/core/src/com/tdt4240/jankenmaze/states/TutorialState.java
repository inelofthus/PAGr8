package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.view.TutorialView;

/**
 * Created by jonas on 13/04/18.
 */

public class TutorialState extends State {
    TutorialView tutorialView;

    public TutorialState() {
        this.tutorialView = new TutorialView();
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        if(Gdx.input.justTouched()){
            gsm.set(new OnlineMenuState());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        tutorialView.render(sb);
    }

    @Override
    public void dispose() {
        tutorialView.dispose();
    }
}

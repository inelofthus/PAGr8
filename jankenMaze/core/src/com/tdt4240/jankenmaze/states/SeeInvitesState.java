package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.view.SeeInvitationsView;

/**
 * Created by jonas on 13/04/18.
 */

public class SeeInvitesState extends State {
    SeeInvitationsView seeInvitationsView;

    public SeeInvitesState() {
        this.seeInvitationsView = new SeeInvitationsView();
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
        seeInvitationsView.render(sb);
    }

    @Override
    public void dispose() {
        seeInvitationsView.dispose();
    }
}

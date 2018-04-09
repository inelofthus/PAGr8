package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tdt4240.jankenmaze.view.OnlineMenuView;

/**
 * Created by karim on 09/04/2018.
 */

public class OnlineMenuState extends State {
    OnlineMenuView onlineMenuView;
    private SpriteBatch batch;
    private ClickListener listenerBtn_invite, listenerBtn_signOut;


    public OnlineMenuState() {
        super();
        gsm = GameStateManager.getGsm();
        this.onlineMenuView = new OnlineMenuView();

        listenerBtn_invite = new ClickListener();
        onlineMenuView.btn_invite.addListener(listenerBtn_invite);

        listenerBtn_signOut = new ClickListener();
        onlineMenuView.btn_signout.addListener(listenerBtn_signOut);

        cam.setToOrtho(false);
    }

    @Override
    protected void handleInput() {
        if (onlineMenuView.btn_invite.isPressed()){
            gsm.playServices.startSelectOpponents(false);
        }
        if (onlineMenuView.btn_signout.isPressed()){
            gsm.playServices.signOut();
            if (!gsm.playServices.isSignedIn()){
                gsm.push(new OfflineMenuState());
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (gsm.playServices.isSignedIn()){
            gsm.push(new OnlineMenuState());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.batch = sb;
        onlineMenuView.render(sb);
    }

    @Override
    public void dispose() {

    }
}

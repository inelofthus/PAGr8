package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tdt4240.jankenmaze.view.OnlineMenuView;

/**
 * Created by karim on 09/04/2018.
 */

public class OnlineMenuState extends State {
    OnlineMenuView onlineMenuView;
    private SpriteBatch batch;
    private ClickListener listenerBtn_invite, listenerBtn_signOut, listenerBtn_playSingle, listenerBtn_tutorial;


    public OnlineMenuState() {
        super();
        gsm = GameStateManager.getGsm();
        this.onlineMenuView = new OnlineMenuView();

        onlineMenuView.btn_invite.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.playServices.startSelectOpponents(false);
            }
        });

        onlineMenuView.btn_signout.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.playServices.signOut();
                if (!gsm.playServices.isSignedIn()){
                    gsm.set(new OfflineMenuState());
                }
            }
        });

        onlineMenuView.btn_PlaySingle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new SinglePlayState(batch));
            }
        });

        onlineMenuView.btn_tutorial.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new TutorialState());
            };
        });

        cam.setToOrtho(false);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        if (!gsm.playServices.isSignedIn()){
            gsm.set(new OfflineMenuState());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.batch = sb;
        onlineMenuView.render(sb);
    }

    @Override
    public void dispose() {
        onlineMenuView.dispose();
    }
}

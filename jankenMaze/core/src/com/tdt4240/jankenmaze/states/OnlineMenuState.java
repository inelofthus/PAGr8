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

    public OnlineMenuState() {
        super();
        initializeMenuState();
    }
  
    public OnlineMenuState(String message) {
        super();
        initializeMenuState();
        onlineMenuView.message.setText(message);
    }

    private void initializeMenuState(){

        gsm = GameStateManager.getGsm();
        this.onlineMenuView = new OnlineMenuView();

        addButtonListener();
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

    private void addButtonListener(){
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

        onlineMenuView.btn_SeeInvitations.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new SeeInvitesState());
            }
        });

        onlineMenuView.btn_tutorial.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new TutorialState());
            };
        });
    }
}

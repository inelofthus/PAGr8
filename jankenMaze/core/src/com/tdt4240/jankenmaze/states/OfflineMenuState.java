package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.view.OfflineMenuView;

/**
 * Created by karim on 09/04/2018.
 */

public class OfflineMenuState extends State {

    OfflineMenuView offlineMenuView;
    private SpriteBatch batch;
    private ClickListener listenerBtn_playSingle, listenerBtn_signIn, listenerBtn_tutorial;


    public OfflineMenuState() {
        super();
        gsm = GameStateManager.getGsm();
        this.offlineMenuView = new OfflineMenuView();

        offlineMenuView.btn_PlaySingle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.set(new SinglePlayState(batch));
            }
        });

        offlineMenuView.btn_signin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.playServices.signIn();
                if (gsm.playServices.isSignedIn()){
                    gsm.set(new OnlineMenuState());
                }
            }
        });

        offlineMenuView.btn_tutorial.addListener( new ClickListener() {
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
        if (gsm.playServices.isSignedIn()){
            gsm.set(new OnlineMenuState());
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        this.batch = sb;
        offlineMenuView.render(sb);
    }

    @Override
    public void dispose() {
        offlineMenuView.dispose();
    }
}

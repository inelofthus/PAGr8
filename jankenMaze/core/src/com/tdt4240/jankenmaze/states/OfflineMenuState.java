package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tdt4240.jankenmaze.GameSettings;
import com.tdt4240.jankenmaze.view.OfflineMenuView;
import com.tdt4240.jankenmaze.view.OnlineMenuView;

/**
 * Created by karim on 09/04/2018.
 */

public class OfflineMenuState extends State {

    OfflineMenuView offlineMenuView;
    private SpriteBatch batch;
    private ClickListener listenerBtn_playSingle, listenerBtn_signIn;


    public OfflineMenuState() {
        super();
        gsm = GameStateManager.getGsm();
        this.offlineMenuView = new OfflineMenuView();

        listenerBtn_playSingle = new ClickListener();
        offlineMenuView.btn_PlaySingle.addListener(listenerBtn_playSingle);

        listenerBtn_signIn = new ClickListener();
        offlineMenuView.btn_signin.addListener(listenerBtn_signIn);

        cam.setToOrtho(false);
    }

    @Override
    protected void handleInput() {
        if (offlineMenuView.btn_PlaySingle.isPressed()){
            GameSettings.getInstance().isMultplayerGame = false;
            gsm.push(new SinglePlayState(batch));
        }
        if (offlineMenuView.btn_signin.isPressed()){
            gsm.playServices.signIn();
            if (gsm.playServices.isSignedIn()){
                gsm.push(new OnlineMenuState());
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
        offlineMenuView.render(sb);
    }

    @Override
    public void dispose() {

    }
}

package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tdt4240.jankenmaze.GameSettings;
import com.tdt4240.jankenmaze.view.GameOverView;
import com.tdt4240.jankenmaze.view.YouLooseView;
import com.tdt4240.jankenmaze.view.YouWinView;

/**
 * Created by bartosz on 4/7/18.
 * This is the state that is activated when the game is over.
 */

public class GameOverState extends State {
    private GameOverView gameOverView;
    private SpriteBatch batch;
    private ClickListener listenerBtn_playAgain, listenerBtn_mainMenu, listenerBtn_quitGame;

    public GameOverState(){
        super();
        this.gameOverView = new YouWinView();

        listenerBtn_playAgain = new ClickListener();
        gameOverView.btn_playAgain.addListener(listenerBtn_playAgain);

        listenerBtn_mainMenu = new ClickListener();
        gameOverView.btn_toMainMenu.addListener(listenerBtn_mainMenu);

        listenerBtn_quitGame = new ClickListener();
        gameOverView.btn_quitGame.addListener(listenerBtn_quitGame);


        cam.setToOrtho(false);



    }

    @Override
    protected void handleInput() {
        if (gameOverView.btn_playAgain.isPressed()){
            if(GameSettings.getInstance().isMultplayerGame){
                gsm.push(new MultiPlayState(batch));
            }else {
                gsm.push(new SinglePlayState(batch));
            }

        }
        if (gameOverView.btn_toMainMenu.isPressed()){
            //TODO: Push correct state
            if(gsm.playServices.isSignedIn()){
                gsm.push(new OnlineMenuState());
            }else {
                gsm.push(new OfflineMenuState());
            }
        }
        if (gameOverView.btn_quitGame.isPressed()){
            Gdx.app.exit();
         }


    }

    @Override
    public void update(float dt) {
        if(Gdx.input.isTouched()){
            handleInput();
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        this.batch = sb;
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        gameOverView.render(batch);
        sb.end();

    }

    @Override
    public void dispose() {

    }
}

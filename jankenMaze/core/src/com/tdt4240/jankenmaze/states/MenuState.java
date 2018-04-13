package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tdt4240.jankenmaze.view.MainMenuView;


public class MenuState extends State {
    MainMenuView mainMenuView;
    private GameStateManager gsm;
    private SpriteBatch batch;
    private ClickListener listenerBtn_joinGame, listenerBtn_createGame, listenerBtn_invite;

    public MenuState() {
        super();
        gsm = GameStateManager.getGsm();
        this.mainMenuView = new MainMenuView();

        listenerBtn_createGame = new ClickListener();
        mainMenuView.btn_createGame.addListener(listenerBtn_createGame);

        listenerBtn_joinGame = new ClickListener();
        mainMenuView.btn_joinGame.addListener(listenerBtn_joinGame);

        listenerBtn_invite = new ClickListener();
        mainMenuView.btn_invite.addListener(listenerBtn_invite);


        cam.setToOrtho(false);

    }

    @Override
    protected void handleInput() {
        if (mainMenuView.btn_joinGame.isPressed()){
            gsm.set(new SinglePlayState(batch));
        }
        if (mainMenuView.btn_createGame.isPressed()){
            //TODO: Push correct state
            //gsm.push(new com.tdt4240.jankenmaze.states.PlayState(batch));
        }
        //INVITE
        if (mainMenuView.btn_invite.isPressed()){
            gsm.playServices.startSelectOpponents(false);
            //TODO: Push correct state
            //gsm.push(new com.tdt4240.jankenmaze.states.PlayState(batch));
        }
        //Tutorial
        if(mainMenuView.btn_tutorial.isPressed()){
            gsm.push(new TutorialState());
        }

    }

    @Override
    public void update(float dt) {
        if(Gdx.input.isTouched()){
            handleInput();
        }
    }

    @Override
    public void render(SpriteBatch sb){
        this.batch = sb;
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        mainMenuView.render(batch);
        sb.end();
    }

    @Override
    public void dispose() {
        mainMenuView.dispose();
    }
}

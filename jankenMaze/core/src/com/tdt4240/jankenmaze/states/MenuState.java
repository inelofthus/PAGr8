package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by jonas on 25/01/2018.
 * Main menu state. Should eventually use MVC-architecture, but not quite there yet (it's very skeletal)
 * Currently just sends us right to playState when screen is touched/clicked
 */

public class MenuState extends State {
    Texture playBtn;
    /* int playBtnX;
    int playBtnY;
    int playBtnHeight;
    int playBtnWidth;
    Rectangle playBtnRect; */
    private GameStateManager gsm;
    private SpriteBatch batch;

    public MenuState() {
        super();
        gsm = GameStateManager.getGsm();

        cam.setToOrtho(false);
        playBtn = new Texture("menuBtn.png");

        System.out.println("Starting");

        //playBtnX = Gdx.graphics.getWidth()/2 - playBtn.getWidth()/2;
        //playBtnY = Gdx.graphics.getHeight()/2 - playBtn.getHeight()/2;
        //playBtnHeight = Gdx.graphics.getHeight();
        //playBtnWidth = (playBtnHeight/480)*360;


        //playBtnRect = new Rectangle(playBtnX, playBtnY, playBtn.getWidth(), playBtn.getHeight());
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.push(new com.tdt4240.jankenmaze.states.PlayState(batch));
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
        sb.draw(playBtn, 0, 100, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {
        playBtn.dispose();
    }
}

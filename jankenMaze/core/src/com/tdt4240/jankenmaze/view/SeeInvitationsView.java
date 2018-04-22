package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by karim on 18/04/2018.
 */

public class SeeInvitationsView extends View{
    private Stage stage;

    public SeeInvitationsView() {
        this.stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        Image seeInvitationImage = new Image(new Texture("SeeInvitations.png"));
        seeInvitationImage.setWidth(stage.getWidth());
        seeInvitationImage.setHeight(stage.getHeight());
        stage.addActor(seeInvitationImage);

    }


    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

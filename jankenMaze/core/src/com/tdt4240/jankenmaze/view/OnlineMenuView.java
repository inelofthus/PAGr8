package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by karim on 09/04/2018.
 */

public class OnlineMenuView extends MenuView {

    public TextButton btn_invite, btn_signout, btn_PlaySingle;
    public Label heading;

    public OnlineMenuView() {
        super();

        btn_invite = new TextButton("Invite", textButtonStyle);
        btn_invite.pad(20);

        btn_PlaySingle = new TextButton("Play Single Player Game", textButtonStyle);
        btn_PlaySingle.pad(20);

        btn_signout = new TextButton("Sign Out", textButtonStyle);
        btn_signout.pad(20);



        //creating heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.WHITE);
        heading = new Label("Multiplayer Janken Maze! \n", headingStyle);
        heading.setFontScale(2);

        // putting stuff together
        table.add(heading);
        table.row();
        table.add(btn_invite);
        table.row();
        table.add(btn_PlaySingle);
        table.row();
        table.add(btn_signout);

        stage.addActor(table);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

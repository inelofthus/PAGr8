package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by karim on 09/04/2018.
 */

public class OnlineMenuView extends MenuView {
    public TextButton btn_invite, btn_signout, btn_SeeInvitations, btn_tutorial;
    public Label heading, message;

    public OnlineMenuView() {
        super();

        btn_invite = new TextButton("Invite", textButtonStyle);
        btn_invite.pad(20);

        btn_SeeInvitations = new TextButton("See Invitations", textButtonStyle);
        btn_SeeInvitations.pad(20);

        btn_signout = new TextButton("Sign Out", textButtonStyle);
        btn_signout.pad(20);

        btn_tutorial = new TextButton("Tutorial", textButtonStyle);
        btn_tutorial.pad(20);



        //creating heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.WHITE);
        heading = new Label("Multiplayer Janken Maze! \n", headingStyle);
        heading.setFontScale(2);

        Label.LabelStyle messageStyle = new Label.LabelStyle(font, Color.GREEN);
        message = new Label("", messageStyle);

        // putting stuff together
        table.add(heading);
        table.row();
        table.add(btn_invite);
        table.row();
        table.add(btn_SeeInvitations);
        table.row();
        table.add(btn_signout);
        table.row();
        table.add(btn_tutorial);
        table.row();
        table.add(message);

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
        btn_invite.remove();
        btn_SeeInvitations.remove();
        btn_signout.remove();
        btn_tutorial.remove();
        heading.remove();
    }
}

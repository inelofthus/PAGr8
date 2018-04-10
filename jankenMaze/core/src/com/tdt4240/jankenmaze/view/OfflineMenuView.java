package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by karim on 09/04/2018.
 */

public class OfflineMenuView extends MenuView {

    public TextButton btn_PlaySingle, btn_signin;
    private Label heading;

    public OfflineMenuView() {
        super();

        btn_PlaySingle = new TextButton("Play Single Player Game", textButtonStyle);
        btn_PlaySingle.pad(20);

        btn_signin = new TextButton("Sign In", textButtonStyle);
        btn_signin.pad(20);

        //creating heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.WHITE);
        heading = new Label("Single Player Janken Maze! \n", headingStyle);
        heading.setFontScale(2);

        // putting stuff together
        table.add(heading);
        table.row();
        table.add(btn_PlaySingle);
        table.row();
        table.add(btn_signin);

        stage.addActor(table);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void update(float dt) {

    }
}

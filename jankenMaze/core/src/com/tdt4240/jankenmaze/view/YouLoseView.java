package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by bartosz on 4/9/18.
 */

public class YouLoseView extends GameOverView {
    private Label heading1;
    private BitmapFont font;
    private Table table;
    public YouLoseView(){
        super();
        font = new BitmapFont();
        table = new Table(super.skin);
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.RED);
        heading1 = new Label("   You Lost \n", headingStyle);
        heading1.setFontScale(2);
        //putting stuff together
        table.add(super.heading1);
        table.row();
        table.add(heading1);
        table.row();
        table.add(super.btn_playAgain);
        table.row();
        table.add(super.btn_toMainMenu);
        table.row();
        table.add(super.btn_quitGame);

        //table.debug();
        super.stage.addActor(table);


    }

    @Override
    public void update(float dt) {
        super.update(dt);
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

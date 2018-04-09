package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by karim on 09/04/2018.
 */

public abstract class MenuView extends View {
    TextButton.TextButtonStyle textButtonStyle;
    protected Stage stage;
    protected TextureAtlas atlas;
    protected Skin skin;
    protected Table table;
    protected BitmapFont font;


    public MenuView() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        atlas = new TextureAtlas("button.pack");
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("btnUp");
        textButtonStyle.down = skin.getDrawable("btnDown");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = new BitmapFont();
        this.font = new BitmapFont();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //sb.begin();
        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        font.dispose();
        skin.dispose();
    }
}

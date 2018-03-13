package com.tdt4240.jankenmaze.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
 * Created by karim on 13/03/2018.
 */

public class MainMenuView extends View {
    TextButton.TextButtonStyle textButtonStyle;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;
    public TextButton btn_joinGame, btn_createGame;
    private Label heading1;
    private BitmapFont font;

    public MainMenuView() {
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
        font = new BitmapFont();


        btn_joinGame = new TextButton("Join Game", textButtonStyle);
        btn_joinGame.pad(20);

        btn_createGame = new TextButton("Create Game", textButtonStyle);
        btn_createGame.pad(20);

        //creating heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.WHITE);
        heading1 = new Label("    TDT4240 \n Janken Maze \n", headingStyle);
        heading1.setFontScale(2);


        // putting stuff together
        table.add(heading1);
        table.row();
        table.add(btn_joinGame);
        table.row();
        table.add(btn_createGame);

        //table.debug();
        stage.addActor(table);

    }


    @Override
    public void update(float dt) {

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

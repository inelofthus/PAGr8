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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;

/**
 * Created by karim on 13/03/2018.
 */

public class MainMenuView extends View {
    TextButton.TextButtonStyle textButtonStyle;
    protected Stage stage;
    protected TextureAtlas atlas;
    protected Skin skin;
    protected Table table;
    public TextButton btn_joinGame, btn_createGame, btn_invite, btn_tutorial;
    private Label heading1;
    protected BitmapFont font;
    Viewport viewport;

    public MainMenuView() {GameSettings gameSettings = GameSettings.getInstance();
        int gameWidth = gameSettings.viewPortWidth;
        int gameHeight = gameSettings.viewPortHeight;
        viewport = new FitViewport(gameWidth, gameHeight);
        viewport.setScreenBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport.apply();
        stage = new Stage(viewport);
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

        btn_invite = new TextButton("Invite", textButtonStyle);
        btn_invite.pad(20);

        btn_invite = new TextButton("How to play", textButtonStyle);
        btn_invite.pad(20);



        //creating heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.WHITE);
        heading1 = new Label("      Janken Maze \n", headingStyle);
        heading1.setFontScale(2);


        // putting stuff together
        table.add(heading1);
        table.row();
        table.add(btn_invite);
        table.row();
        table.add(btn_joinGame);
        table.row();
        table.add(btn_createGame);
        table.row();
        table.add(btn_tutorial);

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

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
 * Created by bartosz on 4/7/18.
 */

public class GameOverView extends View {

    TextButton.TextButtonStyle textButtonStyle;

    protected Stage stage;
    private TextureAtlas atlas;
    protected Skin skin;
    protected Table table;

    public TextButton btn_playAgain, btn_toMainMenu, btn_quitGame;
    protected Label heading1, resultLabel;
    private BitmapFont font;

    public GameOverView(){
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("button.pack");
        skin = new Skin(atlas);
        table = new Table(skin);
        table.setBounds(0,0, gameWidth, gameHeight);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("btnUp");
        textButtonStyle.down = skin.getDrawable("btnDown");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = new BitmapFont();
        font = new BitmapFont();
        btn_playAgain = new TextButton("Play Again!", textButtonStyle);
        btn_playAgain.pad(20);

        btn_toMainMenu = new TextButton("Main Menu", textButtonStyle);
        btn_toMainMenu.pad(20);

        btn_quitGame = new TextButton("Quit Game", textButtonStyle);
        btn_quitGame.pad(20);

        //creating heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.YELLOW);
        heading1 = new Label("   Game Over \n", headingStyle);
        heading1.setFontScale(4);

        Label.LabelStyle messageStyle = new Label.LabelStyle(font, Color.WHITE);
        resultLabel = new Label("The Message", messageStyle);
        resultLabel.setFontScale(1.5f);

        // putting stuff together
        float scale = 0.8f;
        table.add(heading1).colspan(3);
        table.row();
        table.add(resultLabel).colspan(3);
        table.row();
        table.add(btn_playAgain).width(btn_playAgain.getWidth()*scale);
        table.add(btn_toMainMenu).width(btn_playAgain.getWidth()*scale);
        table.add(btn_quitGame).width(btn_playAgain.getWidth()*scale);

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
        btn_playAgain.remove();
        btn_quitGame.remove();
        btn_quitGame.remove();
        heading1.remove();
        resultLabel.remove();
        stage.dispose();
    }

    public void setResultLabel(String resultMessage){
        resultLabel.setText(resultMessage);
    }
}

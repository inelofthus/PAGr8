package com.tdt4240.jankenlabyrinth;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenlabyrinth.states.GameStateManager;
import com.tdt4240.jankenlabyrinth.states.MenuState;

/*
* Overall game-class, does very little on it's own.
* */
public class JankenLabyrinth extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	com.tdt4240.jankenlabyrinth.gameecs.EntityManager entityManager;
	GameStateManager gsm;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = GameStateManager.getGsm();
		gsm.push(new MenuState());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
		//entityManager.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}

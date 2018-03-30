package com.tdt4240.jankenmaze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.onlineservice.SocketConnection;
import com.tdt4240.jankenmaze.states.GameStateManager;
import com.tdt4240.jankenmaze.states.MenuState;
import com.tdt4240.jankenmaze.gameecs.EntityManager;
import com.badlogic.ashley.core.Entity;
import java.util.ArrayList;

/*
* Overall game-class, does very little on it's own.
* */
public class JankenMaze extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture powerUpTexture;
	EntityManager entityManager;
	GameStateManager gsm;
	SocketConnection socket = SocketConnection.getSocketConnection();
	ArrayList<Entity> powerUps = new ArrayList<Entity>();
	int[][] binaryMap = {{1, 0, 0}, {0, 1, 0}, {0, 1, 0}}; //Map is instatiated from a binary matrix
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		socket.connectSocket();
		socket.configSocketEvents();

		gsm = GameStateManager.getGsm();
		gsm.push(new MenuState());
		powerUpTexture = (new Texture("powerUps.png"));
		//Creates wall entities from binaryMap
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

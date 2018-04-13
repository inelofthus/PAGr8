package com.tdt4240.jankenmaze;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.states.GameStateManager;
import com.tdt4240.jankenmaze.states.MenuState;
import com.tdt4240.jankenmaze.gameecs.EntityManager;
import com.tdt4240.jankenmaze.states.MultiPlayState;
import com.tdt4240.jankenmaze.states.OfflineMenuState;
import com.tdt4240.jankenmaze.states.OnlineMenuState;
import com.tdt4240.jankenmaze.states.PlayState;
import com.badlogic.ashley.core.Entity;
import java.util.ArrayList;

/*
* Overall game-class, does very little on it's own.
* */
public class JankenMaze extends ApplicationAdapter implements PlayServices.GameListener {
	SpriteBatch batch;
	Texture img;
	Texture powerUpTexture;
	EntityManager entityManager;
	GameStateManager gsm;
	//SocketConnection socket = SocketConnection.getSocketConnection();
	PlayServices playServices;
	PlayState multiPlayState;


	//Constructor for the android app
	public JankenMaze(PlayServices playServices) {
		this.playServices = playServices;
	}

	//constructor for the Desktop app
	public JankenMaze() {

	}

	ArrayList<Entity> powerUps = new ArrayList<Entity>();
	int[][] binaryMap = {{1, 0, 0}, {0, 1, 0}, {0, 1, 0}}; //Map is instatiated from a binary matrix

	@Override
	public void create () {
		batch = new SpriteBatch();

		//socket.connectSocket();
		//socket.configSocketEvents();
		System.out.println("create is accessed");
		gsm = GameStateManager.getGsm();
		gsm.setPlayServices(playServices);
		playServices.setGameListener(this);
		this.multiPlayState = new MultiPlayState(batch);
		if (playServices.isSignedIn()){
			gsm.push(new OnlineMenuState());
		}else {
			gsm.push(new OfflineMenuState());
		}

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

	@Override
	public void onMultiplayerGameStarting() {
		System.out.println("JankenMaze: onMultiplayerGameStarting");
		gsm.set(multiPlayState);
	}

	@Override
	public void onDisconnectedFromRoom() {
		GameSettings.getInstance().reset();
		//multiPlayState.reset();
		this.multiPlayState = new MultiPlayState(batch);
		gsm.push(new OnlineMenuState("onDisconnectedFromRoom"));
	}

	@Override
	public void resetGameVariables() {
		GameSettings.getInstance().reset();
		System.out.println("JankenMaze: resetGameVariables");
		//this.multiPlayState = new MultiPlayState(batch);
	}

	@Override
	public String toString() {
		return "JankenMazeClass";
	}
}

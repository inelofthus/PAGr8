package com.tdt4240.jankenmaze;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.GameHelper;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;

import java.util.ArrayList;


public class AndroidLauncher extends AndroidApplication implements PlayServices, RoomUpdateListener {

	private GameHelper gameHelper;
	private final static int requestCode = 1;
	private static final int MIN_PLAYERS = 1;
	private static final int MAX_PLAYERS = 7;
	private static final int RC_SELECT_PLAYERS = 10000;
	private static final String TAG = "AndroidLauncher";


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new JankenMaze(this), config);

		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(true);
		gameHelper.setShowErrorDialogs(true);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed(){ }

			@Override
			public void onSignInSucceeded(){ }
		};

		gameHelper.setup(gameHelperListener);
	}

	// we want play services to start automatically when the game begins and stop when the game exits, also we need to handle exceptions when the user fails to sign in. This is where the BaseGameUtil libraries come in, it takes care of all this, we just have to override our Activity methods and pass it on to them.
	@Override
	protected void onStart()
	{
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		// Signing in
		gameHelper.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case RC_SELECT_PLAYERS:
				// we got the result from the "select players" UI -- ready to create the room
				Log.d(TAG, "onActivityResult: RC_SELECT_PLAYERS");
				handleSelectPlayersResult(resultCode, data);
				break;
		}


	}

	//Create room and set up listeners to receive notification
	private void handleSelectPlayersResult(int response, Intent data) {
		Log.d(TAG, "handleSelectPlayersResult: ");
		if (response != Activity.RESULT_OK) {
			Log.w(TAG, "*** select players UI cancelled, " + response);
			return;
		}

		Log.d(TAG, "Select players UI succeeded.");

		// get the invitee list
		final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
		Log.d(TAG, "Invitee count: " + invitees.size());

		// get the automatch criteria
		Bundle autoMatchCriteria = null;
		int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
		int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
		if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
			autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
					minAutoMatchPlayers, maxAutoMatchPlayers, 0);
			Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
		}
		// create the room
		Log.d(TAG, "Creating room...");
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
		rtmConfigBuilder.addPlayersToInvite(invitees);
		rtmConfigBuilder.setMessageReceivedListener(this);
		rtmConfigBuilder.setRoomStatusUpdateListener(this);
		if (autoMatchCriteria != null) {
			rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
		}
		keepScreenOn();
		Games.RealTimeMultiplayer.create(gameHelper.getApiClient(), rtmConfigBuilder.build());
		Log.d(TAG, "Room created, waiting for it to be ready...");
	}



	//Play Services Interface
	@Override
	public void signIn()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.signOut();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}


	@Override
	public boolean isSignedIn()
	{
		return gameHelper.isSignedIn();
	}

	@Override
	public void startSelectOpponents(boolean autoMatch) {
		Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(gameHelper.getApiClient(), MIN_PLAYERS, MAX_PLAYERS, autoMatch);
		startActivityForResult(intent, RC_SELECT_PLAYERS);
	}


	//Sets flag to keep screen on. Important during game setup so game is not cancelled
	void keepScreenOn() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	////////////ROOM UPDATE LISTENER METHODS ///////////////
	@Override
	public void onRoomCreated(int i, Room room) {

	}

	@Override
	public void onJoinedRoom(int i, Room room) {

	}

	@Override
	public void onLeftRoom(int i, String s) {

	}

	@Override
	public void onRoomConnected(int i, Room room) {

	}

	////////////END ROOM UPDATE LISTENER METHODS//////////////
}

package com.tdt4240.jankenmaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	private PlayServiceLauncher playServiceLauncher;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		playServiceLauncher = new PlayServiceLauncher(this);

		initialize(new JankenMaze(playServiceLauncher), config);
	}

	// we want play services to start automatically when the game begins and stop when the game exits, also we need to handle exceptions when the user fails to sign in. This is where the BaseGameUtil libraries come in, it takes care of all this, we just have to override our Activity methods and pass it on to them.
	@Override
	protected void onStart()
	{
		super.onStart();
		playServiceLauncher.onStart();

	}

	@Override
	protected void onStop()
	{
		super.onStop();
		playServiceLauncher.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		playServiceLauncher.onActivityresult(requestCode, resultCode, data);

	}


}

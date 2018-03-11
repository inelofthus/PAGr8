package com.tdt4240.jankenmaze;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tdt4240.jankenmaze.playServices.PlayServices;

public class AndroidLauncher extends AndroidApplication implements PlayServices {
	// Client used to sign in with Google APIs
	private GoogleSignInClient mGoogleSignInClient = null;

	// Request code used to invoke sign in user interactions.
	private static final int RC_SIGN_IN = 9001;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
        // Create the client used to sign in.
	    super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new JankenMaze(), config);
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signIn();
        System.out.println("HIII");
        System.out.println(isSignedIn());
        System.out.println("email" + signedInAccount.getEmail());
	}

	private boolean isSignedIn() {
		return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

	@Override
	public void signIn() {
		if (isSignedIn()){
			signInSilently();
		}
		else{
			startSignInIntent();

		}

	}

	private void signInSilently() {
		GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
				GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
		signInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							// The signed in account is stored in the task's result.
							GoogleSignInAccount signedInAccount = task.getResult();

						} else {
							// Player will need to sign-in explicitly using via UI
						}
					}
				});
        System.out.println("silent sign in");
    }

	@Override
	protected void onResume() {
		super.onResume();
		signInSilently();
	}

	public void startSignInIntent() {
		startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        System.out.println("I have the intension of signing in ");
    }
}

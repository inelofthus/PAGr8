package com.tdt4240.jankenmaze;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.google.android.gms.common.api.ApiException;
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
			System.out.println("Start signin intent");
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
							System.out.println("semail " + signedInAccount.getEmail());
							System.out.println("sdisplayname " + signedInAccount.getDisplayName());
							System.out.println("stostring " + signedInAccount.toString());
							System.out.println("sauthcode " + signedInAccount.getServerAuthCode());

						} else {
							// Player will need to sign-in explicitly using via UI
							System.out.println("Player will need to sign-in explicitly using via UI");
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		System.out.println("abcdefgh");
		if (requestCode == RC_SIGN_IN) {

			Task<GoogleSignInAccount> task =
					GoogleSignIn.getSignedInAccountFromIntent(intent);

			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				System.out.println("Accountz " + account.getEmail());
				//onConnected(account);
			} catch (ApiException apiException) {
				String message = apiException.getMessage();
				if (message == null || message.isEmpty()) {
					//message = getString(R.string.signin_other_error);
				}

				//onDisconnected();

				new AlertDialog.Builder(this)
						.setMessage(message)
						.setNeutralButton(android.R.string.ok, null)
						.show();
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}
}

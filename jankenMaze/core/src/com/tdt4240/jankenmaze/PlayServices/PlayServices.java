package com.tdt4240.jankenmaze.PlayServices;

/**
 * Created by karim on 15/03/2018.
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public boolean isSignedIn();
    void startSelectOpponents(boolean autoMatch);

}

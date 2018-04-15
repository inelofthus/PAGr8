package com.tdt4240.jankenmaze.desktop;

import com.tdt4240.jankenmaze.PlayServices.PlayServices;

/**
 * Created by karim on 09/04/2018.
 * This Class needs to be implemented for desktop in order for multiplayer functionality to work.
 */

public class DesktopPlayServiceLauncher implements PlayServices {

    @Override
    public void signIn() {

    }

    @Override
    public void signOut() {

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void startSelectOpponents(boolean autoMatch) {

    }

    @Override
    public void setGameListener(GameListener gameListener) {

    }

    @Override
    public void setNetworkListener(NetworkListener networkListener) {

    }

    @Override
    public void sendUnreliableMessageToOthers(byte[] messageData) {

    }
}

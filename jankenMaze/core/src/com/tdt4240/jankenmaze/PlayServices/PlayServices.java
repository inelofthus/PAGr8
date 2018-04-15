package com.tdt4240.jankenmaze.PlayServices;

import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

import java.util.List;

/**
 * Created by karim on 15/03/2018.
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public boolean isSignedIn();

    public void startSelectOpponents(boolean autoMatch);
    void setGameListener(GameListener gameListener);
    public void setNetworkListener(NetworkListener networkListener);
    void sendUnreliableMessageToOthers(byte[] messageData);
    void sendReliableMessageToOthers(byte[] messageData);
    void sendReliableMessageTo(String participantId, byte[] messageData);

    public interface NetworkListener{
        void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData);
        void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData);
        void onRoomReady(List<PlayerNetworkData> players);
    }

    public interface GameListener{
        void onMultiplayerGameStarting();
    }

}

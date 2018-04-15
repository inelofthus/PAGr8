package com.tdt4240.jankenmaze.PlayServices;

import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

import java.util.List;

/**
 * Created by karim on 15/03/2018.
 */

public interface PlayServices {
    void signIn();
    void signOut();
    boolean isSignedIn();

    void startSelectOpponents(boolean autoMatch);
    void setGameListener(GameListener gameListener);
    void setNetworkListener(NetworkListener networkListener);
    void sendUnreliableMessageToOthers(byte[] messageData);
    void sendReliableMessageToOthers(byte[] messageData);

    interface NetworkListener{
        void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData);
        void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData);
        void onRoomReady(List<PlayerNetworkData> players);
    }

    interface GameListener{
        void onMultiplayerGameStarting();
    }

}

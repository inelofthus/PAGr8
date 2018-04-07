package com.tdt4240.jankenmaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GameHelper;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.games.GamesStatusCodes.STATUS_OK;
import static com.google.android.gms.games.GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED;
import static com.google.android.gms.games.GamesActivityResultCodes.RESULT_INVALID_ROOM;
import static com.google.android.gms.games.GamesActivityResultCodes.RESULT_LEFT_ROOM;

/**
 * Created by karim on 05/04/2018.
 */

public class PlayServiceLauncher implements PlayServices, RoomUpdateListener, RoomStatusUpdateListener, RealTimeMessageReceivedListener, OnInvitationReceivedListener {

    private final static int requestCode = 1;
    private static final int MIN_PLAYERS = 1;
    private static final int MAX_PLAYERS = 7;
    private static final int RC_SELECT_PLAYERS = 10000;
    private final static int RC_WAITING_ROOM = 10002;
    private static final String TAG = "PlayServiceLauncher";

    public String currentRoomId = null;
    private final Activity activity;
    private GameHelper gameHelper;
    private GameListener gameListener;
    private NetworkListener networkListener;
    private String incomingInvitationId;

    public PlayServiceLauncher(AndroidLauncher activity) {
        this.activity = activity;
        gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(true);
        gameHelper.setShowErrorDialogs(true);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInFailed(){ Log.d(TAG, "onSignInFailed: ");}

            @Override
            public void onSignInSucceeded(){
                Log.d(TAG, "onSignInSucceeded: ");
                if (gameHelper.hasInvitation()) {
                    acceptInviteToRoom(gameHelper.getInvitationId());
                }
            }
        };

        gameHelper.setup(gameHelperListener);
    }

    private void acceptInviteToRoom(String invitationId) {
        Log.d(TAG, "Accepting invitation: " + invitationId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invitationId)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
        keepScreenOn();
        Games.RealTimeMultiplayer.join(gameHelper.getApiClient(), roomConfigBuilder.build());
    }


    @Override
    public void signIn()
    {
        try
        {
            activity.runOnUiThread(new Runnable()
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
            activity.runOnUiThread(new Runnable()
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
        activity.startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
        System.out.println(gameListener.toString());
    }

    @Override
    public void setNetworkListener(NetworkListener networkListener) {
        System.out.println("setNetworkListener");
        this.networkListener = networkListener;
    }

    @Override
    public void sendUnreliableMessageToOthers(byte[] messageData) {
        if (currentRoomId == null){
            System.out.println("RoomID is null!");
            return;
        }
        if (!gameHelper.isSignedIn()){
            System.out.println("not signed in");
            return;
        }

        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(gameHelper.getApiClient(), messageData, currentRoomId);
        Log.d(TAG, "sendUnreliableMessageToOthers: ");

    }


    public void onStart() {
        gameHelper.onStart(activity);
    }

    public void onStop() {
        gameHelper.onStop();
    }

    public void onActivityresult(int requestCode, int resultCode, Intent data){

        switch (requestCode) {
            case RC_SELECT_PLAYERS:
                // we got the result from the "select players" UI -- ready to create the room
                Log.d(TAG, "onActivityResult: RC_SELECT_PLAYERS");
                handleSelectPlayersResult(resultCode, data);
                break;
            case RC_WAITING_ROOM:
                Log.d(TAG, "onActivityResult: RC_WAITING_ROOM");
                handleWaitingRoomResult(resultCode, data);
                break;
            default:
                gameHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleWaitingRoomResult(int resultCode, Intent intent) {
        Room room = intent.getParcelableExtra(Multiplayer.EXTRA_ROOM);
        Log.d(TAG, "handleWaitingRoomResult: ");
        switch (resultCode) {
            case Activity.RESULT_OK:
                Log.d(TAG, "handleWaitingRoomResult: OK");
                List<PlayerNetworkData> playerList = new ArrayList<>();
                gameListener.onMultiplayerGameStarting();
                String currentPlayerId = Games.Players.getCurrentPlayerId(gameHelper.getApiClient());
                for (Participant participant : room.getParticipants()) {
                    String playerId = participant.getPlayer().getPlayerId();
                    PlayerNetworkData playerData = new PlayerNetworkData(playerId, participant.getParticipantId(), participant.getDisplayName());
                    if (currentPlayerId.equals(playerId)) {
                        playerData.isSelf = true;
                    }
                    playerList.add(playerData);
                }
                networkListener.onRoomReady(playerList);
                break;
            case Activity.RESULT_CANCELED:
                Log.d(TAG, "handleWaitingRoomResult: CANCEL");
                // TODO: leave room
                break;
            case RESULT_LEFT_ROOM:
                Log.d(TAG, "handleWaitingRoomResult: RESULT_LEFT_ROOM");
                Games.RealTimeMultiplayer.leave(gameHelper.getApiClient(), this, room.getRoomId());
                break;
            case RESULT_INVALID_ROOM:
                // TODO: handle invalid room
                Log.d(TAG, "handleWaitingRoomResult: INVALID");
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

    //Sets flag to keep screen on. Important during game setup so game is not cancelled
    void keepScreenOn() {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    ////////////ROOM UPDATE LISTENER METHODS ///////////////

    @Override
    public void onRoomCreated(int status, Room room) {
        Log.d(TAG, "onRoomCreated: ");
        switch (status) {
            case STATUS_OK:
                currentRoomId = room.getRoomId();
                showWaitingRoom(room);
                break;
            case STATUS_CLIENT_RECONNECT_REQUIRED:
                signIn();
                break;
            default:
        }

    }

    @Override
    public void onJoinedRoom(int status, Room room) {
       Log.d(TAG, "onJoinedRoom: ");
        switch (status) {
            case STATUS_OK:
                currentRoomId = room.getRoomId();
                showWaitingRoom(room);
                break;
            case STATUS_CLIENT_RECONNECT_REQUIRED:
                signIn();
                break;
            default:
        }

    }

    @Override
    public void onLeftRoom(int i, String s) {
        Log.d(TAG, "onLeftRoom: ");
    }

    @Override
    public void onRoomConnected(int status, Room room) {
        Log.d(TAG, "onRoomConnected(" + status + ", " + room + ")");

        //Need to run on an OpenGL context
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                gameListener.onMultiplayerGameStarting();
            }
        });


        if (status != STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + status);
            showGameError();
            return;
        }

    }

    ////////////END ROOM UPDATE LISTENER METHODS//////////////

    public void showGameError() {
        BaseGameUtils.makeSimpleDialog(activity, "ERROR");
    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    protected void showWaitingRoom(Room room) {
        Log.d(TAG, "showWaitingRoom: " + room);
        if (room == null) {
            Log.w(TAG, "showWaitingRoom: room is null, returning");
            return;
        }
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(gameHelper.getApiClient(), room, MIN_PLAYERS);
        // show waiting room UI
        activity.startActivityForResult(i, RC_WAITING_ROOM);
    }


    ////////RoomStatusUpdateListener methods //////////////////
    @Override
    public void onRoomConnecting(Room room) {
        Log.d(TAG, "onRoomConnecting");
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Log.d(TAG, "onRoomAutoMatching");

    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Log.d(TAG, "onPeerInvitedToRoom");

    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Log.d(TAG, "onPeerDeclined");
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Log.d(TAG, "onPeerJoined");
    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Log.d(TAG, "onPeerLeft");
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom: ");
        stopKeepingScreenOn();
        if (currentRoomId == null) currentRoomId = room.getRoomId();
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Log.d(TAG, "onDisconnectedFromRoom: ");
        currentRoomId = null;
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Log.d(TAG, "onPeersConnected");
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Log.d(TAG, "onPeersDisconnected");
    }

    @Override
    public void onP2PConnected(String s) {
        Log.d(TAG, "onP2PConnected");
    }

    @Override
    public void onP2PDisconnected(String s) {
        Log.d(TAG, "onP2PDisconnected");
    }

    ///////////////////// END RoomStatusUpdateListener methods //////////////////


    // Clears flag keeping the screen on
    void stopKeepingScreenOn() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    ////////////////RealTimeMessageReceivedListener/////////////////////////
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        if (networkListener == null) {
            Gdx.app.debug(TAG, "onRealTimeMessageReceived: NetworkListener is null");
            return;
        }
        byte[] messageData = realTimeMessage.getMessageData();
        String senderParticipantId = realTimeMessage.getSenderParticipantId();
        int describeContents = realTimeMessage.describeContents();
        if (realTimeMessage.isReliable()) {
            networkListener.onReliableMessageReceived(senderParticipantId, describeContents, messageData);
        } else {
            networkListener.onUnreliableMessageReceived(senderParticipantId, describeContents, messageData);
        }
    }

    ////////////////END RealTimeMessageReceivedListener/////////////////////////

    //////////////OnInvitationReceivedListener//////////////////////
    @Override
    public void onInvitationReceived(Invitation invitation) {
        //Store InvitationId and show popup on screen
        Log.d(TAG, "onInvitationReceived");
        incomingInvitationId = invitation.getInvitationId();
        Toast.makeText(activity, invitation.getInviter().getDisplayName() + " has invited you. ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInvitationRemoved(String s) {
        Log.d(TAG, "onInvitationRemoved: ");
    }

    ///////////////END OnInvitationReceivedListener ////////////////////
}

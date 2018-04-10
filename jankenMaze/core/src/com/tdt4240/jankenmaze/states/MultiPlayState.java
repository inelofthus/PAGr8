package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.gameecs.events.GameEvent;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gamesettings.PlayerType;
import com.tdt4240.jankenmaze.gamesettings.PlayerTypes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karim on 09/04/2018.
 */

public class MultiPlayState extends PlayState implements PlayServices.NetworkListener {
    private static final byte  POSITION = 1;
    private static final byte  GAME_OVER = 2;

    public MultiPlayState(SpriteBatch batch) {
        super(batch);
        gsm.playServices.setNetworkListener(this);
        GameSettings.getInstance().isMultplayerGame = true;

        if (!(GameSettings.getInstance().getPlayers() == null)){
            onRoomReady(GameSettings.getInstance().getPlayers());
        }
    }

    @Override
    protected void handleInput() {
        super.handleInput();
        if (Gdx.input.isTouched()){

           /* // will send a byte code for touched
            ByteBuffer buffer = ByteBuffer.allocate(2 * 4 + 1);
            int x = 1;
            int y = 2;
            buffer.put(POSITION);
            buffer.putInt(x);
            buffer.putInt(y);
            gsm.playServices.sendUnreliableMessageToOthers(buffer.array());*/
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        for(GameEvent gameOver: gameOverQueue.getEvents()){
            System.out.println("GameOverEvent");
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put(GAME_OVER);
            gsm.playServices.sendUnreliableMessageToOthers(buffer.array());
            gsm.push(new GameOverState());
        }

        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    ///////////NETWORK LISTENER METHODS //////////////////////
    @Override
    public void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
    }

    @Override
    public void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
        //TODO: A system needs to handle this message
        System.out.println("onUnreliableMessageReceived: " + senderParticipantId + "," + describeContents);

        ByteBuffer buffer = ByteBuffer.wrap(messageData);
        byte messageType = buffer.get();

        switch (messageType){
            case POSITION:
                int x = buffer.getInt();
                int y = buffer.getInt();
                System.out.println("POSITION UPDATED: MessageType: " + messageType + ", x: " + x + ", y:" + y);
                break;
            case GAME_OVER:
                System.out.println("GAME OVER MESSAGE RECEIVED");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        gsm.push(new GameOverState());
                    }
                });

                break;
        }


    }

    @Override
    public void onRoomReady(List<PlayerNetworkData> players) {
        System.out.println("Player1 " + players.get(0).displayName);
        if (GameSettings.getInstance().getPlayers()== null){
            GameSettings.getInstance().setPlayers(players);
        }
        //TODO: Initialize the world with all systems and components. Among other things create a player entity for each PlayerNetworkData

        ArrayList<PlayerType> playerTypes = PlayerTypes.getPlayerTypes();

        for(int i = 0; i < players.size(); i++){
            if (players.get(i).isLocalPlayer) {
                entityManager.createLocalPlayer(playerTypes.get(i % 3)); //Players have to be created after map.
            }
            else{
                entityManager.createPlayer(playerTypes.get(i % 3));
            }
        }
    }

    ////////////// END NETWORK LISTENER METHODS //////////////
}

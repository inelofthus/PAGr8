package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karim on 09/04/2018.
 */

public class MultiPlayState extends PlayState implements PlayServices.NetworkListener {
    private static final byte  POSITION = 1;

    public MultiPlayState(SpriteBatch batch) {
        super(batch);
        gsm.playServices.setNetworkListener(this);
    }

    @Override
    protected void handleInput() {
        super.handleInput();
        if (Gdx.input.isTouched()){

            // will send a byte code for touched
            ByteBuffer buffer = ByteBuffer.allocate(2 * 4 + 1);
            int x = 1;
            int y = 2;
            buffer.put(POSITION);
            buffer.putInt(x);
            buffer.putInt(y);
            gsm.playServices.sendUnreliableMessageToOthers(buffer.array());
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
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
        int x = buffer.getInt();
        int y = buffer.getInt();

        System.out.println("POSITION UPDATED: MessageType: " + messageType + ", x: " + x + ", y:" + y);

    }

    @Override
    public void onRoomReady(List<PlayerNetworkData> players) {
        System.out.println("Player1 " + players.get(0).displayName);
        //TODO: Initialize the world with all systems and components. Among other things create a player entity for each PlayerNetworkData


        ArrayList<String> playerTypes = new ArrayList<String>();
        playerTypes.addAll(Arrays.asList("Rock", "Paper", "Scissors"));

        for(int i = 0; i < players.size(); i++){
            if (players.get(i).isLocalPlayer) {
                entityManager.createLocalPlayer(playerTypes.get(i%3)); //Players have to be created after map.
            }
            else{
                entityManager.createPlayer(playerTypes.get(i%3));
            }
        }
    }

    ////////////// END NETWORK LISTENER METHODS //////////////
}

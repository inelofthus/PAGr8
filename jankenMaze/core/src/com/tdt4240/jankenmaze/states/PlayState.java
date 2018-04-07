package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

import java.awt.Font;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jonas on 07/03/2018.
 * TODO: Figure out better way of getting spritebatch
 *
 * Creates Engine and EntityManager, then calls on entityManager to update effectively delegating
 * all in-game code.
 */

public class PlayState extends State implements PlayServices.NetworkListener {
    Engine engine;
    private static final String TAG = "PlayState";
    private static final byte  POSITION = 1;
    private int numTouches = 0;
    com.tdt4240.jankenmaze.gameecs.EntityManager entityManager;
    SpriteBatch batch;
    int[][] binaryMap = {{1 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}}; //Map is instatiated from a binary matrix (A list of columns)

    public PlayState(SpriteBatch batch){
        super();
        gsm.playServices.setNetworkListener(this);
        this.batch = batch;

        engine = new Engine();
        entityManager = new com.tdt4240.jankenmaze.gameecs.EntityManager(engine, batch);
        entityManager.createMap(binaryMap, new Texture("greyWall.png"));
        entityManager.createHUDItem();
    }
    @Override
    protected void handleInput() {
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
        entityManager.update();
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        //Rendering is handled by Entity Systems for now
    }

    @Override
    public void dispose() {

    }

    ///////////NETWORK LISTENER METHODS //////////////////////
    @Override
    public void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
        Gdx.app.debug(TAG, "onReliableMessageReceived: " + senderParticipantId + "," + describeContents);
    }

    @Override
    public void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
        //TODO: A system needs to handle this message
        Gdx.app.debug(TAG, "onUnreliableMessageReceived: " + senderParticipantId + "," + describeContents);
        System.out.println("onUnreliableMessageReceived: " + senderParticipantId + "," + describeContents);

        ByteBuffer buffer = ByteBuffer.wrap(messageData);
        byte messageType = buffer.get();
        int x = buffer.getInt();
        int y = buffer.getInt();

                System.out.println("POSITION UPDATED: MessageType: " + messageType + ", x: " + x + ", y:" + y);

    }

    @Override
    public void onRoomReady(List<PlayerNetworkData> players) {
        Gdx.app.debug(TAG, "onRoomReady: ");
        System.out.println("Player1 " + players.get(0).displayName);
        //TODO: Initialize the world with all systems and components. Among other things create a player entity for each PlayerNetworkData


        ArrayList<String> playerTypes = new ArrayList<String>();
        playerTypes.addAll(Arrays.asList("Rock", "Paper", "Scissors"));

        for(int i = 0; i < players.size(); i++){
            if (players.get(i).isLocalPlayer) {
                entityManager.createLocalPlayer(playerTypes.get(i)); //Players have to be created after map.
            }
            else{
                entityManager.createPlayer(playerTypes.get(i));
            }
        }
    }

    ////////////// END NETWORK LISTENER METHODS //////////////
}

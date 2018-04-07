package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

import java.awt.Font;
import java.nio.ByteBuffer;
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
    private static final byte  TOUCHED = 1;
    private int numTouches = 0;
    com.tdt4240.jankenmaze.gameecs.EntityManager entityManager;

    public PlayState(SpriteBatch batch){
        super();
        gsm.playServices.setNetworkListener(this);
        engine = new Engine();
        entityManager = new com.tdt4240.jankenmaze.gameecs.EntityManager(engine, batch);

    }
    @Override
    protected void handleInput() {
        if (Gdx.input.isTouched()){

            // will send a byte code for touched
            ByteBuffer buffer = ByteBuffer.allocate(TOUCHED);
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

                numTouches ++;
                System.out.println("TOUCHED: " + numTouches);

    }

    @Override
    public void onRoomReady(List<PlayerNetworkData> players) {
        Gdx.app.debug(TAG, "onRoomReady: ");
        System.out.println("Player1" + players.get(0).displayName);
        //TODO: Initialize the world with all systems and components. Among other things create a player entity for each PlayerNetworkData

    }

    ////////////// END NETWORK LISTENER METHODS //////////////
}

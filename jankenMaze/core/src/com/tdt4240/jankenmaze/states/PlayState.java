package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

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
    com.tdt4240.jankenmaze.gameecs.EntityManager entityManager;

    public PlayState(SpriteBatch batch){
        super();
        engine = new Engine();
        entityManager = new com.tdt4240.jankenmaze.gameecs.EntityManager(engine, batch);
        gsm.playServices.setNetworkListener(this);
    }
    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        entityManager.update();
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
    }

    @Override
    public void onRoomReady(List<PlayerNetworkData> players) {
        Gdx.app.debug(TAG, "onRoomReady: ");
        //TODO: Initialize the world with all systems and components. Among other things create a player entity for each PlayerNetworkData

    }

    ////////////// END NETWORK LISTENER METHODS //////////////
}

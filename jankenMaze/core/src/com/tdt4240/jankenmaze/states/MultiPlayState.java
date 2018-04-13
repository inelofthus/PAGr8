package com.tdt4240.jankenmaze.states;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tdt4240.jankenmaze.gameMessages.HealthMessage;
import com.tdt4240.jankenmaze.gameMessages.MessageCodes;
import com.tdt4240.jankenmaze.gameMessages.PositionMessage;
import com.tdt4240.jankenmaze.gameecs.components.Health;
import com.tdt4240.jankenmaze.gameecs.components.PlayerInfo;
import com.tdt4240.jankenmaze.gameecs.components.Position;
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
            buffer.put(MessageCodes.GAME_OVER);
            gsm.playServices.sendReliableMessageToOthers(buffer.array());
            HealthMessage.getInstance().gameOver(engine);
            gsm.set(new GameOverState());
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
        System.out.println("MultiPlayState:    onReliableMessageReceived: " + senderParticipantId + "," + describeContents);

        ByteBuffer buffer = ByteBuffer.wrap(messageData);
        byte messageType = buffer.get();

        switch (messageType){


            case MessageCodes.GAME_OVER:
                System.out.println("GAME OVER MESSAGE RECEIVED");
                HealthMessage.getInstance().gameOver(engine);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        gsm.set(new GameOverState());
                    }
                });

                break;
            case MessageCodes.HEALTH:
                System.out.println("HEALTH MESSAGE RECEIVED");
                int hp = buffer.getInt();
                if (! (GameSettings.getInstance().getPlayers() == null)){
                    HealthMessage.getInstance().updatePlayerHealth(senderParticipantId, new Health(hp));
                    HealthMessage.getInstance().hasChanged = true;
                }

                break;
        }
    }

    @Override
    public void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
        //TODO: A system needs to handle this message
        System.out.println("onUnreliableMessageReceived: " + senderParticipantId + "," + describeContents);

        ByteBuffer buffer = ByteBuffer.wrap(messageData);
        byte messageType = buffer.get();

        switch (messageType){
            case MessageCodes.POSITION:
                float x=buffer.getFloat();
                float y=buffer.getFloat();
                System.out.println("MultiPlayState: x:" + x + "y: " + y );
                if (! (GameSettings.getInstance().getPlayers() == null)){
                    PositionMessage.getInstance().updateRemotePlayerPostion(senderParticipantId, new Position(x,y));
                }
                break;
            case MessageCodes.GAME_OVER:
                System.out.println("GAME OVER MESSAGE RECEIVED");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        gsm.set(new GameOverState());
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
                entityManager.createLocalPlayer(playerTypes.get(i % playerTypes.size()), players.get(i)); //Players have to be created after map.
            }
            else{
                entityManager.createPlayer(playerTypes.get(i % playerTypes.size()), players.get(i));
            }
        }

        HealthMessage.getInstance().reset();
        PositionMessage.getInstance().reset();
        entityManager.addMPSystemsToEngine(gsm.playServices);
    }

    ////////////// END NETWORK LISTENER METHODS //////////////
}

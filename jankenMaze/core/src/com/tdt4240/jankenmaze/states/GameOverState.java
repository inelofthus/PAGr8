package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tdt4240.jankenmaze.PlayServices.PlayServices;
import com.tdt4240.jankenmaze.gameMessages.HealthMessage;
import com.tdt4240.jankenmaze.gameMessages.MessageCodes;
import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;
import com.tdt4240.jankenmaze.gamesettings.GameSettings;
import com.tdt4240.jankenmaze.gamesettings.Maps;
import com.tdt4240.jankenmaze.view.GameOverView;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by bartosz on 4/7/18.
 * This is the state that is activated when the game is over.
 */

public class GameOverState extends State implements PlayServices.NetworkListener {

    private GameOverView gameOverView;
    private SpriteBatch batch;
    private ClickListener listenerBtn_playAgain, listenerBtn_mainMenu, listenerBtn_quitGame;

    public GameOverState(){
        super();

        this.gameOverView = new GameOverView();
        gsm.playServices.setNetworkListener(this);

        addButtonListeners();

        gameOverView.setResultLabel(HealthMessage.getInstance().getResults());

        cam.setToOrtho(false);
    }

    @Override
    protected void handleInput() {
    }

    @Override
    public void update(float dt) {
        if(Gdx.input.isTouched()){
            handleInput();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        this.batch = sb;
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        gameOverView.render(batch);
        sb.end();
    }

    @Override
    public void dispose() {
        gameOverView.dispose();
    }

    @Override
    public void onReliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {
        System.out.println("GameOverState: onReliableMessageReceived: " + senderParticipantId + "," + describeContents);

        ByteBuffer buffer = ByteBuffer.wrap(messageData);
        byte messageType = buffer.get();

        switch (messageType){
            case MessageCodes.PLAY_AGAIN:
                Maps.getINSTANCE().increment();
                System.out.println("PLAY AGAIN MESSAGE RECEIVED");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {gsm.set(new MultiPlayState(batch));
                    }
                });

                break;
            case MessageCodes.QUIT:
                System.out.println("QUIT MESSAGE RECEIVED");
                gsm.playServices.leaveRoom();

                break;
        }
    }

    @Override
    public void onUnreliableMessageReceived(String senderParticipantId, int describeContents, byte[] messageData) {


    }

    @Override
    public void onRoomReady(List<PlayerNetworkData> players) {

    }

    private void addButtonListeners(){

        gameOverView.btn_playAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Maps.getINSTANCE().increment();
                if(GameSettings.getInstance().isMultplayerGame){
                    ByteBuffer buffer = ByteBuffer.allocate(1);
                    buffer.put(MessageCodes.PLAY_AGAIN);
                    gsm.playServices.sendReliableMessageToOthers(buffer.array());
                    gsm.set(new MultiPlayState(batch));
                }else {
                    gsm.set(new SinglePlayState(batch));
                }
            }
        });

        gameOverView.btn_toMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Maps.getINSTANCE().zeroMaps();
                if(GameSettings.getInstance().isMultplayerGame){
                    ByteBuffer buffer = ByteBuffer.allocate(1);
                    buffer.put(MessageCodes.QUIT);
                    gsm.playServices.sendReliableMessageToOthers(buffer.array());
                    gsm.playServices.leaveRoom();
                    gsm.set(new OnlineMenuState());
                }else {
                    gsm.set(new OfflineMenuState());
                }
            }
        });

        gameOverView.btn_quitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(GameSettings.getInstance().isMultplayerGame){
                    ByteBuffer buffer = ByteBuffer.allocate(1);
                    buffer.put(MessageCodes.QUIT);
                    gsm.playServices.sendReliableMessageToOthers(buffer.array());
                    gsm.playServices.leaveRoom();
                }
                Gdx.app.exit();
            }
        });
    }
}

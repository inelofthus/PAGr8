package com.tdt4240.jankenmaze.onlineservice;

import com.badlogic.gdx.Gdx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Ine on 12.03.2018.
 *
 * This is a singleton class that handles all socket related functionality
 */

public final class SocketConnection {

    private static final SocketConnection socketconn = new SocketConnection();

    private Socket socket;

    public static SocketConnection getSocketConnection(){
        return socketconn;
    }

    public void connectSocket(){
        try {
            socket = IO.socket("http://localhost:8080");
            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void configSocketEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call (Object... args) {
                Gdx.app.log("SocketIO", "Connected");
            }
        }).on("socketID", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + playerId);
                } catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "New player connected: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting New Player ID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    //let relevant system handle event
                    Gdx.app.log("SocketIO", "Player disconnected: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting disconnected player ID");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args){
                JSONArray objects = (JSONArray) args[0];
                //let relevant system handle event
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    //let relevant system handle event
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error retrieving player position");
                }
            }
        });
    }

    public void updateServer(int positionX, int positionY){
        JSONObject data = new JSONObject();
        try {
            data.put("x", positionX);
            data.put("y", positionY);
            socket.emit("playerMoved", data);
        } catch(JSONException e){
            Gdx.app.log("SocketIO", "Error sending update data");
        }
    }
}

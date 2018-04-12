package com.tdt4240.jankenmaze.gameecs.events;

public class RemoteVariable {
    private float x;
    private float y;
    private String playerID;
    public RemoteVariable(float x, float y, String playerID){
        this.x=x;
        this.y=y;
        this.playerID=playerID;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getPlayerID() {
        return playerID;
    }
}
package com.tdt4240.jankenmaze.gamesettings;

import com.tdt4240.jankenmaze.gameecs.components.PlayerNetworkData;

import java.util.List;

/**
 * Created by karim on 10/04/2018.
 */

//Singleton class containing the chosen game settings
public class GameSettings {

    private static final GameSettings INSTANCE = new GameSettings();
    public boolean isMultplayerGame = false;
    private List<PlayerNetworkData> players = null;

    public GameSettings() {
    }

    public static GameSettings getInstance(){
        return INSTANCE;
    }

    public void setPlayers(List<PlayerNetworkData> players) {
        this.players = players;
    }

    public List<PlayerNetworkData> getPlayers() {
        return players;
    }

}

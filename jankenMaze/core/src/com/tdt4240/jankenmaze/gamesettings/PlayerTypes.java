package com.tdt4240.jankenmaze.gamesettings;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Ine on 10.04.2018.
 */

public class PlayerTypes {
    private static ArrayList<PlayerType> PLAYER_LIST;

    private static HashMap<PlayerType, ArrayList<PlayerType>> TARGETED_BY_MAP;

    private static final PlayerTypes playerTypes = new PlayerTypes();

    public PlayerTypes() {
        PLAYER_LIST = initializePlayerList();
        TARGETED_BY_MAP = initializeTargetedByMap();
    }

    private static ArrayList<PlayerType> initializePlayerList() {
        ArrayList<PlayerType> result = new ArrayList<PlayerType>();
        result.add(PlayerType.ROCK);
        result.add(PlayerType.PAPER);
        result.add(PlayerType.SCISSORS);
        return result;
    }

    private static HashMap<PlayerType, ArrayList<PlayerType>> initializeTargetedByMap() {
        HashMap<PlayerType, ArrayList<PlayerType>> result = new HashMap<PlayerType, ArrayList<PlayerType>>();
        result.put(PlayerType.ROCK, new ArrayList<PlayerType>(Arrays.asList(PlayerType.PAPER)));
        result.put(PlayerType.PAPER,new ArrayList<PlayerType>(Arrays.asList(PlayerType.SCISSORS)));
        result.put(PlayerType.SCISSORS, new ArrayList<PlayerType>(Arrays.asList(PlayerType.ROCK)));
        return result;
    }

    public static ArrayList<PlayerType> getPlayerTypes() {
        return PLAYER_LIST;
    }

    /**
     * This is map is not set as a member variable, as Textures
     * need to be instantiated on the correct thread
     * @return HashMap with PlayerType as Key and Texture as value
     */
    public static HashMap<PlayerType, Texture> getPlayerTextures(){
        HashMap<PlayerType, Texture> result = new HashMap<PlayerType, Texture>();
        result.put(PlayerType.ROCK, new Texture("singleRock.png"));
        result.put(PlayerType.PAPER,new Texture("singlePaper.png"));
        result.put(PlayerType.SCISSORS,new Texture("singleScissors.png"));
        return result;
    }

    public static HashMap<PlayerType, ArrayList<PlayerType>> getPlayerTargetedBy(){
        return TARGETED_BY_MAP;
    }

    public static ArrayList<PlayerType> getPlayerTargetedBy(PlayerType type){
        return TARGETED_BY_MAP.get(type);
    }

    public static PlayerTypes getInstance(){
        return playerTypes;
    }

}
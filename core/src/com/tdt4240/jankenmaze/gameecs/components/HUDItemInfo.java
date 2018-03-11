package com.tdt4240.jankenlabyrinth.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Ine on 07.03.2018.
 */

public class HUDItemInfo implements Component {
    public String itemType;

    public HUDItemInfo(String itemType){
        this.itemType = itemType;
    }
}

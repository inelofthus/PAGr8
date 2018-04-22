package com.tdt4240.jankenmaze.gameecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Ine on 07.03.2018.
 * This component consists of a String defining the type of HUDitem.
 */

public class HUDItemInfo implements Component {
    public String itemType;

    public HUDItemInfo(String itemType){
        this.itemType = itemType;
    }
}

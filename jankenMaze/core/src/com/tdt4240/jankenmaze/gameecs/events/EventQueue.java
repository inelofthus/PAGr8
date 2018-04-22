package com.tdt4240.jankenmaze.gameecs.events;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

import java.util.PriorityQueue;

/**
 * Created by Ine on 22.03.2018.
 * This class implements an ashley listener for enabling communication between systems.
 */

public class EventQueue implements Listener<GameEvent> {

    private PriorityQueue<GameEvent> eventQueue;

    public EventQueue(){
        eventQueue = new PriorityQueue<GameEvent>();
    }


    //returns the events in priority queue and then clears the queue
    public GameEvent[] getEvents(){
        GameEvent[] events = eventQueue.toArray(new GameEvent[0]);
        eventQueue.clear();
        return events;
    }

    public GameEvent poll(){
        return eventQueue.poll();
    }


    //adds and GameEvent ot the priority queue
    @Override
    public void receive(Signal<GameEvent> signal, GameEvent event){
        eventQueue.add(event);
    }

}

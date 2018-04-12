package com.tdt4240.jankenmaze.gameecs.events;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class RemoteQueue implements Listener<RemoteVariable>{
    private LinkedList<RemoteVariable> remoteQueue;

    public RemoteQueue(){
        remoteQueue = new LinkedList<RemoteVariable>();
    }

    public RemoteVariable[] getRemoteVariable(){
        System.out.println("RemoteQueue: " );
        RemoteVariable[] variables = remoteQueue.toArray(new RemoteVariable[0]);
        remoteQueue.clear();
        return variables;
    }

    public boolean isEmpty(){
        return remoteQueue.isEmpty();

    }

    public RemoteVariable poll(){
        return remoteQueue.poll();
    }

    @Override
    public void receive(Signal<RemoteVariable> signal, RemoteVariable variable){
        remoteQueue.add(variable);
    }


}

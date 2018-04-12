package com.tdt4240.jankenmaze.gameecs.events;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

import java.util.PriorityQueue;

public class VariableQueue implements Listener<GameVariable> {
    private PriorityQueue<GameVariable> variableQueue;

    public VariableQueue(){
        variableQueue = new PriorityQueue<GameVariable>();
    }

    public GameVariable[] getVariables(){
        GameVariable[] variables = variableQueue.toArray(new GameVariable[0]);
        variableQueue.clear();
        return variables;
    }

    public GameVariable poll(){
        return variableQueue.poll();
    }

    @Override
    public void receive(Signal<GameVariable> signal, GameVariable variable) {
        variableQueue.add(variable);
    }
}

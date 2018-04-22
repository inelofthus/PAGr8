package com.tdt4240.jankenmaze.gameecs.events;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;

import java.util.PriorityQueue;


/**This class is not used in the game. It was created with intention to decide what kind of variable
 * has been changed, but because of changes in the game development it was never used.
 */
public class VariableQueue implements Listener<GameVariable> {
    private PriorityQueue<GameVariable> variableQueue;

    public VariableQueue(){
        variableQueue = new PriorityQueue<GameVariable>();
    }


    //returns gameVariables and clears the queue
    public GameVariable[] getVariables(){
        GameVariable[] variables = variableQueue.toArray(new GameVariable[0]);
        variableQueue.clear();
        return variables;
    }

    public GameVariable poll(){
        return variableQueue.poll();
    }


    //adds gameVariable to the priority queue
    @Override
    public void receive(Signal<GameVariable> signal, GameVariable variable) {
        variableQueue.add(variable);
    }
}

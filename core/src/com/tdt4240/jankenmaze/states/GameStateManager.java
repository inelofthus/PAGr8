package com.tdt4240.jankenmaze.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by jonas on 07/03/2018.
 *
 */

public class GameStateManager {
    private static final GameStateManager gsm = new GameStateManager();
    private Stack<State> states = new Stack<State>();
    private GameStateManager(){
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();
    }

    public static GameStateManager getGsm() {
        return gsm;
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public boolean hasStates(){ return !(states.empty()); }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }
}

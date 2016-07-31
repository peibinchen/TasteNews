package com.example.asus.tastenews.floatingwindow.utils;

/**
 * Created by ASUS on 2016/7/7.
 */
public enum PlayerState {

    STATE_PLAYING(0x15),STATE_PREPARE(0x25),STATE_STOP(0x35),STATE_PAUSE(0x45),STATE_READY(0x55);

    private int state;
    PlayerState(int s){
        state = s;
    }
    int getState(){return state;}

    public boolean isEqual(PlayerState ps){
        return ps.state == this.state;
    }
}

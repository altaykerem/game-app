package com.example.hp.gameapp;

public class Timer {
    private int seconds;
    private boolean running;

    public Timer() {
        seconds = 10;
        running = true;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int newSeconds) {
        seconds = newSeconds;
    }

    public void setRunning(boolean runningState) {
        running = runningState;
    }

    public boolean isRunning() {
        return running;
    }

}
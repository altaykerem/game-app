package com.example.hp.gameapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Session implements Parcelable{
    private int score;
    private int lives;
    private int difficulty = 4;

    private final static int INITIAL_LIVES = 4;

    public Session(){
        setScore(0);
        setLives(INITIAL_LIVES);
    }

    protected Session(Parcel in) {
        score = in.readInt();
        lives = in.readInt();
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeInt(lives);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}

package com.example.hp.gameapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String mail;
    public String location;
    public int quizScore = 0;
    public int flagScore = 0;

    public User(){}

    public User(String username, String mail, String location){
        this.username = username;
        this.mail = mail;
        this.location = location;
    }

    public void setFlagScore(int flagScore) {
        this.flagScore = flagScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }
}

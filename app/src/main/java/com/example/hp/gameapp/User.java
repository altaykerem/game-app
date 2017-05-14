package com.example.hp.gameapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String mail;
    public String location;

    public User(){}

    public User(String username, String mail, String location){
        this.username = username;
        this.mail = mail;
        this.location = location;
    }
}

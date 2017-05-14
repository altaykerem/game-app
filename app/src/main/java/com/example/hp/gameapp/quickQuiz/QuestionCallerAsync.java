package com.example.hp.gameapp.quickQuiz;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionCallerAsync extends AsyncTask{
    private DatabaseReference mDatabase;
    private Activity activity;

    public QuestionCallerAsync(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("db", mDatabase.child("category").child("animal").getKey());
        return null;
    }
}

package com.example.hp.gameapp.QuickQuiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.hp.gameapp.R;
import com.example.hp.gameapp.User;

public class QuickQuizActivity extends Activity{

    private boolean fromQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_quick);

        Intent intent = getIntent();
        int gameID = (int) intent.getExtras().get("gameID");
        User user = intent.getParcelableExtra("user");
        fromQuestion = intent.getBooleanExtra("from_question", false);
        boolean result = intent.getBooleanExtra("result", false);
        int lastCategory = intent.getIntExtra("category_index",-1);
        int lastQuestion = intent.getIntExtra("question_index",-1);

        if(savedInstanceState != null){
            fromQuestion = savedInstanceState.getBoolean("from_question");
        }

        QuickQuizFragment gf = (QuickQuizFragment) getFragmentManager().findFragmentById(R.id.quick_frag);
        gf.setGameID(gameID);
        gf.setUser(user);
        gf.setResult(result);
        gf.setFromQuestion(fromQuestion);
        gf.setCategoryIndex(lastCategory);
        gf.setQuestionIndex(lastQuestion);
        gf.setStates(intent.getIntegerArrayListExtra("states"));
        gf.setNextQuestion(intent.getIntegerArrayListExtra("next_question"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("from_question", fromQuestion);
    }
}

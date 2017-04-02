package com.example.hp.gameapp.QuickQuiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.hp.gameapp.R;
import com.example.hp.gameapp.User;

public class QuestionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_question_holder);

        Intent intent = getIntent();
        int gameID = (int) intent.getExtras().get("gameID");
        User user = intent.getParcelableExtra("user");
        Question question = intent.getParcelableExtra("question");
        int lastCategory = intent.getIntExtra("category_index",-1);
        int lastQuestion = intent.getIntExtra("question_index",-1);

        QuestionFragment gf = (QuestionFragment) getFragmentManager().findFragmentById(R.id.question_fragment);
        gf.setGameID(gameID);
        gf.setUser(user);
        gf.setQuestion(question);
        gf.setCategoryIndex(lastCategory);
        gf.setQuestionIndex(lastQuestion);
        gf.setStates(intent.getIntegerArrayListExtra("states"));
        gf.setNextQuestion(intent.getIntegerArrayListExtra("next_question"));
    }
}

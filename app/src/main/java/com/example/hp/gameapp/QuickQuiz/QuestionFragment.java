package com.example.hp.gameapp.QuickQuiz;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.gameapp.R;
import com.example.hp.gameapp.Timer;
import com.example.hp.gameapp.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuestionFragment extends Fragment {

    private User user;
    private long gameID;
    private int categoryIndex, questionIndex;
    private ArrayList<Integer> states, nextQuestion;
    Question intendedQuestion;
    Timer questionTimer = new Timer();
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        layout = (LinearLayout) view.findViewById(R.id.question_layout);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            TextView questionView = (TextView) view.findViewById(R.id.question);
            questionView.setText(intendedQuestion.getQuestion());

            List<String> choices = intendedQuestion.getMultipleChoice();
            Collections.shuffle(choices);

            for(int i=0;i<choices.size();i++){
                Button b = makeButton(choices.get(i));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionTimer.setRunning(false);
                        returnToCategories(checkChoice((Button) v));
                    }
                });
                layout.addView(b);
            }
        }
        runTimer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            questionTimer.setSeconds(savedInstanceState.getInt("seconds"));
            questionTimer.setRunning(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", questionTimer.getSeconds());
        questionTimer.setRunning(false);
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    private Button makeButton(String label){
        Button b = new Button(getActivity());
        b.setText(label);
        b.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        b.setBackgroundColor(Color.GRAY);
        return b;
    }

    private void runTimer() {
        final TextView timeView = (TextView) getActivity().findViewById(R.id.timer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = questionTimer.getSeconds()/3600;
                int minutes = (questionTimer.getSeconds()%3600)/60;
                int secs = questionTimer.getSeconds()%60;
                String time = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (questionTimer.isRunning()) {
                    questionTimer.setSeconds(questionTimer.getSeconds()-1);
                    if(questionTimer.getSeconds()==0){
                        questionTimer.setRunning(false);
                        returnToCategories(false);
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private boolean checkChoice(Button v){
        return v.getText().equals(intendedQuestion.getCorrectAnswer());
    }

    private void returnToCategories(boolean result){
        View fragmentContainer = getActivity().findViewById(R.id.fragment_container);
        if(fragmentContainer != null){
            QuickQuizFragment quizFragment = new QuickQuizFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            quizFragment.setGameID(gameID);
            quizFragment.setUser(user);
            quizFragment.setResult(result);
            quizFragment.setFromQuestion(true);
            quizFragment.setCategoryIndex(categoryIndex);
            quizFragment.setQuestionIndex(questionIndex);
            quizFragment.setStates(states);
            quizFragment.setNextQuestion(nextQuestion);
            ft.replace(R.id.fragment_container, quizFragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }else{
            Intent intent = new Intent(getActivity(), QuickQuizActivity.class);
            intent.putExtra("gameID", (int)gameID);
            intent.putExtra("user", user);
            intent.putExtra("result", result);
            intent.putExtra("from_question", true);
            intent.putExtra("category_index", categoryIndex);
            intent.putExtra("question_index", questionIndex);
            intent.putIntegerArrayListExtra("states", states);
            intent.putIntegerArrayListExtra("next_question", nextQuestion);
            startActivity(intent);
        }
    }

    public void setUser(User user) { this.user = user; }
    public void setQuestion(Question q){ this.intendedQuestion = q; }

    public void setCategoryIndex(int categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public void setStates(ArrayList<Integer> states) {
        this.states = states;
    }

    public void setNextQuestion(ArrayList<Integer> nextQuestion) {
        this.nextQuestion = nextQuestion;
    }
}

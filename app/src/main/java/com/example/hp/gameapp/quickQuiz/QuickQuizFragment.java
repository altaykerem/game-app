package com.example.hp.gameapp.QuickQuiz;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hp.gameapp.InitialScreen;
import com.example.hp.gameapp.R;
import com.example.hp.gameapp.User;

import java.util.ArrayList;
import java.util.List;

public class QuickQuizFragment extends Fragment {

    TextView nicknameView;
    LinearLayout categoryLayout;
    LinearLayout fields;

    ArrayList<Category> categorizedQuestions = CategorizedQCreator.getCategorizedQuestions();
    ArrayList<Integer> states, nextQuestion;

    private User user;
    private long gameID;
    private boolean result, fromQuestion;
    private int categoryIndex, questionIndex;

    private static final int SCREEN_CATEGORY_NUM = 4;

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            nicknameView = (TextView) view.findViewById(R.id.score);
            categoryLayout = (LinearLayout) view.findViewById(R.id.option_layout);
            nicknameView.setText(user.getName() + ": " + getString(R.string.score, user.getScore()));
        }
        if(states == null){
            states = new ArrayList<>();
            nextQuestion = new ArrayList<>();
            for(int i=0;i<categorizedQuestions.size();i++){
                states.add(Color.BLUE);
                nextQuestion.add(0);
            }
        }

        setAnswer();
        createGame();
        checkGame();
    }

    private void createGame(){
        for(int i=0;i<categorizedQuestions.size();i++){
            //create a "field" for each row containing categories
            if(i% SCREEN_CATEGORY_NUM ==0){
                fields = new LinearLayout(getActivity());
                fields.setOrientation(LinearLayout.HORIZONTAL);
                fields.setWeightSum(SCREEN_CATEGORY_NUM);
            }
            //group consists of categories and associated questions
            LinearLayout group = categoryLayout(categorizedQuestions.get(i).getCategoryName());
            final List questions = categorizedQuestions.get(i).getQuestions();

            for (int j=0;j<questions.size();j++){
                StateButton q = new StateButton(getActivity(), (Question) questions.get(j), i, j);
                if(j==questions.size()-nextQuestion.get(i)-1) q.setState(states.get(i));
                else if(j>questions.size()-nextQuestion.get(i)-1) q.setState(Color.GREEN);
                q.setText(String.valueOf(100*(questions.size()-j)));
                q.setOnClickListener(
                        buttonListener(Color.BLUE, q.getQ(), q.getCategoryNo(), q.getQuestionNo()));
                group.addView(q);
            }
            fields.addView(group);

            if(i% SCREEN_CATEGORY_NUM == SCREEN_CATEGORY_NUM -1||(i==categorizedQuestions.size()-1))
                categoryLayout.addView(fields); //control column number of categories
        }

    }

    private void checkGame(){
        if(!checkAvailable()) {
            Intent toStart = new Intent(getActivity(), InitialScreen.class);
            toStart.putExtra("user",user);
            startActivity(toStart);
        }
    }

    private LinearLayout categoryLayout(String categoryName){
        LinearLayout group = new LinearLayout(getActivity());
        group.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        TextView categoryView = new TextView(getActivity());
        categoryView.setLayoutParams(params);
        categoryView.setBackgroundColor(Color.MAGENTA);
        categoryView.setGravity(Gravity.CENTER);
        categoryView.setText(categoryName);
        group.addView(categoryView);
        return group;
    }

    boolean checkAvailable(){
        for(int state: states)
            if (state == Color.BLUE) return true;
        return false;
    }
/*
    int calculateScore(){
        int total = 0;
        for (StateButton[] buttonGroup : buttons) {
            for (int j = 0; j < buttons[0].length; j++)
                if (buttonGroup[j].getState() == Color.GREEN)
                    total += 100 * (buttonGroup.length - j);
        }
        return total;
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_quiz, container, false);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("gameID", gameID);
        outState.putParcelable("user", user);
        outState.putIntegerArrayList("states", states);
        outState.putIntegerArrayList("next_question", nextQuestion);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            gameID = savedInstanceState.getLong("gameID");
            user = savedInstanceState.getParcelable("user");
            states = savedInstanceState.getIntegerArrayList("states");
            nextQuestion = savedInstanceState.getIntegerArrayList("next_question");
            setFromQuestion(false);


        }
    }

    public View.OnClickListener buttonListener(final int state, final Question question, final int cIndex, final int qIndex){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((StateButton) v).getState()== state){
                    View fragmentContainer = getActivity().findViewById(R.id.fragment_container);
                    if(fragmentContainer != null){
                        QuestionFragment questionFragment = new QuestionFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        questionFragment.setGameID(gameID);
                        questionFragment.setUser(user);
                        questionFragment.setQuestion(question);
                        questionFragment.setCategoryIndex(cIndex);
                        questionFragment.setQuestionIndex(qIndex);
                        questionFragment.setStates(states);
                        questionFragment.setNextQuestion(nextQuestion);
                        ft.replace(R.id.fragment_container, questionFragment);
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                    }else{
                        Intent intent = new Intent(getActivity(), QuestionActivity.class);
                        intent.putExtra("gameID", (int)gameID);
                        intent.putExtra("user", user);
                        intent.putExtra("question", question);
                        intent.putExtra("category_index", cIndex);
                        intent.putExtra("question_index", qIndex);
                        intent.putIntegerArrayListExtra("states", states);
                        intent.putIntegerArrayListExtra("next_question", nextQuestion);
                        startActivity(intent);
                    }
                }
            }
        };
    }

    public void setAnswer(){
        if(fromQuestion){
            if(result){
                if(questionIndex>0) states.set(categoryIndex, Color.BLUE);
                else states.set(categoryIndex, Color.GREEN);
                nextQuestion.set(categoryIndex, nextQuestion.get(categoryIndex) + 1);
                user.setScore(user.getScore() + 100*nextQuestion.get(categoryIndex));
                nicknameView.setText(user.getName() + ": " + getString(R.string.score, user.getScore()));
            }else states.set(categoryIndex, Color.RED);
        }
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }
    public void setUser(User user) { this.user = user; }
    public void setResult(boolean result) { this.result = result; }
    public void setFromQuestion(boolean fromQuestion) {
        this.fromQuestion = fromQuestion;
    }

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

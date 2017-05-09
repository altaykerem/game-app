package com.example.hp.gameapp.QuickQuiz;

import android.content.Context;
import android.graphics.Color;

public class StateButton extends android.support.v7.widget.AppCompatButton{

    int state = Color.GRAY;
    int categoryNo = 0;
    int questionNo = 0;
    Question q;

    public StateButton(Context context){
        super(context);
        this.setBackgroundColor(state);
        this.q = null;
    }
    public StateButton(Context context, Question q, int category, int question) {
        super(context);
        this.setBackgroundColor(state);
        this.q = q;
        this.categoryNo = category;
        this.questionNo = question;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        this.setBackgroundColor(state);
    }

    public Question getQ() {
        return q;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public int getQuestionNo() {
        return questionNo;
    }
}
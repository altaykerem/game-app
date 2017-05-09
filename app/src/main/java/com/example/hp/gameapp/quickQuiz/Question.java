package com.example.hp.gameapp.QuickQuiz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

class Question implements Parcelable{
    private String question, correctAnswer;
    private List<String> answers = new ArrayList<>();

    Question(String question, String answer){
        this.question = question;
        correctAnswer = answer;
        answers.add(correctAnswer);
    }

    protected Question(Parcel in) {
        question = in.readString();
        correctAnswer = in.readString();
        in.readStringList(answers);
    }

    String getQuestion(){
        return question;
    }
    String getCorrectAnswer(){
        return correctAnswer;
    }

    List<String> getMultipleChoice(){
        return answers;
    }

    void addMultipleChoice(String wrongAnswer){
        answers.add(wrongAnswer);
    }

    @Override
    public int describeContents() {
        return answers.size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(correctAnswer);
        dest.writeStringList(answers);
    }


    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}

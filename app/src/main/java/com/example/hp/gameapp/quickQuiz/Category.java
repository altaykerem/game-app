package com.example.hp.gameapp.quickQuiz;

import java.util.ArrayList;

class Category {
    private ArrayList<Question> questions = new ArrayList<>();
    private String categoryName;

    Category(String name){
        categoryName = name;
    }

    String getCategoryName(){return categoryName;}

    void addQuestion(String q, String a, ArrayList<String> wrongChoices){
        Question question = new Question(q,a);
        for(int i=0;i<wrongChoices.size();i++){
            question.addMultipleChoice(wrongChoices.get(i));
        }
        questions.add(question);
    }

    ArrayList<Question> getQuestions(){
        return questions;
    }
}

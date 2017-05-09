package com.example.hp.gameapp.quickQuiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class CategorizedQCreator {
    private static ArrayList<Category> categorizedQuestions = new ArrayList<>();
    private static CategorizedQCreator instance = new CategorizedQCreator();

    static CategorizedQCreator getInstance(){
        return instance;
    }

    static ArrayList<Category> getCategorizedQuestions() {
        return categorizedQuestions;
    }

    private ArrayList<Category> shuffleQuestions(ArrayList<Category> lst){
        for(int i = 0; i< lst.size(); i++){
            Collections.shuffle(lst.get(i).getQuestions());
        }
        return lst;
    }
    private CategorizedQCreator() {
        Category animals = new Category("Animals");
        Category math = new Category("Math");
        Category random = new Category("Random");
        Category literature = new Category("Literature");

        //animals
        ArrayList<String> dingoWrong = new ArrayList<>(
                Arrays.asList("Turkish bull", "Stable owner", "Brazilian fox"));
        animals.addQuestion("What is a dingo?", "Australian canine", dingoWrong);

        ArrayList<String> bologneseWrong = new ArrayList<>(
                Arrays.asList("Sausage spaghetti", "Minced meet hotdog", "Apex predator"));
        animals.addQuestion("What is a bolognese dog?", "Italian gun dog", bologneseWrong);

        ArrayList<String> cuddleWrong = new ArrayList<>(
                Arrays.asList("Underwater zebra", "Weird squid", "Alien"));
        animals.addQuestion("What is a cuddlefish?", "Flashing colored mollusc", cuddleWrong);

        ArrayList<String> catWrong = new ArrayList<>(
                Arrays.asList("Vodka cocktail name", "Easy-going russian", "Mermaid of black sea"));
        animals.addQuestion("What is a blue russian?", "Just a cat", catWrong);

        ArrayList<String> shrimpWrong = new ArrayList<>(
                Arrays.asList("Badass fighter", "Italian gang leader", "Derin devlet"));
        animals.addQuestion("What is true about mantis shrimp?", "Wolds best puncher", shrimpWrong);
        //animal end

        //math
        ArrayList<String> addWrong = new ArrayList<>(
                Arrays.asList("0", "ln(e)", "5"));
        math.addQuestion("What is a 1 + 1?", "ln(1)+2", addWrong);

        ArrayList<String> minWrong = new ArrayList<>(
                Arrays.asList("1", "not defined", "sigmoid(0)"));
        math.addQuestion("What is a 1 - 1?", "tanh(0)", minWrong);

        ArrayList<String> numWrong = new ArrayList<>(
                Arrays.asList("3", "f(3), f(x)=x*x", "0"));
        math.addQuestion("What is equivalent to 3+(1/4)?", "2+(20/16)", numWrong);

        ArrayList<String> adWrong = new ArrayList<>(
                Arrays.asList("1", "2", "3"));
        math.addQuestion("2+2", "4", adWrong);

        ArrayList<String> sandWrong = new ArrayList<>(
                Arrays.asList("2 cops theorem", "2", "3"));
        math.addQuestion("Sandwich theorem...", "Include limits", sandWrong);
        //

        //random
        ArrayList<String> r1Wrong = new ArrayList<>(
                Arrays.asList("strip", "query", "scatter"));
        random.addQuestion("A special fuel", "jellyfish", r1Wrong);

        ArrayList<String> r2Wrong = new ArrayList<>(
                Arrays.asList("lucifer", "devil", "asmodeus"));
        random.addQuestion("Sin chief officer", "satan", r2Wrong);

        ArrayList<String> r3Wrong = new ArrayList<>(
                Arrays.asList("hahahaha", "asdasdas", "hjhjhjhjhh"));
        random.addQuestion("True random", "alekrgnvslhbti", r3Wrong);

        ArrayList<String> r4Wrong = new ArrayList<>(
                Arrays.asList("belly", "pocket", "duodenum"));
        random.addQuestion("There are scorpions in my", "head", r4Wrong);

        ArrayList<String> r5Wrong = new ArrayList<>(
                Arrays.asList("pineapple", "pineapple apple", "pen pineapple"));
        random.addQuestion("Apple", "pen", r5Wrong);

        //literature
        ArrayList<String> faustWrong = new ArrayList<>(
                Arrays.asList("Faust", "Gretchen", "Drunk guy"));
        literature.addQuestion("Men's wretchedness in soothe I so deplore,\n" +
                "Not even I would plague the sorry creatures more.", "Mephistopheles", faustWrong);

        ArrayList<String> orwWrong = new ArrayList<>(
                Arrays.asList("bliss", "smart", "a live style"));
        literature.addQuestion("War is peace. Freedom is slavery. Ignorance is", "strength", orwWrong);

        ArrayList<String> monteWrong = new ArrayList<>(
                Arrays.asList("Albert de Morcerf", "Edmund Dantes", "Fernand Mondego"));
        literature.addQuestion("I am hungry, feed me; I am bored, amuse me.", "Lucien Debray", monteWrong);

        ArrayList<String> oeWrong = new ArrayList<>(
                Arrays.asList("Seer", "Sphinx", "Antigone"));
        literature.addQuestion("O god-all come true, all burst to light! " +
                "O light-now let me look my last on you!", "Oedipus", oeWrong);

        ArrayList<String> araWrong = new ArrayList<>(
                Arrays.asList("Aladdin's Lamp", "Sinbad the Sailor", "The Fisherman and the Jinni"));
        literature.addQuestion("Open Sesame!", "Ali Baba and the Forty Thieves", araWrong);

        categorizedQuestions.add(animals);
        categorizedQuestions.add(math);
        categorizedQuestions.add(random);
        categorizedQuestions.add(literature);

        shuffleQuestions(categorizedQuestions);
    }
}

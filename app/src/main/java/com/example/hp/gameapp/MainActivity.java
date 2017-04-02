package com.example.hp.gameapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hp.gameapp.FlagMemory.MemoryGameActivity;
import com.example.hp.gameapp.FlagMemory.MemoryGameFragment;
import com.example.hp.gameapp.QuickQuiz.QuickQuizActivity;
import com.example.hp.gameapp.QuickQuiz.QuickQuizFragment;

public class MainActivity extends AppCompatActivity implements GameListFragment.GameListListener{

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
    }

    @Override
    public void itemClicked(long id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        switch ((int) id){
            case 0:
                if(fragmentContainer != null){
                    QuickQuizFragment quizFragment = new QuickQuizFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    quizFragment.setGameID(id);
                    quizFragment.setUser(user);
                    ft.replace(R.id.fragment_container, quizFragment);
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }else{
                    Intent intent = new Intent(this, QuickQuizActivity.class);
                    intent.putExtra("gameID", (int)id);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
                break;
            case 1:
                if(fragmentContainer != null){
                    MemoryGameFragment gameFragment = new MemoryGameFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    gameFragment.setGameID(id);
                    gameFragment.setUser(user);
                    ft.replace(R.id.fragment_container, gameFragment);
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }else{
                    Intent intent = new Intent(this, MemoryGameActivity.class);
                    intent.putExtra("gameID", (int)id);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
                break;
        }


    }
}

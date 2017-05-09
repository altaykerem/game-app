package com.example.hp.gameapp.FlagMemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.hp.gameapp.R;
import com.example.hp.gameapp.User;

public class MemoryGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game);

        Intent intent = getIntent();
        int gameID = (int) intent.getExtras().get("gameID");
        User user = intent.getParcelableExtra("user");

        MemoryGameFragment gf = (MemoryGameFragment) getFragmentManager().findFragmentById(R.id.game_frag);
        gf.setGameID(gameID);
        gf.setUser(user);
    }
}

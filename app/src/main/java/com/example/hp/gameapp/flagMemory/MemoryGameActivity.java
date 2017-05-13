package com.example.hp.gameapp.flagMemory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.hp.gameapp.R;
import com.example.hp.gameapp.Session;

public class MemoryGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game);

        Intent intent = getIntent();
        int gameID = (int) intent.getExtras().get("gameID");
        Session session = intent.getParcelableExtra("session");

        MemoryGameFragment gf = (MemoryGameFragment) getFragmentManager().findFragmentById(R.id.game_frag);
        gf.setGameID(gameID);
    }
}

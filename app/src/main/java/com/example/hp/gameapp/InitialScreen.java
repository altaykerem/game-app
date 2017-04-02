package com.example.hp.gameapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class InitialScreen extends AppCompatActivity {

    EditText nickNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        Intent coming = getIntent();

        nickNameField = (EditText) findViewById(R.id.enter_nickname);
        Button startButton = (Button) findViewById(R.id.start_game);
        TextView scoreView = (TextView) findViewById(R.id.high_score);
        User user = coming.getParcelableExtra("user");
        int score;
        if(user==null){
            score = 0;
        }else {
            score = user.getScore();
            makeToast(user.getName()+" score: "+ score);
        }

        scoreView.setText(getString(R.string.score, score));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = nickNameField.getText().toString();
                if(nick.equals(""))
                    makeToast("Please enter your nickname");
                else {
                    User player = new User(nick,0);
                    Intent i = new Intent(InitialScreen.this, MainActivity.class);
                    i.putExtra("user", player);
                    startActivity(i);
                }

            }
        });
    }

    private void makeToast(String note){
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show();
    }
}

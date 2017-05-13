package com.example.hp.gameapp;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hp.gameapp.flagMemory.MemoryGameActivity;
import com.example.hp.gameapp.flagMemory.MemoryGameFragment;
import com.example.hp.gameapp.quickQuiz.QuickQuizActivity;
import com.example.hp.gameapp.quickQuiz.QuickQuizFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements GameListFragment.GameListListener{

    private FirebaseAuth auth;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        session = intent.getParcelableExtra("session");
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void itemClicked(long id) {
        View fragmentContainer = findViewById(R.id.fragment_container);
        Fragment f;
        switch ((int) id) {
            case 0:
                f = new QuickQuizFragment();
                ((QuickQuizFragment)f).setGameID(id);
                respondToFragment(fragmentContainer != null,f,QuickQuizActivity.class, (int) id);
                break;
            case 1:
                f = new MemoryGameFragment();
                ((MemoryGameFragment)f).setGameID(id);
                respondToFragment(fragmentContainer != null,f,MemoryGameActivity.class, (int) id);
                break;
            case 2:
                final DialogFragment newFragment = new LoadFragment();
                newFragment.show(getFragmentManager(),"loader");
                FirebaseAuth.AuthStateListener sl = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // session auth state is changed - session is null
                            // launch login activity
                            startActivity(new Intent(MainActivity.this, InitialScreen.class));
                            finish();
                        }else{
                            makeToast("Check internet");
                            newFragment.dismiss();
                        }
                    }
                };
                auth.signOut();
                auth.addAuthStateListener(sl);
                break;
        }
    }

    private void respondToFragment(boolean check, Fragment fragment, Class activity, int id){
        if (check) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            Intent intent = new Intent(this, activity);
            intent.putExtra("gameID",id);
            intent.putExtra("session", session);
            startActivity(intent);
        }
    }
    private void makeToast(String note){
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show();
    }

}

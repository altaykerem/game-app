package com.example.hp.gameapp;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hp.gameapp.appFunctions.FriendsFragment;
import com.example.hp.gameapp.appFunctions.HighScoreFragment;
import com.example.hp.gameapp.appFunctions.Profile;
import com.example.hp.gameapp.flagMemory.MemoryGameFragment;
import com.example.hp.gameapp.quickQuiz.QuickQuizFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements GameListFragment.GameListListener{

    private FirebaseAuth auth;
    Session session;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition = 0;

    private String[] drawerEntries;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        session = intent.getParcelableExtra("session");
        auth = FirebaseAuth.getInstance();

        drawerEntries = getResources().getStringArray(R.array.games);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawerLayout != null){
            drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, drawerEntries));

            drawerList.setOnItemClickListener(new DrawerItemClickListener());

            if (savedInstanceState != null) {
                currentPosition = savedInstanceState.getInt("position");
                setActionBarTitle(currentPosition);
            } else {
                selectItem(0);
            }

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    R.string.open_drawer, R.string.close_drawer) {
                //Called when a drawer has settled in a completely closed state
                @Override
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu();
                }

                //Called when a drawer has settled in a completely open state.
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();
                }
            };
            drawerLayout.addDrawerListener(drawerToggle);

            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            } else Log.e("action bar", "action bar null");
        }
    }

    @Override
    public void itemClicked(long id) {
        Fragment f;
        switch ((int) id) {
            case 0:
                f = new QuickQuizFragment();
                ((QuickQuizFragment)f).setGameID(id);
                respondToFragment(R.id.fragment_container,f);
                break;
            case 1:
                f = new MemoryGameFragment();
                ((MemoryGameFragment)f).setGameID(id);
                respondToFragment(R.id.fragment_container,f);
                break;
            case 2:
                respondToFragment(R.id.fragment_container, new Profile());
                break;
            case 3:
                respondToFragment(R.id.fragment_container, new FriendsFragment());
                break;
            case 4:
                respondToFragment(R.id.fragment_container, new HighScoreFragment());
                break;
            case 5:
                final DialogFragment newFragment = new LoadFragment();
                newFragment.show(getFragmentManager(), "loader");
                logOut();
                newFragment.dismiss();
                break;
        }
    }

    private void respondToFragment(int frame, Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        currentPosition = position;
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new QuickQuizFragment();
                respondToFragment(R.id.content_frame, fragment);
                break;
            case 1:
                fragment = new MemoryGameFragment();
                respondToFragment(R.id.content_frame, fragment);
                break;
            case 2:
                respondToFragment(R.id.content_frame, new Profile());
                break;
            case 3:
                respondToFragment(R.id.content_frame, new FriendsFragment());
                break;
            case 4:
                respondToFragment(R.id.content_frame, new HighScoreFragment());
                break;
            case 5:
                final DialogFragment newFragment = new LoadFragment();
                newFragment.show(getFragmentManager(), "loader");
                logOut();
                newFragment.dismiss();
                break;
        }

        setActionBarTitle(position);
        drawerLayout.closeDrawer(drawerList);
    }

    private void setActionBarTitle(int position) {
        String entry;
        if (position == 0) {
            entry = getResources().getString(R.string.app_name);
        } else {
            entry = drawerEntries[position];
        }
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(entry);
        } else Log.e("action bar", "action bar null");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(drawerToggle != null)
            drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(drawerToggle != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut(){
        FirebaseAuth.AuthStateListener sl = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // session auth state is changed - session is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, InitialScreen.class));
                    finish();
                }
            }
        };
        auth.signOut();
        auth.addAuthStateListener(sl);
    }
}

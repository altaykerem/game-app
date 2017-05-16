package com.example.hp.gameapp.flagMemory;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.gameapp.InitialScreen;
import com.example.hp.gameapp.R;
import com.example.hp.gameapp.Session;
import com.example.hp.gameapp.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MemoryGameFragment extends Fragment {

    private long gameID;
    private boolean timePassed = false;
    private Session session = new Session();

    private LinearLayout heartContainer;
    private LinearLayout targetContainer;
    private GridView board;
    private TextView timeView;
    private ArrayList<String> flagPaths;
    private ArrayList<String> toGridLayout;
    TextView scoreView;

    Timer questionTimer = new Timer();
    int openIndex = -1;
    int secondFlagIndex = -1;
    int openTime = -5;
    int secondOpenTime = -5;
    private ArrayList<Integer> flagConditions; // 0-open, 1-closed, 2-correct guess

    @Override
    public void onStart() {
        super.onStart();
        if(flagConditions == null){
            flagPaths = getFlags(session.getDifficulty());
            toGridLayout = new ArrayList<>();
            toGridLayout.addAll(flagPaths.subList(0,session.getDifficulty()));
            toGridLayout.addAll(flagPaths);
            Collections.shuffle(toGridLayout);
            flagConditions = new ArrayList<>();
            for(int i=0;i<toGridLayout.size();i++) flagConditions.add(0);
        }

        View view = getView();
        if (view != null) {
            heartContainer = (LinearLayout) view.findViewById(R.id.heart_container);
            targetContainer = (LinearLayout) view.findViewById(R.id.target_flag_container);
            board = (GridView) view.findViewById(R.id.flags);
            board.setNumColumns(session.getDifficulty());
            timeView = (TextView) view.findViewById(R.id.timer);
            scoreView = (TextView) view.findViewById(R.id.score);
            scoreView.setText(getString(R.string.score, session.getScore()));
        }

        createGame();
        runTimer();
    }

    private void play(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int opened = checkFlags();
                if(opened == 1) {
                    if (questionTimer.getSeconds() - openTime > 3) {
                        setTimeUp();
                        if (session.getLives() < 1) {
                            finishGame();
                        }
                        makeToast("Please select under 3 seconds");
                    }
                }else if(opened == 2) {
                    stopInteraction();
                    if(getID(openIndex) != getID(secondFlagIndex)) {
                        if(questionTimer.getSeconds() - secondOpenTime == 3){
                            setWrongAnswer();
                            if(session.getLives()<1){
                                finishGame();
                            }
                        }
                    }else{
                        flagConditions.set(openIndex, 2);
                        flagConditions.set(secondFlagIndex,2);
                        openTime = -5;
                        session.setScore(session.getScore()+100);
                        scoreView.setText(getString(R.string.score, session.getScore()));
                    }
                } else startInteraction();
                if(checkGameFinished()) {
                    session.setDifficulty(session.getDifficulty()+1);
                    if(session.getDifficulty()>6) finishGame();
                    else{
                        restartGame();
                    }
                }
                handler.post(this);
            }
        });
    }

    private int checkFlags(){
        int opened = 0;
        for(int i=0;i<board.getChildCount();i++) {
            FlagBox flagBox = (FlagBox) board.getChildAt(i);

            if(flagBox.isOpen()&&flagConditions.get(i)!=2) {
                opened++;
                flagConditions.set(i,0);
                if(opened == 1) {
                    openIndex = i;
                    if(openTime<0) openTime = questionTimer.getSeconds();
                }else if(opened ==2){
                    secondFlagIndex = i;
                    if(secondOpenTime<0) secondOpenTime = questionTimer.getSeconds();
                }
            }
        }
        return opened;
    }

    private void stopInteraction(){
        for(int i=0;i<board.getChildCount();i++) {
            FlagBox flagBox = (FlagBox) board.getChildAt(i);
            flagBox.stopListener();
        }
    }

    private boolean checkGameFinished(){
        for (int state: flagConditions) {
            if(state != 2) return false;
        }
        return true;
    }

    private void finishGame(){
        Intent intent = new Intent(getActivity(), InitialScreen.class);
        startActivity(intent);
    }

    private void startInteraction(){
        for(int i=0;i<board.getChildCount();i++) {
            FlagBox flagBox = (FlagBox) board.getChildAt(i);
            flagBox.setClicker();
        }
    }

    private void closeFromIndex(int n){
        ((FlagBox)board.getChildAt(n)).closeFlag();
    }

    private int getID(int n){
        return ((FlagBox)board.getChildAt(n)).getFlagID();
    }
    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = questionTimer.getSeconds()/3600;
                int minutes = (questionTimer.getSeconds()%3600)/60;
                int secs = questionTimer.getSeconds()%60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                if (questionTimer.isRunning()) {
                    if(questionTimer.getSeconds()==5){
                        timePassed = true;
                        changeBoardVisibility();
                        play();
                    }
                    questionTimer.setSeconds(questionTimer.getSeconds()+1);
                }
                timeView.setText(time);
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void createGame(){
        int hearts = session.getLives();

        // add hearts
        for (int i=0;i<hearts;i++){
            ImageView heartImage = new ImageView(getActivity());
            heartImage.setImageResource(R.drawable.heart);
            heartImage.setPadding(1,0,1,0);
            heartContainer.addView(heartImage);
        }

        // add targets
        for(int i=0;i<session.getDifficulty();i++){
            ImageView flagImage = new ImageView(getActivity());
            AssetManager manager = getActivity().getAssets();
            InputStream streamImage = null;
            try {
                streamImage = manager.open(flagPaths.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap temp = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(streamImage),150,120,false);
            flagImage.setImageBitmap(temp);
            flagImage.setPadding(1,0,1,0);
            targetContainer.addView(flagImage);
        }

        // add flags
        board.setAdapter(new MemoryGridAdapter(getActivity(),toGridLayout, flagConditions));
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionTimer.setSeconds(0);
        if (savedInstanceState != null) {
            timePassed = savedInstanceState.getBoolean("time_passed");
            gameID = savedInstanceState.getLong("gameID");
            questionTimer.setSeconds(savedInstanceState.getInt("seconds"));
            flagPaths = savedInstanceState.getStringArrayList("flag_paths");
            toGridLayout = savedInstanceState.getStringArrayList("grid_paths");
            flagConditions = savedInstanceState.getIntegerArrayList("flag_conditions");
        }

        new GetFlagsAsync(null).doInBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        questionTimer.setRunning(true);
        return inflater.inflate(R.layout.fragment_memory_game, container, false);
    }

    private ArrayList<String> getFlags(int n){
        AssetManager manager = getActivity().getAssets();
        ArrayList<String> flagPaths = new ArrayList<>();
        ArrayList<String> allFlags = new ArrayList<>();
        try {
            String[] continents = manager.list("");
            for(int i=0;i<6;i++){
                for(String flag : manager.list(continents[i])){
                    allFlags.add(continents[i]+"/"+flag);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(allFlags);
        for (int i=0;i<n*n-n;i++){
            flagPaths.add(allFlags.get(i));
        }
        return flagPaths;
    }

    private void changeBoardVisibility(){
        for(int i=0;i<board.getChildCount();i++){
            ((FlagBox) board.getChildAt(i)).closeFlag();
        }
        for(int i=0;i<flagConditions.size();i++)
            flagConditions.set(i,1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        questionTimer.setRunning(false);
        outState.putBoolean("time_passed", timePassed);
        outState.putLong("gameID", gameID);
        outState.putInt("seconds", questionTimer.getSeconds());
        outState.putStringArrayList("flag_paths", flagPaths);
        outState.putStringArrayList("grid_paths", toGridLayout);
        outState.putIntegerArrayList("flag_conditions", flagConditions);
        super.onSaveInstanceState(outState);
    }

    /**private void clearGame(){
        heartContainer.removeAllViews();
        targetContainer.removeAllViews();
        flagPaths = new ArrayList<>();
        toGridLayout = new ArrayList<>();
        flagPaths = getFlags(user.getDifficulty());
        toGridLayout = new ArrayList<>();
        toGridLayout.addAll(flagPaths.subList(0,user.getDifficulty()));
        toGridLayout.addAll(flagPaths);
        Collections.shuffle(toGridLayout);
        flagConditions = new ArrayList<>();
        for(int i=0;i<toGridLayout.size();i++) flagConditions.add(0);
    }*/

    private void restartGame(){

    }

    private void setWrongAnswer(){
        session.setLives(session.getLives()-1);
        flagConditions.set(openIndex, 1);
        closeFromIndex(openIndex);
        flagConditions.set(secondFlagIndex,1);
        closeFromIndex(secondFlagIndex);
        openTime = -5;
        secondOpenTime = -5;
        secondFlagIndex = -5;
        heartContainer.removeViewAt(session.getLives());
    }

    private void setTimeUp(){
        session.setLives(session.getLives()-1);
        flagConditions.set(openIndex, 1);
        closeFromIndex(openIndex);
        openTime = -5;
        secondFlagIndex = -5;
        secondOpenTime = -5;
        heartContainer.removeViewAt(session.getLives());
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    private void makeToast(String note){
        Toast.makeText(getActivity(), note, Toast.LENGTH_SHORT).show();
    }
}

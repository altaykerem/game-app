package com.example.hp.gameapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HighScoreFragment extends Fragment {
    private static final String TAG = "Hig scores";

    ListView quizScoreView;
    ListView flagScoreView;
    ArrayList<String> quizHighScores = new ArrayList<>();
    ArrayList<String> flagHighScores = new ArrayList<>();

    public HighScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            quizScoreView = (ListView) view.findViewById(R.id.quiz_list);
            flagScoreView = (ListView) view.findViewById(R.id.flag_list);
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        Query flagQuery = mDatabase.orderByChild("flagScore").limitToLast(10);
        Query quizQuery = mDatabase.orderByChild("quizScore").limitToLast(10);

        ArrayAdapter<String> flagAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, flagHighScores);
        flagScoreView.setAdapter(flagAdapter);
        flagQuery.addValueEventListener(getScores(flagAdapter));

        ArrayAdapter<String> quizAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, quizHighScores);
        quizScoreView.setAdapter(quizAdapter);
        quizQuery.addValueEventListener(getScores(quizAdapter));
    }

    private ValueEventListener getScores(final ArrayAdapter<String> users){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    users.add((String) postSnapshot.child("username").getValue());
                    users.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, ":onCancelled", databaseError.toException());
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_score, container, false);
    }
}

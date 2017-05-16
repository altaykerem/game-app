package com.example.hp.gameapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {
    private static final String TAG = "Edit Profile";

    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference mDatabase;

    EditText nameView;
    EditText locView;
    Button editView;
    int flagScore;
    int quizScore;

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            nameView = (EditText) view.findViewById(R.id.edit_username);
            locView = (EditText) view.findViewById(R.id.edit_location);
            editView = (Button) view.findViewById(R.id.edit_button);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = new User(nameView.getText().toString(), user.getEmail(), locView.getText().toString());
                u.setFlagScore(flagScore);
                u.setQuizScore(quizScore);
                mDatabase.child("users").child(user.getUid()).setValue(u);
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot userSnap = dataSnapshot.child("users").child(user.getUid());
                nameView.setText((String) userSnap.child("username").getValue());
                locView.setText((String) userSnap.child("location").getValue());
                flagScore = (int)(long) userSnap.child("flagScore").getValue();
                quizScore = (int)(long) userSnap.child("quizScore").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "profile:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            nameView.setText(savedInstanceState.getString("username"));
            locView.setText(savedInstanceState.getString("location"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("username", nameView.getText().toString());
        outState.putString("location", locView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}

package com.example.hp.gameapp.appFunctions;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.gameapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRequests extends Fragment {
    private static final String TAG = "Request Fragment";
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mDatabase;

    ListView reqList;
    ArrayList<String> requests = new ArrayList<>();
    ArrayAdapter<String> reqAdapter;
    ArrayList<String> userNames = new ArrayList<>();

    public FriendRequests() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            reqList = (ListView) view.findViewById(R.id.request_list);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        reqAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, requests);
        reqList.setAdapter(reqAdapter);

        mDatabase.addValueEventListener(getReqs(reqAdapter));

        reqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"Friend request accepted", Toast.LENGTH_LONG).show();
                mDatabase.child(user.getUid()).child("friends")
                        .child(requests.get(position)).child("username")
                        .setValue(userNames.get(position));
            }
        });

    }


    private ValueEventListener getReqs(final ArrayAdapter<String> users){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.child(user.getUid()).child("requests").getChildren()) {
                    users.add(postSnapshot.getKey());
                    userNames.add((String) dataSnapshot.child(postSnapshot.getKey()).child("username").getValue());
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
        return inflater.inflate(R.layout.fragment_friend_requests, container, false);
    }

}

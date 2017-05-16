package com.example.hp.gameapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {
    private static final String TAG = "Friends Fragment";
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mDatabase;

    SearchView sv;
    ListView userList;
    ListView friendList;
    ArrayList<String> users = new ArrayList<>();
    ArrayList<String> friends = new ArrayList<>();
    ArrayAdapter<String> userAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            userList = (ListView) view.findViewById(R.id.query_list);
            friendList = (ListView) view.findViewById(R.id.friend_list);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        ArrayAdapter<String> friendAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, friends);
        friendList.setAdapter(friendAdapter);
        mDatabase.child("friends").addValueEventListener(findUser(friendAdapter));

        userAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, users);
        userList.setAdapter(userAdapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    private void addFriend(String username){
        friends.add(username);
        mDatabase.child("users").child(user.getUid()).child("friends").setValue(friends);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menuSearch);
        sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query userQuery = mDatabase.orderByChild("username").equalTo(query);
                users.clear();
                userQuery.addValueEventListener(findUser(userAdapter));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }

    private ValueEventListener findUser(final ArrayAdapter<String> users){
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.search_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


}
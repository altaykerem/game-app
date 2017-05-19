package com.example.hp.gameapp.flagMemory;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

class GetFlagsAsync extends AsyncTask<String, String, Bitmap[]>{
    private final static String TAG = "AsyncTaskLoadImage";

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private int difficulty;
    private ArrayList<Bitmap> flags;
    private ArrayList<String> flagsPaths = new ArrayList<>();
    private ArrayList<String> selectedFlagPaths = new ArrayList<>();

    GetFlagsAsync(ArrayList<Bitmap> flagContainer, int difficulty){
        flags = flagContainer;
        this.difficulty = difficulty;
    }

    @Override
    protected Bitmap[] doInBackground(String... params) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("flag_paths");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot flagSnap: snapshot.getChildren()){
                    flagsPaths.add((String) flagSnap.getValue());
                }

                Collections.shuffle(flagsPaths);
                for (int i=0;i<difficulty*difficulty-difficulty;i++){
                    selectedFlagPaths.add(flagsPaths.get(i));
                }
                Log.v(TAG, "Flags retrieved");

                for (int i=0;i<selectedFlagPaths.size();i++){
                    StorageReference imageRef = storageRef.child(selectedFlagPaths.get(i));
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.v(TAG,"Image retrieve success");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "flags not received", databaseError.toException());
            }

        });

        return new Bitmap[0];
    }

    @Override
    protected void onPostExecute(Bitmap[] bitmaps) {
        Collections.addAll(flags, bitmaps);
    }
}

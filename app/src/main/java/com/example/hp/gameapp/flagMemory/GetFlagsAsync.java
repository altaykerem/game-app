package com.example.hp.gameapp.flagMemory;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class GetFlagsAsync extends AsyncTask<String, String, Bitmap[]>{
    private final static String TAG = "AsyncTaskLoadImage";

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private ArrayList<Bitmap> flags;

    public GetFlagsAsync(ArrayList<Bitmap> flagContainer){
        flags = flagContainer;
    }

    @Override
    protected Bitmap[] doInBackground(String... params) {
        storageRef.getStream();
        return new Bitmap[0];
    }

    @Override
    protected void onPostExecute(Bitmap[] bitmaps) {
        Collections.addAll(flags, bitmaps);
    }
}

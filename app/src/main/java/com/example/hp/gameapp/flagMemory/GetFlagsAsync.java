package com.example.hp.gameapp.flagMemory;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

class GetFlagsAsync extends AsyncTask<String, String, Bitmap[]>{
    private final static String TAG = "AsyncTaskLoadImage";

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference().child("Africa/Africa-Lesotho.png");

    private ArrayList<Bitmap> flags;

    GetFlagsAsync(ArrayList<Bitmap> flagContainer){
        flags = flagContainer;
    }

    @Override
    protected Bitmap[] doInBackground(String... params) {
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.v(TAG,"");
            }
        });
        return new Bitmap[0];
    }

    @Override
    protected void onPostExecute(Bitmap[] bitmaps) {
        Collections.addAll(flags, bitmaps);
    }
}

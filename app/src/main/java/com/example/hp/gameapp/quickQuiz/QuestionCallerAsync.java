package com.example.hp.gameapp.quickQuiz;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hp.gameapp.LoadFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

class QuestionCallerAsync extends AsyncTask<String, String, ArrayList<Category>>{
    private static final String TAG = "Async question call";
    private Activity activity;
    private ArrayList<Category> data = new ArrayList<>();

    QuestionCallerAsync(Activity activity, ArrayList<Category> data){
        this.activity = activity;
        this.data = data;
    }

    @Override
    protected ArrayList<Category> doInBackground(String... params) {
        final DialogFragment newFragment = new LoadFragment();
        newFragment.show(activity.getFragmentManager(), "loader");

        data = new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot categories = snapshot.child("category");
                DataSnapshot questionSnaps = snapshot.child("question");

                for(DataSnapshot categorySnap: categories.getChildren()){
                    Category cat = new Category((String) categorySnap.getValue());
                    data.add(cat);
                }

                for(DataSnapshot questionSnap: questionSnaps.getChildren()){
                    HashMap qmap = (HashMap) questionSnap.getValue();
                    ArrayList opts = (ArrayList) qmap.get("options");

                    data.get((int)((long) qmap.get("category_id")) - 1)
                            .addQuestion((String) qmap.get("question"), (String) qmap.get("answer"), opts);
                }

                newFragment.dismiss();
                Log.v(TAG, "done");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }


        });
        return data;
    }
}

/*Code to build data base
        mDatabase = FirebaseDatabase.getInstance().getReference().child("question");
        int i = 1;
        int cat = 1;
        for (Category c:categorizedQuestions) {
            for (Question q:c.getQuestions()) {
                mDatabase.child("c"+Integer.toString(cat)+"q"+Integer.toString(i)).child("question").setValue(q.getQuestion());
                mDatabase.child("c"+Integer.toString(cat)+"q"+Integer.toString(i)).child("answer").setValue(q.getCorrectAnswer());
                mDatabase.child("c"+Integer.toString(cat)+"q"+Integer.toString(i)).child("category_id").setValue(cat);
                mDatabase.child("c"+Integer.toString(cat)+"q"+Integer.toString(i)).child("options").setValue(q.getMultipleChoice());
                i++;
            }
            i=1;
            cat++;
        }
 */

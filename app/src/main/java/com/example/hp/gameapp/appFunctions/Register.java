package com.example.hp.gameapp.appFunctions;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.gameapp.MainActivity;
import com.example.hp.gameapp.R;
import com.example.hp.gameapp.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private static final int PASSWORD_LENGTH = 6;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    EditText mailView;
    EditText passView;
    EditText nameView;
    EditText locView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameView = (EditText) findViewById(R.id.sign_username);
        mailView = (EditText) findViewById(R.id.sign_mail);
        passView = (EditText) findViewById(R.id.sign_password);
        locView = (EditText) findViewById(R.id.sign_location);

        if(savedInstanceState != null){
            nameView.setText(savedInstanceState.getString("username"));
            mailView.setText(savedInstanceState.getString("mail"));
            passView.setText(savedInstanceState.getString("password"));
            locView.setText(savedInstanceState.getString("location"));
        }

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void onSignUp(View view){
        String mail = mailView.getText().toString();
        if (!isEmailValid(mail))
            Toast.makeText(Register.this, "Provide a valid email please", Toast.LENGTH_LONG).show();
        else if(!checkPassword())
            Toast.makeText(Register.this, "Password must be valid", Toast.LENGTH_LONG).show();
        else{
            auth.createUserWithEmailAndPassword(mail, passView.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                                Log.e("Register", "Failed Registration", task.getException());
                            } else {
                                writeNewUser(auth.getCurrentUser(), nameView.getText().toString(), locView.getText().toString());
                                Intent i = new Intent(Register.this, MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
        }
    }

    private void writeNewUser(FirebaseUser fuser, String name, String location) {
        if(fuser != null){
            User user = new User(name, fuser.getEmail(), location);
            mDatabase.child("users").child(fuser.getUid()).setValue(user);
        } else
            makeToast("User null");
    }

    private boolean isEmailValid(String mail){
        CharSequence cs = mail.subSequence(0,mail.length());
        return Patterns.EMAIL_ADDRESS.matcher(cs).matches();
    }
    private boolean checkPassword(){
        return passView.getText().length()>PASSWORD_LENGTH;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("username", nameView.getText().toString());
        outState.putString("mail",mailView.getText().toString());
        outState.putString("password",passView.getText().toString());
        outState.putString("location", locView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void makeToast(String note){
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show();
    }
}
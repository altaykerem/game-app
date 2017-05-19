package com.example.hp.gameapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.example.hp.gameapp.appFunctions.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

public class InitialScreen extends AppCompatActivity {

    EditText mailField, passwordField;
    private AVLoadingIndicatorView loader;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        mailField = (EditText) findViewById(R.id.enter_mail);
        passwordField = (EditText) findViewById(R.id.enter_password);
        Button registerButton = (Button) findViewById(R.id.register_button);

        loader = (AVLoadingIndicatorView) findViewById(R.id.login_loader);

        auth = FirebaseAuth.getInstance();

        //already logged in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(InitialScreen.this, MainActivity.class));
            finish();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InitialScreen.this, Register.class);
                startActivity(i);
            }
        });
    }

    private void makeToast(String note){
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show();
    }

    public void onLogin(View view) {
        String email = mailField.getText().toString();
        final String password = passwordField.getText().toString();

        if (!isEmailValid(email)) {
            makeToast("Enter mail");
            return;
        }

        if (!checkPassword()) {
            makeToast("Enter valid password");
            return;
        }
        loader.show();

        //authenticate session
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(InitialScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the session. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in session can be handled in the listener.
                        loader.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            makeToast("Login failed");
                            passwordField.setText("");
                        } else {
                            Intent intent = new Intent(InitialScreen.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private boolean isEmailValid(String mail){
        CharSequence cs = mail.subSequence(0,mail.length());
        return Patterns.EMAIL_ADDRESS.matcher(cs).matches();
    }
    private boolean checkPassword(){
        return passwordField.getText().length()>6;
    }
}

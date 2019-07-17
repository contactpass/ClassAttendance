package com.example.petong.classattendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginLecturerActivity extends AppCompatActivity {

    private TextView register;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button login;
    private FirebaseAuth mAuth;
    String TAG = "emailpassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_lecturer);
        initInstance();



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
/*
        if (currentUser != null){
            Log.d(TAG, "user is login");
            String userID = currentUser.getUid();
            Intent intent = new Intent(getApplicationContext(), LecturerActivity.class);
            intent.putExtra("Lecturer", userID);
            startActivity(intent);
        } else {
            Log.d(TAG, "user is not login");
        }*/
        //updateUI(currentUser);
    }

    private void initInstance(){
        mAuth = FirebaseAuth.getInstance();
        mEmailField = findViewById(R.id.fieldUsername);
        mPasswordField = findViewById(R.id.fieldPassword);
        login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
        //loadingProgressBar = findViewById(R.id.loading);
        register = findViewById(R.id.textRegister);
        SpannableString content = new SpannableString(("Register"));
        content.setSpan(new UnderlineSpan(),0, content.length(), 0);
        register.setText(content);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null){
                                String userID = user.getUid();
                                Intent intent = new Intent(getApplicationContext(), LecturerActivity.class);
                                intent.putExtra("Lecturer", userID);
                                startActivity(intent);
                            }

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginLecturerActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //mStatusTextView.setText(R.string.auth_failed);
                        }
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void gotoRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

}

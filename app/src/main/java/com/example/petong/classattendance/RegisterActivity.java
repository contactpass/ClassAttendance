package com.example.petong.classattendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText firstName;
    private EditText lastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize

        initInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Log.d("emailpassword", "user is login");
        } else {
            Log.d("emailpassword", "user is not login");
        }
        //updateUI(currentUser);
    }

    private void initInstance() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        firstName = findViewById(R.id.fieldFirstname);
        lastName = findViewById(R.id.fieldLastname);
        findViewById(R.id.registerButton).setOnClickListener(this);
        findViewById(R.id.cancelButton).setOnClickListener(this);
        //findViewById(R.id.logoutButton).setOnClickListener(this);         //Log out
    }


    private void createAccount(String email, String password) {
        Log.d("emailpassword", "createAccount:" + email);

        if (!validateForm()) {
            return;
        }
        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createAccount", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userID = user.getUid();
                                String firstname = firstName.getText().toString();
                                String lastname = lastName.getText().toString();
                                Lecturer lecturer = new Lecturer(firstname, lastname);
                                Log.d("createAccount", userID);
                                Log.d("createAccount", lecturer.getFirstname() + " " + lecturer.getLastname());
                                db.collection("Lecturer").document(userID).set(lecturer);
                                Log.d("createAccount", "Add DB Success");

                                gotoLogin();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("emailpassword", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            return;
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }




    /*
    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verifyEmailButton).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verifyEmailButton).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("emailpassword", "sendEmailVerification", task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }*/

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

        String firstname = firstName.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            firstName.setError("Required.");
            valid = false;
        } else {
            firstName.setError(null);
        }

        String lastname = lastName.getText().toString();
        if (TextUtils.isEmpty(lastname)) {
            lastName.setError("Required.");
        } else {
            lastName.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        else if (i == R.id.cancelButton) {
            //signIn(mEmailField.getText().toString() + "@cmu.ac.th", mPasswordField.getText().toString());
        }/* else if (i == R.id.logoutButton) {
            signOut();
        } else if (i == R.id.verifyEmailButton) {
            sendEmailVerification();
        }*/
    }
    private void gotoLogin() {
        Intent intent = new Intent(this, LoginLecturerActivity.class);
        startActivity(intent);
    }

}

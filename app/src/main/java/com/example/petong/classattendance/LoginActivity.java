package com.example.petong.classattendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private ProgressBar loadingProgressBar;
    private TextView lecturer;
    private User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstance();
        final LoginUser userLogin = new LoginUser();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin.setUserName(username.getText().toString() + "@cmu.ac.th");
                userLogin.setPassword(password.getText().toString());

                try{
                    JSONObject data = new JSONObject();
                    data.put("UserName",userLogin.getUserName());
                    data.put("Password",userLogin.getPassword());
                    HashMap<String, JSONObject> jsonParams = new HashMap<>();
                    jsonParams.put("params", data);

                    callAPI(jsonParams);
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Log.e("CallApi", "unexpected JSON exception", e);
                }
            }
        });

    }

    private void initInstance(){
        username = findViewById(R.id.fieldUsername);
        password = findViewById(R.id.fieldPassword);
        login = findViewById(R.id.buttonLogin);
        loadingProgressBar = findViewById(R.id.loading);
        lecturer = findViewById(R.id.textLecturer);
        lecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginLecturer();
            }
        });

    }

    public void gotoLoginLecturer() {
        Intent intent = new Intent(this, LoginLecturerActivity.class);
        startActivity(intent);
    }

    private void callAPI(HashMap<String, JSONObject> jsonParams){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        Call<User> call = HttpManager.getInstance().getService().getUser(body);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()){
                    Log.d ("CallApi", "Respond Api success");
                    userData = response.body();
                    setUserData(userData);
                    if (userData.getStatus().equals("valid")){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("User", userData);
                        startActivity(intent);
                    } else {
                        Log.d("CallApi", "email or password is incorrect");
                        Toast.makeText(LoginActivity.this, "email or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e ("CallApi", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d ("CallApi", "Fail");
            }
        });
    }

    private void setUserData(User userData) {
        this.userData = userData;
        if (userData != null){
            Log.d("CallApi", "DAO set success: " + userData.getStatus());
            Log.d("CallApi", "LoginActivity: "+ userData.getData().getStudentCode());
        }
    }
}


package com.example.petong.classattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
                Toast.makeText(LoginActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                userLogin.setUserName(username.getText().toString());
                userLogin.setPassword(password.getText().toString());

                try{
                    //JSONObject params = new JSONObject();
                    JSONObject data = new JSONObject();
                    data.put("UserName",userLogin.getUserName());
                    data.put("Password",userLogin.getPassword());
                    //params.put("params", data);
                    HashMap<String, JSONObject> jsonParams = new HashMap<>();
                    jsonParams.put("params", data);

                    callAPI(jsonParams);
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Log.e("CallApi", "unexpected JSON exception", e);
                }


                //HashMap<String, Object> jsonParams = new HashMap<>();
                //jsonParams.put("params", userLogin);


            }
        });

    }

    private void initInstance(){
        username = findViewById(R.id.fieldUsername);
        password = findViewById(R.id.fieldPassword);
        login = findViewById(R.id.buttonLogin);
        loadingProgressBar = findViewById(R.id.loading);

    }

    private void callAPI(HashMap<String, JSONObject> jsonParams){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        Log.d ("CallApi",body.toString());

        Call<User> call = HttpManager.getInstance().getService().getUser(body);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Log.d ("CallApi", "Respond Api success");
                    userData = response.body();
                    setUserData(userData);

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
            Log.d("CallApi", userData.getData().toString());
        }
    }
}


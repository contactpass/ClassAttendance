package com.example.petong.classattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LecturerActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView lecName;
    private Button addCoursebutton;
    private EditText editTextCourseno;
    private EditText editTextTitle;
    private EditText editTextDay;
    private EditText editTextsTime;
    private EditText editTexteTime;
    private EditText editTextSection;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);
        initInstance();

    }

    private void initInstance(){
        lecName = findViewById(R.id.fullnameView);
        addCoursebutton = findViewById(R.id.addCourseButton);
        addCoursebutton.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addCourseButton) {
            //Log.d("DialogA", "Hellooo");
            openDialog();

        }/*
        else if (i == R.id.cancelButton) {
            //signIn(mEmailField.getText().toString() + "@cmu.ac.th", mPasswordField.getText().toString());
        } else if (i == R.id.logoutButton) {
            signOut();
        } else if (i == R.id.verifyEmailButton) {
            sendEmailVerification();
        }*/
    }
    private void gotoLogin() {
        Intent intent = new Intent(this, LoginLecturerActivity.class);
        startActivity(intent);
    }
    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = (View) inflater.inflate(R.layout.dialog_add_course, null);
        editTextCourseno = view.findViewById(R.id.edit_couseno);
        editTextTitle = view.findViewById(R.id.edit_title);
        editTextDay = view.findViewById(R.id.edit_day);
        editTextsTime = view.findViewById(R.id.edit_starttime);
        editTexteTime = view.findViewById(R.id.edit_endtime);
        editTextSection = view.findViewById(R.id.edit_section);
        builder.setView(view)
                .setTitle("ADD COURSE")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String courseno = editTextCourseno.getText().toString();
                        String title = editTextTitle.getText().toString();
                        String day = editTextDay.getText().toString();
                        String time = editTextsTime.getText().toString() + " - " + editTexteTime.getText().toString();
                        String section = editTextSection.getText().toString();
                        Map<String, Object> course = new HashMap<>();
                        course.put("title", title);
                        course.put("day", day);
                        course.put("time", time);
                        course.put("status", true);
                        db.collection("Course").document(courseno).set(course);
                        //Map<String, Object> sec = new HashMap<>();
                        //sec.put(courseno, section);
                        //Map<String, Object> courseSec = new HashMap<>();
                        //courseSec.put("courseID", sec);
                        //db.collection("Lecturer").document(.........).set(sec);

                    }
                });
        builder.show();
    }


}

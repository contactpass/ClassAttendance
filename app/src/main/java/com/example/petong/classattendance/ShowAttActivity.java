package com.example.petong.classattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowAttActivity extends AppCompatActivity {

    private String courseno;
    private String stuID;
    private TextView number;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_att);

        Intent intent = getIntent();
        courseno = intent.getStringExtra("CourseID");
        stuID = intent.getStringExtra("StudentID");
        number = findViewById(R.id.numbetAttText);
        db = FirebaseFirestore.getInstance();




    }
}

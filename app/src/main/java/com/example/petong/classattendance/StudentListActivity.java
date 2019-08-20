package com.example.petong.classattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class StudentListActivity extends AppCompatActivity {
    private TextView mTextCourseno;
    private TextView mTextTitle;
    private TextView mTextSection;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private StudentListAdapter adapter;
    private String courseNo;
    private String title;
    private String section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Bundle extras = getIntent().getExtras();
        courseNo = extras.getString("CourseNO");
        title = extras.getString("Title");
        section = extras.getString("Section");

        initInstance();
        //Log.d("studentList", courseNo + " " + section);
        mTextCourseno.setText(courseNo);
        mTextTitle.setText(title);
        mTextSection.setText(section);

        setupRecycleView(courseNo);
    }

    private void initInstance() {
        mTextCourseno = findViewById(R.id.textViewCourseno);
        mTextTitle = findViewById(R.id.textViewTitle);
        mTextSection = findViewById(R.id.textViewSection);
        recyclerView = findViewById(R.id.recycleviewStudent);

        db = FirebaseFirestore.getInstance();
    }

    private void setupRecycleView(String id) {
        Query query = db.collection("Student").whereEqualTo("courseID." + courseNo, section);
        FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class)
                .build();
        adapter =  new StudentListAdapter(this, courseNo, options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

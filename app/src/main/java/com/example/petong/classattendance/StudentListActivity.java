package com.example.petong.classattendance;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class StudentListActivity extends AppCompatActivity {
    private TextView mTextCourseno;
    private TextView mTextTitle;
    private TextView mTextSection;
    private RecyclerView recyclerViewStudent;
    private RecyclerView recyclerViewHistory;
    private TextView textViewDate;
    private Button buttonDate;
    private FirebaseFirestore db;
    private StudentListAdapter studentAdapter;
    private DateHistoryAdapter dateHistoryAdapter;

    private String courseNo;
    private String title;
    private String section;
    private String date;

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

        setupStudentRecyclerView(courseNo);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(StudentListActivity.this, /*android.R.style.Theme_Holo_Light_Dialog,*/ new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "-" + month + "-" + year;
                        textViewDate.setText(date);
                    }
                }, year, month, day);
                //datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkByDateDialog();
            }
        });


    }

    private void initInstance() {
        mTextCourseno = findViewById(R.id.textViewCourseno);
        mTextTitle = findViewById(R.id.textViewTitle);
        mTextSection = findViewById(R.id.textViewSection);
        recyclerViewStudent = findViewById(R.id.recycleviewStudent);
        textViewDate = findViewById(R.id.textViewDate);
        buttonDate = findViewById(R.id.buttonDate);

        db = FirebaseFirestore.getInstance();
    }

    private void checkByDateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_student_online, null);
        setupHistoryRecyclerView(view);
        dateHistoryAdapter.startListening();

        builder.setView(view)
                .setTitle("STUDENT ATTENDANCE")
                .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateHistoryAdapter.stopListening();
                        dialog.dismiss();

                    }
                });
        builder.show();

    }

    private void setupHistoryRecyclerView(View view) {
        Query query = db.collection("ClassAttendance").whereEqualTo("date", date).whereEqualTo("courseID", courseNo).orderBy("studentID", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>()
                .setQuery(query, Attendance.class)
                .build();
        dateHistoryAdapter = new DateHistoryAdapter(options);
        recyclerViewHistory = view.findViewById(R.id.recyclerview_student);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(dateHistoryAdapter);

    }

    private void setupStudentRecyclerView(String id) {
        Query query = db.collection("Student").whereEqualTo("courseID." + courseNo, section);
        FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class)
                .build();
        studentAdapter =  new StudentListAdapter(this, courseNo, options);
        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudent.setAdapter(studentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        studentAdapter.stopListening();
    }
}

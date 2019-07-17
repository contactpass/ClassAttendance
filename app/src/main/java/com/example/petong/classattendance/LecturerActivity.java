package com.example.petong.classattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class LecturerActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView lecName;
    private Button addCoursebutton;
    private Button showonlineButton;
    private Button updateCourseButton;
    private EditText editTextCourseno;
    private EditText editTextTitle;
    private EditText editTextDay;
    private EditText editTextsTime;
    private EditText editTexteTime;
    private EditText editTextSection;
    private String lecturerID;
    private Lecturer lecturer;
    //private CourseLec course;
    private ShowStudentAdapter studentAdapter;
    private ShowCourseAdapter courseAdapter;
    private RecyclerView recyclerView;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);

        Intent intent = getIntent();
        lecturerID = intent.getStringExtra("Lecturer");       //get intent Lec ID
        setLecID(lecturerID);
        initInstance();
        getLecturerData(lecturerID);
        setupCourseRecycleView(lecturerID);
    }

    private void setLecID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    private void initInstance(){
        lecName = findViewById(R.id.fullnameView);
        addCoursebutton = findViewById(R.id.addCourseButton);
        addCoursebutton.setOnClickListener(this);
        //updateCourseButton = findViewById(R.id.updateButton);
        //updateCourseButton.setOnClickListener(this);
        showonlineButton = findViewById(R.id.showOnlineButton);
        showonlineButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.course_recycleview);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addCourseButton) {
            openAddDialog();

        }
        else if (i == R.id.showOnlineButton) {
            openShowDialog();
        }/* else if (i == R.id.logoutButton) {
            signOut();
        } else if (i == R.id.updateButton) {
            openUpdateDialog();
        }*/
    }
    private void getLecturerData(String lecturerID) {
        db.collection("Lecturer").document(lecturerID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        lecturer = documentSnapshot.toObject(Lecturer.class);
                        String fullname = lecturer.getFirstname() + " " + lecturer.getLastname();
                        lecName.setText(fullname);
                        //Log.d("Course1", lecturer.getCourseID().toString());
                        setLecturerData(lecturer);

                    }
                });
    }

    private void setLecturerData(Lecturer lecturer) {
        this.lecturer = lecturer;

    }
/*
    private void setCourse(Lecturer lectur){

        Map<String, String> courseList = lectur.getCourseID();

        int i = 0;
        Log.d("Hello",String.valueOf(courseList.size()));
        final CourseLec[] course = new CourseLec[courseList.size()];
        for (String key : courseList.keySet()) {
            String sec = courseList.get(key);
            db.collection("Course").document(key).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            CourseLec x = documentSnapshot.toObject(CourseLec.class);
                        }
                    });

            i++;
        }
    }*/



    private void gotoLogin() {
        Intent intent = new Intent(this, LoginLecturerActivity.class);
        startActivity(intent);
    }
    private void signOut() {
        auth.signOut();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            Log.d("emailpassword", "user is not logout");
        } else {
            Log.d("emailpassword", "user is logout");
        }
        gotoLogin();
    }
    private void openAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_course, null);
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
                        String sec = editTextSection.getText().toString();
                        HashMap<String, String> section = new HashMap<>();
                        section.put(sec, lecturerID);
                        HashMap<String, String> room = new HashMap<>();
                        section.put(sec, "CSB309");

                        CourseLec course = new CourseLec(courseno ,title, day, time,true, section, room);
                        db.collection("Course").document(courseno).set(course);     //Add replace ** use update if old doc

                    }
                });
        builder.show();
    }

    private void openUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_course, null);
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
                        String sec = editTextSection.getText().toString();
                        HashMap<String, String> section = new HashMap<>();
                        section.put(sec, lecturerID);
                        HashMap<String, String> room = new HashMap<>();
                        section.put(sec, "CSB309");

                        CourseLec course = new CourseLec(courseno ,title, day, time,true, section, room);
                        db.collection("Course").document(courseno).set(course);


                    }
                });
        builder.show();
    }


    private void openShowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_student_online, null);

        setupStudentRecyclerView(view);
        studentAdapter.startListening();

        builder.setView(view)
                .setTitle("SHOW STUDENT ONLINE")
                .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        studentAdapter.stopListening();
                        dialog.dismiss();

                    }
                });
        builder.show();

    }
    private void setupStudentRecyclerView(View view) {
        Query query = db.collection("Student").whereEqualTo("status", true)/*.orderBy(FieldPath.documentId(), Query.Direction.ASCENDING)*/;
        FirestoreRecyclerOptions<Student> options = new FirestoreRecyclerOptions.Builder<Student>()
                .setQuery(query, Student.class)
                .build();
        studentAdapter = new ShowStudentAdapter(lecturerID, options);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(studentAdapter);

    }

    private void setupCourseRecycleView(String lecturerID){
        Query query = db.collection("Course").whereEqualTo("sectionLec.001", lecturerID)/*.orderBy("courseID", Query.Direction.ASCENDING)*/;
        FirestoreRecyclerOptions<CourseLec> options = new FirestoreRecyclerOptions.Builder<CourseLec>()
                .setQuery(query, CourseLec.class)
                .build();
        courseAdapter = new ShowCourseAdapter(lecturerID, options);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(courseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        courseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        courseAdapter.stopListening();
    }
}
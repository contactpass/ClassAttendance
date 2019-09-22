package com.example.petong.classattendance;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class LecturerActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private TextView lecName;
    private Button addCoursebutton;
    private Button showonlineButton;
    private EditText editTextCourseno;
    private EditText editTextTitle;
    private TextView textViewsTime;
    private TextView textVieweTime;
    private EditText editTextSection;
    private EditText editTextRoom;
    private Spinner spinnerDays;
    private String lecturerID;
    private Lecturer lecturer;
    //private CourseLec course;
    private ShowStudentAdapter studentAdapter;
    private ShowCourseAdapter courseAdapter;
    private RecyclerView recyclerViewCourse;
    private RecyclerView recyclerViewStudent;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String days;
    private Boolean whatTime = true;
    private String stime;
    private String etime;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer);
        mContext = this;
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
        showonlineButton = findViewById(R.id.showOnlineButton);
        showonlineButton.setOnClickListener(this);
        recyclerViewCourse = findViewById(R.id.course_recycleview);

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
        } else if (i == R.id.textview_starttime) {
            whatTime = true;
            TimePickerDialog timePicker = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog,this,0,0,true);
            timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePicker.show();
        } else if (i == R.id.textview_endtime) {
            whatTime = false;
            TimePickerDialog timePicker = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog,this,0,0,true);
            timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePicker.show();
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRM SIGN OUT")
                .setMessage("Are you sure you want to sign out")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null){
                            Log.d("emailpassword", "user is not logout");
                        } else {
                            Log.d("emailpassword", "user is logout");
                        }
                        gotoLogin();
                    }
                });
        builder.show();

    }
    private void openAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_course, null);
        editTextCourseno = view.findViewById(R.id.edit_couseno);
        editTextTitle = view.findViewById(R.id.edit_title);
        spinnerDays = view.findViewById(R.id.spinnerDay);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.days, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinnerDays.setAdapter(adapter);
        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 days = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                days = null;
            }
        });
        textViewsTime = view.findViewById(R.id.textview_starttime);
        textVieweTime = view.findViewById(R.id.textview_endtime);
        textViewsTime.setOnClickListener(this);
        textVieweTime.setOnClickListener(this);

        editTextSection = view.findViewById(R.id.edit_section);
        editTextRoom = view.findViewById(R.id.edit_room);
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
                        String time = stime + " - " + etime;
                        String sec = editTextSection.getText().toString();
                        HashMap<String, String> section = new HashMap<>();
                        section.put(lecturerID, sec);
                        HashMap<String, String> room = new HashMap<>();
                        room.put(sec, editTextRoom.getText().toString());

                        CourseLec course = new CourseLec(courseno ,title, days, time,true, section, room);
                        db.collection("Course").document(courseno).set(course);     //Add replace ** use update if old doc

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
        recyclerViewStudent = view.findViewById(R.id.recyclerview_student);
        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudent.setAdapter(studentAdapter);

    }

    private void setupCourseRecycleView(String lecturerID){
        Query query = db.collection("Course").whereGreaterThanOrEqualTo("sectionLec." + lecturerID, ""/*lecturerID*/)/*.orderBy("courseID", Query.Direction.ASCENDING)*/;
        FirestoreRecyclerOptions<CourseLec> options = new FirestoreRecyclerOptions.Builder<CourseLec>()
                .setQuery(query, CourseLec.class)
                .build();
        courseAdapter = new ShowCourseAdapter(mContext ,lecturerID, options);
        recyclerViewCourse.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCourse.setAdapter(courseAdapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lec_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.button_logout) {
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);

        if (hourOfDay < 10) {
            hour = "0" + hourOfDay;
        } if (minute < 10) {
            min = "0" + minute;
        }
        String time = hour + " : " + min;
        if (whatTime) {
            stime = hour + min;
            textViewsTime.setText(time);
        } else {
            etime = hour + min;
            textVieweTime.setText(time);
        }
    }
}
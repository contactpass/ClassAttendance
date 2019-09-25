package com.example.petong.classattendance;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private static final int REQUEST_FINE_LOCATION = 124;
    //private int[] rssiOneMeter = new int[] {-46, -45, -35}; //home
    private int[] rssiOneMeter = new int[] {-46, -45, -40}; //jumbo1
    //private int[] rssiOneMeter = new int[] {-44, -43, -40}; //jumbo all
    //private final double[][] positionsAP = new double[][] { { 0.0, 0.0 }, { 0.86, 5.7 }, { 9.8, 5.1 } };  //home
    private final double[][] positionsAP = new double[][] { { 5.5, 0.0 }, { 0.0, 0.0}, { 2, 4.5 } };        //309
    //private final double[][] positionsAP = new double[][] { { 8.0, 0.0 }, { 8.0, 15.0}, { 0, 14. } };        //floor3
    private double[] distance = new double[3];
    //private  final int[][] room1 =  {{-1, 5}, {-2, 4}}; //{{x1, x2}, {y1, y2}}    home
    private  final int[][] room1 =  {{-4, 8}, {-4, 12}};        //309 test
    //private  final int[][] room1 =  {{-4, 4}, {8, 20}};        //309jumbo
    private ScanBoradcastReceiver wifiReceiver;
    private ScanWifiTask wifiTask;
    private FirebaseFirestore db;
    private int count = 0;
    private String courseNow;
    private String studentID;
    private String fullname;
    private Map<String, Object> courseList;
    private Attendance attendance;

    private TextView stuID;
    private TextView stuName;

    private TableLayout courseOpen;
    private TextView titleTV;
    private TextView sectionTV;
    private TextView dayTV;
    private TextView timeTV;
    private TextView statusTV;
    private String inArea = "in the area";
    private EditText courseno;
    private Button addButton;

    private HistoryAdapter historyAdapter;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }

        Bundle bunndle = getIntent().getExtras();
        final User userData = bunndle.getParcelable("User");


        intInstance();

        stuID.setText(studentID);
        stuName.setText(fullname);


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver.setWifiManager(wifiManager);

        IntentFilter filter = new IntentFilter((WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(wifiReceiver, filter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShowDialog(userData.getData());
            }
        });
        getCourse();
        if (courseList != null){
            checkDateTime(courseList, new CheckDateTimeInterface() {
                @Override
                public void onDateTimeMatch(CourseLec course) {
                    courseNow = course.getCourseID();
                    scanWifi(new ScanWifiInterface() {
                        @Override
                        public void onReceiveRssi(int[] rssi) {
                            Log.d("Dayd", rssi[0] + " " + rssi[1] + " " + rssi[2]);
                            Toast.makeText(getApplicationContext(), "Rssi " + rssi[0] + ", " + rssi[1] + ", " + rssi[2], Toast.LENGTH_LONG).show();
                            for (int i = 0; i < 3; i++) {
                                distance[i] = calDistance(rssi[i], rssiOneMeter[i]);
                            }
                            //Toast.makeText(getApplicationContext(), "Distance " + String.format("%.2f", distance[0]) + ", " + String.format("%.2f", distance[1]) + ", " + String.format("%.2f", distance[2]), Toast.LENGTH_LONG).show();
                            Log.d("Dayd", String.format("%.2f", distance[0]) + " " + String.format("%.2f", distance[1]) + " " + String.format("%.2f", distance[2]));
                            double[] position = calPosition(positionsAP, distance);
                            //Log.d("Dayd",String.format("%.2f", position[0]) + ", " + String.format("%.2f", position[1]));
                            if (position[0] >= room1[0][0] && position[0] <= room1[0][1] ) {
                                if (position[1] >= room1[1][0] && position[1] <= room1[1][1]) {
                                    Map<String, Object> status = new HashMap<>();
                                    status.put("status", true);
                                    db.collection("Student").document(userData.getData().getStudentCode()).update(status);
                                    statusTV.setText(inArea);
                                    Toast.makeText(getApplicationContext(),"You are in the area " + String.format("%.2f", position[0]) + ", " + String.format("%.2f", position[1]), Toast.LENGTH_LONG).show();
                                    count++;
                                } else {
                                    Map<String, Object> status = new HashMap<>();
                                    status.put("status", false);
                                    db.collection("Student").document(userData.getData().getStudentCode()).update(status);
                                    Toast.makeText(getApplicationContext(),"Position " + String.format("%.2f", position[0]) + ", " + String.format("%.2f", position[1]), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Map<String, Object> status = new HashMap<>();
                                status.put("status", false);
                                db.collection("Student").document(userData.getData().getStudentCode()).update(status);
                                Toast.makeText(getApplicationContext(),"Position " + String.format("%.2f", position[0]) + ", " + String.format("%.2f", position[1]), Toast.LENGTH_LONG).show();
                            }
                            if (count == 5){
                                attendanceClass(userData.getData());
                                wifiTask.cancel(true);
                                Toast.makeText(getApplicationContext(), "Class Attendance Success", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
    }

    private void intInstance(){
        stuID = findViewById(R.id.stuIDView);
        stuName = findViewById(R.id.fullnameView);
        titleTV = findViewById(R.id.textTitle);
        sectionTV = findViewById(R.id.sectionView);
        dayTV = findViewById(R.id.dayView);
        timeTV = findViewById(R.id.timeView);
        statusTV = findViewById(R.id.statusView);
        addButton = findViewById(R.id.add_button);

        db = FirebaseFirestore.getInstance();
        wifiReceiver = new ScanBoradcastReceiver();
        studentID = userData.getStudentCode();
        fullname = userData.getFullName();

    }

    private void getCourse() {

        courseList = new HashMap<>();
        Query queryCourse = db.collection("Course").whereGreaterThanOrEqualTo("studentID." + studentID, "");
        queryCourse.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CourseLec course = document.toObject(CourseLec.class);
                        courseList.put(document.getId(), course);
                        Log.d("checkCourse", course.getCourseID());
                    }
                } else {}
            }
        });
    }

    private void openShowDialog( UserData userData) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_student_online, null);

        setupStudentRecyclerView(view);
        historyAdapter.startListening();

        builder.setView(view)
                .setTitle("SHOW STUDENT ONLINE")
                .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        historyAdapter.stopListening();
                        dialog.dismiss();

                    }
                });
        builder.show();

        db.collection("ClassAttendance").whereEqualTo("studentID", studentID).whereEqualTo("courseID", courseno.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int k = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                k++;
                            } Toast.makeText(getApplicationContext(), "Your Class Attendance is " + k, Toast.LENGTH_LONG).show();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });

    }

    private void setupStudentRecyclerView(View view) {
        Query query = db.collection("ClassAttendance").whereEqualTo("studentID", studentID).whereEqualTo("courseID", courseno.getText().toString())/*.orderBy("date", Query.Direction.ASCENDING)*/;
        FirestoreRecyclerOptions<Attendance> options = new FirestoreRecyclerOptions.Builder<Attendance>()
                .setQuery(query, Attendance.class)
                .build();
        historyAdapter = new HistoryAdapter(options);
        recyclerView = view.findViewById(R.id.student_recyclrview );
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(historyAdapter);

    }

    interface SearchCourseInterface {
        void onSearchCourseFinished(HashMap<String, Object> courseList);
    }

    interface ScanWifiInterface {
        void onReceiveRssi(int[] rssi);
    }

    interface CheckDateTimeInterface {
        void onDateTimeMatch(CourseLec course);
    }

    public void checkDateTime(Map<String, Object> courseList, CheckDateTimeInterface callback){
        new CheckDateTimeTask(courseList, callback).execute();
    }

    public void searchCourse(String studentID, SearchCourseInterface callback){
        new SearchCourseTask(studentID, callback).execute();
    }

    public void scanWifi(ScanWifiInterface callback){
        wifiTask = new  ScanWifiTask(callback);
        wifiTask.setWifiReceiver(wifiReceiver); //add this
        wifiTask.setWifiManager(wifiManager);
        wifiTask.execute();
    }

    private void attendanceClass(final UserData userData){

        final CollectionReference attendanceRef = db.collection("ClassAttendance");
        /*
        Instant date1 = Instant.now();
        //Log.d("Attendance", date1.toString());
        Query checkAttendance = attendanceRef.whereEqualTo("date", date1);


        checkAttendance.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("Attendance", "Try New :)");
            }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Attendance", "Very Good Man", e);

                }
            });
        */
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day + "-" + month + "-" + year;

        attendance = new Attendance(date, studentID, courseNow, fullname);

        db.collection("ClassAttendance").add(attendance)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Attendance", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Attendance", "Error adding document", e);
                    }
                });
        Toast.makeText(getApplicationContext(), "Class Attendance Success", Toast.LENGTH_LONG).show();
        String checkIn = "Success";
        statusTV.setText(checkIn);
    }


    private void addStudentData(UserData userData, HashMap<String, Object> courseList ) {
        //List<String> courseID = new ArrayList<>(courseList.keySet());

        Map<String, String> courseID = new HashMap<>();
        for (String key : courseList.keySet()){
            Course course = (Course) courseList.get(key);
            if (course.getSection_lab().equals("000")) {
                courseID.put(key, course.getSection_lec());
            } else {
                courseID.put(key, course.getSection_lab());
            }
        }
        Student student = new Student(userData.getStudentCode(), userData.getPreName(), userData.getFirstName(), userData.getLastName(), courseID, false);
        db.collection("Student").document(userData.getStudentCode()).set(student)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Attendance", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Attendance", "Error writing document", e);
                    }
                });
    }

    private double calDistance(int rssi, int rssiOneMeter) {
        double distance;
        double x;
        x = rssiOneMeter - rssi;
        x = x/20;
        distance = Math.pow(10, x);
        return distance;
    }
    private double[] calPosition(double[][] positions, double[] distances){
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();
        double[] centroid = optimum.getPoint().toArray(); //ตำแหน่งได้จากนี้แล้ว// the answer
// error and geometry information; may throw SingularMatrixException depending the threshold argument provided
        //RealVector standardDeviation = optimum.getSigma(0); //ไม่ใช้
        //RealMatrix covarianceMatrix = optimum.getCovariances(0); //ไม่ใช้
        //Log.d("Location",String.format("%.2f", centroid[0]) + ", " + String.format("%.2f", centroid[1]) + "\n");

        return centroid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The requested permission is granted.

                } else {
                    // The user disallowed the requested permission.
                    mayRequestLocation();
                }
            }
        }
    }
    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        return false;
    }

}
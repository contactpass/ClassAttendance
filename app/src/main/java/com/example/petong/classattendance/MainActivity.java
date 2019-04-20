package com.example.petong.classattendance;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;
/*
import com.google.firebase.database.DataSnapshot;       //RDB
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
*/
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private static final int REQUEST_FINE_LOCATION = 124;
    private int[] rssiOneMeter = new int[] {-46, -45, -36};
    //private int rssiOneMeter2 = -45;
    //private int rssiOneMeter3 = -36;
    private final double[][] positions = new double[][] { { 3.6, 0.0 }, { 0.0, 0.0 }, { 0.3, 3.4 } };
    private double[] distance = new double[3];
    private TextView showDistance;
    private Button buttonShow;
    private TextView showRssi1;
    private TextView showRssi2;
    private TextView showRssi3;
    private TextView showDB;
    private int[] rssi = new int[3];
    private static final String TAG = "MyActivity";
    //public DatabaseReference myRef;   RDB
    //public DatabaseReference stuRef;

    ScanBoradcastReceiver wifiReceiver = new ScanBoradcastReceiver();
    ScanWifiTask wifiTask = new ScanWifiTask();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        //registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));   Delete



        /*      //Firebase RDB
        myRef = FirebaseDatabase.getInstance().getReference();  //Firebase Ref

        stuRef = myRef.child("Student");
        //DatabaseReference testRef = (DatabaseReference) myRef.child("Test");    //test firebase
        showDB = (TextView) findViewById(R.id.stuDB);

        stuRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map map = (Map)dataSnapshot.getValue();
                String value = String.valueOf(map.get("StuID"));
                showDB.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver.setWifiManager(wifiManager);
        wifiTask.setWifiReceiver(wifiReceiver); //add this
        wifiTask.setWifiManager(wifiManager);

        IntentFilter filter = new IntentFilter((WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registerReceiver(wifiReceiver, filter);
        //scanWifi();
        wifiTask.execute();

        showRssi1 = (TextView) findViewById(R.id.rssiText1);
        showRssi2 = (TextView) findViewById(R.id.rssiText2);
        showRssi3 = (TextView) findViewById(R.id.rssiText3);

        wifiTask.setTextview(showRssi1, showRssi2, showRssi3);
        showDistance = (TextView)  findViewById(R.id.ap);

        buttonScan = findViewById(R.id.scanBtn);
        buttonScan.setOnClickListener(new View.OnClickListener() {      //on click listener
            @Override
            public void onClick(View view) {        //on click
                for (int i = 0; i < 3; i++) {
                    rssi[i] = wifiTask.rssi[i];
                    distance[i] = calDistance(rssi[i], rssiOneMeter[i]);
                    showDistance.append("AP"+ (i+1) +": " + distance[i] + "\n");
                }
                calPosition(positions, distance);
                //showDistance.append("AP1: " + calDistance(rssi[])] + "\n");
                //showDistance.append("AP2: " + rssi[1] + "\n");
                //showDistance.append("AP3: " + rssi[2] + "\n");
                //showRssi1.setText(wifiTask.rssi1);
                //showRssi2.setText(wifiTask.rssi2);
                //showRssi3.setText(wifiTask.rssi3);

            }
        });


        final Student student = new Student("Narklove", "Tripi", "0871234567");

        buttonShow = findViewById(R.id.showButt);
        buttonShow.setOnClickListener(new View.OnClickListener() {          //Add data
            @Override
            public void onClick(View v) {
                db.collection("Student").document("570510710")
                        .set(student)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

            }
        });

        /*
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        scanWifi();


        /*
        buttonShow = findViewById(R.id.showButt);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <= 2; i++) {
                    Toast.makeText(getBaseContext(), String.valueOf(distance[i]), Toast.LENGTH_SHORT).show();
                }
            }
        });
        */

    }


    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The requested permission is granted.

                } else {
                    // The user disallowed the requested permission.
                    mayRequestLocation();
                }
                return;
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

    private void scanWifi() {
        arrayList.clear();
        wifiManager.startScan();
        //Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
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

// the answer
        double[] centroid = optimum.getPoint().toArray(); //ตำแหน่งได้จากนี้แล้ว

// error and geometry information; may throw SingularMatrixException depending the threshold argument provided
        //RealVector standardDeviation = optimum.getSigma(0); //ไม่ใช้
        //RealMatrix covarianceMatrix = optimum.getCovariances(0); //ไม่ใช้

        showDistance.append(String.format("%.2f", centroid[0]) + ", " + String.format("%.2f", centroid[1]) + "\n");


        return centroid;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

}

/*
        buttonget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWifiMan = (WifiManager)getSystemService(WIFI_SERVICE);
                if (mWifiMan != null){
                    WifiInfo info = mWifiMan.getConnectionInfo();
                    if(info.getSSID() != null) {
                        String rssi = String.valueOf(info.getRssi());
                        String ipaddr = netInfo.getIPAddress();
                        String macAdr = netInfo.getWifiMACAddress();
                        //String ipaddr = String.valueOf(info.getIpAddress());
                        //Toast.makeText(getBaseContext(), rssi, Toast.LENGTH_LONG).show();
                        mSSIDTextView.setText(info.getSSID());
                        mIPadrTextView.setText(ipaddr);
                        mMACadrTextView.setText(macAdr);
                        mRSSITextView.setText(rssi);
                    }
                }

            }
        });

}*/


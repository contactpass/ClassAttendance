package com.example.petong.classattendance;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private static final int REQUEST_FINE_LOCATION = 124;
    private final String bssid1 = "68:7f:74:6f:cd:2c";
    private final String bssid2 = "20:aa:4b:c5:9b:9b";
    private final String bssid3 = "a0:72:2c:0e:db:74";
    private int rssiOneMeter1 = -46;
    private int rssiOneMeter2 = -45;
    private int rssiOneMeter3 = -36;
    private final double[][] positions = new double[][] { { 3.6, 0.0 }, { 0.0, 0.0 }, { 0.3, 3.4 } };
    private double[3] distance;
    private 
    private TextView showDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }


        showDistance = (TextView)  findViewById(R.id.ap);
        buttonScan = findViewById(R.id.scanBtn);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanWifi();
            }
        });

        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        /*
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        */
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        scanWifi();
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
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        //Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                switch (scanResult.BSSID) {
                    case bssid1: {
                        //Toast.makeText(getBaseContext(), "Access Point 1", Toast.LENGTH_LONG).show();
                        distance = calDistance(scanResult.level, rssiOneMeter1);
                        showDistance.append("Distance ap1: " + distance + " " + scanResult.level +"\n");
                        break;
                    }
                    case bssid2: {
                        //Toast.makeText(getBaseContext(), "Access Point 2", Toast.LENGTH_LONG).show();
                        distance = calDistance(scanResult.level, rssiOneMeter2);
                        showDistance.append("Distance ap2: " + distance + " " + scanResult.level +"\n");
                        break;
                    }
                    case bssid3: {
                        //Toast.makeText(getBaseContext(), "Access Point 3", Toast.LENGTH_LONG).show();
                        distance = calDistance(scanResult.level, rssiOneMeter3);
                        showDistance.append("Distance ap3: " + distance + " " + scanResult.level +"\n");
                        break;
                    }
                }
                arrayList.add(scanResult.SSID + " - " + scanResult.capabilities + " " + scanResult.BSSID + " " + scanResult.level);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private double calDistance(int rssi, int rssiOneMeter) {
        double distance;
        double x;
        x = rssiOneMeter - rssi;
        x = x/20;
        distance = Math.pow(10, x);
        return distance;
    }

}

//final NetInfo netInfo = new NetInfo(this);
        /*
        mSSIDTextView = findViewById(R.id.ssid);
        mIPadrTextView = findViewById(R.id.ipAdr);
        mMACadrTextView = findViewById(R.id.macAdr);
        mRSSITextView = findViewById(R.id.rssi);
        Button buttonget = findViewById(R.id.button);

     ]*/

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
    /*
    private void scanWifi() {
        mWifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    scanFailure();
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
    }

    private void scanSuccess() {
        List<ScanResult> results = mWifiMan.getScanResults();
    }
    private void scanFailure() {
        List<ScanResult> results = mWifiMan.getScanResults();
    }




}*/

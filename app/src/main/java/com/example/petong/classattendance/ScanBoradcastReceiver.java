package com.example.petong.classattendance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;


class ScanBoradcastReceiver extends BroadcastReceiver {
    //private final String bssid1 = "68:7f:74:6f:cd:2c";      //classroom1
    private final String bssid1 = "00:7e:95:cb:0a:40";      //jumbo1
    //private final String bssid2 = "20:aa:4b:c5:9b:9b";      //classroom2
    private final String bssid2 = "00:7e:95:cb:40:a0";      //jumbo2
    //private final String bssid3 = "c4:b8:b4:fb:6a:c8";    //home
    private final String bssid3 = "00:7e:95:cb:3e:80";     //Jumbo3
    private ArrayList<String> arrayList = new ArrayList<>();
    private List<ScanResult> results;
    public int[]rssi = new int[3];
    private WifiManager wifiManager;
    public int tmp;


    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
        if (success) {
            results = wifiManager.getScanResults();
        } else {
            scanFailure();
            results = wifiManager.getScanResults(); //old results
        }

        for (ScanResult scanResult : results) {
            switch (scanResult.BSSID) {
                case bssid1: {
                    rssi[0] = scanResult.level;
                    tmp = scanResult.level;  //test
                    break;
                }
                case bssid2: {
                    rssi[1] = scanResult.level;
                    tmp = scanResult.level;
                    break;
                }
                case bssid3: {
                    rssi[2] = scanResult.level;
                    tmp = scanResult.level;
                    break;
                }
            }
            arrayList.add(scanResult.SSID + " - " + scanResult.capabilities + " " + scanResult.BSSID + " " + scanResult.level);
            //adapter.notifyDataSetChanged();
        }
    }

    public void scanFailure() {
        //Can I do some thing.
    }


    public void setWifiManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }
}

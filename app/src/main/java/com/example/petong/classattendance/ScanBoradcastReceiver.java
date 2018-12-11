package com.example.petong.classattendance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;


class ScanBoradcastReceiver extends BroadcastReceiver {
    private final String bssid1 = "68:7f:74:6f:cd:2c";
    private final String bssid2 = "20:aa:4b:c5:9b:9b";
    private final String bssid3 = "a0:72:2c:0e:db:74";
    private ArrayList<String> arrayList = new ArrayList<>();
    private List<ScanResult> results;
    public int[]rssi = new int[3];
    private WifiManager wifiManager;
    public int tmp;


    @Override
    public void onReceive(Context context, Intent intent) {
        results = wifiManager.getScanResults();
        for (ScanResult scanResult : results) {
            switch (scanResult.BSSID) {
                case bssid1: {
                    rssi[0] = scanResult.level;
                    //tmp = scanResult.level;  test
                    break;
                }
                case bssid2: {
                    rssi[1] = scanResult.level;
                    break;
                }
                case bssid3: {
                    rssi[2] = scanResult.level;
                    break;
                }
            }
            arrayList.add(scanResult.SSID + " - " + scanResult.capabilities + " " + scanResult.BSSID + " " + scanResult.level);
            //adapter.notifyDataSetChanged();
        }
    }

    public void setWifiManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }
}

package com.example.petong.classattendance;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;


public class ScanWifiTask extends AsyncTask<Void, int[], int[]> {

    private WifiManager wifiManager;
    private ScanBoradcastReceiver wifiReceiver;
    public int[] rssi = new int[3];
    private int[] issr = new int[3];
    private int[] avg_rssi = new int[3];
    private double total;
    private boolean success;
    private String rssi1;
    private String rssi2;
    private String rssi3;
    final MainActivity.ScanWifiInterface callback;
    //private TextView showRssi1;
    //private TextView showRssi2;
    //private TextView showRssi3;
    boolean first = true;

    public ScanWifiTask(MainActivity.ScanWifiInterface callback) {
        this.callback = callback;
    }

    @Override
    protected int[] doInBackground(Void... voids) {
        int i;
        int count = 0;

        while (true) {

            success = wifiManager.startScan();
            if (!success) {
                wifiReceiver.scanFailure();
            } try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (i = 0; i < 3; i++) {
                rssi[i] = wifiReceiver.rssi[i];
            }
            if (rssi[0] != 0 && rssi[1] != 0 && rssi[2] != 0) {
                if (count == 0) {
                    for (i = 0; i < 3; i++) {
                        avg_rssi[i] = rssi[i];
                    }
                    count++;
                    if (first) {
                        publishProgress(avg_rssi);
                        first = false;
                    }
                } else {
                    for (i = 0; i < 3; i++) {
                        avg_rssi[i] = avg_rssi[i] + rssi[i];
                    }
                    count++;

                    if (count == 3) {
                        for (i = 0; i < 3; i++) {
                            total = avg_rssi[i];
                            total = total/3;
                            avg_rssi[i] = (int) Math.round(total);
                        }
                        publishProgress(avg_rssi);
                        count = 0;
                    }

                }
            }
        }
/*
        while (true) {
            success = wifiManager.startScan();
            if (!success) {
                wifiReceiver.scanFailure();
            }
            try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            //wifiManager.startScan();

            for (int k = 0; k < 3; k++) {
                rssi[k] = wifiReceiver.rssi[k];
            }
                 //AVG rssi
            if (rssi[0] != 0 && rssi[1] != 0 && rssi[2] != 0) {
                if (first) {
                    for (i = 0; i < 3; i++) {
                        avg_rssi[i] = rssi[i];
                    }
                    first = false;
                    publishProgress(avg_rssi);
                } else {
                    for (i = 0; i < 3; i++) {
                        int wAvg = 1;
                        int wIssr = 1;
                        total = wAvg*avg_rssi[i] + wIssr*rssi[i];
                        total = total / (wAvg + wIssr);
                        avg_rssi[i] = (int) Math.round(total);

                    }first = false;
                    publishProgress(avg_rssi);
                }
                try {
                    Thread.sleep(5000);
                    //Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    @Override
    protected void onProgressUpdate(int[]... values) {
        super.onProgressUpdate(values);
        callback.onReceiveRssi(values[0]);
    }

    @Override
    protected void onPostExecute(int[] ints) {
        super.onPostExecute(ints);
    }

    public void setWifiManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public void setWifiReceiver(ScanBoradcastReceiver wifiReceiver) {
        this.wifiReceiver = wifiReceiver;
    }
}
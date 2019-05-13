package com.example.petong.classattendance;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class ScanWifiTask extends AsyncTask<Void, int[], int[]> {

    private WifiManager wifiManager;
    private ScanBoradcastReceiver wifiReceiver;
    private int i = 0;
    public int[] rssi = new int[3];
    private int[] issr = new int[3];
    private int[] avg_rssi = new int[3];
    private double total;
    private boolean success;
    private String rssi1;
    private String rssi2;
    private String rssi3;
    private TextView showRssi1;
    private TextView showRssi2;
    private TextView showRssi3;
    boolean first = true;
    int count = 0;

    @Override
    protected int[] doInBackground(Void... voids) {
//        while (true) {
//
//            for (i = 0; i < 3; i++) {
//                wifiManager.startScan();
//                if (!success) {
//                    wifiReceiver.scanFailure();
//                } try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                for (j = 0; j < 3; j++) {
//                    rssi[i][j] = wifiReceiver.rssi[j];
//                    issr[j] = rssi[i][j];
//                }
//                publishProgress(issr);
//            }
//
//            for (i = 0; i < 3; i++) {
//                total = average(rssi[0][i], rssi[1][i], rssi[2][i]);
//                avg_rssi[i] = (int) Math.round(total);
//
//                rssi[0][i] = 0;
//                rssi[1][i] = 0;
//                rssi[2][i] = 0;
//            }
//        }

        while (true) {
            if (first){
                count = 0;
            }
            //wifiManager.startScan();
            success = wifiManager.startScan();
            if (!success) {
                wifiReceiver.scanFailure();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int k = 0; k < 3; k++) {
                issr[k] = wifiReceiver.rssi[k];
            }
            //publishProgress(issr);
            if (issr[0] != 0 && issr[1] != 0 && issr[2] != 0) {
                if (first) {
                    avg_rssi[0] = issr[0];
                    avg_rssi[1] = issr[1];
                    avg_rssi[2] = issr[2];
//                    avg_rssi = issr; //Pass by ref WTF!!!
                    first = false;
                    publishProgress(avg_rssi);
                } else {
                    for (i = 0; i < 3; i++) {
                        int wAvg = 1;
                        int wIssr = 1;
                        total = wAvg*avg_rssi[i] + wIssr*issr[i];
                        total = total / (wAvg + wIssr);
                        avg_rssi[i] = (int) Math.round(total);
                        first = false;
                    }
                    publishProgress(avg_rssi);
                }
            }
        }
    }

    @Override
    protected void onProgressUpdate(int[]... values) {
        super.onProgressUpdate(values);
        rssi = values[0];   //Pass by ref
        rssi1 = String.valueOf(rssi[0]);
        rssi2 = String.valueOf(rssi[1]);
        rssi3 = String.valueOf(rssi[2]);
        showRssi1.setText(rssi1);
        showRssi2.setText(rssi2);
        showRssi3.setText(rssi3);

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

    public void setTextview(TextView showRssi1, TextView showRssi2, TextView showRssi3) {
        this.showRssi1 = showRssi1;
        this.showRssi2 = showRssi2;
        this.showRssi3 = showRssi3;
    }
}
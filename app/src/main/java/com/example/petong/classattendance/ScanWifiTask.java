package com.example.petong.classattendance;

import android.app.ProgressDialog;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class ScanWifiTask extends AsyncTask<Void, String, int[]> {
    private WifiManager wifiManager;
    private Context context;
    private int i = 0;
    private int j = 0;
    private int[][] rssi = new int[3][3];
    private int[] avg_rssi = new int[3];
    private double total;
    ScanBoradcastReceiver wifiReceiver = new ScanBoradcastReceiver();
    String rssi1;
    String rssi2;
    String rssi3;
    private TextView showRssi1;
    String res;
    int temp;


    @Override
    protected int[] doInBackground(Void... voids) {
        wifiManager.startScan();
        while (i != 4) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (i = 0; i < 3; i++) {
                wifiManager.startScan();
                for (j = 0; j < 3; j++) {
                    temp = wifiReceiver.tmp;    //test
                    rssi[i][j] = wifiReceiver.rssi[j];
                    //res = "453";
                    res = String.valueOf(rssi[i][j]);
                    publishProgress(res);
                    //publishProgress(rssi[i][j]);
                }
            }
            for (i = 0; i < 3; i++) {
                total = average(rssi[0][i], rssi[1][i], rssi[2][i]);
                avg_rssi[i] = (int) Math.round(total);


            }
        }

        return wifiReceiver.rssi;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        rssi1 = values[0];
        showRssi1.setText(rssi1);
        //rssi1 = "Hello";


        //Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, rssi2, Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, rssi3, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPostExecute(int[] ints) {
        super.onPostExecute(ints);
        //rssi1 = String.valueOf(ints[0]);
        rssi2 = String.valueOf(ints[1]);
        rssi3 = String.valueOf(ints[2]);
    }

    public double average(double x, double y, double z)
    {
        return (x + y + z) / 3;
    }

    public void setWifiManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }


    public void setTextview(TextView showRssi1) { this.showRssi1 = showRssi1;}
}
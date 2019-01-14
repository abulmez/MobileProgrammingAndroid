package com.example.abulm.mobileprogramming.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {

    private NetworkStatus(){}

    private static Boolean networkAvailable = null;

    public static boolean isNetworkAvailable(){
        if(networkAvailable == null){
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) MyApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            networkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return networkAvailable;
    }
}

package com.jesushghar.uss.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by PAT on 5/5/2016.
 */
public class CheckNetwork {
    public static boolean isConnectionAvailable( Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()
                    && networkInfo.isConnectedOrConnecting()
                    && networkInfo.isAvailable()) {
                return true;
            }
        }
        return false;
     }
}

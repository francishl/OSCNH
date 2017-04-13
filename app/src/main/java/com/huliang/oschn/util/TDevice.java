package com.huliang.oschn.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.huliang.oschn.base.BaseApplication;

/**
 * Created by huliang on 4/12/17.
 */
public class TDevice {

    public static boolean hasInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.context()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }
}

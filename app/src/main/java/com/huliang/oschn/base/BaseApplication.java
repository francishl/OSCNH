package com.huliang.oschn.base;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.huliang.oschn.improve.widget.SimplexToast;

/**
 * Created by huliang on 17/3/20.
 */
public class BaseApplication extends Application {

    static Context _context;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }

    public static void showToastShort(String message) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }

    public static void showToast(String message, int duration, int icon, int gravity) {
        SimplexToast.show(_context, message, gravity, duration);
    }
}

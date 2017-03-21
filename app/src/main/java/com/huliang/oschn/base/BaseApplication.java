package com.huliang.oschn.base;

import android.app.Application;
import android.content.Context;

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
}

package com.huliang.oschn;

import android.util.Log;

import com.huliang.oschn.api.ApiHttpClient;

/**
 * Created by huliang on 17/3/20.
 */
public class OSCApplication extends AppContext {
    private static final String TAG = "OSCApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate ---- 创建");
        init();
    }

    private void init() {
        // 初始化网络请求
        ApiHttpClient.init(this);
    }
}

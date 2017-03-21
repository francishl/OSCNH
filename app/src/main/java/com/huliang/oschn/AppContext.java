package com.huliang.oschn;

import com.huliang.oschn.base.BaseApplication;

/**
 * 全局应用程序类
 * 用于保存和调用全局应用配置及访问网络数据
 * <p/>
 * Created by huliang on 17/3/20.
 */
public class AppContext extends BaseApplication {

    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }
}

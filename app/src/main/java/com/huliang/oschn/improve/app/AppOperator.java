package com.huliang.oschn.improve.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.huliang.oschn.improve.app.gson.ImageJsonDeserializer;
import com.huliang.oschn.improve.app.gson.StringJsonDeserializer;
import com.huliang.oschn.improve.bean.Tweet;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huliang on 17/3/17.
 */
public final class AppOperator {

    private static ExecutorService EXECUTORS_INSTANCE;

    /**
     * 获取线程池
     *
     * @return
     */
    public static Executor getExecutor() {
        if (EXECUTORS_INSTANCE == null) {
            /**
             * 当一个线程访问object的一个synchronized(this)同步代码块时，它就获得了这个object的对象锁。
             * 结果:
             * 1. 其他线程仍然可以访问该object中的非synchronized(this)同步代码块。
             * 2. 其它线程对该object对象所有同步代码部分的访问都被暂时阻塞。
             */
            synchronized (AppOperator.class) {
                if (EXECUTORS_INSTANCE == null) {
                    EXECUTORS_INSTANCE = Executors.newFixedThreadPool(6);
                }
            }
        }
        return EXECUTORS_INSTANCE;
    }

    /**
     * 将一条任务添加到线程池执行(不立即执行)
     *
     * @param runnable
     */
    public static void runOnThread(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    /**
     * 创建gson对象
     *
     * @return
     */
    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        JsonDeserializer deserializer = new StringJsonDeserializer();
        gsonBuilder.registerTypeAdapter(String.class, deserializer);

        deserializer = new ImageJsonDeserializer();
        gsonBuilder.registerTypeAdapter(Tweet.Image.class, deserializer);

        return gsonBuilder.create();
    }
}

package com.huliang.oschn.improve.base.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import butterknife.ButterKnife;

/**
 * Created by huliang on 17/3/17.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected RequestManager mImageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initBundle(getIntent().getExtras())) {
            setContentView(getContentView());

            // 绑定activity
            ButterKnife.bind(this);

            // 初始化界面和数据
            initWindow();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * 使用传入参数 bundle 初始化
     *
     * @param bundle
     * @return
     */
    protected boolean initBundle(Bundle bundle) {
        return true;
    }

    protected void initWindow() {

    }

    protected void initWidget() {

    }

    protected void initData() {

    }

    /**
     * 图片加载管理器
     *
     * @return
     */
    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = Glide.with(this);
        }
        return mImageLoader;
    }
}

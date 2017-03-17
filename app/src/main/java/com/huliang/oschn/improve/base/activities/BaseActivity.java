package com.huliang.oschn.improve.base.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by huliang on 17/3/17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initBundle(getIntent().getExtras())) {
            setContentView(getContentView());

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

    protected boolean initBundle(Bundle bundle) {
        return true;
    }

    protected void initWindow() {

    }

    protected void initWidget() {

    }

    protected void initData() {

    }


}

package com.huliang.oschn;

import android.content.Intent;

import com.huliang.oschn.improve.app.AppOperator;
import com.huliang.oschn.improve.base.activities.BaseActivity;
import com.huliang.oschn.improve.main.MainActivity;

/**
 * 应用启动界面
 * <p>
 * Created by huliang on 17/3/17.
 */
public class LaunchActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.app_start;
    }

    @Override
    protected void initData() {
        super.initData();

        // 在这里我们检测是否是新版本安装，如果是则进行老版本数据迁移工作
        // 该工作可能消耗大量时间所以放在自线程中执行
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                doMerge();
            }
        });
    }

    private void doMerge() {
        // 判断是否是新版本

        // Delay...
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 完成后进行跳转操作
        redirectTo();
    }

    private void redirectTo() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

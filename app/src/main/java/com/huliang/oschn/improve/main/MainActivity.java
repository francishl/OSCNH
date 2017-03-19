package com.huliang.oschn.improve.main;

import android.support.v4.app.FragmentManager;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.activities.BaseActivity;
import com.huliang.oschn.improve.main.nav.NavFragment;
import com.huliang.oschn.improve.main.nav.NavigationButton;

/**
 * Created by huliang on 17/3/17.
 */
public class MainActivity extends BaseActivity implements NavFragment.OnNavigationReselectListener {
    private static final String TAG = "MainActivity";

    private NavFragment mNavBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_main_ui;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 初始化tab fragment
        FragmentManager manager = getSupportFragmentManager();
        mNavBar = (NavFragment) manager.findFragmentById(R.id.fag_nav); // 根据ID来找到对应的Fragment实例
        mNavBar.setup(this, manager, R.id.main_container, this); // main_container: 切换页面对应的区域
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onReselect(NavigationButton navigationButton) {

    }
}

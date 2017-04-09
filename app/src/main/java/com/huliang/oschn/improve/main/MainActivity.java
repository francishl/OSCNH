package com.huliang.oschn.improve.main;

import android.support.v4.app.FragmentManager;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.activities.BaseActivity;
import com.huliang.oschn.improve.main.nav.NavFragment;
import com.huliang.oschn.improve.main.nav.NavigationButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huliang on 17/3/17.
 */
public class MainActivity extends BaseActivity implements NavFragment.OnNavigationReselectListener {
    private static final String TAG = "MainActivity";

    private NavFragment mNavBar;

    private List<TurnBackListener> mTurnBackListeners = new ArrayList<>();

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

    @Override
    public void onBackPressed() {
        for (TurnBackListener listener : mTurnBackListeners) {
            // 返回 true 则拦截动作不再往下传递
            if (listener.onTurnBack()) {
                return;
            }
        }
    }

    public void addOnTurnBackListeners(TurnBackListener mTurnBackListener) {
        this.mTurnBackListeners.add(mTurnBackListener);
    }

    /**
     * 返回按钮点击监听器
     */
    public interface TurnBackListener {
        boolean onTurnBack(); // 返回 true 则拦截动作不再往下传递, 返回 false 则继续传递该操作给下面的监听者
    }
}

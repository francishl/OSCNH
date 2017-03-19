package com.huliang.oschn.improve.base.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Fragment基础类
 * <p/>
 * Created by huliang on 17/3/17.
 */
public abstract class BaseFragment extends Fragment {

    protected View mRoot;

    /**
     * 重写onCreateView决定Fragemnt的布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot != null) {
            // 如果不为空,则将其从父容器中移除
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null) {
                parent.removeView(mRoot);
            }
        } else {
            // 由子类传入布局id,初始化界面,绑定控件;
            mRoot = inflater.inflate(getLayoutId(), container, false);

            // Do something
            onBindViewBefore(mRoot);

            // Bind view
            ButterKnife.bind(this, mRoot);

            // Init
            initWidget(mRoot);
            initData();
        }

        return mRoot;
    }

    /**
     * 视图绑定前的准备工作(加载布局,从而才能顺利完成控件绑定)
     *
     * @param root
     */
    protected void onBindViewBefore(View root) {

    }

    protected abstract int getLayoutId();

    protected void initWidget(View root) {

    }

    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

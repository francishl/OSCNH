package com.huliang.oschn.improve.base.fragments;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewStub;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.widget.TitleBar;

/**
 * Created by huliang on 17/3/17.
 */
public abstract class BaseTitleFragment extends BaseFragment {

    TitleBar mTitleBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_title;
    }

    @Override
    protected void onBindViewBefore(View root) {
        super.onBindViewBefore(root);

        // on before onBindViewBefore call
        ViewStub viewStub = (ViewStub) root.findViewById(R.id.lay_content);
        viewStub.setLayoutResource(getContentLayoutId());

        // 调用inflate()函数将其要装载的目标布局给加载出来，从而达到延迟加载的效果，
        // 这个要被加载的布局通过android:layout属性来设置
        viewStub.inflate();
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // not null
        mTitleBar = (TitleBar) root.findViewById(R.id.nav_title_bar);
        mTitleBar.setTitle(getTitleRes());
        mTitleBar.setIcon(getIconRes());
        mTitleBar.setIconOnClickListener(getIconClickListener());
    }

    /**
     * 获取title_bar下方内容部分的布局
     *
     * @return
     */
    protected abstract
    @LayoutRes
    int getContentLayoutId();

    /**
     * 设置标题
     *
     * @return
     */
    protected abstract
    @StringRes
    int getTitleRes();

    /**
     * 设置图标
     *
     * @return
     */
    protected
    @DrawableRes
    int getIconRes() {
        return 0;
    }

    /**
     * 设置icon事件监听
     *
     * @return
     */
    protected View.OnClickListener getIconClickListener() {
        return null;
    }
}

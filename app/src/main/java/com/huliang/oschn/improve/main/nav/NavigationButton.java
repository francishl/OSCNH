package com.huliang.oschn.improve.main.nav;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huliang.oschn.R;

/**
 * Created by huliang on 17/3/17.
 */
public class NavigationButton extends FrameLayout {
    private Fragment mFragment = null;
    private Class<?> mClx;
    private ImageView mIconView; // tab icon
    private TextView mTitleView; // tab title
    private TextView mDot; // tab red dot
    private String mTag; // tab tag

    /**
     * 自定义控件,必须同时实现其三个constructor
     *
     * @param context
     */
    public NavigationButton(Context context) {
        super(context);
        init();
    }

    public NavigationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Denotes that the annotated element should only be called on the given API level or higher.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NavigationButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_nav_item, this, true);

        mIconView = (ImageView) findViewById(R.id.nav_iv_icon);
        mTitleView = (TextView) findViewById(R.id.nav_tv_title);
        mDot = (TextView) findViewById(R.id.nav_tv_dot);
    }

    /**
     * 外部接口,初始化button
     *
     * @param resId
     * @param strId
     * @param clz
     */
    public void init(@DrawableRes int resId, @StringRes int strId, Class<?> clz) {
        mIconView.setImageResource(resId);
        mTitleView.setText(strId);
        mClx = clz;
        mTag = mClx.getName();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mIconView.setSelected(selected);
        mTitleView.setSelected(selected);
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public Class<?> getClx() {
        return mClx;
    }

    public String getTag() {
        return mTag;
    }
}

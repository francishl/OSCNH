package com.huliang.oschn.improve.widget;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * 自定义实现 FragmentPagerAdapter
 * <p/>
 * Created by huliang on 4/7/17.
 */
public abstract class FragmentPagerAdapter extends PagerAdapter {

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}

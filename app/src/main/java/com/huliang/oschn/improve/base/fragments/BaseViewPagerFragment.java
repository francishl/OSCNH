package com.huliang.oschn.improve.base.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.huliang.oschn.R;

import butterknife.Bind;

/**
 * 动弹 viewPager + tabLayout
 * Created by huliang on 17/3/19.
 */
public abstract class BaseViewPagerFragment extends BaseTitleFragment {

    @Bind(R.id.tab_nav)
    TabLayout mTabNav;
    @Bind(R.id.base_viewPager)
    ViewPager mBaseViewPager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_base_viewpager;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // Fragment嵌套Fragment要用getChildFragmentManager
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getChildFragmentManager(), getPagers());
        mBaseViewPager.setAdapter(adapter);

        // 将tabLayout与viewPager联动起来
        mTabNav.setupWithViewPager(mBaseViewPager);
        mBaseViewPager.setCurrentItem(0, true);
    }

    /**
     * 获取页面列表pagers
     *
     * @return
     */
    protected abstract PagerInfo[] getPagers();

    /**
     * pager对应页面
     */
    public static class PagerInfo {
        private String title;
        private Class<?> clx;
        private Bundle args;

        public PagerInfo(String title, Class<?> clx, Bundle args) {
            this.title = title;
            this.clx = clx;
            this.args = args;
        }
    }

    public class BaseViewPagerAdapter extends FragmentPagerAdapter {
        private PagerInfo[] mInfoList;
        private Fragment mCurFragment;

        public BaseViewPagerAdapter(FragmentManager fm, PagerInfo[] infoList) {
            super(fm);
            mInfoList = infoList;
        }

        @Override
        public Fragment getItem(int position) {
            PagerInfo info = mInfoList[position];
            return Fragment.instantiate(getContext(), info.clx.getName(), info.args);
        }

        @Override
        public int getCount() {
            return mInfoList.length;
        }

        /**
         * 需要重写adapter的getPageTitle,否则无法显示TabLayout上的标签
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mInfoList[position].title;
        }
    }
}

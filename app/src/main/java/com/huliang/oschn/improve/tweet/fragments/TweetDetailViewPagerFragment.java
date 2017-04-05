package com.huliang.oschn.improve.tweet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huliang.oschn.R;
import com.huliang.oschn.base.User;
import com.huliang.oschn.improve.bean.simple.TweetComment;
import com.huliang.oschn.improve.tweet.contract.TweetDetailContract;
import com.huliang.oschn.util.TLog;

/**
 * 动弹详情页, 评论和点赞列表 viewPager
 * Created by huliang on 3/28/17.
 */
public class TweetDetailViewPagerFragment extends Fragment implements
        TweetDetailContract.IAgencyView, TweetDetailContract.IThumbupView, TweetDetailContract.ICmnView {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    protected FragmentStatePagerAdapter mAdapter;
    private TweetDetailContract.Operator mOperator;

    public static TweetDetailViewPagerFragment instantiate() {
        return new TweetDetailViewPagerFragment();
    }

    /**
     * fragment 首次绑定到 context 时调用, onCreate() 随后调用
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // context 即 fragment 绑定的 TweetDetailActivity (已经实现了 Operator 接口)
        mOperator = (TweetDetailContract.Operator) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_view_pager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_nav);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAdapter == null) {
            final ListTweetLikeUsersFragment mLikeFragment;
            mLikeFragment = ListTweetLikeUsersFragment.instantiate(mOperator, this);

            final ListTweetCommentFragment mCommentFragment;
            mCommentFragment = new ListTweetCommentFragment();

            mAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            return mLikeFragment;
                        case 1:
                            return mCommentFragment;
                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 2;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    switch (position) {
                        case 0:
                            return String.format("赞(%s)", mOperator.getTweetDetail().getLikeCount());
                        case 1:
                            return String.format("评论(%s)", mOperator.getTweetDetail().getCommentCount());
                    }
                    return null;
                }
            };

            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager); // tabLayout与viewPager关联起来
            mViewPager.setCurrentItem(1);
        } else {
            mViewPager.setAdapter(mAdapter);
        }
    }

    @Override
    public void resetLikeCount(int count) {
        TLog.log("点赞数更新 " + count);
        // 重置数据 count
        mOperator.getTweetDetail().setLikeCount(count);
        // 刷新界面
        TabLayout.Tab tab = mTabLayout.getTabAt(0);
        if (tab != null) {
            tab.setText(String.format("赞(%s)", count));
        }
    }

    @Override
    public void resetCmnCount(int count) {

    }

    @Override
    public void onCommentSuccess(TweetComment comment) {

    }

    @Override
    public void onLikeSuccess(boolean isUp, User user) {

    }
}

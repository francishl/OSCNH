package com.huliang.oschn.improve.main.tabs;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.fragments.BaseViewPagerFragment;
import com.huliang.oschn.improve.tweet.fragments.TweetFragment;

/**
 * Created by huliang on 17/3/19.
 */
public class TweetViewPagerFragment extends BaseViewPagerFragment {

    @Override
    protected int getTitleRes() {
        return R.string.main_tab_name_tweet;
    }

    @Override
    protected int getIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected View.OnClickListener getIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "btn_search_normal", Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }

    /**
     * 设置tweetFragment的类型
     *
     * @param catalog
     * @return
     */
    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(TweetFragment.BUNDLE_KEY_REQUEST_CATALOG, catalog);
        return bundle;
    }

    @Override
    protected PagerInfo[] getPagers() {
        return new PagerInfo[]{
                new PagerInfo("最新动弹", TweetFragment.class, getBundle(TweetFragment.CATALOG_NEW)),
                new PagerInfo("热门动弹", TweetFragment.class, getBundle(TweetFragment.CATALOG_HOT)),
                new PagerInfo("每日乱弹", TweetFragment.class, getBundle(TweetFragment.CATALOG_FRIENDS)),
                new PagerInfo("我的动弹", TweetFragment.class, getBundle(TweetFragment.CATALOG_MYSELF)),
        };
    }
}

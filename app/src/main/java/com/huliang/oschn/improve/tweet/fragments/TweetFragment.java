package com.huliang.oschn.improve.tweet.fragments;

import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.huliang.oschn.api.remote.OSChinaApi;
import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.base.fragments.BaseGeneralRecyclerFragment;
import com.huliang.oschn.improve.bean.Tweet;
import com.huliang.oschn.improve.bean.base.PageBean;
import com.huliang.oschn.improve.bean.base.ResultBean;
import com.huliang.oschn.improve.tweet.activities.TweetDetailActivity;
import com.huliang.oschn.improve.user.adapter.UserTweetAdapter;
import com.huliang.oschn.util.TLog;

import java.lang.reflect.Type;

/**
 * 动弹 recyclerView
 * <p>
 * Created by huliang on 17/3/19.
 */
public class TweetFragment extends BaseGeneralRecyclerFragment<Tweet> implements
        BaseRecyclerAdapter.OnItemLongClickListener {
    private static final String TAG = "TweetFragment";

    public int mReqCatalog; //请求类型

    public TweetFragment() {
    }

    public static final int CATALOG_NEW = 0X0001;
    public static final int CATALOG_HOT = 0X0002;
    public static final int CATALOG_MYSELF = 0X0003;
    public static final int CATALOG_FRIENDS = 0X0004;
    public static final int CATALOG_TAG = 0X0005;
    public static final int CATALOG_SOMEONE = 0X0006;

    public static final String BUNDLE_KEY_REQUEST_CATALOG = "BUNDLE_KEY_REQUEST_CATALOG";

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        mReqCatalog = bundle.getInt(BUNDLE_KEY_REQUEST_CATALOG, CATALOG_NEW);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter.setOnItemLongClickListener(this);
    }

    @Override
    protected void requestData() {
        super.requestData();

        String pageToken = isRefreshing ? null : mBean.getNextPageToken();
        switch (mReqCatalog) {
            case CATALOG_NEW:
                OSChinaApi.getTweetList(null, null, 1, 1, pageToken, mHandler);
                break;

            case CATALOG_HOT:
                OSChinaApi.getTweetList(null, null, 1, 2, pageToken, mHandler);
                break;
        }
    }

    @Override
    protected boolean isNeedEmptyView() {
        return mReqCatalog != CATALOG_TAG && mReqCatalog != CATALOG_SOMEONE;
    }

    @Override
    public void onItemClick(int position, long itemId) {
        Tweet tweet = mAdapter.getItem(position);
        if (tweet == null) {
            return;
        }
        TweetDetailActivity.show(getContext(), tweet);
    }

    @Override
    public void onItemLongClick(int position, long itemId) {
        TLog.log("=========");
    }

    @Override
    protected BaseRecyclerAdapter<Tweet> getRecyclerAdapter() {
        return new UserTweetAdapter(getContext());
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Tweet>>>() {
        }.getType();
    }
}

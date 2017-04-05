package com.huliang.oschn.improve.tweet.fragments;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.huliang.oschn.AppContext;
import com.huliang.oschn.api.remote.OSChinaApi;
import com.huliang.oschn.base.User;
import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.base.fragments.BaseRecyclerViewFragment;
import com.huliang.oschn.improve.bean.base.PageBean;
import com.huliang.oschn.improve.bean.base.ResultBean;
import com.huliang.oschn.improve.bean.simple.TweetLike;
import com.huliang.oschn.improve.tweet.adapter.TweetLikeUsersAdapter;
import com.huliang.oschn.improve.tweet.contract.TweetDetailContract;
import com.huliang.oschn.util.TLog;

import java.lang.reflect.Type;

/**
 * 动弹详情, 点赞列表 recyclerView
 * <p>
 * Created by huliang on 3/28/17.
 */
public class ListTweetLikeUsersFragment extends BaseRecyclerViewFragment<TweetLike>
        implements TweetDetailContract.IThumbupView {

    private TweetDetailContract.Operator mOperator;
    private TweetDetailContract.IAgencyView mAgencyView;

    public static ListTweetLikeUsersFragment instantiate(TweetDetailContract.Operator operator,
                                                         TweetDetailContract.IAgencyView mAgencyView) {
        ListTweetLikeUsersFragment fragment = new ListTweetLikeUsersFragment();
        fragment.mOperator = operator;

        // "赞+评论" tabLayout 区域回调, 传递点赞总数给 TweetDetailViewPagerFragment
        fragment.mAgencyView = mAgencyView;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mOperator = (TweetDetailContract.Operator) context;
    }

    @Override
    protected BaseRecyclerAdapter<TweetLike> getRecyclerAdapter() {
        return new TweetLikeUsersAdapter(getContext());
    }

    /**
     * 加载网络数据, 实际填充界面由 TweetLikeUsersAdapter 完成
     */
    @Override
    protected void requestData() {
        // 刷新则 token 为空, 继续加载则使用上个翻页的 pageToken
        String token = isRefreshing ? null : mBean.getNextPageToken();
        OSChinaApi.getTweetLikeList(mOperator.getTweetDetail().getId(), token, mHandler);
    }

    @Override
    protected void onRequestSuccess(int code) {
        super.onRequestSuccess(code);
        TLog.log("当前点赞数: " + mAdapter.getCount());
        if (mAdapter.getCount() < 20 && mAgencyView != null) {
            mAgencyView.resetLikeCount(mAdapter.getCount());
        }
    }

    /**
     * 指定 json 字符串转换成特定类型
     *
     * @return
     */
    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<TweetLike>>>() {
        }.getType();
    }

    @Override
    public void onLikeSuccess(boolean isUp, User user) {
        AppContext.showToastShort("onLikeSuccess");
    }
}

package com.huliang.oschn.improve.tweet.fragments;

import com.google.gson.reflect.TypeToken;
import com.huliang.oschn.AppContext;
import com.huliang.oschn.api.remote.OSChinaApi;
import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.base.fragments.BaseRecyclerViewFragment;
import com.huliang.oschn.improve.bean.base.PageBean;
import com.huliang.oschn.improve.bean.base.ResultBean;
import com.huliang.oschn.improve.bean.simple.TweetComment;
import com.huliang.oschn.improve.tweet.adapter.TweetCommentAdapter;
import com.huliang.oschn.improve.tweet.contract.TweetDetailContract;
import com.huliang.oschn.util.TLog;

import java.lang.reflect.Type;

/**
 * 动弹详情, 评论列表 recyclerView
 * <p/>
 * Created by huliang on 3/28/17.
 */
public class ListTweetCommentFragment extends BaseRecyclerViewFragment<TweetComment>
        implements TweetDetailContract.ICmnView {

    TweetDetailContract.Operator mOperator;
    private TweetDetailContract.IAgencyView mAgencyView;

    public static ListTweetCommentFragment instantiate(TweetDetailContract.Operator operator,
                                                       TweetDetailContract.IAgencyView mAgencyView) {
        ListTweetCommentFragment fragment = new ListTweetCommentFragment();
        fragment.mOperator = operator;

        // "赞+评论" tabLayout 区域回调, 传递点赞总数给 TweetDetailViewPagerFragment
        fragment.mAgencyView = mAgencyView;
        return fragment;
    }

    /**
     * 加载网络数据, 实际填充界面由 TweetCommentAdapter 完成
     */
    @Override
    protected void requestData() {
        // 刷新则 token 为空, 继续加载则使用上个页面的 pageToken
        String token = isRefreshing ? null : mBean.getNextPageToken();
        OSChinaApi.getTweetCommentList(mOperator.getTweetDetail().getId(), token, mHandler);
    }

    @Override
    protected void onRequestSuccess(int code) {
        super.onRequestSuccess(code);

        TLog.log("当前评论数: " + mAdapter.getCount());

        // 数据累积增长, 最多出现一次 <20 的情况 ???
        if (mAdapter.getCount() < 20 && mAgencyView != null) {
            mAgencyView.resetCmnCount(mAdapter.getCount());
        }
    }

    @Override
    protected BaseRecyclerAdapter<TweetComment> getRecyclerAdapter() {
        return new TweetCommentAdapter(getContext());
    }

    /**
     * 指定 json 字符串转换成特定类型
     *
     * @return
     */
    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<TweetComment>>>() {
        }.getType();
    }

    @Override
    public void onCommentSuccess(TweetComment comment) {
        AppContext.showToastShort("onCommentSuccess");
    }
}

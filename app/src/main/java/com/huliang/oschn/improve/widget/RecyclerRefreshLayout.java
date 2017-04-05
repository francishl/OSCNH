package com.huliang.oschn.improve.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 下拉刷新上拉加载控件，目前适用于 RecyclerView
 * <p/>
 * Created by huliang on 17/3/19.
 */
public class RecyclerRefreshLayout extends SwipeRefreshLayout implements
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "RecyclerRefreshLayout";

    private RecyclerView mRecycleView;

    private SuperRefreshLayoutListener listener;
    private boolean mIsOnLoading = false;
    private boolean mCanLoadMore = true;

    public RecyclerRefreshLayout(Context context) {
        this(context, null);
    }

    public RecyclerRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        // 如果未在加载中则开始加载,防止重复加载
        if (listener != null && !mIsOnLoading) {
            listener.onRefreshing();
        } else {
            setRefreshing(false);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mRecycleView == null) {
            getRecycleView();
        }
    }

    /**
     * 获取RecyclerView
     */
    private void getRecycleView() {
        if (getChildCount() > 0) {
            View childView = getChildAt(0);
            if (childView != null && childView instanceof RecyclerView) {
                mRecycleView = (RecyclerView) childView;
                mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (mCanLoadMore && canLoad()) {
                            loadData();
                        }
                    }
                });
            }
        }
    }

    /**
     * 加载结束记得调用
     */
    public void onComplete() {
        setRefreshing(false);
    }

    /**
     * 如果到了最底部,而且是上拉操作,那么执行onLoad方法
     */
    private void loadData() {
        if (listener != null) {
            listener.onLoadMore();
        }
    }

    /**
     * 是否可加载更多
     *
     * @param mCanLoadMore
     */
    public void setCanLoadMore(boolean mCanLoadMore) {
        this.mCanLoadMore = mCanLoadMore;
    }

    /**
     * 添加加载和刷新监听
     *
     * @param listener
     */
    public void setSuperRefreshLayoutListener(SuperRefreshLayoutListener listener) {
        this.listener = listener;
    }

    /**
     * 是否可以加载更多, 条件是到了最底部
     *
     * @return
     */
    private boolean canLoad() {
        return isScrollBottom() && !mIsOnLoading;
    }

    /**
     * 判断是否到了最底部
     *
     * @return
     */
    private boolean isScrollBottom() {
        return mRecycleView != null && mRecycleView.getAdapter() != null
                && getLastVisiblePosition() == mRecycleView.getAdapter().getItemCount() - 1;
    }

    /**
     * 获取RecyclerView可见的最后一项
     *
     * @return
     */
    public int getLastVisiblePosition() {
        int position;
        if (mRecycleView.getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else {
            position = mRecycleView.getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 传递加载和刷新动作给调用者
     */
    public interface SuperRefreshLayoutListener {
        void onRefreshing();

        void onLoadMore();
    }
}

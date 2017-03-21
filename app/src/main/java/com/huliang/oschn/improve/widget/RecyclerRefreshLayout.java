package com.huliang.oschn.improve.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by huliang on 17/3/19.
 */
public class RecyclerRefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "RecyclerRefreshLayout";

    private SuperRefreshLayoutListener listener;
    private boolean mIsOnLoading = false;

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

        }
    }

    /**
     * 加载结束记得调用
     */
    public void onComplete() {
        setRefreshing(false);
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
     * 传递加载和刷新动作给调用者
     */
    public interface SuperRefreshLayoutListener {
        void onRefreshing();

        void onLoadMore();
    }
}

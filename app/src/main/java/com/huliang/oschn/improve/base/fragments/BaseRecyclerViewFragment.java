package com.huliang.oschn.improve.base.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.app.AppOperator;
import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.bean.base.PageBean;
import com.huliang.oschn.improve.bean.base.ResultBean;
import com.huliang.oschn.improve.widget.RecyclerRefreshLayout;
import com.huliang.oschn.util.TLog;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * 基本列表类，重写getLayoutId()自定义界面
 * <p/>
 * Created by huliang on 17/3/19.
 */
public abstract class BaseRecyclerViewFragment<T> extends BaseFragment implements
        RecyclerRefreshLayout.SuperRefreshLayoutListener {
    private static final String TAG = "BaseRecyclerView";

    protected BaseRecyclerAdapter<T> mAdapter;
    protected RecyclerView mRecyclerView;
    protected RecyclerRefreshLayout mRefreshLayout;

    protected TextHttpResponseHandler mHandler;
    protected boolean isRefreshing;
    protected PageBean<T> mBean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        mRefreshLayout = (RecyclerRefreshLayout) root.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        super.initData();
        mBean = new PageBean<>();

        mAdapter = getRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRefreshLayout.setSuperRefreshLayoutListener(this); // 添加刷新加载监听
        mRefreshLayout.setColorSchemeResources(R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                TLog.log(responseString.substring(0, 500));
                TLog.log(responseString.substring(500, 1000));
                TLog.log(responseString.substring(1500, 2000));
                TLog.log(responseString.substring(responseString.length() - 500,
                        responseString.length()));
                try {
                    ResultBean<PageBean<T>> resultBean = AppOperator.createGson().fromJson(
                            responseString, getType());
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                        setListData(resultBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onRequestFinish();
            }
        };
    }

    @Override
    public void onRefreshing() {
        isRefreshing = true;
        requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    /**
     * 网络请求结束
     */
    protected void onRequestFinish() {
        onComplete();
    }

    /**
     * 传递状态给RecyclerRefreshLayout,停止刷新
     */
    protected void onComplete() {
        mRefreshLayout.onComplete();
        isRefreshing = false;
    }

    protected void setListData(ResultBean<PageBean<T>> resultBean) {
        mBean.setNextPageToken(resultBean.getResult().getNextPageToken());
        if (isRefreshing) {
            Log.i(TAG, "isRefreshing: " + isRefreshing);
            mAdapter.addAll(resultBean.getResult().getItems());
        } else {
            Log.i(TAG, "isRefreshing: " + isRefreshing);
            mAdapter.addAll(resultBean.getResult().getItems());
        }
    }

    /**
     * 开始获取网络数据
     */
    protected void requestData() {

    }

    /**
     * 由具体页面传入adapter以填充recycleView
     *
     * @return
     */
    protected abstract BaseRecyclerAdapter<T> getRecyclerAdapter();

    /**
     * 由具体页面传入类型 <T> 以解析json字符串
     *
     * @return
     */
    protected abstract Type getType();
}

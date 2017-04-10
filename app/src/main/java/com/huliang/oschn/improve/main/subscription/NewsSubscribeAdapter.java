package com.huliang.oschn.improve.main.subscription;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.huliang.oschn.improve.base.adapter.BaseRecyclerAdapter;
import com.huliang.oschn.improve.bean.SubscribeBean;

/**
 * 新闻订阅栏目
 * <p/>
 * Created by huliang on 4/10/17.
 */
public class NewsSubscribeAdapter extends BaseRecyclerAdapter<SubscribeBean> {

    public NewsSubscribeAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return null;
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, SubscribeBean item, int position) {

    }
}

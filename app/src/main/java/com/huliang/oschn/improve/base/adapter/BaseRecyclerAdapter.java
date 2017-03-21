package com.huliang.oschn.improve.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter 基类
 * <p>
 * Created by huliang on 17/3/20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mItems;
    protected Context mContext;

    public final int BEHAVIOR_MODE;

    public static final int NEITHER = 0;
    public static final int ONLY_HEADER = 1;
    public static final int ONLY_FOOTER = 2;
    public static final int BOTH_HEADER_FOOTER = 3;

    public BaseRecyclerAdapter(Context context, int mode) {
        mItems = new ArrayList<>();
        mContext = context;
        BEHAVIOR_MODE = mode;
    }

    /**
     * 根据item类别加载不同ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
                final RecyclerView.ViewHolder holder = onCreateDefaultViewHolder(parent, viewType);
                return holder;
        }
    }

    /**
     * 将数据与item视图进行绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            default:
                onBindDefaultViewHolder(holder, getItems().get(getIndex(position)), position);
                break;
        }
    }

    protected abstract RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type);

    protected abstract void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, T item, int position);

    public final List<T> getItems() {
        return mItems;
    }

    /**
     * 将position转换成真正index(由于header的存在index需要-1)
     *
     * @param position
     * @return
     */
    protected int getIndex(int position) {
        return BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER ?
                position - 1 : position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addAll(List<T> items) {
        if (items != null) {
            mItems.addAll(items);
            notifyItemRangeInserted(mItems.size(), items.size());
        }
    }

    public final void addItem(T item) {
        if (item != null) {
            mItems.add(item);
            // notifyItemChanged(int position): 在位置position上的item发生变化
            notifyItemChanged(mItems.size());
        }
    }
}

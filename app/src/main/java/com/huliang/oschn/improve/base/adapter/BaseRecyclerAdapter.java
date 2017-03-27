package com.huliang.oschn.improve.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huliang.oschn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter 基类
 * <p/>
 * Created by huliang on 17/3/20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    private static final String TAG = "BaseRecyclerAdapter";

    protected List<T> mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public final int BEHAVIOR_MODE;
    protected int mState;

    // 加载状态
    public static final int STATE_NO_MORE = 1;
    public static final int STATE_LOAD_MORE = 2;
    public static final int STATE_INVALID_NETWORK = 3;
    public static final int STATE_HIDE = 5;
    public static final int STATE_REFRESHING = 6;
    public static final int STATE_LOAD_ERROR = 7;
    public static final int STATE_LOADING = 8;

    // 头部OR尾部
    public static final int NEITHER = 0;
    public static final int ONLY_HEADER = 1;
    public static final int ONLY_FOOTER = 2;
    public static final int BOTH_HEADER_FOOTER = 3;

    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_HEADER = -1;
    public static final int VIEW_TYPE_FOOTER = -2;

    public BaseRecyclerAdapter(Context context, int mode) {
        mItems = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        BEHAVIOR_MODE = mode;
        mState = STATE_HIDE;
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
        Log.i(TAG, "adapter item的类型: " + viewType);
        switch (viewType) {
            case VIEW_TYPE_FOOTER:
                return new FooterViewHolder(mInflater.inflate(R.layout.recycler_footer_view, parent, false));
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
            case VIEW_TYPE_FOOTER:
                FooterViewHolder footHolder = (FooterViewHolder) holder;
                footHolder.itemView.setVisibility(View.VISIBLE);
                switch (mState) {
                    case STATE_LOADING:
                        footHolder.tv_footer.setText(mContext.getResources().getString(R.string.state_loading));
                        footHolder.pb_footer.setVisibility(View.VISIBLE);
                        break;
                    case STATE_HIDE:
                        footHolder.itemView.setVisibility(View.GONE);
                        break;
                }
                break;
            default:
                onBindDefaultViewHolder(holder, getItems().get(getIndex(position)), position);
                break;
        }
    }

    /**
     * 重写 getItemViewType() 方法，根据需求规则给出不同 position 的 type 值
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && (BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER)) {
            return VIEW_TYPE_HEADER;
        } else if (position == getItemCount() - 1 && (BEHAVIOR_MODE == ONLY_FOOTER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER)) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_NORMAL;
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
        // 如果有额外布局（如：头部尾部）需要修改getItemCount()方法的返回数量
        if (BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == ONLY_FOOTER) {
            return mItems.size() + 1;
        } else if (BEHAVIOR_MODE == BOTH_HEADER_FOOTER) {
            return mItems.size() + 2;
        } else {
            return mItems.size();
        }
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

    public final void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void setState(int state, boolean isUpdate) {
        mState = state;
        if (isUpdate) {
            updateItem(getItemCount() - 1);
        }
    }

    public int getState() {
        return mState;
    }

    public void updateItem(int position) {
        if (position < getItemCount()) {
            // 在位置position上的item发生变化
            notifyItemChanged(position);
        }
    }

    /**
     * Footer view
     */
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pb_footer;
        public TextView tv_footer;

        public FooterViewHolder(View itemView) {
            super(itemView);
            pb_footer = (ProgressBar) itemView.findViewById(R.id.pb_footer);
            tv_footer = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }
}

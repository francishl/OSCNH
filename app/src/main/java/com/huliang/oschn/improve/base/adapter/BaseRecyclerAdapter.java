package com.huliang.oschn.improve.base.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
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
 * <p>
 * Created by huliang on 17/3/20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    private static final String TAG = "BaseRecyclerAdapter";

    protected List<T> mItems;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public final int BEHAVIOR_MODE;
    protected int mState;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

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
        initListener();
    }

    /**
     * 初始化 listener, 监听点击和点长击事件,
     * 并传递给自定义 OnItemClickListener 和 OnItemLongClickListener
     */
    private void initListener() {
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(int position, long itemId) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, itemId);
                }
            }
        };

        onLongClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(int position, long itemId) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(position, itemId);
                    return true;
                }
                return false;
            }
        };
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
            case VIEW_TYPE_FOOTER:
                return new FooterViewHolder(mInflater.inflate(R.layout.recycler_footer_view, parent, false));
            default:
                final RecyclerView.ViewHolder holder = onCreateDefaultViewHolder(parent, viewType);
                if (holder != null) {
                    // 对 viewHolder 添加事件监听
                    holder.itemView.setTag(holder);
                    holder.itemView.setOnClickListener(onClickListener);
                    holder.itemView.setOnLongClickListener(onLongClickListener);
                }
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
                    case STATE_NO_MORE:
                        footHolder.tv_footer.setText(R.string.state_not_more);
                        footHolder.pb_footer.setVisibility(View.GONE);
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

    /**
     * 头部尾部之外的数据 count
     *
     * @return
     */
    public int getCount() {
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

    public final T getItem(int position) {
        int p = getIndex(position);
        if (p < 0 || p >= mItems.size()) {
            return null;
        }
        return mItems.get(p);
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

    public void updateItem(final int position) {
        if (position < getItemCount()) {
            // 在位置position上的item发生变化
//            notifyItemChanged(position);

            // 当recyclerView正在计算layout的时候，任何试图更新adapter内容的操作都会导致异常。
            // 而当view在layout正在计算过程中的一些回调，我们可以通过handler或者其他类似机制延后这些改变
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemChanged(position);
                }
            });
        }
    }

    Handler handler = new Handler();

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

    /**
     * 添加 item 点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 添加 item 点长击事件
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * recyclerAdapter item 点击监听
     */
    public interface OnItemClickListener {
        void onItemClick(int position, long itemId);
    }

    /**
     * recyclerAdapter item 长按监听
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(int position, long itemId);
    }

    /**
     * 子类来实现, 传递点击操作
     * 可以共用同一个listener，相对高效
     */
    public static abstract class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 获取点击 item
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            // 传递动作事件
            onClick(holder.getAdapterPosition(), holder.getItemId());
        }

        public abstract void onClick(int position, long itemId);
    }

    /**
     * 子类来实现, 传递点长击操作
     * 可以共用同一个listener，相对高效
     */
    public static abstract class OnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            // 获取点击 item
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            // 传递动作事件
            return onLongClick(holder.getAdapterPosition(), holder.getItemId());
        }

        public abstract boolean onLongClick(int position, long itemId);
    }
}

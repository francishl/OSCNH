package com.huliang.oschn.improve.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.bean.SubTab;
import com.huliang.oschn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态 tab 选择器
 * <p/>
 * 请通过{@link #setTabPickerManager(TabPickerDataManager)}来设置 active 数据和原始数据,数据对象根据
 * 需要实现{@link Object#hashCode()}和{@link Object#equals(Object)}方法,因为 inactive 数据是通过使用
 * {@link List#contains(Object)}方法从原始数据剔除 active 数据实现的。
 * <p/>
 * 活动动态栏目的添加、删除、移动、选择通过{@link OnTabPickingListener}来实现的，你可以通过方法
 * {@link #setOnTabPickingListener(OnTabPickingListener)}来监听。
 * <p/>
 * 通过{@link #show(int)}和{@link #hide()}方法来显示隐藏动态栏目选择器界面。
 * <p/>
 * 通过{@link #onTurnBack()}响应回退事件。
 * <p/>
 * Created by huliang on 4/6/17.
 */
public class TabPickerView extends FrameLayout {
    private TextView mViewOperator;
    private TextView mViewDone;
    private RecyclerView mRecyclerActive;
    private RecyclerView mRecyclerInactive;
    private LinearLayout mLayoutWrapper;
    private NestedScrollView mViewScroller;
    private RelativeLayout mLayoutTop;

    private Action1<ViewPropertyAnimator> mOnShowAnimator; // 显示监听器
    private Action1<ViewPropertyAnimator> mOnHideAnimator; // 隐藏监听器
    private TabAdapter<TabAdapter.ViewHolder> mActiveAdapter;
    private TabAdapter<TabAdapter.ViewHolder> mInactiveAdapter;
    private TabPickerDataManager mTabManager;
    private OnTabPickingListener mTabPickingListener;
    private int mSelectedIndex = 0;

    public TabPickerView(Context context) {
        this(context, null);
    }

    public TabPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWidgets();
    }

    /**
     * 设置 tabPicker 管理器, 并传递数据
     *
     * @param manager
     */
    public void setTabPickerManager(TabPickerDataManager manager) {
        if (manager == null) {
            return;
        }
        mTabManager = manager;
        initRecyclerView();
    }

    public TabPickerDataManager getTabPickerManager() {
        return mTabManager;
    }

    public void setOnTabPickingListener(OnTabPickingListener mTabPickingListener) {
        this.mTabPickingListener = mTabPickingListener;
    }

    /**
     * 填充数据到 recyclerView
     */
    private void initRecyclerView() {
        // 创建 active recyclerView 适配器
        mActiveAdapter = new TabAdapter<TabAdapter.ViewHolder>(mTabManager.getActiveDataSet()) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_tab_item, parent, false));
            }

            @Override
            public void onBindViewHolder(TabAdapter.ViewHolder holder, int position) {
                SubTab item = items.get(position);
                holder.mViewTab.setText(item.getName());

                if (item.isFixed()) {
                    holder.mViewTab.setActivated(false);
                } else {
                    holder.mViewTab.setActivated(true);
                }

                if (mSelectedIndex == position) {
                    holder.mViewTab.setSelected(true);
                } else {
                    holder.mViewTab.setSelected(false);
                }

                // 固定 tab 不能被删除
                if (isEditMode() && !item.isFixed()) {
                    holder.mViewDel.setVisibility(VISIBLE);
                } else {
                    holder.mViewDel.setVisibility(GONE);
                }
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        };

        // 1. 给适配器添加动作监听器
        mActiveAdapter.setOnClickItemListener(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                int tempIndex = mSelectedIndex;
                mSelectedIndex = position;
                mActiveAdapter.notifyItemChanged(tempIndex); // 取消上一个tab的选中状态
                mActiveAdapter.notifyItemChanged(mSelectedIndex); // 更新当前tab的选中状态
                hide();
            }
        });

        // 2. 给适配器添加动作监听器
        mActiveAdapter.setOnDeleteItemListener(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                TLog.log("将要删除第 " + position + " 个tab");
                SubTab tab = mActiveAdapter.getItem(position);
                if (tab == null || tab.isFixed()) {
                    return;
                }

                // 1. 从 active 适配器中移除
                tab = mActiveAdapter.removeItem(position);

                // 2. 添加到 inactive 适配器
                mInactiveAdapter.addItem(tab);

                // 3. 通知 dynamicTabFragment 进行回调处理

            }
        });

        mRecyclerActive.setAdapter(mActiveAdapter);
        mRecyclerActive.setLayoutManager(new GridLayoutManager(getContext(), 4)); // 必须指定 layoutManager

        // 创建 inactive recyclerView 适配器
        mInactiveAdapter = new TabAdapter<TabAdapter.ViewHolder>(mTabManager.getInactiveDataSet()) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_tab_item, parent, false));
            }

            @Override
            public void onBindViewHolder(TabAdapter.ViewHolder holder, int position) {
                holder.mViewTab.setText(items.get(position).getName());
            }

            @Override
            public int getItemCount() {
                return items.size();
            }
        };

        // 给适配器添加动作监听器
        mInactiveAdapter.setOnClickItemListener(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                TLog.log("将要添加第 " + position + " 个tab");
                if (position < 0 || position >= mInactiveAdapter.getItemCount()) {
                    return;
                }

                // 1. 从 inactive 适配器中移除
                SubTab tab = mInactiveAdapter.removeItem(position);

                // 2. 添加到 active 适配器
                mActiveAdapter.addItem(tab);

                // 3. 通知 dynamicTabFragment 进行回调处理
                if (mTabPickingListener != null) {
                    mTabPickingListener.onInsert(tab);
                }
            }
        });

        mRecyclerInactive.setAdapter(mInactiveAdapter);
        mRecyclerInactive.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }

    /**
     * 监听各种动作, 负责传递动作给监听者
     *
     * @param <T>
     */
    public interface Action1<T> {
        void call(T t);
    }

    private void initWidgets() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_tab_picker, this, false);

        mRecyclerActive = (RecyclerView) view.findViewById(R.id.view_recycler_active);
        mRecyclerInactive = (RecyclerView) view.findViewById(R.id.view_recycler_inactive);
        mLayoutTop = (RelativeLayout) view.findViewById(R.id.layout_top);
        mViewScroller = (NestedScrollView) view.findViewById(R.id.view_scroller);
        mLayoutWrapper = (LinearLayout) view.findViewById(R.id.layout_wrapper);
        mViewOperator = (TextView) view.findViewById(R.id.tv_operator);
        mViewDone = (TextView) view.findViewById(R.id.tv_done);

        mViewDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewDone.getText().toString().equals("排序删除")) {
                    mActiveAdapter.startEditMode();
                } else {
                    mActiveAdapter.cancelEditMode();
                }
            }
        });
        this.addView(view);
    }

    public void setOnShowAnimator(Action1<ViewPropertyAnimator> mOnShowAnimator) {
        this.mOnShowAnimator = mOnShowAnimator;
    }

    public void setOnHideAnimator(Action1<ViewPropertyAnimator> mOnHideAnimator) {
        this.mOnHideAnimator = mOnHideAnimator;
    }

    /**
     * 显示 tabPickerView
     *
     * @param selectedIndex
     */
    public void show(int selectedIndex) {
        TLog.log("当前选择tab: " + selectedIndex);

        int tempIndex = mSelectedIndex;
        mSelectedIndex = selectedIndex;
        mActiveAdapter.notifyItemChanged(tempIndex); // 取消上一个tab的选中状态
        mActiveAdapter.notifyItemChanged(mSelectedIndex); // 更新当前tab的选中状态

        // imageButton 动画
        if (mOnShowAnimator != null) {
            mOnShowAnimator.call(null);
        }
        this.setVisibility(VISIBLE);

        // 1. 透明度的起点
        mLayoutTop.setAlpha(0);
        // 2. 透明度的终点 alpha(1)
        mLayoutTop.animate()
                .alpha(1)
                .setDuration(380)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

        // 1. top顶部所在坐标, 位移的起点
        mViewScroller.setTranslationY(-mViewScroller.getHeight());
        // 2. 位移至坐标 Y=0
        mViewScroller.animate()
                .translationY(0)
                .setDuration(380)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
    }

    /**
     * 隐藏 tabPickerView
     */
    public void hide() {
        if (mTabPickingListener != null) {
            mTabPickingListener.onRestore(mTabManager.mActiveDataSet); // 持久化存储 active 的 tabs
            mTabPickingListener.onSelected(mSelectedIndex);
        }

        if (mOnHideAnimator != null) {
            mOnHideAnimator.call(null);
        }

        mLayoutTop.animate()
                .alpha(0)
                .setDuration(380)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setVisibility(GONE); // 动画结束时, 移除该视图
                    }
                });

        mViewScroller.animate()
                .translationY(-mViewScroller.getHeight())
                .setDuration(380);
    }

    /**
     * tabPicker 退出
     *
     * @return
     */
    public boolean onTurnBack() {
        // 1. 如果未编辑模式, 则退出编辑模式
        if (mActiveAdapter.isEditMode()) {
            mActiveAdapter.cancelEditMode();
            return true;
        }

        // 2. 如果已经展示, 则隐藏
        if (this.getVisibility() == VISIBLE) {
            hide();
            return true;
        }
        return false;
    }

    /**
     * 自定义 TabPicker_recyclerView adapter
     *
     * @param <VH>
     */
    private abstract class TabAdapter<VH extends RecyclerView.ViewHolder>
            extends RecyclerView.Adapter<VH> {

        private View.OnClickListener mClickTabItemListener;
        private View.OnClickListener mClickDeleteListener;
        private Action1<Integer> mSelectItemAction; // 传递选择tab操作给自定义回调函数
        private Action1<Integer> mDeleteItemAction; // 传递删除tab操作给自定义回调函数

        private boolean isEditMode = false;
        List<SubTab> items;

        TabAdapter(List<SubTab> items) {
            this.items = items;
        }

        public SubTab removeItem(int position) {
            SubTab tab = items.remove(position);
            notifyItemRemoved(position); // 刷新 recyclerView
            return tab;
        }

        public void addItem(SubTab tab) {
            items.add(tab);
            notifyItemInserted(items.size() - 1); // 添加 item 到队尾
        }

        public SubTab getItem(int position) {
            if (position < 0 || position >= items.size()) {
                return null;
            }
            return items.get(position);
        }

        /**
         * active 适配器开始编辑模式
         */
        void startEditMode() {
            mViewOperator.setText("拖动排序");
            mViewDone.setText("完成");
            mLayoutWrapper.setVisibility(GONE);

            isEditMode = true;
            notifyDataSetChanged(); // 刷新 recyclerView, 显示 del 按钮
        }

        /**
         * active 适配器退出编辑模式
         */
        void cancelEditMode() {
            mViewOperator.setText("切换栏目");
            mViewDone.setText("排序删除");
            mLayoutWrapper.setVisibility(VISIBLE);

            isEditMode = false;
            notifyDataSetChanged();
        }

        boolean isEditMode() {
            return isEditMode;
        }

        /**
         * 点击tab
         *
         * @return
         */
        public OnClickListener getClickTabItemListener() {
            if (mClickTabItemListener == null) {
                mClickTabItemListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TLog.log("点击tab " + ((TabAdapter.ViewHolder) v.getTag()).getAdapterPosition());
                        TabAdapter.ViewHolder holder = (TabAdapter.ViewHolder) v.getTag();
                        if (holder == null) { // tag 可能为空
                            return;
                        }
                        // 传递当前 tab 被点击动作给自定义回调函数
                        if (mSelectItemAction != null) {
                            mSelectItemAction.call(holder.getAdapterPosition());
                        }
                    }
                };
            }
            return mClickTabItemListener;
        }

        /**
         * 删除tab
         *
         * @return
         */
        public OnClickListener getDeleteItemListener() {
            if (mClickDeleteListener == null) {
                mClickDeleteListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TLog.log("删除tab " + ((TabAdapter.ViewHolder) v.getTag()).getAdapterPosition());
                        TabAdapter.ViewHolder holder = (TabAdapter.ViewHolder) v.getTag();
                        if (holder == null) {
                            return;
                        }
                        // 传递当前 tab 被点击动作给自定义回调函数
                        if (mDeleteItemAction != null) {
                            mDeleteItemAction.call(holder.getAdapterPosition());
                        }
                    }
                };
            }
            return mClickDeleteListener;
        }

        /**
         * 自定义回调函数, 监听 tab 点击操作
         *
         * @param listener
         */
        void setOnClickItemListener(Action1<Integer> listener) {
            this.mSelectItemAction = listener;
        }

        /**
         * 自定义回调函数, 监听 tab 删除操作
         *
         * @param listener
         */
        void setOnDeleteItemListener(Action1<Integer> listener) {
            this.mDeleteItemAction = listener;
        }

        /**
         * 订阅 tab viewHolder
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mViewTab;
            ImageView mViewDel;

            public ViewHolder(View view) {
                super(view);

                mViewTab = (TextView) view.findViewById(R.id.tv_content);
                mViewDel = (ImageView) view.findViewById(R.id.iv_delete);

                // 不同状态对应不同颜色
                // 状态前加负号, 表示状态为 false
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_activated},
                                new int[]{}},
                        new int[]{0XFF24CF5F, 0XFF6A6A6A});
                mViewTab.setTextColor(colorStateList);
                mViewTab.setActivated(true);

                mViewTab.setTag(this); // 将 viewHolder 作为此 tab 的 tag
                mViewDel.setTag(this);
                mViewTab.setOnClickListener(getClickTabItemListener());
                mViewDel.setOnClickListener(getDeleteItemListener());
            }
        }
    }

    /**
     * TabPicker 管理器, 负责处理数据和动作
     */
    public abstract static class TabPickerDataManager {
        public List<SubTab> mActiveDataSet;
        public List<SubTab> mInactiveDataSet;
        public List<SubTab> mOriginalDataSet;

        public TabPickerDataManager() {
            mActiveDataSet = setupActiveDataSet();
            mOriginalDataSet = setupOriginalDataSet();
            mInactiveDataSet = new ArrayList<>();

            // 1. 原始数据不能为空
            if (mOriginalDataSet == null || mOriginalDataSet.size() == 0) {
                throw new RuntimeException("Original Data Set can't be null or empty");
            }

            // 2. active 数据不存在, 则从原始数据中筛选
            if (mActiveDataSet == null) {
                mActiveDataSet = new ArrayList<>();
                for (SubTab tab : mOriginalDataSet) {
                    if (tab.isActived() || tab.isFixed()) {
                        mActiveDataSet.add(tab);
                    }
                }
            }

            // 3. 筛选之后的剩余数据则为 inactive 数据
            for (SubTab tab : mOriginalDataSet) {
                if (mActiveDataSet.contains(tab)) {
                    continue;
                }
                mInactiveDataSet.add(tab);
            }
        }

        public List<SubTab> getActiveDataSet() {
            return mActiveDataSet;
        }

        public List<SubTab> getInactiveDataSet() {
            return mInactiveDataSet;
        }

        public List<SubTab> getOriginalDataSet() {
            return mOriginalDataSet;
        }

        /**
         * 获取 active tabs 数据
         *
         * @return
         */
        protected abstract List<SubTab> setupActiveDataSet();

        /**
         * 获取原始 tabs 数据
         *
         * @return
         */
        protected abstract List<SubTab> setupOriginalDataSet();
    }

    /**
     * 监听 tab 增,删,移动等操作
     */
    public interface OnTabPickingListener {
        /**
         * 单击选择某个tab
         *
         * @param position
         */
        void onSelected(int position);

        /**
         * 删除某个tab
         *
         * @param position
         * @param tab
         */
        void onRemove(int position, SubTab tab);

        /**
         * 添加某个tab
         *
         * @param tab
         */
        void onInsert(SubTab tab);

        /**
         * 交换tab
         *
         * @param op
         * @param np
         */
        void onMove(int op, int np);

        /**
         * 持久化存储active的tabs
         *
         * @param activeTabs the actived tabs
         */
        void onRestore(List<SubTab> activeTabs);
    }
}

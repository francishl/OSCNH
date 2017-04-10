package com.huliang.oschn.improve.main.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.huliang.oschn.AppContext;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.app.AppOperator;
import com.huliang.oschn.improve.base.fragments.BaseTitleFragment;
import com.huliang.oschn.improve.bean.SubTab;
import com.huliang.oschn.improve.main.MainActivity;
import com.huliang.oschn.improve.main.subscription.SubscribeFragment;
import com.huliang.oschn.improve.widget.TabPickerView;
import com.huliang.oschn.util.TLog;

import net.oschina.common.utils.StreamUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 动态栏目Fragment
 * <p/>
 * Created by huliang on 17/3/17.
 */
public class DynamicTabFragment extends BaseTitleFragment {

    @Bind(R.id.layout_tab)
    TabLayout mLayoutTab;
    @Bind(R.id.iv_arrow_down)
    ImageView mViewArrowDown;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.view_tab_picker)
    TabPickerView mViewTabPicker;

    private static TabPickerView.TabPickerDataManager mTabPickerDataManager;
    private MainActivity activity;
    private android.support.v4.app.FragmentPagerAdapter mAdapter;
    List<SubTab> tabs;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_dynamic_tab;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        // activity 捕捉返回按钮动作, onTurnBack() 负责具体处理动作逻辑
        activity.addOnTurnBackListeners(new MainActivity.TurnBackListener() {
            @Override
            public boolean onTurnBack() {
                TLog.log("DynamicTabFragment 返回按钮");
                return mViewTabPicker != null && mViewTabPicker.onTurnBack();
            }
        });
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mViewTabPicker.setTabPickerManager(initTabPickerManager());
        TLog.log("tabs 个数: " + mViewTabPicker.getTabPickerManager().getOriginalDataSet().size());

        mViewTabPicker.setOnTabPickingListener(new TabPickerView.OnTabPickingListener() {
            private boolean isChangeIndex = false;

            @Override
            public void onSelected(final int position) {
                TLog.log("onSelected " + position);
                TLog.log("个数" + mLayoutTab.getTabCount());

                // tabLayout数目发生变化后TabLayout位置出现偏移, 需要延迟设置才能起效？？？
                // 可能原因: 界面刷新 loop
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLayoutTab.getTabAt(position).select();
//                    }
//                });

                mLayoutTab.getTabAt(position).select();
            }

            @Override
            public void onRemove(int position, SubTab tab) {
                isChangeIndex = true;
            }

            @Override
            public void onInsert(SubTab tab) {
                TLog.log("onInsert " + tab.getName());
                isChangeIndex = true;
            }

            @Override
            public void onMove(int op, int np) {
                isChangeIndex = true;
            }

            @Override
            public void onRestore(List<SubTab> activeTabs) {
                // 如果 tabs 未发生变化, 则不保存
                if (!isChangeIndex) {
                    return;
                }
                isChangeIndex = false;

                // 更新当前 tabLayout
                TLog.log("onRestore() 更新tabLayout, 当前个数: " + activeTabs.size());
                tabs.clear();
                tabs.addAll(activeTabs);

                // 通知更新 viewPager
                // 因为 tabLayout 已经与 viewPager 绑定, 因此 tabLayout 会同步自动更新
                mAdapter.notifyDataSetChanged();
            }
        });

        mViewTabPicker.setOnShowAnimator(new TabPickerView.Action1<ViewPropertyAnimator>() {
            @Override
            public void call(ViewPropertyAnimator viewPropertyAnimator) {
                // 动画开始设置 false, 动画结束设置 true, 防止一次动画之间重复点击, 同时可多次动画
                mViewArrowDown.setEnabled(false);
                mViewArrowDown.animate()
                        .rotation(225) // 180 + 45
                        .setDuration(380)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mViewArrowDown.setRotation(45);
                                mViewArrowDown.setEnabled(true);
                            }
                        }).start();
            }
        });

        mViewTabPicker.setOnHideAnimator(new TabPickerView.Action1<ViewPropertyAnimator>() {
            @Override
            public void call(ViewPropertyAnimator viewPropertyAnimator) {
                mViewArrowDown.setEnabled(false);
                mViewArrowDown.animate()
                        .rotation(-180)
                        .setDuration(380)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mViewArrowDown.setRotation(0);
                                mViewArrowDown.setEnabled(true);
                            }
                        }).start();
            }
        });

        tabs = new ArrayList<>();
        tabs.addAll(mViewTabPicker.getTabPickerManager().getActiveDataSet()); // 添加 active 标签数据
        for (SubTab tab : tabs) {
            mLayoutTab.addTab(mLayoutTab.newTab().setText(tab.getName())); // 创建 active tab
        }

        mViewPager.setAdapter(mAdapter = new android.support.v4.app.FragmentPagerAdapter(
                getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new SubscribeFragment();
            }

            @Override
            public int getCount() {
                return tabs.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs.get(position).getName();
            }
        });

        mLayoutTab.setupWithViewPager(mViewPager);
        mLayoutTab.setSmoothScrollingEnabled(true);
    }

    /**
     * 创建 tabPicker 管理器, 并从本地文件读取 tabs 数据
     *
     * @return
     */
    public static TabPickerView.TabPickerDataManager initTabPickerManager() {
        if (mTabPickerDataManager == null) {
            mTabPickerDataManager = new TabPickerView.TabPickerDataManager() {
                @Override
                protected List<SubTab> setupActiveDataSet() {
                    return null;
                }

                @Override
                protected List<SubTab> setupOriginalDataSet() {
                    InputStreamReader reader = null;
                    try {
                        reader = new InputStreamReader(AppContext.getInstance().getAssets().open(
                                "sub_tab_original.json"), "UTF-8");
                        return AppOperator.getGson().fromJson(reader,
                                new TypeToken<ArrayList<SubTab>>() {
                                }.getType());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        StreamUtil.close(reader);
                    }
                    return null;
                }
            };
        }
        return mTabPickerDataManager;
    }

    /**
     * 利用 rotation 角度, 模拟一个 toggle 按钮
     */
    @OnClick(R.id.iv_arrow_down)
    public void onClickArrow() {
        if (mViewArrowDown.getRotation() != 0) {
            mViewTabPicker.onTurnBack();
        } else {
            mViewTabPicker.show(mLayoutTab.getSelectedTabPosition());
        }
    }

    @Override
    protected int getTitleRes() {
        return R.string.main_tab_name_news;
    }

    @Override
    protected int getIconRes() {
        return R.mipmap.btn_search_normal;
    }

    @Override
    protected View.OnClickListener getIconClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "btn_search_normal", Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }
}

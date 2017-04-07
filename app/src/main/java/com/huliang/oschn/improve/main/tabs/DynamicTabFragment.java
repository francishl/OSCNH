package com.huliang.oschn.improve.main.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.TabLayout;
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
 * <p>
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

    List<SubTab> tabs;
    private static TabPickerView.TabPickerDataManager mTabPickerDataManager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_dynamic_tab;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mViewTabPicker.setTabPickerManager(initTabPickerManager());
        TLog.log("tabs 个数: " + mViewTabPicker.getTabPickerManager().getOriginalDataSet().size());

        mViewTabPicker.setTabPickingListener(new TabPickerView.OnTabPickingListener() {
            @Override
            public void onSelected(int position) {
                TLog.log("onSelected " + position);
                mLayoutTab.getTabAt(position).select();
            }

            @Override
            public void onRemove(int position, SubTab tab) {

            }

            @Override
            public void onInsert(SubTab tab) {
                TLog.log("onInsert " + tab.getName());

            }

            @Override
            public void onMove(int op, int np) {

            }

            @Override
            public void onRestore(List<SubTab> activeTabs) {
                // 更新当前 tabLayout
                tabs.clear();
                tabs.addAll(activeTabs);

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

        mLayoutTab.setupWithViewPager(mViewPager);
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

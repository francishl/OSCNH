package com.huliang.oschn.improve.main.nav;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.base.fragments.BaseFragment;
import com.huliang.oschn.improve.main.tabs.DynamicTabFragment;
import com.huliang.oschn.improve.main.tabs.ExploreFragment;
import com.huliang.oschn.improve.main.tabs.TweetViewPagerFragment;
import com.huliang.oschn.improve.user.fragments.UserInfoFragment;

import net.oschina.common.widget.drawable.shape.BorderShape;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huliang on 17/3/17.
 */
public class NavFragment extends BaseFragment {
    private static final String TAG = "NavFragment";

    @Bind(R.id.nav_item_news)
    NavigationButton mNavNews;
    @Bind(R.id.nav_item_tweet)
    NavigationButton mNavTweet;
    @Bind(R.id.nav_item_explore)
    NavigationButton mNavExplore;
    @Bind(R.id.nav_item_me)
    NavigationButton mNavMe;

    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnNavigationReselectListener mOnNavigationReselectListener;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 设置底部tab栏背景色和边框
        // RectF(0, 1, 0, 0) : paddingTop = 1
        ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(0, 1, 0, 0)));
        lineDrawable.getPaint().setColor(getResources().getColor(R.color.list_divider_color));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        root.setBackgroundDrawable(layerDrawable);

        // 初始化tabs
        mNavNews.init(R.drawable.tab_icon_new,
                R.string.main_tab_name_news,
                DynamicTabFragment.class);

        mNavTweet.init(R.drawable.tab_icon_tweet,
                R.string.main_tab_name_tweet,
                TweetViewPagerFragment.class);

        mNavExplore.init(R.drawable.tab_icon_explore,
                R.string.main_tab_name_explore,
                ExploreFragment.class);

        mNavMe.init(R.drawable.tab_icon_me,
                R.string.main_tab_name_my,
                UserInfoFragment.class);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    /**
     * 外部接口,初始化tab fragment
     *
     * @param context
     * @param fragmentManager
     * @param contentId
     * @param listener
     */
    public void setup(Context context, FragmentManager fragmentManager, int contentId,
                      OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavNews);
    }

    @OnClick({R.id.nav_item_news, R.id.nav_item_tweet, R.id.nav_item_tweet_pub,
            R.id.nav_item_explore, R.id.nav_item_me})
    public void onClick(View view) {
        if (view instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) view;
            doSelect(nav);
        } else if (view.getId() == R.id.nav_item_tweet_pub) {
            Log.i(TAG, "onClick: 发布新动态");
        }
    }

    /**
     * 清除当前activity中除navFragment之外的所有其他fragment
     */
    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0) {
            return;
        }

        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }

        // 实际发生移除操作,才提交事务
        if (doCommit) {
            transaction.commitNow();
        }
    }

    /**
     * 处理被选中的tab
     *
     * @param newNavButton
     */
    private void doSelect(NavigationButton newNavButton) {
        NavigationButton oldNavButton = null;

        // 如果tab不是首次点击
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            // 如果动作为重复点击(当前tab和上一次tab相同)
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            // 上一次tab取消选中状态
            oldNavButton.setSelected(false);
        }

        // 当前tab设置选中状态
        newNavButton.setSelected(true);
        mCurrentNavButton = newNavButton;
        doTabChanged(oldNavButton, newNavButton);
    }

    /**
     * tab重复点击
     *
     * @param navigationButton
     */
    private void onReselect(NavigationButton navigationButton) {
        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }

    /**
     * tab变化导致页面切换
     *
     * @param oldNavButton
     * @param newNavButton
     */
    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            // 解绑原button对应的fragment
            if (oldNavButton.getFragment() != null) {
                transaction.detach(oldNavButton.getFragment());
            }
        }

        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                // 如果新fragment为空,则创建
                Fragment fragment = Fragment.instantiate(mContext, newNavButton.getClx().getName(),
                        null);
                transaction.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                // 绑定新button对应的fragment
                transaction.attach(newNavButton.getFragment());
            }
        }

        transaction.commit();
    }

    /**
     * 监听tab重复点击
     */
    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);
    }
}

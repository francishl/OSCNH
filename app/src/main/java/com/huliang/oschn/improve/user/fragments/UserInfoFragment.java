package com.huliang.oschn.improve.user.fragments;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huliang.oschn.R;
import com.huliang.oschn.improve.account.AccountHelper;
import com.huliang.oschn.improve.base.fragments.BaseFragment;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户个人界面
 * <p/>
 * Created by huliang on 17/3/19.
 */
public class UserInfoFragment extends BaseFragment {

    @Bind(R.id.user_view_solar_system)
    View mSolarSystem;
    @Bind(R.id.iv_logo_setting)
    ImageView mIvLogoSetting;
    @Bind(R.id.iv_logo_zxing)
    ImageView mIvLogoZxing;
    @Bind(R.id.user_info_head_container)
    FrameLayout mFlUserInfoHeadContainer;
    @Bind(R.id.iv_portrait)
    CircleImageView mCirclePortrait;
    @Bind(R.id.iv_gender)
    ImageView mIvGender;
    @Bind(R.id.user_info_icon_container)
    FrameLayout mFlUserInfoIconContainer;
    @Bind(R.id.tv_nick)
    TextView mTvName;
    @Bind(R.id.tv_score)
    TextView mTvScore;
    @Bind(R.id.rl_show_my_info)
    LinearLayout mRlShowInfo;
    @Bind(R.id.about_line)
    View mAboutLine;
    @Bind(R.id.tv_tweet)
    TextView mTvTweetCount;
    @Bind(R.id.lay_about_info)
    LinearLayout mLayAboutCount;
    @Bind(R.id.tv_favorite)
    TextView mTvFavoriteCount;
    @Bind(R.id.tv_following)
    TextView mTvFollowCount;
    @Bind(R.id.tv_follower)
    TextView mTvFollowerCount;
    @Bind(R.id.user_info_notice_fans)
    TextView mFansView;
    @Bind(R.id.user_info_notice_message)
    TextView mMesView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_user_home;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    protected void initData() {
        super.initData();

        requestUserCache();
    }

    /**
     * 获取 user_info
     * 如果 isLogin 则读取本地缓存并请求网络数据; 否则显示未登录界面
     */
    private void requestUserCache() {
        if (AccountHelper.isLogin()) {

        } else {
            hideView();
        }
    }

    /**
     * 未登录状态界面
     */
    private void hideView() {
        mCirclePortrait.setImageResource(R.mipmap.widget_default_face);
        mTvName.setText(R.string.user_hint_login);
        mIvGender.setVisibility(View.INVISIBLE);
        mTvScore.setVisibility(View.INVISIBLE);
        mAboutLine.setVisibility(View.GONE);
        mLayAboutCount.setVisibility(View.GONE);
    }

}

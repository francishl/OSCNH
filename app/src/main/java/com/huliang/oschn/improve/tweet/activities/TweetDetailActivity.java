package com.huliang.oschn.improve.tweet.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.huliang.oschn.AppContext;
import com.huliang.oschn.R;
import com.huliang.oschn.api.remote.OSChinaApi;
import com.huliang.oschn.improve.app.AppOperator;
import com.huliang.oschn.improve.base.activities.BaseActivity;
import com.huliang.oschn.improve.bean.Tweet;
import com.huliang.oschn.improve.bean.base.ResultBean;
import com.huliang.oschn.improve.bean.simple.TweetComment;
import com.huliang.oschn.improve.tweet.contract.TweetDetailContract;
import com.huliang.oschn.improve.tweet.fragments.TweetDetailViewPagerFragment;
import com.huliang.oschn.util.TLog;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 动弹详情
 * <p>
 * Created by huliang on 3/28/17.
 */
public class TweetDetailActivity extends BaseActivity implements TweetDetailContract.Operator {
    public static final String BUNDLE_KEY_TWEET = "BUNDLE_KEY_TWEET";

    @Bind(R.id.iv_portrait)
    CircleImageView ivPortrait;
    @Bind(R.id.tv_nick)
    TextView tvNick;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.fragment_container)
    FrameLayout mFrameLayout;
    @Bind(R.id.layout_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private Tweet tweet;

    public static void show(Context context, Tweet tweet) {
        Intent intent = new Intent(context, TweetDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_TWEET, tweet);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tweet_detail;
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        tweet = (Tweet) getIntent().getSerializableExtra(BUNDLE_KEY_TWEET);
        if (tweet == null) {
            AppContext.showToastShort("tweet对象没找到");
            return false;
        }
        return super.initBundle(bundle);
    }

    @Override
    protected void initWidget() {
        mToolbar.setTitle("动弹详情");

        // 使用ToolBar控件替代ActionBar控件，需要使用setSupportActionBar()方法
        setSupportActionBar(mToolbar);

        // 点击navigation button操作, 必须先设置navigation icon
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish 之前有一个退出过渡
                supportFinishAfterTransition();
            }
        });

        // 使用传递过来的tweet填充数据
        setupDetailView();

        TweetDetailViewPagerFragment mPagerFrag = TweetDetailViewPagerFragment.instantiate();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mPagerFrag) // fragment_container填充fragment
                .commit();
    }

    @Override
    protected void initData() {
        // 获取动弹详情
        OSChinaApi.getTweetDetail(tweet.getId(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AppContext.showToastShort("获取数据失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                TLog.log(responseString);
                ResultBean<Tweet> resultBean = AppOperator.createGson().fromJson(responseString,
                        new TypeToken<ResultBean<Tweet>>() {
                        }.getType());
                if (resultBean.isSuccess()) {
                    if (resultBean.getResult() == null) {
                        AppContext.showToastShort(getString(R.string.tweet_detail_data_null));
                        finish(); // 动态详情数据为空,则退出
                        return;
                    }

                    tweet = resultBean.getResult();
                    setupDetailView();
                } else {
                    onFailure(500, headers, "妈的智障", null);
                }
            }
        });
    }

    /**
     * 填充数据
     */
    private void setupDetailView() {
        // 有可能传入的tweet只有id这一个值
        if (tweet == null) {
            return;
        }

        if (tweet.getAuthor() != null) {
            if (TextUtils.isEmpty(tweet.getAuthor().getPortrait())) {
                ivPortrait.setImageResource(R.mipmap.widget_default_face);
            } else {
                getImageLoader()
                        .load(tweet.getAuthor().getPortrait())
                        .asBitmap()
                        .placeholder(R.mipmap.widget_default_face)
                        .error(R.mipmap.widget_default_face)
                        .into(ivPortrait);
            }

            tvNick.setText(tweet.getAuthor().getName());
        }

        if (!TextUtils.isEmpty(tweet.getContent())) {
            tvContent.setText(tweet.getContent());
        }
    }

    @Override
    public Tweet getTweetDetail() {
        return tweet;
    }

    @Override
    public void toReply(TweetComment comment) {

    }

    @Override
    public void onScroll() {

    }
}

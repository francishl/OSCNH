package com.huliang.oschn.improve.account.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huliang.oschn.AppContext;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.account.base.AccountBaseActivity;
import com.huliang.oschn.open.constants.OpenConstant;
import com.huliang.oschn.open.factory.OpenBuilder;
import com.huliang.oschn.util.TDevice;
import com.huliang.oschn.util.TLog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huliang on 4/9/17.
 */
public class LoginActivity extends AccountBaseActivity implements
        View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener,
        IUiListener {

    private static final String TAG = "LoginActivity";

    @Bind(R.id.ly_retrieve_bar)
    LinearLayout mLayBackBar;
    @Bind(R.id.iv_login_logo)
    ImageView mIvLoginLogo;
    @Bind(R.id.et_login_username)
    EditText mEtLoginUsername;
    @Bind(R.id.iv_login_username_del)
    ImageView mIvLoginUsernameDel;
    @Bind(R.id.ll_login_username)
    LinearLayout mLlLoginUsername;
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    @Bind(R.id.iv_login_pwd_del)
    ImageView mIvLoginPwdDel;
    @Bind(R.id.ll_login_pwd)
    LinearLayout mLlLoginPwd;
    @Bind(R.id.iv_login_hold_pwd)
    ImageView mIvHoldPwd;
    @Bind(R.id.tv_login_forget_pwd)
    TextView mTvLoginForgetPwd;
    @Bind(R.id.bt_login_submit)
    Button mBtLoginSubmit;
    @Bind(R.id.bt_login_register)
    Button mBtLoginRegister;
    @Bind(R.id.ll_login_layer)
    View mLlLoginLayer;
    @Bind(R.id.ib_login_weibo)
    ImageView mIbLoginWeiBo;
    @Bind(R.id.ib_login_wx)
    ImageView mIbLoginWx;
    @Bind(R.id.ib_login_qq)
    ImageView mImLoginQq;
    @Bind(R.id.ll_login_options)
    LinearLayout mLlLoginOptions;
    @Bind(R.id.ll_login_pull)
    LinearLayout mLlLoginPull;
    @Bind(R.id.webView)
    WebView mWVloginOSC;

    private int openType;

    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_login;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mLlLoginLayer.setVisibility(View.GONE);

        mEtLoginUsername.setOnFocusChangeListener(this);
        mEtLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String username = s.toString().trim();

                // 1. username 更新输入框背景和删除按钮
                if (username.length() > 0) {
                    mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvLoginUsernameDel.setVisibility(View.VISIBLE);
                } else {
                    mLlLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvLoginUsernameDel.setVisibility(View.INVISIBLE);
                }

                // 2. 如果用户名和密码都不为空, 则激活"登录"按钮
                String pwd = mEtLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        mEtLoginPwd.setOnFocusChangeListener(this);
        mEtLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int pwdLength = s.toString().trim().length();

                // 1. pwd 更新输入框背景和删除按钮
                if (pwdLength > 0) {
                    mLlLoginPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvLoginPwdDel.setVisibility(View.VISIBLE);
                } else {
                    mLlLoginPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    mIvLoginPwdDel.setVisibility(View.INVISIBLE);
                }

                // 2. 如果用户名为空, 则提示为空
                String username = mEtLoginUsername.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    AppContext.showToastShort(R.string.message_username_null);
                }

                // 3. 如果用户名和密码都不为空, 则激活"登录"按钮
                String pwd = mEtLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        TextView label = (TextView) mLayBackBar.findViewById(R.id.tv_navigation_label);
        label.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();

        // 初始化控件数据, 例如: username输入框内容自动填充为"记住账号"

    }

    @Override
    protected void onResume() {
        super.onResume();
        // getViewTreeObserver() 获取控件的 ViewTreeObserver
        mLayBackBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLayBackBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @OnClick({R.id.ib_navigation_back, R.id.et_login_username, R.id.iv_login_username_del,
            R.id.et_login_pwd, R.id.iv_login_pwd_del, R.id.iv_login_hold_pwd,
            R.id.tv_login_forget_pwd, R.id.bt_login_submit, R.id.bt_login_register,
            R.id.lay_login_container, R.id.ll_login_layer,
            R.id.ib_login_weibo, R.id.ib_login_wx, R.id.ib_login_qq, R.id.ll_login_pull})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;

            case R.id.et_login_username:
                mEtLoginPwd.clearFocus();
                mEtLoginUsername.setFocusableInTouchMode(true);
                mEtLoginUsername.requestFocus();
                break;

            case R.id.iv_login_username_del:
                mEtLoginUsername.setText(null);
                break;

            case R.id.et_login_pwd:
                mEtLoginUsername.clearFocus();
                mEtLoginPwd.setFocusableInTouchMode(true);
                mEtLoginPwd.requestFocus();
                break;

            case R.id.iv_login_pwd_del:
                mEtLoginPwd.setText(null);
                break;

            case R.id.iv_login_hold_pwd:
                break;
            case R.id.tv_login_forget_pwd:
                break;
            case R.id.bt_login_submit:
                // 用户登录
                loginRequest();
                break;

            case R.id.bt_login_register:
                break;

            case R.id.lay_login_container:
                // 点击屏幕空闲区, 隐藏键盘
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;

            case R.id.ib_login_weibo:
                break;
            case R.id.ib_login_wx:
                // 微信登录
                weChatLogin();
                break;

            case R.id.ib_login_qq:
                // QQ登录
                tencentLogin();
                break;

            case R.id.ll_login_layer:
            case R.id.ll_login_pull:
                // 1. 结束当前动画
                mLlLoginLayer.animate().cancel();
                mLlLoginPull.animate().cancel();

                // 2. 设置新动画参数
                int height = mLlLoginOptions.getHeight();
                // 上一次动画的进度, 从而根据进度来控制动画时间
                float progress = (mLlLoginLayer.getTag() != null && mLlLoginLayer.getTag() instanceof Float) ?
                        (float) mLlLoginLayer.getTag() : 1;
                int time = (int) (360 * progress);

                // 通过 tag 设置模拟 toggle 按钮
                if (mLlLoginPull.getTag() != null) {
                    mLlLoginPull.setTag(null);
                    slideDown(height, progress, time);
                } else {
                    mLlLoginPull.setTag(true);
                    slideUp(height, progress, time);
                }
                break;

            default:
                break;
        }
    }

    /**
     * QQ 登录
     */
    private void tencentLogin() {
        openType = OpenConstant.TENCENT;
        OpenBuilder.with(this)
                .useTencent(OpenConstant.QQ_APP_ID)
                .login(this, new OpenBuilder.Callback() {
                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onSuccess() {

                    }
                });

    }

    /**
     * 微信登录
     */
    private void weChatLogin() {
        openType = OpenConstant.WECHAT;
        OpenBuilder.with(this)
                .useWeChat(OpenConstant.WECHAT_APP_ID)
                .login(new OpenBuilder.Callback() {
                    @Override
                    public void onFailed() {
                        TLog.log("-----微信授权失败");
                    }

                    @Override
                    public void onSuccess() {
                        TLog.log("-----微信授权成功");

                    }
                });
    }

    /**
     * 用户登录准备
     */
    private void loginRequest() {
        String tempUsername = mEtLoginUsername.getText().toString().trim();
        String tempPwd = mEtLoginPwd.getText().toString().trim();

        if (!TextUtils.isEmpty(tempUsername) && !TextUtils.isEmpty(tempPwd)) {
            if (TDevice.hasInternet()) {
                requestLogin(tempUsername, tempPwd);
            } else {
                AppContext.showToastShort(R.string.footer_type_net_error);
            }
        } else {
            AppContext.showToastShort(R.string.login_input_username_hint_error);
        }
    }

    /**
     * 网络请求: 用户登录
     *
     * @param tempUsername
     * @param tempPwd
     */
    private void requestLogin(String tempUsername, String tempPwd) {
        String url = String.format("https://www.oschina.net/action/oauth2/authorize?" +
                        "response_type=code&client_id=%s&redirect_uri=%s",
                OpenConstant.OSC_APP_ID, OpenConstant.REDIRECT_URL);
        mWVloginOSC.setVisibility(View.VISIBLE);
        mWVloginOSC.loadUrl(url);

//        OSChinaApi.loginAuthorize(OpenConstant.OSC_APP_ID, "code", OpenConstant.OSC_APP_REDIRECT_URL,
//                "xyz", new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                        TLog.log(responseString);
//                    }
//                });


//        OSChinaApi.login(tempUsername, getSha1(tempPwd), new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                TLog.log(TAG, responseString);
//                try {
//                    Type type = new TypeToken<ResultBean<User>>() {
//                    }.getType();
//                    ResultBean<User> resultBean = AppOperator.createGson().fromJson(responseString, type);
//                    if (resultBean.isSuccess()) {
//                        AppContext.showToastShort(R.string.login_success_hint);
//                    } else {
//                        AppContext.showToastShort(resultBean.getMessage());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    onFailure(statusCode, headers, responseString, e);
//                }
//            }
//        });
    }

    /**
     * 登录面板向上滑出
     */
    private void slideUp(int height, float progress, int time) {
        mLlLoginPull.animate()
                .translationYBy(height * progress)
                .translationY(0)
                .setDuration(time);

        mLlLoginLayer.animate()
                .alphaBy(1 - progress)
                .alpha(1)
                .setDuration(time)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mLlLoginLayer.setVisibility(View.VISIBLE); // 显示图层遮盖界面
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 登录面板向上滑出动画完成
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }
                });
    }

    /**
     * 登录面板向下隐藏
     */
    private void slideDown(int height, float progress, int time) {
        mLlLoginPull.animate()
                .translationYBy(height - height * progress) // 当前进度百分比 progress
                .translationY(height)
                .setDuration(time);

        mLlLoginLayer.animate()
                .alphaBy(1 * progress)
                .alpha(0)
                .setDuration(time)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                        mLlLoginLayer.setVisibility(View.GONE); // 隐藏图层显示界面
                    }
                });
    }

    /**
     * 监控指定控件焦点变化情况
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.et_login_username) {
            if (hasFocus) {
                mLlLoginUsername.setActivated(true);
                mLlLoginPwd.setActivated(false);
            }
        } else if (id == R.id.et_login_pwd) {
            if (hasFocus) {
                mLlLoginUsername.setActivated(false);
                mLlLoginPwd.setActivated(true);
            }
        }
    }

    /**
     * Callback method to be invoked when the global layout state or the visibility of views
     * within the view tree changes
     * <p/>
     * 监控控件发生变化 (键盘弹出, 登录面板滑出等)
     */
    @Override
    public void onGlobalLayout() {
        final ImageView ivLogo = mIvLoginLogo;

    }

    /**
     * Tencent callback
     */
    @Override
    public void onComplete(Object o) {

    }

    /**
     * Tencent callback
     */
    @Override
    public void onError(UiError uiError) {

    }

    /**
     * Tencent callback
     */
    @Override
    public void onCancel() {

    }
}

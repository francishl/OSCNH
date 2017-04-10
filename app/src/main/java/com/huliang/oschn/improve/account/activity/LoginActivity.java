package com.huliang.oschn.improve.account.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huliang.oschn.AppContext;
import com.huliang.oschn.R;
import com.huliang.oschn.improve.account.base.AccountBaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huliang on 4/9/17.
 */
public class LoginActivity extends AccountBaseActivity implements View.OnFocusChangeListener {

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

    }

    @Override
    protected void initData() {
        super.initData();

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
                if (username.length() > 0) {
                    mIvLoginUsernameDel.setVisibility(View.VISIBLE);
                } else {
                    mIvLoginUsernameDel.setVisibility(View.INVISIBLE);
                }

                // 如果用户名和密码都不为空, 则激活"登录"按钮
                String pwd = mEtLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    mBtLoginSubmit.setPressed(true);
                } else {
                    mBtLoginSubmit.setPressed(false);
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
                String pwd = s.toString().trim();
                if (pwd.length() > 0) {
                    mIvLoginPwdDel.setVisibility(View.VISIBLE);
                } else {
                    mIvLoginPwdDel.setVisibility(View.INVISIBLE);
                }

                // 如果用户名为空, 则提示为空
                String username = mEtLoginUsername.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    AppContext.showToastShort(R.string.message_username_null);
                }
            }
        });

        TextView label = (TextView) mLayBackBar.findViewById(R.id.tv_navigation_label);
        label.setVisibility(View.INVISIBLE);
    }

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
                break;
            case R.id.iv_login_username_del:
                mEtLoginUsername.setText(null);
                break;
            case R.id.et_login_pwd:
                break;
            case R.id.iv_login_pwd_del:
                mEtLoginPwd.setText(null);
                break;
            case R.id.iv_login_hold_pwd:
                break;
            case R.id.tv_login_forget_pwd:
                break;
            case R.id.bt_login_submit:
                break;
            case R.id.bt_login_register:
                break;
            case R.id.lay_login_container:
                break;
            case R.id.ll_login_layer:
                break;
            case R.id.ib_login_weibo:
                break;
            case R.id.ib_login_wx:
                break;
            case R.id.ib_login_qq:
                break;
            case R.id.ll_login_pull:
                break;
        }
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
}

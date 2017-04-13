package com.huliang.oschn.improve.account.base;

import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import com.huliang.oschn.improve.base.activities.BaseActivity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by huliang on 4/9/17.
 */
public class AccountBaseActivity extends BaseActivity {

    private InputMethodManager mInputMethodManager;

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initData() {
        super.initData();

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    /**
     * 隐藏键盘
     */
    protected void hideKeyBoard(IBinder windowToken) {
        InputMethodManager inputMethodManager = mInputMethodManager;
        if (inputMethodManager == null) {
            return;
        }
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
    }

    /**
     * SHA1-HEX
     *
     * @param tempPwd
     * @return
     */
    protected String getSha1(String tempPwd) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(tempPwd.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();

            StringBuilder tempHex = new StringBuilder();
            // 字节数组转换为十六进制数
            for (byte aByte : bytes) {
                String shaHex = Integer.toHexString(aByte & 0xff);
                if (shaHex.length() < 2) {
                    tempHex.append(0);
                }
                tempHex.append(shaHex);
            }
            return tempHex.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tempPwd;
    }
}

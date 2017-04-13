package com.huliang.oschn.open.factory;

import android.app.Activity;
import android.util.Log;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * Created by huliang on 4/12/17.
 */
public class OpenBuilder {
    private static final String TAG = "OpenBuilder";

    private Activity activity;

    public static OpenBuilder with(Activity activity) {
        OpenBuilder builder = new OpenBuilder();
        builder.activity = activity;
        return builder;
    }

    public WeChatOperator useWeChat(String appId) {
        return new WeChatOperator(appId);
    }

    public TencentOperator useTencent(String appId) {
        Tencent tencent = Tencent.createInstance(appId, activity);
        return new TencentOperator(tencent);
    }

    /**
     * QQ
     */
    public class TencentOperator {
        Tencent tencent;

        TencentOperator(Tencent tencent) {
            this.tencent = tencent;
        }

        public int login(IUiListener listener, Callback callback) {
            return tencent.login(activity, "all", listener);
        }
    }

    /**
     * 微信
     */
    public class WeChatOperator {
        String appId;

        WeChatOperator(String appId) {
            this.appId = appId;
        }

        public void login(Callback callback) {
            Log.i(TAG, "-------开始微信登录-------");
            IWXAPI iwxapi = init();

            if (iwxapi == null) {
                if (callback != null) {
                    callback.onFailed(); // 创建失败
                }
                return;
            }

            // 唤起微信登录授权
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_login";
            if (!iwxapi.sendReq(req)) {
                if (callback != null) {
                    callback.onFailed(); // 失败回调
                }
            } else {
                if (callback != null) {
                    callback.onSuccess(); // 成功回调
                }
            }
        }

        /**
         * 创建 IWXAPI
         *
         * @return
         */
        private IWXAPI init() {
            IWXAPI iwxapi = WXAPIFactory.createWXAPI(activity, appId, false);
            if (iwxapi.isWXAppInstalled() && iwxapi.isWXAppSupportAPI() && iwxapi.registerApp(appId)) {
                return iwxapi;
            }
            return null;
        }
    }

    public interface Callback {
        void onFailed();

        void onSuccess();
    }
}

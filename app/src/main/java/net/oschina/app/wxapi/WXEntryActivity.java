package net.oschina.app.wxapi;

import android.app.Activity;

import com.huliang.oschn.util.TLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by huliang on 4/12/17.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        TLog.log("=========== ok ===========");

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                break;

            default:
                break;
        }
    }
}

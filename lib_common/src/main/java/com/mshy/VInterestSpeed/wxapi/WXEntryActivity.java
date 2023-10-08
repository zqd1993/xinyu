package com.mshy.VInterestSpeed.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.live.vquonline.base.utils.UtilsKt;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.mshy.VInterestSpeed.common.bean.PayResultEvent;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx61b5a6be779642f4");
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            if (!api.handleIntent(getIntent(), this)) {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            Log.e("WXEntryActivity", "extraData=" + extraData + " code=" + resp.errCode);
        }

        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            switch (resp.getType()) {
                case ConstantsAPI.COMMAND_SENDAUTH:
                    String code = ((SendAuth.Resp) resp).code;
//                    RxBus.getDefault().post(new CommonEvent(Constants.NOTIFY_WX_LOGIN_SUCCESS, code));
                    UtilsKt.toast("支付成功", Toast.LENGTH_SHORT);
                    EventBus.getDefault().post(new PayResultEvent());
                    break;
                case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                    //微信分享回调
                    UtilsKt.toast("分享成功", Toast.LENGTH_SHORT);
                    break;

                case ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM:
                    WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
                    String extraData = launchMiniProResp.extMsg;
                    if (TextUtils.equals(extraData, "paySuccess") || TextUtils.equals(extraData, "0000")) {
                        UtilsKt.toast("支付成功", Toast.LENGTH_SHORT);
//                        RxBus.getDefault().post(new CommonEvent(Constants.NOTIFY_UPDATE_USER_INFO_SUCCESS));
                        EventBus.getDefault().post(new PayResultEvent());
                    } else {
                        UtilsKt.toast("支付失败", Toast.LENGTH_SHORT);
                    }
                    break;
            }
        } else {
            switch (resp.getType()) {
                case ConstantsAPI.COMMAND_SENDAUTH:
                    break;
                case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                    UtilsKt.toast("分享失败", Toast.LENGTH_SHORT);
                    //微信分享回调
                    break;
            }
        }
        finish();
    }

}
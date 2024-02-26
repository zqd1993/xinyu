package com.sand.cashier;

import android.content.Context;
import android.util.Log;

import com.sand.qzf.paytypesdk.base.ErrorEnum;
import com.sand.qzf.paytypesdk.base.PayTypeSdk;
import com.sand.sandbao.spsdock.ISpsListener;
import com.sand.sandbao.spsdock.SpsDock;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

//不能被混淆，类名和方法名不能修改，方法实现可以修改。里面的方法由SDK调用，app不需要调用。
//如果不接杉德宝支付，可以删除startSandBaoPay方法以及SandbaoSps.aar
public class StartPay {

    //微信支付 支付结果在WXEntryActivity的onResp返回
    public void startWxPay(Context context, String payExtra) throws JSONException{
        JSONObject payExtraObj = new JSONObject(payExtra);
        String subAppId = payExtraObj.getString("subAppId"); //移动应用AppId
        String ghOriId = payExtraObj.getString("ghOriId"); //小程序原始id
        String pathUrl = payExtraObj.getString("pathUrl");
        String miniProgramType = payExtraObj.getString("miniProgramType");
        String tokenId = payExtraObj.getString("tokenId");
        IWXAPI api = WXAPIFactory.createWXAPI(context, subAppId);
        api.registerApp(subAppId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = ghOriId;
        req.path = pathUrl + "token_id=" + tokenId + "&funcCode=02010005";
        req.miniprogramType = Integer.parseInt(miniProgramType);// 正式版:0、开发版:1、体验版:2
        api.sendReq(req);
    }

    //银联支付 支付结果在Activity的onActivityResult返回
    public void startUnionPay(Context context, String tn) {
        UPPayAssistEx.startPay(context, null, null, tn, "00");
    }

    //杉德宝支付
    public void startSandBaoPay(Context context, String tn, final PayTypeSdk.OnPayCallBack callBack) {
        SpsDock spsDock = new SpsDock(context);
        spsDock.callSps(tn, new ISpsListener() {
            @Override
            public void spsReturn(String tn, String state) {
                Log.i(">", "spsReturn:" + tn + "," + state);
                /*
                 * 支付控件返回字符串:
                 * 0000、0001、0002 分别代表支付成功，支付取消，支付失败
                 */
                if (state != null) {
                    String message = "支付异常";
                    if ("0000".equals(state)) {
                        message = "支付成功";
                    } else if ("0001".equals(state)) {
                        message = "用户取消支付";
                    } else if ("0002".equals(state)) {
                        message = "支付流程无法进行";
                    }
                    callBack.onCallBack(state, message);
                }
            }
        });
    }
}
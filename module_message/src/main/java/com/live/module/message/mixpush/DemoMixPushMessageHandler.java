package com.live.module.message.mixpush;

import android.content.Context;


import com.mshy.VInterestSpeed.common.constant.RouteUrl;
import com.mshy.VInterestSpeed.common.helper.ARouterHelperKt;
import com.netease.nimlib.sdk.mixpush.MixPushMessageHandler;

import java.util.Map;

/**
 * Created by hzchenkang on 2016/11/10.
 */

public class DemoMixPushMessageHandler implements MixPushMessageHandler {


    public static final String PAYLOAD_SESSION_ID = "sessionID";
    public static final String PAYLOAD_SESSION_TYPE = "sessionType";

    // 对于华为推送，这个方法并不能保证一定会回调
    @Override
    public boolean onNotificationClicked(Context context, Map<String, String> payload) {
        ARouterHelperKt.startARouterActivity(RouteUrl.Main.CommonVquMainAcitvity);
        return true;
    }



    // 将音视频通知 Notification 缓存，清除所有通知后再次弹出 Notification，避免清除之后找不到打开正在进行音视频通话界面的入口
    @Override
    public boolean cleanMixPushNotifications(int pushType) {
        return true;
    }
}
package com.live.module.agora;

import android.content.Context;
import android.util.Log;

import com.live.vquonline.base.BaseApplication;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

/**
 * author: Lau
 * date: 2022/7/19
 * description:
 */
public class RtcEngineManager {

    private static RtcEngine mRtcEngine;
    private static RtcEngineEventHandlerProxy mRtcEventHandler;

    public static void init(Context context) {
        if (mRtcEngine == null) {
            mRtcEventHandler = new RtcEngineEventHandlerProxy();

            try {
                mRtcEngine = RtcEngine.create(context, BaseApplication.context.getString(R.string.agora_app_id), mRtcEventHandler);
                mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
//                mRtcEngine.enableDualStreamMode(true);
//                mRtcEngine.enableWebSdkInteroperability(true);
            } catch (Exception e) {
                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }
        }
    }

    public static RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    public static void addRtcHandler(RtcEngineEventHandler handler) {
        mRtcEventHandler.addEventHandler(handler);
    }

    public static void removeRtcHandler(RtcEngineEventHandler handler) {
        mRtcEventHandler.removeEventHandler(handler);
    }
}

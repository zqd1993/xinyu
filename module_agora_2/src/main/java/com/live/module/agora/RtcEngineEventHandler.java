package com.live.module.agora;

import io.agora.rtc.IRtcEngineEventHandler;

public interface RtcEngineEventHandler {
    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

    void onUserJoined(int uid, int elapsed);

    void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed);

    void onLeaveChannel(IRtcEngineEventHandler.RtcStats rtcStats);
}

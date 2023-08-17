package com.live.module.agora.manager

import android.util.Log
import com.live.module.agora.RtcEngineManager
import com.live.vquonline.base.bean.AgoraServiceEvent
import com.mshy.VInterestSpeed.common.bean.ExitAgoraEvent
import com.mshy.VInterestSpeed.common.constant.HANGUP
import com.mshy.VInterestSpeed.common.utils.AgoraUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import io.agora.rtc.IRtcEngineEventHandler.RtcStats
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/7/30
 * description:
 */
object AudioCallManager : CallManager() {

    var TAG = AudioCallManager::class.java.simpleName

    const val WHAT_SEND_HEARTBEAT: Int = 0//发送心跳包
    const val WHAT_CHECK_HEARTBEAT: Int = 1 //检测心跳
    const val WHAT_CALLING_TIME: Int = 4 //显示通话时长

    const val LINE_IS_BUSY_CODE = -9999001

    var isVideo: Boolean = false

    private var audioCall: AudioCall? = null

    var isStarted = false


    fun init(
        callBean: com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean?,
        roomId: Int,
        socketUrl: String?,
        isCaller: Boolean,
        isMatch: Boolean
    ) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        audioCall = AudioCall()
        audioCall?.callBean = callBean
        audioCall?.vquRoomId = roomId
        audioCall?.isCaller = isCaller
        audioCall?.socketUrl = socketUrl
        audioCall?.isMatch = isMatch
    }

    fun getCallBean(): com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean? {
        return audioCall?.callBean
    }

    fun getVideoCall(): AudioCall? {
        return audioCall
    }

    fun initAudio() {
        audioCall?.initAudio()
    }

    fun initWebSocket() {
        audioCall?.initWebSocket()

    }


    /**
     * 加入频道
     */
    fun joinChannel() {
        audioCall?.joinChannel()
    }

    fun addRoomStateListener(listener: RoomStateListener?) {
        audioCall?.addRoomStateListener(listener)
    }

    fun removeRoomStateListener(listener: RoomStateListener?) {
        audioCall?.removeRoomStateListener(listener)
    }

    interface RoomStateListener {
        fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int)

        fun onUserOffline(uid: Int, reason: Int)

        fun onUserJoined(uid: Int, elapsed: Int)

        fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int)

        fun onLeaveChannel(rtcStats: RtcStats?)

        fun onCameraClosed()

        fun onSurfaceDestroyed()

        fun onFinish()
    }

    interface WebSocketListener {
        fun onRequest()

        fun onResponse()

        fun onStartConnecting()

        fun onHangUp(json: String)

        fun onNotice(json: String)

        fun onTimeChanged(time: String, currentSecond: Long)

        fun onAddWaitGiftBean(noticeGiftBean: com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean, fromUid: String?)

        fun onAddSvg(giftsvga: String?)

        fun onNoCoin()
    }

    fun addWebSocketListener(listener: WebSocketListener?) {
        audioCall?.addWebSocketListener(listener)
    }

    fun removeWebSocketListener(listener: WebSocketListener?) {
        audioCall?.removeWebSocketListener(listener)
    }

    fun showMiniWindow() {
        audioCall?.showMiniWindow()
    }

    fun sendObjectMessage(bean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<*>) {
        audioCall?.vquWsManager?.sendObjectMessage(bean)
    }


    fun release() {
        isStarted = false
        isVideo = false
        AgoraUtils.contactType = AgoraUtils.CONTACT_TYPE_NO
//        isConnecting = false
        audioCall = null

        UserManager.isVideo = false

        EventBus.getDefault().post(AgoraServiceEvent(false))

        Log.d(TAG, "release: " + UserManager.isVideo)
    }

    fun isConnecting(): Boolean {
        return audioCall?.isConnecting ?: false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onExitAgoraEvent(event: ExitAgoraEvent) {
        if (audioCall != null) {
            val requestBean =
                com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
            requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
            requestBean.data.room_id = audioCall?.vquRoomId ?: 0
            requestBean.method = HANGUP
            sendObjectMessage(requestBean)

        }
    }

    fun ringTimer() {
        audioCall?.ringTimer()
    }

    fun hangUpLineIsBusy() {
        audioCall?.hangUpLineIsBusy()
    }

    fun setNoUseMikePhone(selected: Boolean) {
        audioCall?.isNoUseMikePhone = selected

        RtcEngineManager.getRtcEngine().muteLocalAudioStream(selected)
    }

    fun setEnableSpeakerphone(selected: Boolean) {
        audioCall?.enableSpeakerphone = selected
    }
}
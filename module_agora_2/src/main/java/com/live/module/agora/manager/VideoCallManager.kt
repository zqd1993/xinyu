package com.live.module.agora.manager

import android.view.ViewGroup
import com.faceunity.nama.control.FaceBeautyControlView
import com.live.module.agora.RtcEngineManager
import com.live.vquonline.base.bean.AgoraServiceEvent
import com.mshy.VInterestSpeed.common.bean.ExitAgoraEvent
import com.mshy.VInterestSpeed.common.bean.StartCameraEvent
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
object VideoCallManager {

    var TAG = VideoCallManager::class.java.simpleName

    const val WHAT_SEND_HEARTBEAT: Int = 0//发送心跳包
    const val WHAT_CHECK_HEARTBEAT: Int = 1 //检测心跳
    const val WHAT_CALLING_TIME: Int = 4 //显示通话时长
    const val WHAT_CAMERA_CHANGED: Int = 6 //摄像头发生改变

    const val LINE_IS_BUSY_CODE = -9999001

    const val CAPTURE_WIDTH = 1280
    const val CAPTURE_HEIGHT = 720
    const val CAPTURE_FRAME_RATE = 15

    var isVideo: Boolean = false

    private var videoCall: VideoCall? = null

    var isStarted = false


    fun init(
        callBean: com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean?,
        roomId: Int,
        socketUrl: String?,
        isCaller: Boolean,
        isMatch: Boolean,
    ) {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        videoCall = VideoCall()
        videoCall?.callBean = callBean
        videoCall?.vquRoomId = roomId
        videoCall?.isCaller = isCaller
        videoCall?.socketUrl = socketUrl
        videoCall?.isMatch = isMatch
        videoCall?.initData()
    }

    fun getCallBean(): com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean? {
        return videoCall?.callBean
    }

    fun getVideoCall(): VideoCall? {
        return videoCall
    }

    fun initVideo() {
        videoCall?.initVideo()
    }

    fun initWebSocket() {
        videoCall?.initWebSocket()

    }

    fun setLocalView(viewGroup: ViewGroup) {
        videoCall?.setLocalView(viewGroup)
    }


    fun setRemoteVideoView(viewGroup: ViewGroup) {
        videoCall?.setRemoteVideoView(viewGroup)
    }

    /**
     * 加入频道
     */
    fun joinChannel() {
        videoCall?.joinChannel()
    }

    fun addRoomStateListener(listener: RoomStateListener?) {
        videoCall?.addRoomStateListener(listener)
    }

    fun removeRoomStateListener(listener: RoomStateListener?) {
        videoCall?.removeRoomStateListener(listener)
    }

    interface RoomStateListener {
        fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int)

        fun onUserOffline(uid: Int, reason: Int)

        fun onUserJoined(uid: Int, elapsed: Int)

        fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int)

        fun onLeaveChannel(rtcStats: RtcStats?)

        fun onCameraClosed()

        fun onSurfaceDestroyed()
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
        videoCall?.addWebSocketListener(listener)
    }

    fun removeWebSocketListener(listener: WebSocketListener?) {
        videoCall?.removeWebSocketListener(listener)
    }

    fun showMiniWindow() {
        videoCall?.showMiniWindow()
    }

    fun sendObjectMessage(bean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<*>) {
        videoCall?.vquWsManager?.sendObjectMessage(bean)
    }

    fun bindFaceData(fbcvFaceBeauty: FaceBeautyControlView) {
        videoCall?.bindFaceData(fbcvFaceBeauty)
    }

    fun release() {
        isVideo = false
        isStarted = false
        AgoraUtils.contactType = AgoraUtils.CONTACT_TYPE_NO

        UserManager.isVideo = false
//        UserManager.isVideo = false
        EventBus.getDefault().post(AgoraServiceEvent(false))
        videoCall = null

    }

    fun switchCamera() {
        videoCall?.switchCamera()
    }

    fun getCameraState(): Int {
        return videoCall?.cameraState ?: 0
    }

    fun isConnecting(): Boolean {
        return videoCall?.isConnecting ?: false
    }

    fun ringTimer() {
        videoCall?.ringTimer()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onExitAgoraEvent(event: ExitAgoraEvent) {
        if (videoCall != null) {
            val requestBean =
                com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
            requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
            requestBean.data.room_id = videoCall?.vquRoomId ?: 0
            requestBean.method = HANGUP
            sendObjectMessage(requestBean)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStartCameraEvent(event: StartCameraEvent) {
        videoCall?.startCamera()
    }

    fun hangUpLineIsBusy() {
        videoCall?.hangUpLineIsBusy()
    }

    fun setEnableVideo(enableVideo: Boolean) {
        videoCall?.enableVideo = enableVideo
    }

    fun changedCamera(reason: Int) {
        videoCall?.cameraChanged(reason)
    }

    fun startCamera() {
        videoCall?.startCamera()
    }

    fun setNoUseMikePhone(selected: Boolean) {
        videoCall?.isNoUseMikePhone = selected
        RtcEngineManager.getRtcEngine().muteLocalAudioStream(selected)
    }

}
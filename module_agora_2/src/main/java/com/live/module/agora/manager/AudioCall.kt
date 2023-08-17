package com.live.module.agora.manager

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.live.module.agora.R
import com.live.module.agora.RtcEngineEventHandler
import com.live.module.agora.RtcEngineManager
import com.live.module.agora.activity.AgoraTantaVideo2Activity
import com.live.module.agora.bean.AgoraVquNoticeBean
import com.live.module.agora.bean.VquHangUpBean
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.AppManager
import com.live.vquonline.base.utils.GsonUtil
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.constant.*
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.CustomNotification
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig
import com.shuyu.gsyvideoplayer.GSYVideoManager
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.video.VideoCanvas
import okhttp3.OkHttpClient
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * author: Lau
 * date: 2022/7/30
 * description:
 */
class AudioCall : RtcEngineEventHandler {
    companion object {
        const val TAG = "VideoCall"
    }

    var enableSpeakerphone: Boolean = true
    var callBean: com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean? = null

    var vquRoomId = 0

    var socketUrl: String? = null

    var isMatch = false


    /**
     * 是否呼叫着，false为被呼叫者，true为呼叫者
     */
    var isCaller = false

    var isConnecting = false

    /**
     * 计时器线程池
     */
    private val mTimerPool = Executors.newScheduledThreadPool(10)

    /**
     * 发送心跳包的锁，当该值为true时，不发送心跳检测
     * 在[AgoraWsStatusListener.onOpen]方法中被修改为false
     * 当[mCountTimerTask]运行40秒，没有收到从[mHeartTimerTask]发送的心跳消息
     * 则将该值设置为true，将不在发送心跳检测，查看[lockWebSocketTimeOutTimer]方法
     */
    private var mHeartTimerTaskLock = true

    /**
     * 心跳检测的锁，当该值为true时，结束倒计时
     * 在[AgoraWsStatusListener.onOpen]方法中被修改为false
     * 当[mCountTimerTask]运行40秒，没有收到从[mHeartTimerTask]发送的心跳消息
     * 则将该值设置为true，将不在发送心跳检测，查看[lockWebSocketTimeOutTimer]方法
     */
    private var mCountTimerTaskLock = true

    /**
     * 心跳发送计时器，是否开启，用于防止心跳检测重复调用timer.scheduleAtFixedRate
     */
    private var mHeartTimerTaskIsRunning = false

    /**
     * 心跳检测的计时器，每个30秒发送一次心跳检测消息
     */
    private val mHeartTimerTask by lazy {
        object : TimerTask() {
            override fun run() {
                if (!mHeartTimerTaskLock) {
                    val message = Message()
                    message.what = AgoraTantaVideo2Activity.WHAT_SEND_HEARTBEAT
                    mHandler.sendMessage(message)
                }
            }
        }
    }

    /**
     * 心跳检测检测倒计时初始时间
     * 当该数==0，表示WebSocket断连，重连WebSocket
     */
    private var mHearBeatCount = 40

    /**
     * 心跳检测计时器，是否开启，防止重复调用
     */
    private var mCountTimerTaskIsRunning = false

    /**
     * 心跳检测间隔计算器，每秒检测一次，并且倒计时，40秒没有收到新的心跳检测消息
     * 则表示WebSocket已经断连
     */
    private val mCountTimerTask by lazy {
        object : TimerTask() {
            override fun run() {
                if (!mCountTimerTaskLock) {
//                    Log.d(TAG, "TimerTask: mCountTimerTask")
                    val message = Message()
                    message.what = AudioCallManager.WHAT_CHECK_HEARTBEAT
                    mHandler.sendMessage(message)

                }
            }
        }
    }

    private var mBaseTime = SystemClock.elapsedRealtime()

    /**
     * 时长
     */
    private var mRoomDuration = 0L

    /**
     * 拨通电话后的计时器
     */
    private val mCallingTimerTask by lazy {
        object : TimerTask() {
            override fun run() {
                val msg = Message()
                msg.what = AudioCallManager.WHAT_CALLING_TIME
                mHandler.sendMessage(msg)
            }
        }
    }


    private var ringTimer = 0L

    private var mLockRingTimerTask = false

    /**
     * 铃声计时器，用于占线时假装拨打用
     */
    private val mRingTimerTask by lazy {
        object : TimerTask() {
            override fun run() {
                if (mLockRingTimerTask) {
                    return
                }
                ringTimer++
                if (ringTimer == 60L) {
                    hangUpLineIsBusy()
                }
            }
        }
    }

    var isNoUseMikePhone = false


    var mRoomStateListeners: AudioCallManager.RoomStateListener? = null
    var mWebSocketStateListeners: AudioCallManager.WebSocketListener? = null


    /**
     * WebSocket
     */
    var vquWsManager: com.mshy.VInterestSpeed.common.websocket.WsManager? = null

    fun initAudio() {
        RtcEngineManager.addRtcHandler(this)

        initRoom()

    }

    fun initWebSocket() {

        if (socketUrl.isNullOrEmpty()) {
            return
        }

        vquWsManager = com.mshy.VInterestSpeed.common.websocket.WsManager.Builder(BaseApplication.context)
            .client(
                OkHttpClient().newBuilder().pingInterval(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true).build()
            )
            .needReconnect(true)
            .wsUrl("$socketUrl&reconnect=0")
            .build()

        vquWsManager?.setWsStatusListener(AgoraWsStatusListener())

        vquWsManager?.startConnect()
    }

    private fun initRoom() {

        //记录视频视频状态
        if (Settings.canDrawOverlays(BaseApplication.context)) {
            com.mshy.VInterestSpeed.common.ui.view.notification.SwipeMessageNotificationManager.getInstance(BaseApplication.application)
                .removeNotificationAll()
        }

        RtcEngineManager.getRtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        RtcEngineManager.getRtcEngine().enableDeepLearningDenoise(true)
        RtcEngineManager.getRtcEngine().disableVideo()
        RtcEngineManager.getRtcEngine().enableAudio()
        RtcEngineManager.getRtcEngine()
            .setEnableSpeakerphone(true)
    }


    private fun removeFromParent(canvas: VideoCanvas?): ViewGroup? {
        if (canvas != null) {
            val parent = canvas.view.parent
            if (parent != null) {
                val group = parent as ViewGroup
                group.removeView(canvas.view)
                return group
            }
        }
        return null
    }


    private fun lockWebSocketTimeOutTimer() {
        mHeartTimerTaskLock = true
        mCountTimerTaskLock = true
    }


    /**
     * ======================== RtcEngineEventHandler =======================
     */
    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        Log.d(AudioCallManager.TAG, "onJoinChannelSuccess: ")
        isConnecting = true
        mRoomStateListeners?.onJoinChannelSuccess(channel, uid, elapsed)
//        mRoomStateListeners.forEach {
//            it.onJoinChannelSuccess(channel, uid, elapsed)
//        }
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        Log.d(AudioCallManager.TAG, "onUserOffline: ")
        mRoomStateListeners?.onUserOffline(uid, reason)
//        mRoomStateListeners.forEach {
//            it.onUserOffline(uid, reason)
//        }
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        Log.d(AudioCallManager.TAG, "onUserJoined: ")
        mHandler.post {
            mRoomStateListeners?.onUserJoined(uid, elapsed)
//            mRoomStateListeners.forEach {
//                it.onUserJoined(uid, elapsed)
//            }
        }

    }


    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        Log.d(AudioCallManager.TAG, "onRemoteVideoStateChanged: ")
    }

    override fun onLeaveChannel(rtcStats: IRtcEngineEventHandler.RtcStats?) {
        Log.d(AudioCallManager.TAG, "onLeaveChannel: ")
        mRoomStateListeners?.onLeaveChannel(rtcStats)
//        mRoomStateListeners.forEach {
//            it.onLeaveChannel(rtcStats)
//        }
    }
    /**
     * ======================== RtcEngineEventHandler =======================
     */

    /**
     * 加入频道
     */
    fun joinChannel() {
        if (UserManager.userInfo == null) {
            UserManager.userInfo = UserSpUtils.getBean(SpKey.userInfo, VquUserInfo::class.java)
        }

        RtcEngineManager.getRtcEngine()
            .joinChannel(
                null,
                vquRoomId.toString(),
                "AgoraWithBeauty",
                UserManager.userInfo?.userId?.toInt() ?: 0
            )
    }

    private inner class AgoraWsStatusListener : com.mshy.VInterestSpeed.common.websocket.listener.WsStatusListener() {
        override fun onOpen(response: Response?) {
            super.onOpen(response)
            mHeartTimerTaskLock = false
            mCountTimerTaskLock = false

            if (!mHeartTimerTaskIsRunning) {
                //开启心跳计时器
                mHeartTimerTaskIsRunning = true
                mTimerPool.scheduleAtFixedRate(mHeartTimerTask, 0, 30, TimeUnit.SECONDS)
            }
        }

        override fun onMessage(text: String?) {
            super.onMessage(text)
            Log.d(TAG, "onMessage text: $text")

            val json = text?.replace("@@", "") ?: ""
            if (json.isNotEmpty()) {
                val bean = GsonUtil.GsonToBean(json, com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean::class.java)
                when (bean?.method) {
                    //请求者，也就是呼叫者
                    REQUEST -> {
                        //发送呼叫的消息给被呼叫着
                        sendCustomNotificationCall()
                        mWebSocketStateListeners?.onRequest()
//                        mWebSocketStateListeners.forEach {
//                            it.onRequest()
//                        }
                    }

                    //被叫者连接成功
                    RESPONSE -> {
                        mWebSocketStateListeners?.onResponse()
//                        mWebSocketStateListeners.forEach {
//                            it.onResponse()
//                        }
                    }

                    //挂断
                    HANGUP -> {
                        Log.d(AudioCallManager.TAG, "onMessage: $HANGUP$vquRoomId")
                        mWebSocketStateListeners?.onHangUp(json)
                        hangUpAfterSocket(json)
//                        mWebSocketStateListeners.forEach {
//                            it.onHangUp(json)
//                        }
                    }

                    //开始视频，这个方法会发生在被呼叫者发送接受的消息之后
                    STARTVIDEO -> {

                        AudioCallManager.isStarted = true

                        startAudio()
                        mWebSocketStateListeners?.onStartConnecting()
//                        mWebSocketStateListeners.forEach {
//                            it.onStartConnecting()
//                        }

                        val jsonObject: JSONObject
                        mRoomDuration = 0
                        try {
                            jsonObject = JSONObject(json)
                            val jsonObject1: JSONObject = jsonObject.getJSONObject("data")
                            mRoomDuration = jsonObject1.getLong("duration")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        startCallingTimer()
                    }

                    NOTICE -> {  //通知
                        noticeAfterSocket(json)

                        mWebSocketStateListeners?.onNotice(json)
//                        mWebSocketStateListeners.forEach {
//                            it.onNotice(json)
//                        }
                    }

                    HEARTBEAT -> {    //心跳检测
                        heartBeatAfterSocket()
                    }
                }
            }

        }

        override fun onClosed(code: Int, reason: String?) {
            super.onClosed(code, reason)
            Log.d(AudioCallManager.TAG, "onClosed: $vquRoomId")
        }

        override fun onClosing(code: Int, reason: String?) {
            super.onClosing(code, reason)
            Log.d(AudioCallManager.TAG, "onClosing: $vquRoomId")
        }
    }

    private fun startCallingTimer() {
        mBaseTime = SystemClock.elapsedRealtime()
        mTimerPool.scheduleAtFixedRate(mCallingTimerTask, 0, 1, TimeUnit.SECONDS)
//        mCallingTimer.schedule(mCallingTimerTask, 0, 1000)
    }

    /**
     * 心跳检测，WebSocket回调事件
     */
    private fun heartBeatAfterSocket() {
        mHearBeatCount = 40
        if (!mCountTimerTaskIsRunning) {
            mCountTimerTaskIsRunning = true
            mTimerPool?.scheduleAtFixedRate(mCountTimerTask, 0, 1, TimeUnit.SECONDS)
        }
    }

    /**
     * 通知，WebSocket回调事件
     */
    private fun noticeAfterSocket(json: String) {
        val noticeBean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<AgoraVquNoticeBean> = Gson().fromJson(
            json,
            object : TypeToken<com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<AgoraVquNoticeBean?>?>() {}.type
        )

        if (noticeBean.data.type == 3) {
            val noticeGiftBean: com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean = Gson().fromJson(
                noticeBean.data.content,
                com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean::class.java
            )

            mWebSocketStateListeners?.onAddWaitGiftBean(noticeGiftBean, noticeBean.data.from_uid)

            if (noticeGiftBean.gifttype == 1) {
                mWebSocketStateListeners?.onAddSvg(noticeGiftBean.giftsvga)
            }

//            mWebSocketStateListeners.forEach {
//                it.onAddWaitGiftBean(noticeGiftBean, noticeBean.data.from_uid)
//                if (noticeGiftBean.gifttype == 1) {
//                    it.onAddSvg(noticeGiftBean.giftsvga)
//                }
//            }
        }

        if (noticeBean.data.type == 1) {
            if (noticeBean.data.gifttype == 1 &&
                !noticeBean.data.giftsvga.isNullOrEmpty()
            ) {
                mWebSocketStateListeners?.onAddSvg(noticeBean.data.giftsvga)

//                mWebSocketStateListeners.forEach {
//                    it.onAddSvg(noticeBean.data.giftsvga)
//                }
            }
        }

        if (!noticeBean.data.link_type.isNullOrEmpty()
            && !noticeBean.data.link_url.isNullOrEmpty()
        ) {
            val type: Int = noticeBean.data.link_type.toInt()
            if (type != 2 || "rechargeCoin" == noticeBean.data.link_url) {

                if (AppManager.isMiniWindow) {
                    EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.ShowDialogWhenUserInCallEvent())
                    return
                }
                mWebSocketStateListeners?.onNoCoin()
//                mWebSocketStateListeners.forEach {
//                    it.onNoCoin()
//                }
            }
        }
    }

    /**
     * 挂断操作
     */
    private fun hangUpAfterSocket(json: String) {

        if (isConnecting) {
            leaveChannel()
        } else {
            preLeaveChannel()
        }

        mHandler.postDelayed({
            if (AppManager.isMiniWindow) {
                com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().hide()
                AppManager.isMiniWindow = false
            }
        }, 500)

        mRoomStateListeners?.onFinish()
//        mRoomStateListeners.forEach {
//            it.onFinish()
//        }

        AudioCallManager.release()

        RtcEngineManager.removeRtcHandler(this)

        val hangUpBean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<VquHangUpBean>? = Gson().fromJson(
            json,
            object :
                TypeToken<com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<VquHangUpBean?>?>() {}.type
        )

        when (hangUpBean?.data?.type ?: 7) {
            1 -> {
                toast("已取消")
            }
            2 -> {
                toast("已拒绝")
            }
            3 -> {
                toast("超时无应答")
            }
            4 -> {
                if (isCaller) {
                    toast("对方已挂断")
                } else {
                    toast("通话已结束")
                }
//                toVideoFinish()
            }
            5 -> {
                if (isCaller) {
                    toast("通话已结束")
                } else {
                    toast("对方已挂断")
                }
//                toVideoFinish()
            }
            6 -> {
                toast("费用不足已断开")
//                toVideoFinish()
            }
            7 -> {
            }
            10 -> {
                if (isCaller) {
                    toast("本次语音违规，请遵守平台相关规则")
                } else {

                    toast("因对方语音通话违规系统挂断，请严格遵守平台相关规定。")
                }
//                toVideoFinish()
            }
            11 -> {
                if (isCaller) {

                    toast("因对方语音通话违规系统挂断，请严格遵守平台相关规定。")
//                    toVideoFinish()
                } else {
                    toast("本次语音违规，请遵守平台相关规则")
                }
            }
        }

    }


    /**
     * 推出房间
     */
    private fun leaveChannel() {
        preLeaveChannel()
        RtcEngineManager.getRtcEngine().leaveChannel()
    }

    private fun preLeaveChannel() {

        vquWsManager?.stopConnect()

        stopTimer()

        mHandler.post {
            if (AppManager.isMiniWindow) {
                com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<FrameLayout>(R.id.floating_audio_contact_parent)
                    ?.gone()
                com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<CardView>(R.id.floating_end_contact_parent)
                    ?.visible()
            }
        }


        GSYVideoManager.instance().isNeedMute = false

        SpUtils.putBean(SpKey.user_in_call,
            com.mshy.VInterestSpeed.common.bean.UserInCallEvent(false, false))
        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.UserInCallEvent(
            false,
            false))


    }

    private fun stopTimer() {
        mCallingTimerTask.cancel()
        mHeartTimerTask.cancel()
        mCountTimerTask.cancel()
        mTimerPool.shutdownNow()
    }

    /**
     * 发送呼叫的消息给被叫者
     */
    fun sendCustomNotificationCall() {
        val vquRequestBean =
            com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean()
        vquRequestBean.id = 21
        vquRequestBean.data =
            com.mshy.VInterestSpeed.common.bean.video.VideoRequestDataBean()
        vquRequestBean.data.callTime = (com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getTime() / 1000).toString() + ""
        vquRequestBean.data.roomid = vquRoomId.toString()
        vquRequestBean.data.title = "通话邀请"
        vquRequestBean.data.isMatch = isMatch
        vquRequestBean.data.content = callBean?.to_nickname + "邀请你进行语音通话"
        val gsonNotification = Gson()
        val content = gsonNotification.toJson(vquRequestBean)

        val notification = CustomNotification()
        notification.sessionId = callBean?.to_uid.toString()
        notification.sessionType = SessionTypeEnum.P2P
        notification.content = content
        notification.isSendToOnlineUserOnly = false
        notification.apnsText = callBean?.to_nickname + "邀请你进行语音通话"

        /**
         * 测试环境回调
         */
        if (NetBaseUrlConstant.BASE_URL == "http://appta.pre.vqu.show/") {
            notification.env = "pre"
        }

        val config = CustomNotificationConfig()
        config.enablePush = true
        config.enableUnreadCount = false
        notification.config = config

        NIMClient.getService(MsgService::class.java).sendCustomNotification(notification)
            .setCallback(object : RequestCallback<Void> {
                override fun onSuccess(param: Void?) {
                    Log.e("sendCustomNotification", "onSuccess:")
                }

                override fun onFailed(code: Int) {
                    Log.e("sendCustomNotification", "onFailed: $code")
                }

                override fun onException(exception: Throwable?) {
                    Log.e("sendCustomNotification", "exception: ")
                }
            })
    }

    fun addRoomStateListener(listener: AudioCallManager.RoomStateListener?) {
        mRoomStateListeners = listener

//        if (listener != null) {
//            mRoomStateListeners.add(listener)
//        }
    }

    fun removeRoomStateListener(listener: AudioCallManager.RoomStateListener?) {
        mRoomStateListeners = null
//        if (listener != null) {
//            mRoomStateListeners.remove(listener)
//        }
    }


    fun addWebSocketListener(listener: AudioCallManager.WebSocketListener?) {
        mWebSocketStateListeners = listener
//        if (listener != null) {
//            mWebSocketStateListeners.add(listener)
//        }
    }

    fun removeWebSocketListener(listener: AudioCallManager.WebSocketListener?) {
        mWebSocketStateListeners = null
//        if (listener != null) {
//            mWebSocketStateListeners.remove(listener)
//        }
    }

    fun showMiniWindow() {

        AppManager.isMiniWindow = true

        com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().show()
        val view = com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view

        view.findViewById<FrameLayout>(R.id.floating_video_parent).gone()
        view.findViewById<CardView>(R.id.floating_audio_contact_parent).visible()
        view.findViewById<CardView>(R.id.floating_end_contact_parent).gone()

//        EventBus.getDefault().post(ExitMatchEvent().apply { type = 1 })
    }


    private fun startAudio() {
        AudioCallManager.isVideo = true

        //通话中，其他跟音视频有关的显示toast或者静音处理
        GSYVideoManager.instance().isNeedMute = true

        SpUtils.putBean(SpKey.user_in_call,
            com.mshy.VInterestSpeed.common.bean.UserInCallEvent(true, true))

        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.UserInCallEvent(
            true,
            true))

//        if (!isCaller) {
        joinChannel()
//        }

//        mVquMediaPlayer?.stop()

    }

    private val mHandler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                /**
                 * 心跳检测发送
                 */
                AudioCallManager.WHAT_SEND_HEARTBEAT -> {
                    try {
                        val heartBean =
                            com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
                        heartBean.method = HEARTBEAT
                        heartBean.data =
                            com.mshy.VInterestSpeed.common.bean.RoomIdBean()
                        heartBean.data.room_id = vquRoomId
                        vquWsManager?.sendObjectMessage(heartBean)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                /**
                 * 心跳检测倒计时器
                 */
                AudioCallManager.WHAT_CHECK_HEARTBEAT -> {
                    mHearBeatCount = --mHearBeatCount
                    if (mHearBeatCount == 0) {
                        lockWebSocketTimeOutTimer()
                        vquWsManager?.stopConnect()
                        vquWsManager?.startConnect()
                    }
                }

                AudioCallManager.WHAT_CALLING_TIME -> {
                    callingTime()
                }

            }
        }
    }

    private fun callingTime() {
        val time =
            ((SystemClock.elapsedRealtime() - mBaseTime) / 1000).toInt() + mRoomDuration
        val hh = DecimalFormat("00").format((time / 3600))
        val mm = DecimalFormat("00").format((time % 3600 / 60))
        val second = time % 60
        val ss = DecimalFormat("00").format((second))
        val timeFormat = "$hh:$mm:$ss"

        if (AppManager.isMiniWindow) {
            com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<TextView>(R.id.floating_audio_time)?.text =
                timeFormat
        }

        mWebSocketStateListeners?.onTimeChanged(timeFormat, second)
//        mWebSocketStateListeners.forEach {
//            it.onTimeChanged(timeFormat, second)
//        }

    }

    fun ringTimer() {
        mTimerPool.scheduleAtFixedRate(mRingTimerTask, 0, 1, TimeUnit.SECONDS)
    }

    fun hangUpLineIsBusy() {
        mLockRingTimerTask = true

        mHandler.post {
            mRingTimerTask.cancel()

            preLeaveChannel()

            mRoomStateListeners?.onFinish()
//            mRoomStateListeners.forEach {
//                it.onFinish()
//            }

            VideoCallManager.release()
        }
    }

}
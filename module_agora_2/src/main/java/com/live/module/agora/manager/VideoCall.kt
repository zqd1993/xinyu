package com.live.module.agora.manager

import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.provider.Settings
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import com.faceunity.nama.FURenderer
import com.faceunity.nama.control.FaceBeautyControlView
import com.faceunity.nama.data.FaceUnityDataFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.live.module.agora.R
import com.live.module.agora.RtcEngineEventHandler
import com.live.module.agora.RtcEngineManager
import com.live.module.agora.VideoManager
import com.live.module.agora.activity.AgoraTantaVideo2Activity
import com.live.module.agora.bean.AgoraVquChatShootBean
import com.live.module.agora.bean.AgoraVquNoticeBean
import com.live.module.agora.bean.VquCountdownBean
import com.live.module.agora.bean.VquHangUpBean
import com.live.module.agora.framework.PreprocessorFaceUnity
import com.live.module.agora.framework.RtcVideoConsumer
import com.live.module.agora.net.AgoraVquApiService
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.AppManager
import com.live.vquonline.base.utils.GsonUtil
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.constant.*
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.CustomNotification
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig
import com.shuyu.gsyvideoplayer.GSYVideoManager
import io.agora.capture.video.camera.CameraVideoManager
import io.agora.capture.video.camera.Constant
import io.agora.capture.video.camera.VideoCapture
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * author: Lau
 * date: 2022/7/30
 * description:
 */
class VideoCall : SensorEventListener, RtcEngineEventHandler {
    companion object {
        const val TAG = "VideoCallAAAA"
    }

    var enableVideo: Boolean = true

    private val agoraApi = GlobalServiceManage.getRetrofit().create(AgoraVquApiService::class.java)

    var callBean: com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean? = null

    var vquRoomId = 0

    private var vquMPornCheckBeanV2: com.mshy.VInterestSpeed.common.bean.video.VideoVquPornCheckBeanV2? =
        null

    private var vquIsOpenDetection: Int = 0

    private var vquDetectionTime: Int = 0

    private var vquMPornCheckV2List: ArrayList<com.mshy.VInterestSpeed.common.bean.video.VideoVquPornCheckBeanV2.PornCheckV2>? =
        null

    private var checkFromUser: Int = 0

    var mVideoManager: CameraVideoManager? = null

    private var mSensorManager: SensorManager? = null

    private var preprocessor: PreprocessorFaceUnity? = null

    private var mLocalVideoCanvas: VideoCanvas? = null

    private var mRemoteVideoCanvas: VideoCanvas? = null

    val mFURenderer: FURenderer = FURenderer.getInstance()

    var socketUrl: String? = null

    var surfaceIsCreated = false

    var cameraState = 0

    private var remoteId = -1


    private var vquNeedChatShoot = true

    var isNoUseMikePhone = false

    /**
     * 是否呼叫着，false为被呼叫者，true为呼叫者
     */
    var isCaller = false

    /**
     * 是否速配
     */
    var isMatch = false

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
                Log.d(TAG, "onOpen heartTimer:")
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
                Log.d(TAG, "onOpen count: $mHearBeatCount")
                if (!mCountTimerTaskLock) {
//                    Log.d(TAG, "TimerTask: mCountTimerTask")
                    val message = Message()
                    message.what = VideoCallManager.WHAT_CHECK_HEARTBEAT
                    mHandler.sendMessage(message)

                }
            }
        }
    }

    private var ringTimer = 0L

    private var mLockRingTimerTask = false

    /**
     * 心跳检测间隔计算器，每秒检测一次，并且倒计时，40秒没有收到新的心跳检测消息
     * 则表示WebSocket已经断连
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
//                runOnUiThread {
                val msg = Message()
                msg.what = VideoCallManager.WHAT_CALLING_TIME
                mHandler.sendMessage(msg)
            }
        }
    }


    var mRoomStateListeners: VideoCallManager.RoomStateListener? = null
    var mWebSocketStateListeners: VideoCallManager.WebSocketListener? = null

    val mFaceUnityDataFactory = FaceUnityDataFactory(0)

    var isCameraIsError = false

    var hangupType = 0

    var roomId = 0

    var isHangup = false

    var isShowCanCallTimeToast = false

    /**
     * WebSocket
     */
    var vquWsManager: com.mshy.VInterestSpeed.common.websocket.WsManager? = null

    fun initVideo() {
//        mRoomStateListeners.clear()
//        mWebSocketStateListeners.clear()

        RtcEngineManager.addRtcHandler(this)
        initVideoModule()

        mSensorManager =
            BaseApplication.application.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        preprocessor?.setRenderEnable(true)
        mVideoManager?.startCapture()

        initRoom()

    }

    fun initWebSocket() {

        if (socketUrl.isNullOrEmpty()) {
            return
        }

        vquWsManager =
            com.mshy.VInterestSpeed.common.websocket.WsManager.Builder(BaseApplication.context)
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

        if (Settings.canDrawOverlays(BaseApplication.application)) {
            com.mshy.VInterestSpeed.common.ui.view.notification.SwipeMessageNotificationManager.getInstance(
                BaseApplication.application
            )
                .removeNotificationAll()
        }

        RtcEngineManager.getRtcEngine().setVideoSource(RtcVideoConsumer())

        RtcEngineManager.getRtcEngine().setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
        RtcEngineManager.getRtcEngine().enableDeepLearningDenoise(true)
        RtcEngineManager.getRtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        RtcEngineManager.getRtcEngine().enableVideo()
    }

    private fun initVideoModule() {
        mVideoManager = VideoManager.getManager()
        mVideoManager?.setCameraStateListener(object : VideoCapture.VideoCaptureStateListener {
            override fun onFirstCapturedFrame(p0: Int, p1: Int) {
                Log.d(TAG, "onFirstCapturedFrame: ")
            }

            override fun onCameraCaptureError(p0: Int, p1: String?) {
                // When there is a camera error, the capture should
                // be stopped to reset the internal states.
                Log.d(TAG, "onCameraCaptureError: ")

                isCameraIsError = true

                mVideoManager?.stopCapture()

                if (AppManager.mActivityCount > 0) {
                    mVideoManager?.startCapture()
                }
            }

            override fun onCameraClosed() {
//                mRoomStateListeners.forEach {
//                    it.onCameraClosed()
//                }

                mRoomStateListeners?.onCameraClosed()
                Log.d(TAG, "onCameraClosed: ")

            }
        })

        preprocessor = mVideoManager?.preprocessor as PreprocessorFaceUnity

        mVideoManager?.setPictureSize(
//            UiUtils.dip2px(BaseApplication.application, 100f),
//            UiUtils.dip2px(BaseApplication.application, 130f)
            VideoCallManager.CAPTURE_WIDTH,
            VideoCallManager.CAPTURE_HEIGHT
        )


        mVideoManager?.setFrameRate(VideoCallManager.CAPTURE_FRAME_RATE)
        mVideoManager?.setFacing(Constant.CAMERA_FACING_FRONT)
        mVideoManager?.setLocalPreviewMirror(Constant.MIRROR_MODE_AUTO)

        setLocalVideoView()



        preprocessor?.setSurfaceListener(object : PreprocessorFaceUnity.SurfaceViewListener {
            override fun onSurfaceCreated() {
                Log.d(TAG, "onSurfaceCreated: ")
                surfaceIsCreated = true
                mFaceUnityDataFactory.bindCurrentRenderer()
            }


            override fun onSurfaceDestroyed() {
                mFURenderer.release()
            }
        })

    }

    fun bindFaceData(fbcvFaceBeauty: FaceBeautyControlView) {
        val isEnableFaceBeauty = SpUtils.getBoolean(SpKey.FACE_BEAUTY_ENABLE, true) ?: true

        mFaceUnityDataFactory.mFaceBeautyDataFactory?.enableFaceBeauty(isEnableFaceBeauty)

        fbcvFaceBeauty.bindDataFactory(mFaceUnityDataFactory.mFaceBeautyDataFactory)
        fbcvFaceBeauty.setCurrentId(R.id.beauty_radio_skin_beauty)
    }

    private fun setLocalVideoView() {
//        val localView = SurfaceView(BaseApplication.application)
//        localView.setZOrderMediaOverlay(true)

        val localView = RtcEngine.CreateTextureView(BaseApplication.context)

        mLocalVideoCanvas = VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0)


        mVideoManager?.setLocalPreview(localView)

    }


    fun setLocalView(viewGroup: ViewGroup) {
        if (mLocalVideoCanvas?.view == null) {
            return
        }
        removeFromParent(mLocalVideoCanvas)
        viewGroup.addView(mLocalVideoCanvas?.view)
        if (mLocalVideoCanvas?.view is SurfaceView) {
            (mLocalVideoCanvas?.view as SurfaceView).setZOrderMediaOverlay(true)
        }

        viewGroup.requestFocus()
        viewGroup.invalidate()

        RtcEngineManager.getRtcEngine().setLocalRenderMode(VideoCanvas.RENDER_MODE_HIDDEN)
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
        isConnecting = true
        mRoomStateListeners?.onJoinChannelSuccess(channel, uid, elapsed)
//        mRoomStateListeners.forEach {
//            it.onJoinChannelSuccess(channel, uid, elapsed)
//        }
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        mRoomStateListeners?.onUserOffline(uid, reason)
//        mRoomStateListeners.forEach {
//            it.onUserOffline(uid, reason)
//        }
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        Log.d(VideoCallManager.TAG, "onUserJoined in VideoCall: $vquRoomId")
        if (remoteId == -1) {
            remoteId = uid
            mHandler.post {
//            remoteUid = uid
//            switchConnecting()
//            switchView(mLocalVideoCanvas!!)
                setRemoteVideoView(uid)
                mRoomStateListeners?.onUserJoined(uid, elapsed)
//                mRoomStateListeners.forEach {
//                    it.onUserJoined(uid, elapsed)
//                }


//            mIsStarted = true
//            mBinding.localParent.postDelayed({
//                mBinding.videoGift.visibility = View.VISIBLE
//                vquVisibleButton()
//            }, 3 * 1000)
            }
        }

    }

    private fun setRemoteVideoView(uid: Int) {
        //        val surfaceView = RtcEngine.CreateRendererView(this)
        val remoteView = RtcEngine.CreateTextureView(BaseApplication.application)
//        remoteView.setZOrderMediaOverlay(false)
//        setRoundVideoView(textureView)
        mRemoteVideoCanvas = VideoCanvas(
            remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid
        )

        RtcEngineManager.getRtcEngine().setupRemoteVideo(
            mRemoteVideoCanvas
        )

    }

    fun setRemoteVideoView(viewGroup: ViewGroup) {
        if (mRemoteVideoCanvas?.view == null) {
            return
        }

        removeFromParent(mRemoteVideoCanvas)
        viewGroup.addView(mRemoteVideoCanvas?.view)
    }


    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        cameraState = reason

        mHandler.post {
            cameraChanged(cameraState)
        }
        mRoomStateListeners?.onRemoteVideoStateChanged(uid, state, reason, elapsed)
//        mRoomStateListeners.forEach {
//            it.onRemoteVideoStateChanged(uid, state, reason, elapsed)
//        }
    }

    fun cameraChanged(reason: Int) {

        if (reason == 5) {
            if (AppManager.isMiniWindow) {


                com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<FrameLayout>(
                    R.id.floating_remote_video_view
                )
                    ?.gone()

                val remoteAvatar =
                    com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<ImageView>(
                        R.id.floating_remote_avatar
                    )

                Log.d(TAG, "cameraChanged: $vquAvatar")

//                remoteAvatar?.vquLoadImage(
//                    vquAvatar
//                )
            }
        } else if (reason == 0) {
            if (AppManager.isMiniWindow) {
//                FloatWindow.get().view?.findViewById<ImageView>(R.id.floating_remote_avatar)
//                    ?.gone()
                com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<FrameLayout>(
                    R.id.floating_remote_video_view
                )
                    ?.visible()
            }
        }
    }

    override fun onLeaveChannel(rtcStats: IRtcEngineEventHandler.RtcStats?) {
        isConnecting = false
        mRoomStateListeners?.onLeaveChannel(rtcStats)
//        mRoomStateListeners.forEach {
//            it.onLeaveChannel(rtcStats)
//        }

    }
    /**
     * ======================== RtcEngineEventHandler =======================
     */

    /**
     * ======================== SensorEventListener =======================
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
//            val z = event.values[2]
            if (abs(x) > 3 || abs(y) > 3) {
                if (abs(x) > abs(y)) {
                    (if (x > 0) 0 else 180).also { mFURenderer.deviceOrientation = it }
                } else {
                    mFURenderer.deviceOrientation = if (y > 0) 90 else 270
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    /**
     * ======================== SensorEventListener =======================
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

    private inner class AgoraWsStatusListener :
        com.mshy.VInterestSpeed.common.websocket.listener.WsStatusListener() {
        override fun onOpen(response: Response?) {
            super.onOpen(response)

            Log.d(TAG, "onOpen: ")


            mHeartTimerTaskLock = false
            mCountTimerTaskLock = false

            if (!mHeartTimerTaskIsRunning) {
                //开启心跳计时器
                mHeartTimerTaskIsRunning = true
                mTimerPool.scheduleAtFixedRate(mHeartTimerTask, 0, 3, TimeUnit.SECONDS)
            }
        }

        override fun onMessage(text: String?) {
            super.onMessage(text)
            Log.d("VideoCallManager2", "onMessage text: $text")

            val json = text?.replace("@@", "") ?: ""
            if (json.isNotEmpty()) {
                val bean = GsonUtil.GsonToBean(
                    json,
                    com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean::class.java
                )
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
                        hangUpAfterSocket(json)
//                        mWebSocketStateListeners.forEach {
//                            it.onHangUp(json)
//                        }
                    }

                    //开始视频，这个方法会发生在被呼叫者发送接受的消息之后
                    STARTVIDEO -> {

                        VideoCallManager.isStarted = true

                        startVideo()
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

                    CANCALLTIME -> {
                        if(isShowCanCallTimeToast){
                            return
                        }
                        val countdownBean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<VquCountdownBean> =
                            Gson().fromJson(
                                json,
                                object :
                                    TypeToken<com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<VquCountdownBean?>?>() {}.type
                            )
                        Log.d("video countdownBean", "countdownBean.data?.tip = ${countdownBean.data?.tip}")
                        if(countdownBean.data?.tip == 1){
                            Handler().postDelayed({
                                toast("费用不足已断开")
                                isShowCanCallTimeToast = true
                            }, 1000)
                        }
                    }
                }
            }


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
        val noticeBean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<AgoraVquNoticeBean> =
            Gson().fromJson(
                json,
                object :
                    TypeToken<com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<AgoraVquNoticeBean?>?>() {}.type
            )

        if (noticeBean.data.type == 3) {
            val noticeGiftBean: com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean =
                Gson().fromJson(
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
                    EventBus.getDefault()
                        .post(com.mshy.VInterestSpeed.common.bean.ShowDialogWhenUserInCallEvent())
                    return
                }

                mWebSocketStateListeners?.onNoCoin()
//                mWebSocketStateListeners.forEach {
//                    it.onNoCoin()
//                }
            }
        }
    }


//    mHandler.postDelayed({
//        if (AppManager.isMiniWindow) {
//            FloatWindow.get().hide()
//            AppManager.isMiniWindow = false
//            UserManager.isVideo = false
//        }
//    }, 500)
//
//
//    mRoomStateListeners.forEach {
//
//        it.onSurfaceDestroyed()
//    }
//
//    VideoCallManager.release()

    /**
     * 挂断操作
     */
    fun hangUpAfterSocket(json: String) {
        if(isHangup){
            return
        }
        synchronized(VideoCall::class.java) {
            hangupAfterSocketSync(json)
        }
    }

    private fun hangupAfterSocketSync(json: String) {

        val hangUpBean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<VquHangUpBean> =
            Gson().fromJson(
                json,
                object :
                    TypeToken<com.mshy.VInterestSpeed.common.bean.websocket.WebSocketResultBean<VquHangUpBean?>?>() {}.type
            )
        Log.d("video hangUpBean", "hangUpBean.data?.type = ${hangUpBean.data?.type}")
        if (hangupType == hangUpBean.data?.type!! && roomId == hangUpBean.data?.roomId!!) {
            return
        }
        hangupType = hangUpBean.data?.type!!
        roomId = hangUpBean.data?.roomId!!
        if (hangUpBean.data?.type != 10 && hangUpBean.data?.type != 11) {
            isHangup = true
            mWebSocketStateListeners?.onHangUp(json)
            VideoCallManager.isStarted = false

            mHandler.postDelayed({
                if (AppManager.isMiniWindow) {
                    com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().hide()
                    AppManager.isMiniWindow = false
                }
            }, 500)

            if (isConnecting) {
                leaveChannel()
            } else {
                preLeaveChannel()
            }
            RtcEngineManager.removeRtcHandler(this)

            preprocessor?.releaseFURender()
            mVideoManager?.stopCapture()
            mSensorManager?.unregisterListener(this)


            VideoCallManager.release()
            mRoomStateListeners?.onSurfaceDestroyed()


//        mRoomStateListeners.forEach {
//            it.onSurfaceDestroyed()
//        }
        }
        when (hangUpBean.data?.type ?: 7) {
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
            }

            5 -> {
                if (isCaller) {
                    toast("通话已结束")
                } else {
                    toast("对方已挂断")
                }
            }

            6 -> {
                if(isShowCanCallTimeToast){
                    return
                }
                toast("费用不足已断开")
                isShowCanCallTimeToast = true
            }

            7 -> {}
            10 -> {
                if (isCaller) {
                    toast("本次视频违规，请遵守平台相关规则")
                    mWebSocketStateListeners?.onPronRemind()
                } else {

                    toast("由于出现敏感信息，已关闭对方视频。")
                }
            }

            11 -> {
                if (isCaller) {
                    toast("由于出现敏感信息，已关闭对方视频。")
                } else {
                    toast("本次视频违规，请遵守平台相关规则")
                    mWebSocketStateListeners?.onPronRemind()
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

        //todo 需要回调
//        if (UserManager.isMatchReturn) {
//            mViewModel.vquAddMatching("video")
//        }

//        mVquMediaPlayer?.stop()
//        mVquMediaPlayer = null

        vquWsManager?.stopConnect()

        stopTimer()

        if (AppManager.isMiniWindow) {
            com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<FrameLayout>(
                R.id.floating_video_parent
            )?.gone()
            com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<CardView>(
                R.id.floating_end_contact_parent
            )
                ?.visible()
        }

        GSYVideoManager.instance().isNeedMute = false

        SpUtils.putBean(
            SpKey.user_in_call,
            com.mshy.VInterestSpeed.common.bean.UserInCallEvent(false, false)
        )
        EventBus.getDefault().post(
            com.mshy.VInterestSpeed.common.bean.UserInCallEvent(
                false,
                false
            )
        )
    }

    private fun stopTimer() {
        mCallingTimerTask.cancel()
        mHeartTimerTask.cancel()
        mCountTimerTask.cancel()
        mTimerPool.shutdownNow()

        vquWsManager = null
    }

    /**
     * 发送呼叫的消息给被叫者
     */
    fun sendCustomNotificationCall() {
        val vquRequestBean =
            com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean()
        vquRequestBean.id = 11
        vquRequestBean.data =
            com.mshy.VInterestSpeed.common.bean.video.VideoRequestDataBean()
        vquRequestBean.data.callTime =
            (com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getTime() / 1000).toString() + ""
        vquRequestBean.data.roomid = vquRoomId.toString()
        vquRequestBean.data.title = "通话邀请"
        vquRequestBean.data.isMatch = isMatch
        vquRequestBean.data.content = callBean?.to_nickname + "邀请你进行视频通话"
        val gsonNotification = Gson()
        val content = gsonNotification.toJson(vquRequestBean)

        val notification = CustomNotification()
        notification.sessionId = callBean?.to_uid.toString()
        notification.sessionType = SessionTypeEnum.P2P
        notification.content = content
        notification.isSendToOnlineUserOnly = false
        notification.apnsText = callBean?.to_nickname + "邀请你进行视频通话"
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
        //  ChatManager.getInstance().sendVideoRequest(content, mToUid.toString() + "")
    }

    fun addRoomStateListener(listener: VideoCallManager.RoomStateListener?) {
//        if (listener != null) {
//            mRoomStateListeners.add(listener)
//        }
        mRoomStateListeners = listener
    }

    fun removeRoomStateListener(listener: VideoCallManager.RoomStateListener?) {
//        if (listener != null) {
//            mRoomStateListeners.remove(listener)
//        }
        mRoomStateListeners = null
    }


    fun addWebSocketListener(listener: VideoCallManager.WebSocketListener?) {
//        if (listener != null) {
//            mWebSocketStateListeners.add(listener)
//        }
        mWebSocketStateListeners = listener
    }

    fun removeWebSocketListener(listener: VideoCallManager.WebSocketListener?) {
//        if (listener != null) {
//            mWebSocketStateListeners.remove(listener)
//        }
        mWebSocketStateListeners = null
    }

    private var vquAvatar = ""

    /**
     * 显示最小化窗口
     */
    fun showMiniWindow() {

        synchronized(VideoCall::class.java) {
            showMiniWindowSync()
        }


    }

    private fun showMiniWindowSync() {
        /**
         * 判断是否视频中，并且远程界面是否为空
         * 如果不在视频中，视频界面又是空，则不显示最小化窗口
         */
        if (!VideoCallManager.isStarted || mRemoteVideoCanvas?.view == null) {
            return
        }

        AppManager.isMiniWindow = true

        com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().show()
        val view = com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view

        view.findViewById<FrameLayout>(R.id.floating_video_parent).visible()
        view.findViewById<CardView>(R.id.floating_audio_contact_parent).gone()
        view.findViewById<CardView>(R.id.floating_end_contact_parent).gone()

        val floatingLocalVideoView = view.findViewById<FrameLayout>(R.id.floating_remote_video_view)

        removeFromParent(mRemoteVideoCanvas)

        /**
         * 判断是否视频中，并且远程界面是否为空
         * 如果不在视频中，视频界面又是空，则隐藏最小化窗口
         */
        if (!VideoCallManager.isStarted || mRemoteVideoCanvas?.view == null) {
            com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().hide()
            return
        } else {
            floatingLocalVideoView.addView(mRemoteVideoCanvas?.view)
        }

        val remoteAvatar = view.findViewById<ImageView>(R.id.floating_remote_avatar)


        vquAvatar = if (isCaller) {
            NetBaseUrlConstant.IMAGE_URL + callBean?.to_avatar
        } else {
            NetBaseUrlConstant.IMAGE_URL + callBean?.from_avatar
        }

        remoteAvatar.vquLoadRoundImage(
            vquAvatar,
            BaseApplication.application.dp2px(8f),
            R.mipmap.ic_common_head_def
        )

        if (cameraState == 5) {
//            remoteAvatar.visible()
            floatingLocalVideoView.gone()
        } else {
//            remoteAvatar.gone()
            floatingLocalVideoView.visible()
        }
    }


    private fun startVideo() {
        VideoCallManager.isVideo = true

        //通话中，其他跟音视频有关的显示toast或者静音处理
        GSYVideoManager.instance().isNeedMute = true

        SpUtils.putBean(
            SpKey.user_in_call,
            com.mshy.VInterestSpeed.common.bean.UserInCallEvent(true, false)
        )

        EventBus.getDefault().post(
            com.mshy.VInterestSpeed.common.bean.UserInCallEvent(
                true,
                false
            )
        )

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
                VideoCallManager.WHAT_SEND_HEARTBEAT -> {
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
                VideoCallManager.WHAT_CHECK_HEARTBEAT -> {
                    mHearBeatCount = --mHearBeatCount
                    if (mHearBeatCount == 0) {
                        lockWebSocketTimeOutTimer()
                        vquWsManager?.stopConnect()
                        vquWsManager?.startConnect()
                    }
                }

                VideoCallManager.WHAT_CALLING_TIME -> {
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
//                    mBinding.videoRemoteDuration.text = timeFormat

        if (AppManager.isMiniWindow) {
            com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().view?.findViewById<TextView>(
                R.id.floating_video_time
            )?.text =
                timeFormat
        }

//        mCurSecondInMinute = second

//        mWebSocketStateListeners.forEach {
//            it.onTimeChanged(timeFormat, second)
//        }

        mWebSocketStateListeners?.onTimeChanged(timeFormat, second)


        /**
         * 截屏鉴黄
         */
        CoroutineScope(Dispatchers.IO).launch {
            shootScreen(time)
        }
    }

    fun switchCamera() {
        mVideoManager?.switchCamera()
    }

    /**
     * 截屏鉴黄用
     */
    private suspend fun shootScreen(time: Long) {
        if (!vquNeedChatShoot) {
            return
        }
        if (vquIsOpenDetection == 1 && !vquMPornCheckV2List.isNullOrEmpty()) {
            val pornCheckV2: com.mshy.VInterestSpeed.common.bean.video.VideoVquPornCheckBeanV2.PornCheckV2? =
                vquMPornCheckV2List?.get(0)
            if (pornCheckV2 != null) {
                val start: Long = pornCheckV2.start
                val end: Long = pornCheckV2.end
                if (time in start..end) {
                    if (time == end) {
                        vquMPornCheckV2List?.removeAt(0)
                    }
                    if (vquDetectionTime != 0) {
                        if (time != 0L && time % vquDetectionTime == 0L) {
                            if (isCaller) {  //主叫者截取远程视频，则为被叫者图片
                                shootScreen(callBean?.to_uid?.toString() ?: "")
                            } else {
                                if (checkFromUser == 1) {
                                    shootScreen(callBean?.from_uid?.toString() ?: "")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 截屏鉴黄用
     * 在[shootScreen] (time:Long)中调用
     */
    private suspend fun shootScreen(uid: String) {
        val view = mRemoteVideoCanvas?.view ?: return

        val bitmap: Bitmap? = if (view is TextureView) {
            val width = view.width.toFloat() / 3f
            val ratio = view.height.toFloat() / view.width.toFloat()
            val height = width * ratio
            view.getBitmap(width.toInt(), height.toInt())
        } else {
            view.drawToBitmap()
        }
        if (bitmap != null) {
            val base64: String = bitmap2StrByBase64(bitmap)
            LogUtil.e("$isCaller..base64...111=$base64")
            if (!TextUtils.isEmpty(base64)) {
                request<BaseResponse<AgoraVquChatShootBean>> {
                    agoraApi.vquPostChatShootFromCall(vquRoomId.toString(), base64, uid).run {
                        responseCodeExceptionHandler(code, message) {
                            emit(this)
                        }
                    }
                }
                    .catch { }
                    .collect {
                        bitmap.recycle()
                        vquNeedChatShoot =
                            it.data.is_upload == 1
                    }
            }
        }
    }

    /**
     * 截屏鉴黄用
     * 在[shootScreen]中调用
     */
    private fun bitmap2StrByBase64(bit: Bitmap): String {
        val bos = ByteArrayOutputStream()
        var options = 50
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos) //参数100表示不压缩
        while (bos.toByteArray().size / 1024 > 30) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            bos.reset() //重置baos即清空baos
            bit.compress(Bitmap.CompressFormat.JPEG, options, bos) //这里压缩options%，把压缩后的数据存放到baos中
            if (options > 5) {
                options -= 5 //每次都减少10
            }
        }
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
    }

    fun initData() {
        vquMPornCheckBeanV2 = callBean?.porn_check_v2
        vquIsOpenDetection = vquMPornCheckBeanV2?.is_open ?: 0
        vquDetectionTime = vquMPornCheckBeanV2?.interval ?: 0
        vquMPornCheckV2List = vquMPornCheckBeanV2?.periodArray
        checkFromUser = vquMPornCheckBeanV2?.check_from_user ?: 0
    }

    fun ringTimer() {
        mTimerPool.scheduleAtFixedRate(mRingTimerTask, 0, 1, TimeUnit.SECONDS)
    }

    fun hangUpLineIsBusy() {
        mLockRingTimerTask = true

        mHandler.post {
            mRingTimerTask.cancel()

            preLeaveChannel()

            RtcEngineManager.removeRtcHandler(this@VideoCall)
            preprocessor?.releaseFURender()
            mVideoManager?.stopCapture()
            mSensorManager?.unregisterListener(this@VideoCall)

            if (!surfaceIsCreated) {
                mRoomStateListeners?.onSurfaceDestroyed()
//                mRoomStateListeners.forEach {
//                    it.onSurfaceDestroyed()
//                }
            }

            VideoCallManager.release()
        }
    }

    fun startCamera() {
        if (isCameraIsError) {
            isCameraIsError = false
            mVideoManager?.startCapture()
        }
    }

    /**
     * 发起请求封装
     * 该方法将flow的执行切换至IO线程
     *
     * @param requestBlock 请求的整体逻辑
     * @return Flow<T>
     */
    private fun <T> request(
        requestBlock: suspend FlowCollector<T>.() -> Unit
    ): Flow<T> {
        return flow(block = requestBlock).flowOn(Dispatchers.IO).catch {
            if (it is HttpException) {
                toast("网络异常，请稍后再试")
            } else {
                throw it
            }
        }
    }

}
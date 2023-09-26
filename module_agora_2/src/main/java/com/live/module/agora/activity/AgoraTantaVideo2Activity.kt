package com.live.module.agora.activity

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.live.module.agora.R
import com.live.module.agora.RtcEngineManager
import com.live.module.agora.bean.VquGiftBean
import com.live.module.agora.databinding.AgoraTantaActivityVideoBinding
import com.live.module.agora.manager.VideoCall
import com.live.module.agora.manager.VideoCallManager
import com.live.module.agora.vm.AgoraTantaVideoViewModel
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.isVisible
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.constant.*
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.*
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil
import com.opensource.svgaplayer.*
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc.IRtcEngineEventHandler
import org.dync.giftlibrary.GiftControl
import org.dync.giftlibrary.widget.CustormAnim
import org.dync.giftlibrary.widget.GiftModel
import org.greenrobot.eventbus.EventBus
import java.net.URL


/**
 * author: Lau
 * date: 2022/7/19
 * description:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Agora2.AgoraTantaVideoActivity)
class AgoraTantaVideo2Activity :
    BaseActivity<AgoraTantaActivityVideoBinding, AgoraTantaVideoViewModel>(),
    VideoCallManager.RoomStateListener, VideoCallManager.WebSocketListener {

    companion object {
        const val WHAT_SEND_HEARTBEAT: Int = 0//发送心跳包
        const val WHAT_CHECK_HEARTBEAT: Int = 1 //检测心跳
        const val WHAT_CALLING_TIME: Int = 4 //显示通话时长

        const val CAPTURE_WIDTH = 1280
        const val CAPTURE_HEIGHT = 720
        const val CAPTURE_FRAME_RATE = 15
    }

    private val TAG = AgoraTantaVideo2Activity::class.java.simpleName

    override val mViewModel: AgoraTantaVideoViewModel by viewModels()

    private var mRemoteUid = -1


    /**
     * MP3播放器，铃声
     */
    private var mVquMediaPlayer: MediaPlayer? = null


    private var mVquNeedChatShoot = true

    @Autowired(name = RouteKey.SOCKET_URL)
    @JvmField
    var mSocketUrl: String? = null


    @Autowired(name = RouteKey.IS_CALLER)
    @JvmField
    var mIsCaller = false

    @Autowired(name = RouteKey.CALL_BEAN)
    @JvmField
    var mCallBean: com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean? = null

    @Autowired(name = RouteKey.IS_MATCH)
    @JvmField
    var isMatch = false

    /**
     * 当code==-9999001的时候，表示占线
     */
    @Autowired(name = RouteKey.CODE)
    @JvmField
    var mCode = 0


    private var mVquRoomId = 0

    private var mVquMPornCheckBeanV2: com.mshy.VInterestSpeed.common.bean.video.VideoVquPornCheckBeanV2? =
        null

    private var mVquIsOpenDetection: Int = 0
    private var mVquDetectionTime: Int = 0
    private var mVquMPornCheckV2List: ArrayList<com.mshy.VInterestSpeed.common.bean.video.VideoVquPornCheckBeanV2.PornCheckV2>? =
        null
    private var mCheckFromUser: Int = 0

    private var mVquMDiffNum: Int = 0
    private var mVquMGuardPrice: Int = 0

    // 礼物面板
    private val giftControl: GiftControl by lazy {


        GiftControl(this@AgoraTantaVideo2Activity)
            .apply {
                setGiftLayout(mBinding.llGift1, 2)
                setHideMode(false)
                setCustormAnim(CustormAnim())
            }
    }

    private var mCurSecondInMinute = 0L

    private var mEnableVideo = true

    override fun initObserve() {
//        mViewModel.vquChatShootBean.observe(this) {
//            mVquNeedChatShoot = it.is_upload == 1
//        }
    }

    override fun initRequestData() {
//        mCallingTimerPool.scheduleAtFixedRate(mCallingTimerTask, 0, 1, TimeUnit.SECONDS)
    }

    override fun setStatusBar() {
//        super.setStatusBar()
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(false)
            .init()
    }

    override fun onStart() {
        super.onStart()
        activityIsShow = true

        VideoCallManager.startCamera()

        ActivityStackManager.addSingleInstanceActivityToStack(this)
    }

    override fun AgoraTantaActivityVideoBinding.initView() {
        UserManager.videoRequestBean = null

        initLocalViewParent()

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_SECURE
        )

        if (AppManager.isMiniWindow) {
            com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow.get().hide()
            AppManager.isMiniWindow = false
        }

        //记录视频视频状态
        UserManager.isVideo = true

        if (VideoCallManager.isVideo && mCallBean == null) {

            VideoCallManager.addRoomStateListener(this@AgoraTantaVideo2Activity)
            VideoCallManager.addWebSocketListener(this@AgoraTantaVideo2Activity)

            mCallBean = VideoCallManager.getCallBean()
            mVquRoomId = VideoCallManager.getVideoCall()?.vquRoomId ?: 0
            mIsCaller = VideoCallManager.getVideoCall()?.isCaller ?: false
            mEnableVideo = VideoCallManager.getVideoCall()?.enableVideo ?: true
            val isNoUseMikePhone = VideoCallManager.getVideoCall()?.isNoUseMikePhone ?: false


            initData()

            switchCloseCameraUi()


            mBinding.vquVideoMike.isSelected = isNoUseMikePhone

//            RtcEngineManager.getRtcEngine().muteLocalAudioStream(isNoUseMikePhone)

            VideoCallManager.setNoUseMikePhone(isNoUseMikePhone)
            VideoCallManager.setLocalView(mBinding.localVideoView)
            VideoCallManager.setRemoteVideoView(mBinding.remoteVideoView)

            VideoCallManager.bindFaceData(mBinding.fbcvFaceBeauty)

            changedCamera(VideoCallManager.getCameraState())

            switchConnecting()


        } else {
            initRoom()
            initData()
            initPermission()

        }

        initEvent()

        mBinding.stvAgoraVquWarm.isSelected = true

    }

    private fun initLocalViewParent() {
        val layoutParams = mBinding.localParent.layoutParams

        val ratio = ScreenUtil.screenHeight.toFloat() / ScreenUtil.screenWidth.toFloat()

        layoutParams.width = dp2px(100f)
        layoutParams.height = (layoutParams.width * ratio).toInt()

    }

    private fun initRoom() {
        mVquMediaPlayer = MediaPlayer.create(this, R.raw.resources_vqu_call_bg)
        mVquMediaPlayer?.isLooping = true
        mVquMediaPlayer?.start()
//        mBinding.openMiniWindow.postDelayed({ mVquMediaPlayer?.start() }, 2000)
    }


    private fun initVideo() {

        VideoCallManager.init(mCallBean, mVquRoomId, mSocketUrl, mIsCaller, isMatch)

        VideoCallManager.initVideo()

        VideoCallManager.addRoomStateListener(this)
        VideoCallManager.addWebSocketListener(this)

        VideoCallManager.bindFaceData(mBinding.fbcvFaceBeauty)

//        if (mIsCaller) {
//            VideoCallManager.joinChannel()
//        }

        if (mCode != VideoCallManager.LINE_IS_BUSY_CODE) {
            VideoCallManager.initWebSocket()
        } else {
            VideoCallManager.ringTimer()
        }

        VideoCallManager.setLocalView(mBinding.remoteVideoView)


    }

    private fun initPermission() {
        //权限获取
        PermissionUtils.videoPermission(this@AgoraTantaVideo2Activity,
            PermissionDescribe.vquVideoPermissionTxt,
            PermissionDescribe.vquVideoPermissionTxt,
            { allGranted, _, _ ->
                if (allGranted) {

                    //视频
                    initVideo()

                } else {
                    if (!mIsCaller) {
                        hangUpVideo()
                    }
                    VideoCallManager.release()
                    mVquMediaPlayer?.stop()
                    finish()
                    ToastUtils.showToast("未授权权限，无法通话", 2000)

                }
            }
        ) {
            if (!mIsCaller) {
                hangUpVideo()
            }
            VideoCallManager.release()
            mVquMediaPlayer?.stop()
            finish()

            false
        }
    }

    private fun initData() {
        if (isMatch) {
            com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
                this,
                com.mshy.VInterestSpeed.common.utils.UmUtils.TRUNONVIDEOMATCH
            )
        }

        try {
            mVquRoomId = mCallBean?.room_id?.toInt() ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            toast("无法获取通话ID")
        }

        setUserInfo()

        if (mCallBean?.guard != null) {
            mVquMDiffNum = mCallBean?.guard?.diff_num ?: 0
            mVquMGuardPrice = mCallBean?.guard?.guard_price ?: 0
        }

        mVquMPornCheckBeanV2 = mCallBean?.porn_check_v2
        mVquIsOpenDetection = mVquMPornCheckBeanV2?.is_open ?: 0
        mVquDetectionTime = mVquMPornCheckBeanV2?.interval ?: 0
        mVquMPornCheckV2List = mVquMPornCheckBeanV2?.periodArray
        mCheckFromUser = mVquMPornCheckBeanV2?.check_from_user ?: 0
    }

    private fun setUserInfo() {

        val avatar: String?
        val gender: Int?
        val nickname: String?
        val desc = StringBuilder()
        val age: String?
        val height: String?
        val weight: String?
        val occupation: String?

        if (mIsCaller) {

            mBinding.videoCalleeCancel.visible()
            mBinding.videoCalleeAnswer.gone()

            avatar = NetBaseUrlConstant.IMAGE_URL + mCallBean?.to_avatar
            nickname = mCallBean?.to_nickname
            gender = mCallBean?.to_gender
            age = mCallBean?.to_age_text
            height = mCallBean?.to_height
            occupation = mCallBean?.to_occupation

            mBinding.vquVideoStatusDes.text = "正在等待对方接受邀请…"
        } else {

            mBinding.videoCalleeCancel.visible()
            mBinding.videoCalleeAnswer.visible()

            avatar = NetBaseUrlConstant.IMAGE_URL + mCallBean?.from_avatar
            nickname = mCallBean?.from_nickname
            gender = mCallBean?.from_gender
            age = mCallBean?.from_age_text
            height = mCallBean?.from_height
            weight = mCallBean?.from_weight
            occupation = mCallBean?.from_occupation
            mBinding.vquVideoStatusDes.text = "正在邀请你视频通话"
        }

        mBinding.vquIvCallHead.vquLoadRoundImage(
            avatar,
            dp2px(10f),
            R.mipmap.ic_common_head_def
        )

        mBinding.videoStartName.text = nickname

        com.mshy.VInterestSpeed.common.helper.CommonVquAddRankAndGradeViewHelper.addGender(
            this,
            findViewById(R.id.layout_name_id),
            gender ?: 0,
            ""
        )
        //年龄
        if (!age.isNullOrEmpty()) {
            if (desc.isNotEmpty()) {
                desc.append(" | ")
            }
            desc.append(age)
        }

        //身高
        if (!height.isNullOrEmpty()) {
            if (desc.isNotEmpty()) {
                desc.append(" | ")
            }
            desc.append(height)
        }

        //职业
        if (!occupation.isNullOrEmpty()) {
            if (desc.isNotEmpty()) {
                desc.append(" | ")
            }
            desc.append(occupation)
        }
        mBinding.videoStartCost.text = desc.toString()
    }


    private fun initEvent() {
        mBinding.videoCalleeCancel.setViewClickListener {

            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (!mIsShowButton) {
                vquVisibleButton()
                return@setViewClickListener
            }
            clickCancel()
        }

        mBinding.videoCalleeAnswer.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
                this@AgoraTantaVideo2Activity,
                com.mshy.VInterestSpeed.common.utils.UmUtils.TURNONTHEVIDEO
            )
            mVquMediaPlayer?.stop()
            //被叫者接听
            calleeAgreeVideo()

//            joinChannel()
        }

        mBinding.ivVideoBeauty.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (!mIsShowButton) {
                vquVisibleButton()
                return@setViewClickListener
            }

            clickBeauty()
        }

        mBinding.remoteVideoView.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (mBinding.fbcvFaceBeauty.isVisible) {
                mBinding.fbcvFaceBeauty.gone()
                mBinding.rlDealLayout.visible()
            } else {
                if (VideoCallManager.isStarted) {
                    vquVisibleButton()
                }
            }

        }

        //切换摄像头
        mBinding.videoShoot.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (!mIsShowButton) {
                vquVisibleButton()
                return@setViewClickListener
            }

            VideoCallManager.switchCamera()
        }

        mBinding.vquVideoMike.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (!mIsShowButton) {
                vquVisibleButton()
                return@setViewClickListener
            }

            mBinding.vquVideoMike.isSelected = !mBinding.vquVideoMike.isSelected


            if (mBinding.vquVideoMike.isSelected) {
                toast("已关闭麦克风")
            } else {
                toast("已开启麦克风")
            }

            VideoCallManager.setNoUseMikePhone(mBinding.vquVideoMike.isSelected)
        }

//        mBinding.remoteVideoView.setViewClickListener {
//            if (mIsStarted) {
//                vquVisibleButton()
//            }
//        }

        mBinding.remoteVideoAvatar.setViewClickListener {

            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (mBinding.fbcvFaceBeauty.isVisible) {
                mBinding.fbcvFaceBeauty.gone()
                mBinding.rlDealLayout.visible()
            } else {
                if (VideoCallManager.isStarted) {
                    vquVisibleButton()
                }
            }
        }

        mBinding.videoGift.setViewClickListener {

            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (mBinding.fbcvFaceBeauty.isVisible) {
                mBinding.fbcvFaceBeauty.gone()
                mBinding.rlDealLayout.visible()
            } else {

                if (!mIsShowButton) {
                    vquVisibleButton()
                    return@setViewClickListener
                }

                clickGift()
            }
        }

        mBinding.sllAgoraVquVideoCloseCamera.setViewClickListener(500) {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (mBinding.fbcvFaceBeauty.isVisible) {
                mBinding.fbcvFaceBeauty.gone()
                mBinding.rlDealLayout.visible()
            } else {

                clickCloseCamera()
            }
        }

        mBinding.openMiniWindow.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (mBinding.fbcvFaceBeauty.isVisible) {
                mBinding.fbcvFaceBeauty.gone()
                mBinding.rlDealLayout.visible()
            } else {
                if (!mIsShowButton) {
                    vquVisibleButton()
                    return@setViewClickListener
                }
                clickMini()
            }
        }
    }

    private fun clickMini() {

        VideoCallManager.showMiniWindow()

        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.ExitMatchEvent()
            .apply { type = 1 })

        finish()
    }

    private fun clickCloseCamera() {
        mEnableVideo = !mEnableVideo

        switchCloseCameraUi()

        VideoCallManager.setEnableVideo(mEnableVideo)

        RtcEngineManager.getRtcEngine().muteLocalVideoStream(!mEnableVideo)
    }

    private fun switchCloseCameraUi() {
        if (!mEnableVideo) {

//            RtcEngineManager.getRtcEngine().disableVideo()
            mBinding.ivAgoraVquVideoCloseCameraIcon.setImageResource(R.mipmap.agora_vqu_video_open)
            mBinding.tvAgoraVquVideoCloseCameraTxt.text = "打开摄像头"

            closeCamera()

        } else {
            mBinding.ivAgoraVquVideoCloseCameraIcon.setImageResource(R.mipmap.agora_vqu_video_close)
            mBinding.tvAgoraVquVideoCloseCameraTxt.text = "关闭摄像头"
            mBinding.localVideoView.visible()
            mBinding.ivLocalAvatar.gone()
        }
    }

    private fun closeCamera() {
        val vquAvatar = if (mIsCaller) {
            NetBaseUrlConstant.IMAGE_URL + mCallBean?.from_avatar
        } else {
            NetBaseUrlConstant.IMAGE_URL + mCallBean?.to_avatar
        }

        mBinding.ivLocalAvatar.vquLoadRoundImage(
            vquAvatar,
            dp2px(8f),
            R.mipmap.ic_common_head_def
        )

        mBinding.localVideoView.gone()
        mBinding.ivLocalAvatar.visible()
    }

    /**
     * 赠送礼物
     */
    private fun clickGift() {
        val bottomGiftFragmentDialog =
            com.mshy.VInterestSpeed.common.ui.dialog.BottomGiftFragmentDialog()
        bottomGiftFragmentDialog.setAutoTouchClose(false)
        val bundle = Bundle()
        bundle.putInt("sendGiftType", 0)
        bottomGiftFragmentDialog.arguments = bundle
        bottomGiftFragmentDialog.setOnGiftItemClickedListener(object :
            com.mshy.VInterestSpeed.common.ui.dialog.BottomGiftFragmentDialog.OnGiftItemClickedListener {
            override fun onGiftClicked(
                bean: com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean?,
                giftCount: Int
            ) {
                if (bean != null) {
                    //赠送礼物的socket
                    val requestBean: com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<VquGiftBean> =
                        com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<VquGiftBean>()
                    val vquBean = VquGiftBean()
                    vquBean.gift_count = giftCount
                    vquBean.gift_id = bean.id.toString()
                    vquBean.room_id = mVquRoomId
                    vquBean.gift_type_id = bean.gift_type_id
                    requestBean.method = "gift"
                    requestBean.data = vquBean
                    VideoCallManager.sendObjectMessage(requestBean)
                }
            }

            override fun onWalletClicked() {
//                    "余额不足，请先充值".toast()
                //RechargeActivity.getInstance(AgoraWithFUVideoActivity.this);
                mPayViewModel.showRechargeDialog(supportFragmentManager)
//                    val amountDialogFragment = CommonRechargeDialog()
//                    amountDialogFragment.show(supportFragmentManager, "充值")
            }
        })
        bottomGiftFragmentDialog.show(supportFragmentManager)
    }

    private fun calleeAgreeVideo() {
        try {
            val requestBean =
                com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
            requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
            requestBean.data.room_id = mVquRoomId
            requestBean.method = AGREE
            VideoCallManager.sendObjectMessage(requestBean)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun clickBeauty() {
        mBinding.fbcvFaceBeauty.visible()
        mBinding.rlDealLayout.invalidate()
    }

    /**
     * 男女号处理挂断分别处理
     */
    private fun clickCancel() {
        if (VideoCallManager.isStarted) {
            dealHangUpInCalling()
        } else {
            mVquMediaPlayer?.stop()
            if (mIsCaller) {
                //主叫者取消拨打
                callerCancelVideo()
            } else {
                //被叫者取消拨打
                calleeRefuseVideo()
            }
        }
    }

    private fun calleeRefuseVideo() {
        val requestBean =
            com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
        requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
        requestBean.data.room_id = mVquRoomId
        requestBean.method = REFUSE
        VideoCallManager.sendObjectMessage(requestBean)
    }

    private fun callerCancelVideo() {
        if (mCode != VideoCallManager.LINE_IS_BUSY_CODE) {
            val requestBean =
                com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
            requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
            requestBean.data.room_id = mVquRoomId
            requestBean.method = CANCEL
            VideoCallManager.sendObjectMessage(requestBean)
        } else {
            VideoCallManager.hangUpLineIsBusy()
        }
    }

    private fun dealHangUpInCalling() {
        val womanHangUpTips = SpUtils.getInt(SpKey.woman_hang_up_tips)
        LogUtil.e("curSecondInMinute=$mCurSecondInMinute")
        LogUtil.e("woman_hang_up_tips=$womanHangUpTips")
        if (UserManager.userInfo?.gender == 1 && womanHangUpTips != 0
            && mCurSecondInMinute < womanHangUpTips!!
        ) {
            showMultipleHangUpByFemaleDialog()
        } else {
            showNormalHangUpDialog()
        }
    }

    /**
     * 女号主动挂断提示
     */
    private fun showMultipleHangUpByFemaleDialog() {

        com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
            .setTitle("温馨提示")
            .setContent(
                "多次主动挂断音视频通话，会降低你的推荐值以及女神星级。如果发现为恶意、" +
                        "无故挂断导致对方金币损失，平台会进行封号处理！请真诚交友。"
            )
            .setLeftText("继续聊")
            .setRightText("挂断")
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object :
                com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {
                    dialogFragment.dismissAllowingStateLoss()
                }

                override fun onRight(dialogFragment: DialogFragment) {
                    hangUpVideo()
                    dialogFragment.dismissAllowingStateLoss()
                }
            })
            .show(supportFragmentManager)
    }

    /**
     * 正常挂断
     */
    private fun showNormalHangUpDialog() {
        com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setContent("是否挂断")
            .setLeftText("取消")
            .setRightText("确定")
            .setOnClickListener(object :
                com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {
                    dialogFragment.dismissAllowingStateLoss()
                }

                override fun onRight(dialogFragment: DialogFragment) {
                    hangUpVideo()
                    dialogFragment.dismissAllowingStateLoss()
                }
            })
            .show(supportFragmentManager)
    }

    private fun hangUpVideo() {
        val requestBean =
            com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
        requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
        requestBean.data.room_id = mVquRoomId
        requestBean.method = HANGUP
        VideoCallManager.sendObjectMessage(requestBean)
        VideoCallManager.hangup(mVquRoomId)
    }

    private var activityIsShow = true

    override fun onResume() {
        super.onResume()

        AgoraUtils.contactType = AgoraUtils.CONTACT_TYPE_VIDEO
        UserManager.isMatchReturn = false

    }

    override fun onPause() {
        super.onPause()
        activityIsShow = false
    }

    /*
    private fun setRoundVideoView(surface: View) {
        surface.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                if (view == null || outline == null) {
                    return
                }
//                Log.d(TAG, "getOutline: width:${view.width}  height:${view.height}")
//                Log.d(TAG, "getOutline: clipToOutline:${view.clipToOutline}")

                outline.setRoundRect(0, 0, view.width, view.height, 24F)
            }
        }
        surface.clipToOutline = true
    }

     */

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
//        Log.d(TAG, "onJoinChannelSuccess: (channel:${channel}, uid:${uid}, elapsed:${elapsed})")

//        if (mIsCaller){
//            mVquMediaPlayer?.start()
//        }
    }

    override fun onUserOffline(uid: Int, reason: Int) {
//        Log.d(TAG, "onUserOffline: (uid:${uid}, reason:${reason})")
        runOnUiThread { this.onRemoteUserLeft() }
    }

    private fun onRemoteUserLeft() {
        mRemoteUid = -1
        removeRemoteView()
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
//        if (mRemoteUid == -1) {

        Log.d(VideoCallManager.TAG, "onUserJoined in activity: $mVquRoomId")

        runOnUiThread {
            mRemoteUid = uid
            VideoCallManager.setLocalView(mBinding.localVideoView)
            VideoCallManager.setRemoteVideoView(mBinding.remoteVideoView)

            mBinding.localParent.postDelayed({
                mBinding.videoGift.visibility = View.VISIBLE
                vquVisibleButton()
            }, 3 * 1000)
        }
    }

    private fun removeRemoteView() {
        mBinding.remoteVideoView.removeAllViews()
    }

    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        Log.d(TAG, "onRemoteVideoStateChanged: ")
        runOnUiThread {
            changedCamera(reason)
        }
    }

    private fun changedCamera(reason: Int) {
        if (reason == 5) {
            mBinding.remoteVideoView.gone()
            mBinding.remoteVideoAvatar.visible()
            val vquAvatar = if (mIsCaller) {
                NetBaseUrlConstant.IMAGE_URL + mCallBean?.to_avatar
            } else {
                NetBaseUrlConstant.IMAGE_URL + mCallBean?.from_avatar
            }
            mBinding.remoteVideoAvatar.vquLoadImage(
                vquAvatar,
                R.mipmap.ic_common_head_def
            )

        } else if (reason == 0) {
            mBinding.remoteVideoView.visible()
            mBinding.remoteVideoAvatar.gone()
        }
    }

    private fun switchConnecting() {
        mBinding.localParent.visible()
        mBinding.ivVideoBeauty.visible()
        mBinding.videoShoot.visible()
        mBinding.videoCalleeAnswer.gone()
        mBinding.videoRemoteDuration.visible()
        mBinding.vquVideoStatusDes.gone()
        mBinding.openMiniWindow.visible()
        mBinding.videoGift.visible()
        mBinding.vquVideoMike.visible()
    }

    override fun onLeaveChannel(rtcStats: IRtcEngineEventHandler.RtcStats?) {

    }

    override fun onCameraClosed() {

    }

    override fun onSurfaceDestroyed() {
        Log.d(VideoCall.TAG, "onSurfaceDestroyed in activity: $mVquRoomId")
        mVquMediaPlayer?.stop()

//        if (!VideoCallManager.isConnecting()) {
        finish()
//        }
    }

    override fun onDestroy() {


        mVquMediaPlayer?.stop()

        super.onDestroy()
    }

    /**
     * 画面互换，将远端画面和本地画面进行调换
     * 因为无法解决圆角切换后丢失问题，暂时不用
     */
//    private fun switchView(canvas: VideoCanvas?) {
//        if (canvas == null) {
//            return
//        }
//        val parent: ViewGroup? = removeFromParent(canvas)
//        if (parent === mBinding.localVideoView) {
//            mBinding.remoteVideoView.addView(canvas.view)
//            if (canvas.view is SurfaceView) {
//                (canvas.view as SurfaceView).setZOrderMediaOverlay(false)
//            }
//        } else if (parent === mBinding.remoteVideoView) {
//            mBinding.localVideoView.addView(canvas.view)
//            if (canvas.view is SurfaceView) {
//                (canvas.view as SurfaceView).setZOrderMediaOverlay(true)
//            }
//            mBinding.localVideoView.requestFocus()
//            mBinding.localVideoView.invalidate()
//        }
//    }


    private fun showRechargeHintDialog() {
        val dialog = com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
            .setTitle("余额不足")


            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object :
                com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {}
                override fun onRight(dialogFragment: DialogFragment) {
                    mPayViewModel.showRechargeDialog(supportFragmentManager)
//                    val amountDialogFragment = CommonRechargeDialog()
//                    amountDialogFragment.show(supportFragmentManager, "充值")
                }
            })

        if (UserManager.userInfo?.isAnchor == 1) {
            dialog.setContent("余额不足，去充值")
            dialog.setLeftText(getString(R.string.cancel))
            dialog.setRightText(getString(R.string.confirm))
        } else {
            dialog.setContent("金币余额不足1分钟，请及时充值避免错过缘分！")
            dialog.setLeftText(getString(R.string.agora_vqu_dialog_left_txt))
            dialog.setRightText(getString(R.string.agora_vqu_dialog_right_txt))
        }


        dialog.show(supportFragmentManager)
    }

    private var mSvgCount = 0L

    private fun addSvg(giftsvga: String?) {
        mSvgCount++

        val vquSvgaImageView = SVGAImageView(this)
        val vquFlContent = findViewById<FrameLayout>(android.R.id.content)
        vquFlContent.addView(vquSvgaImageView, vquFlContent.childCount)
        SVGAParser.shareParser().init(this)
        val vquSvgaParser = SVGAParser(this)


        try {
            val url = URL(NetBaseUrlConstant.IMAGE_URL + giftsvga)

            vquSvgaParser.decodeFromURL(url, object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    val drawable = SVGADrawable(videoItem)
                    vquSvgaImageView.loops = 1
                    vquSvgaImageView.setImageDrawable(drawable)
                    vquSvgaImageView.callback = object : SVGACallback {
                        override fun onPause() {}
                        override fun onFinished() {
                            vquFlContent.removeView(vquSvgaImageView)
                            mSvgCount--
                        }

                        override fun onRepeat() {}
                        override fun onStep(frame: Int, percentage: Double) {}
                    }
                    vquSvgaImageView.startAnimation()
                }

                override fun onError() {
                    mSvgCount--
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            mSvgCount--
        }

    }

    /**
     * 添加等待处理的礼物消息
     * 同时开启动画效果展示
     */
    private fun addWaitGiftBean(
        noticeGiftBean: com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean,
        fromUid: String?
    ) {
        val id: String
        val nickName: String
        val avater: String
        if (UserManager.userInfo == null) {
            UserManager.userInfo = UserSpUtils.getBean(SpKey.userInfo, VquUserInfo::class.java)
        }
        if (fromUid.equals(UserManager.userInfo?.userId)) {
            id = UserManager.userInfo?.userId ?: ""
            nickName = UserManager.userInfo?.nickname ?: ""
            avater = NetBaseUrlConstant.IMAGE_URL + UserManager.userInfo?.avatar
        } else {
            if (mIsCaller) {
                id = mCallBean?.to_uid.toString()
                nickName = mCallBean?.to_nickname ?: ""
                avater = NetBaseUrlConstant.IMAGE_URL + mCallBean?.to_avatar
            } else {
                id = mCallBean?.from_uid?.toString() ?: ""
                nickName = mCallBean?.from_nickname ?: ""
                avater = NetBaseUrlConstant.IMAGE_URL + mCallBean?.from_avatar
            }
        }

        val giftModel = GiftModel()
        giftModel.setGiftId("" + noticeGiftBean.giftid)
            .setGiftName("赠送了" + noticeGiftBean.giftname)
            .setGiftCount(noticeGiftBean.giftcount)
            .setGiftPic(NetBaseUrlConstant.IMAGE_URL + noticeGiftBean.gifticon)
            .setSendUserId(id)
            .setSendUserName(nickName)
            .setSendUserPic(avater)
            .setSendGiftTime(System.currentTimeMillis()).isCurrentStart = false

        Log.d(TAG, "addWaitGiftBean 1: ")
        giftControl.loadGift(giftModel)
    }


    private var mIsShowButton = true

    /**
     * 修改界面显示模式
     */
    private fun vquVisibleButton() {
        if (mIsShowButton) {
            ObjectAnimator.ofFloat(mBinding.vquVideoHeadLayout, "alpha", 1F, 0F)
                .setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.stvAgoraVquWarm, "alpha", 1F, 0F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.rlDealLayout, "alpha", 1F, 0F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.openMiniWindow, "alpha", 1F, 0F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.videoGift, "alpha", 1F, 0F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.videoRemoteDuration, "alpha", 1F, 0F)
                .setDuration(300)
                .start()
            mIsShowButton = false
        } else {
            ObjectAnimator.ofFloat(mBinding.vquVideoHeadLayout, "alpha", 0F, 1F)
                .setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.stvAgoraVquWarm, "alpha", 0F, 1F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.rlDealLayout, "alpha", 0F, 1F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.openMiniWindow, "alpha", 0F, 1F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.videoGift, "alpha", 0F, 1F).setDuration(300)
                .start()
            ObjectAnimator.ofFloat(mBinding.videoRemoteDuration, "alpha", 0F, 1F)
                .setDuration(300)
                .start()
            mIsShowButton = true
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onFloatingEvent(event: FloatingEvent) {
//
//        AppManager.isMiniWindow = false
//
//        val videoCanvas = mRemoteVideoCanvas
//
//        if (videoCanvas?.view == null) {
//            return
//        }
//
////        removeFromParent(videoCanvas)
//
//        mBinding.remoteVideoView.addView(videoCanvas.view)
//
//        if (cameraState == 5) {
//            mBinding.remoteVideoView.gone()
//            mBinding.remoteVideoAvatar.visible()
//        } else {
//            mBinding.remoteVideoView.visible()
//            mBinding.remoteVideoAvatar.gone()
//        }
//
//        FloatWindow.get().hide()
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onExitAgoraEvent(event: ExitAgoraEvent) {
//        hangUpVideo()
//    }

    override fun finish() {
//        ActivityStackManager.popActivityToStack(this)
        VideoCallManager.removeWebSocketListener(this)
        VideoCallManager.removeRoomStateListener(this)
        super.finish()
    }

    override fun onRequest() {

    }

    override fun onResponse() {
        runOnUiThread {
            if (isMatch) {
                calleeAgreeVideo()
            } else {
                mVquMediaPlayer?.start()
            }
        }
    }

    override fun onStartConnecting() {
//        startVideo()

        mVquMediaPlayer?.stop()

        runOnUiThread {
            switchConnecting()
        }
    }

    override fun onHangUp(json: String) {
        mVquMediaPlayer?.stop()
    }

    override fun onNotice(json: String) {

    }

    override fun onTimeChanged(time: String, currentSecond: Long) {
        mCurSecondInMinute = currentSecond
        mBinding.videoRemoteDuration.text = time
    }

    override fun onAddWaitGiftBean(
        noticeGiftBean: com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean,
        fromUid: String?
    ) {
        Log.d(TAG, "onAddWaitGiftBean: ")
        runOnUiThread {
            addWaitGiftBean(noticeGiftBean, fromUid)
        }
    }

    override fun onAddSvg(giftsvga: String?) {
        Log.d(TAG, "onAddSvg: ")
        addSvg(giftsvga)
    }

    override fun onNoCoin() {
        showRechargeHintDialog()
    }

    override fun onPronRemind() {
        VideoCallManager.setEnableVideo(false)

        RtcEngineManager.getRtcEngine().muteLocalVideoStream(true)
        mBinding.sllAgoraVquVideoCloseCamera.visibility = View.GONE
        closeCamera()
    }
}
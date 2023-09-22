package com.live.module.agora.activity

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.live.module.agora.R
import com.live.module.agora.RtcEngineManager
import com.live.module.agora.bean.VquGiftBean
import com.live.module.agora.databinding.AgoraTantaActivityAudioBinding
import com.live.module.agora.manager.AudioCallManager
import com.live.module.agora.manager.VideoCallManager
import com.live.module.agora.vm.AgoraTantaAudioViewModel
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.constant.*
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.*
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import com.opensource.svgaplayer.*
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc.IRtcEngineEventHandler
import org.dync.giftlibrary.GiftControl
import org.dync.giftlibrary.widget.CustormAnim
import org.dync.giftlibrary.widget.GiftModel
import org.greenrobot.eventbus.EventBus
import org.jetbrains.annotations.NotNull
import java.net.MalformedURLException
import java.net.URL

/**
 * author: Lau
 * date: 2022/7/25
 * description:
 */
@Route(path = RouteUrl.Agora2.AgoraTantaAudioActivity)
@EventBusRegister
@AndroidEntryPoint
class AgoraTantaAudio2Activity : BaseActivity<AgoraTantaActivityAudioBinding, AgoraTantaAudioViewModel>(),
    AudioCallManager.RoomStateListener, AudioCallManager.WebSocketListener {

    override val mViewModel: AgoraTantaAudioViewModel by viewModels()

    /**
     * MP3播放器，铃声
     */
    private var mVquMediaPlayer: MediaPlayer? = null

    private var mCurSecondInMinute = 0L

    private var mIsConnecting = false

//    private var mIsStarted = false

    private var mRemoteUid = -1

    companion object {
        private val TAG = AgoraTantaAudio2Activity::class.java.simpleName
    }

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

    private var activityIsShow = true

//    private var isNoUseMikePhone = false

    // 礼物面板
    private val giftControl: GiftControl by lazy {
        GiftControl(this@AgoraTantaAudio2Activity)
            .apply {
                setGiftLayout(mBinding.vquVoiceGift1, 2)
                setHideMode(false)
                setCustormAnim(CustormAnim())
            }
    }

    override fun onStart() {
        super.onStart()
//        FloatWindow.get().hide()

        activityIsShow = true
        ActivityStackManager.addSingleInstanceActivityToStack(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d(AudioCallManager.TAG, "onResume: " + mVquRoomId)
        AgoraUtils.contactType = AgoraUtils.CONTACT_TYPE_AUDIO
        UserManager.isMatchReturn = false
    }

    override fun onPause() {
        super.onPause()
        Log.d(AudioCallManager.TAG, "onPause: " + mVquRoomId)
        activityIsShow = false
    }


    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(false)
            .init()
    }


    override fun AgoraTantaActivityAudioBinding.initView() {

        UserManager.videoRequestBean = null

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

        UserManager.isVideo = true

        if (AudioCallManager.isVideo && mCallBean == null) {
            mCallBean = AudioCallManager.getCallBean()
            mVquRoomId = AudioCallManager.getVideoCall()?.vquRoomId ?: 0
            mIsCaller = AudioCallManager.getVideoCall()?.isCaller ?: false

            val isNoUseMikePhone = AudioCallManager.getVideoCall()?.isNoUseMikePhone ?: false

            mBinding.vquVoiceMike.isSelected = isNoUseMikePhone

            AudioCallManager.setNoUseMikePhone(isNoUseMikePhone)

//            RtcEngineManager.getRtcEngine().muteLocalAudioStream(isNoUseMikePhone)

            val enableSpeakerphone = AudioCallManager.getVideoCall()?.enableSpeakerphone ?: true
            mBinding.vquVoiceSound.isSelected = !enableSpeakerphone

            initData()

            AudioCallManager.addRoomStateListener(this@AgoraTantaAudio2Activity)
            AudioCallManager.addWebSocketListener(this@AgoraTantaAudio2Activity)

            switchConnecting()

        } else {
            initRoom()
            initData()
            initPermission()
        }

        initEvent()


        mBinding.stvAgoraVquWarm.isSelected = true


    }

    private fun initRoom() {
        mVquMediaPlayer = MediaPlayer.create(this, R.raw.resources_vqu_call_bg)
        mVquMediaPlayer?.isLooping = true
        mBinding.openMiniWindow.postDelayed({ mVquMediaPlayer?.start() }, 2000)
    }

    private fun initEvent() {

        mBinding.vquVoiceCalleeAnswer.setViewClickListener {

            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            calleeAgreeVideo()
        }

        mBinding.vquVoiceCalleeCancel.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }
            clickCancel()
        }

        mBinding.vquVoiceMike.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }
            mBinding.vquVoiceMike.isSelected = !mBinding.vquVoiceMike.isSelected

            if (mBinding.vquVoiceMike.isSelected) {
                toast("已关闭麦克风")
            }else{
                toast("已开启麦克风")
            }

            AudioCallManager.setNoUseMikePhone(mBinding.vquVoiceMike.isSelected)


        }

        mBinding.videoGift.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }
            clickGift()
        }

        mBinding.vquVoiceSound.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }

            if (mBinding.vquVoiceSound.isSelected) {
                mBinding.vquVoiceSound.isSelected = false
                toast("已开启扬声器")
            } else {
                toast("已开启听筒")
                mBinding.vquVoiceSound.isSelected = true
            }

            AudioCallManager.setEnableSpeakerphone(!mBinding.vquVoiceSound.isSelected)

            RtcEngineManager.getRtcEngine()
                .setEnableSpeakerphone(!mBinding.vquVoiceSound.isSelected)
        }

        mBinding.openMiniWindow.setViewClickListener {
            if (mSvgCount > 0) {
                return@setViewClickListener
            }
            clickMini()
        }
    }

    private fun clickMini() {

        AudioCallManager.showMiniWindow()

//        AppManager.isMiniWindow = true
//
//        FloatWindow.get().show()
//        val view = FloatWindow.get().view
//
//        view.findViewById<FrameLayout>(R.id.floating_video_parent).gone()
//        view.findViewById<CardView>(R.id.floating_audio_contact_parent).visible()
//        view.findViewById<CardView>(R.id.floating_end_contact_parent).gone()

        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.ExitMatchEvent()
            .apply { type = 1 })

        finish()

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
            override fun onGiftClicked(bean: com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean?, giftCount: Int) {
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
                    AudioCallManager.sendObjectMessage(requestBean)
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

    private fun clickCancel() {
        if (AudioCallManager.isStarted) {
            dealHangUpInCalling()
        } else {
            mVquMediaPlayer?.stop()
            if (mIsCaller) {
                //主叫者取消拨打
                callerCancelVoice()
            } else {
                //被叫者取消拨打
                calleeRefuseVoice()
            }
        }
    }


    private fun calleeRefuseVoice() {
        val requestBean =
            com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
        requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
        requestBean.data.room_id = mVquRoomId
        requestBean.method = REFUSE
        AudioCallManager.sendObjectMessage(requestBean)
    }

    private fun callerCancelVoice() {
        if (mCode != AudioCallManager.LINE_IS_BUSY_CODE) {
            val requestBean =
                com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
            requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
            requestBean.data.room_id = mVquRoomId
            requestBean.method = CANCEL
            AudioCallManager.sendObjectMessage(requestBean)
        } else {
            AudioCallManager.hangUpLineIsBusy()
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
            .setOnClickListener(object : com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {
                    dialogFragment.dismissAllowingStateLoss()
                }

                override fun onRight(dialogFragment: DialogFragment) {
                    hangUpVoice()
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
            .setOnClickListener(object : com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {
                    dialogFragment.dismissAllowingStateLoss()
                }

                override fun onRight(dialogFragment: DialogFragment) {
                    hangUpVoice()
                    dialogFragment.dismissAllowingStateLoss()
                }
            })
            .show(supportFragmentManager)
    }

    private fun hangUpVoice() {
        val requestBean =
            com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
        requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
        requestBean.data.room_id = mVquRoomId
        requestBean.method = HANGUP
        AudioCallManager.sendObjectMessage(requestBean)
    }

    private fun initPermission() {
        PermissionUtils.vquVoicePermission(this@AgoraTantaAudio2Activity,
            PermissionDescribe.vquVoicePermissionTxt,
            PermissionDescribe.vquVoicePermissionTxt,
            { allGranted, _, _ ->
                if (allGranted) {
                    initVoice()
                } else {
                    hangUpVoice()
                    AudioCallManager.release()
                    mVquMediaPlayer?.stop()
                    finish()
                }
            }
        ) {
            hangUpVoice()
            AudioCallManager.release()
            mVquMediaPlayer?.stop()
            finish()
            false
        }
    }


    private fun initVoice() {

        AudioCallManager.init(mCallBean, mVquRoomId, mSocketUrl, mIsCaller, isMatch)

        AudioCallManager.initAudio()

        AudioCallManager.addRoomStateListener(this)
        AudioCallManager.addWebSocketListener(this)

        if (mCode != VideoCallManager.LINE_IS_BUSY_CODE) {
            AudioCallManager.initWebSocket()
        } else {
            AudioCallManager.ringTimer()
        }

    }

    private fun initData() {
        if (isMatch) {
            com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(this, com.mshy.VInterestSpeed.common.utils.UmUtils.TRUNONVIDEOMATCH)
        }

        try {
            mVquRoomId = mCallBean?.room_id?.toInt() ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            toast("无法获取通话ID")
        }

        setUserInfo()

    }

    override fun initObserve() {

    }

    override fun initRequestData() {
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

            mBinding.vquVoiceCalleeCancel.visible()
            mBinding.vquVoiceCalleeAnswer.gone()

            avatar = NetBaseUrlConstant.IMAGE_URL + mCallBean?.to_avatar
            nickname = mCallBean?.to_nickname
            gender = mCallBean?.to_gender
            age = mCallBean?.to_age_text
            height = mCallBean?.to_height
            occupation = mCallBean?.to_occupation
            mBinding.vquVoiceStatusDes.text = "正在等待对方接受邀请…"
        } else {

            mBinding.vquVoiceCalleeCancel.visible()
            mBinding.vquVoiceCalleeAnswer.visible()

            avatar = NetBaseUrlConstant.IMAGE_URL + mCallBean?.from_avatar
            nickname = mCallBean?.from_nickname
            gender = mCallBean?.from_gender
            age = mCallBean?.from_age_text
            height = mCallBean?.from_height
            weight = mCallBean?.from_weight
            occupation = mCallBean?.from_occupation

            mBinding.vquVoiceStatusDes.text = "正在邀请你语音通话"
        }

        mBinding.vquIvCallHead.vquLoadRoundImage(
            avatar,
            dp2px(60f),
            R.mipmap.ic_common_head_def
        )

        Glide.with(this)
            .load("$avatar?x-oss-process=image/blur,r_25,s_25/quality,q_30")
            .into(mBinding.vquVoiceBg)

        mBinding.vquVoiceStartName.text = nickname

//        CommonVquAddRankAndGradeViewHelper.addGender(
//            this,
//            findViewById(R.id.layout_name_id),
//            gender,
//            ""
//        )
//        //年龄
//        if (!age.isNullOrEmpty()) {
//            if (desc.isNotEmpty()) {
//                desc.append(" | ")
//            }
//            desc.append(age)
//        }
//
//        //身高
//        if (!height.isNullOrEmpty()) {
//            if (desc.isNotEmpty()) {
//                desc.append(" | ")
//            }
//            desc.append(height)
//        }
//
//        //职业
//        if (!weight.isNullOrEmpty()) {
//            if (desc.isNotEmpty()) {
//                desc.append(" | ")
//            }
//            desc.append(weight)
//        }
//        mBinding.videoStartCost.text = desc.toString()
    }


    override fun onDestroy() {

        Log.d(AudioCallManager.TAG, "onDestroy: " + mVquRoomId)

        mVquMediaPlayer?.stop()
        mVquMediaPlayer = null

        super.onDestroy()
    }

    private fun showRechargeHintDialog() {
        val dialog = com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
            .setTitle("余额不足")
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object : com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
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

    /**
     * 添加svg动画
     */
    fun addSvg(vquSvgUrl: String?) {
        mSvgCount++

        val vquSvgaImageView = SVGAImageView(this)

        val vquFlContent = findViewById<FrameLayout>(android.R.id.content)
        vquFlContent.addView(vquSvgaImageView, vquFlContent.childCount)
        SVGAParser.shareParser().init(this)
        val vquSvgaParser = SVGAParser(this)

        try {
            val url = URL(NetBaseUrlConstant.IMAGE_URL + vquSvgUrl)

            vquSvgaParser.decodeFromURL(url, object : SVGAParser.ParseCompletion {
                override fun onComplete(@NotNull svgaVideoEntity: SVGAVideoEntity) {
                    val drawable = SVGADrawable(svgaVideoEntity)
                    vquSvgaImageView.loops = 1
                    vquSvgaImageView.setImageDrawable(drawable)
                    vquSvgaImageView.callback = object : SVGACallback {
                        override fun onPause() {

                        }

                        override fun onFinished() {
                            vquFlContent.removeView(vquSvgaImageView)
                            mSvgCount--
                        }

                        override fun onRepeat() {

                        }

                        override fun onStep(i: Int, v: Double) {

                        }
                    }
                    vquSvgaImageView.startAnimation()


                }

                override fun onError() {
                    mSvgCount--
                }
            })
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            mSvgCount--
        }
    }

    /**
     * 添加等待处理的礼物消息
     * 同时开启动画效果展示
     */
    private fun addWaitGiftBean(noticeGiftBean: com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean, fromUid: String?) {
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
                id = mCallBean?.from_uid.toString()
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
        giftControl.loadGift(giftModel)
    }


    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        Log.d(
            AudioCallManager.TAG,
            "onJoinChannelSuccess: (channel:${channel}, uid:${uid}, elapsed:${elapsed})"
        )
        mIsConnecting = true
        runOnUiThread {
//            mBinding.videoCalleeCancel.visible()
//            mBinding.videoCalleeAnswer.gone()
        }
    }

    override fun onUserOffline(uid: Int, reason: Int) {
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
//        Log.d(AudioCallManager.TAG, "onUserJoined: (uid:${uid}, elapsed:${elapsed})")
        if (mRemoteUid == -1) {
            runOnUiThread {
                mRemoteUid = uid
            }
        }
    }


    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
    }

    override fun onLeaveChannel(rtcStats: IRtcEngineEventHandler.RtcStats?) {
        mIsConnecting = false
//        finish()
    }

    override fun onCameraClosed() {

    }

    override fun onSurfaceDestroyed() {

    }

    override fun onFinish() {
        finish()
    }


    private fun switchConnecting() {
//        mBinding..gone()
        mBinding.vquVoiceCalleeAnswer.gone()
        mBinding.openMiniWindow.visible()
        mBinding.vquVoiceMike.visible()
        mBinding.vquVoiceSound.visible()
        mBinding.videoGift.visible()
//        mBinding.videoGift.visible()
        val desc = StringBuilder()
        val age: String?
        val height: String?
        val weight: String?
        val occupation: String?

        if (mIsCaller) {
            age = mCallBean?.to_age_text
            height = mCallBean?.to_height
            weight = mCallBean?.to_weight
            occupation = mCallBean?.to_occupation
        } else {
            age = mCallBean?.from_age_text
            height = mCallBean?.from_height
            weight = mCallBean?.from_weight
            occupation = mCallBean?.from_occupation
        }

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

        mBinding.vquVoiceStatusDes.text = desc
        mBinding.vquVoiceStatusDes.setTextColor(Color.parseColor("#FFA3AABE"))

    }

    private fun calleeAgreeVideo() {
        try {
            val requestBean =
                com.mshy.VInterestSpeed.common.bean.websocket.WebSocketRequestBean<com.mshy.VInterestSpeed.common.bean.RoomIdBean>()
            requestBean.data = com.mshy.VInterestSpeed.common.bean.RoomIdBean()
            requestBean.data.room_id = mVquRoomId
            requestBean.method = AGREE
            AudioCallManager.sendObjectMessage(requestBean)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onBackPressed() {
//        super.onBackPressed()
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onFloatingEvent(event: FloatingEvent) {
//        AppManager.isMiniWindow = false
//
//        FloatWindow.get().hide()
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onExitAgoraEvent(event: ExitAgoraEvent) {
//        hangUpVoice()
//    }

    override fun finish() {
        AudioCallManager.removeWebSocketListener(this)
        AudioCallManager.removeRoomStateListener(this)
        super.finish()
    }

    override fun onRequest() {

    }

    override fun onResponse() {
        runOnUiThread {
            Log.d(TAG, "onResponse: " + isMatch)
            if (isMatch) {
                calleeAgreeVideo()
            } else {
                mVquMediaPlayer?.start()
            }
        }
    }

    override fun onStartConnecting() {
        mVquMediaPlayer?.stop()

        runOnUiThread {
            switchConnecting()

        }

    }

    override fun onHangUp(json: String) {
        Log.d(TAG, "onHangUp: ")
        mVquMediaPlayer?.stop()
    }

    override fun onNotice(json: String) {
    }

    override fun onTimeChanged(time: String, currentSecond: Long) {
        mBinding.vquVoiceRemoteDuration.text = time
        mCurSecondInMinute = currentSecond
    }

    override fun onAddWaitGiftBean(noticeGiftBean: com.mshy.VInterestSpeed.common.bean.websocket.NoticeGiftBean, fromUid: String?) {
        addWaitGiftBean(noticeGiftBean, fromUid)
    }

    override fun onAddSvg(giftsvga: String?) {
        addSvg(giftsvga)
    }

    override fun onNoCoin() {
        showRechargeHintDialog()
    }

}
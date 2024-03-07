package com.live.module

import android.app.Application
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.apkfuns.logutils.LogUtils
import com.google.auto.service.AutoService
import com.google.gson.Gson
import com.heytap.msp.push.HeytapPushManager
//import com.huawei.hms.support.common.ActivityMgr/华为
import com.live.module.message.constant.NIMVquConfig
import com.live.module.message.mixpush.DemoMixPushMessageHandler
import com.live.module.message.mixpush.DemoPushContentProvider
import com.live.module.message.viewholder.TipViewHolder
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.app.AppFrontBackHelper
import com.live.vquonline.base.app.ApplicationLifecycle
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.bean.notification.NotificationEvent
import com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.event.CommonNotificationEvent
import com.mshy.VInterestSpeed.common.event.DynamicLikeEvent
import com.mshy.VInterestSpeed.common.event.RedPacketEvent
import com.mshy.VInterestSpeed.common.event.UnReadCountEvent
import com.mshy.VInterestSpeed.common.ui.view.notification.SwipeNotificationManager
import com.mshy.VInterestSpeed.common.utils.CallUtil
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.uikit.NiMUIKitVquApplication
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.api.model.recent.RecentCustomization
import com.mshy.VInterestSpeed.uikit.attchment.*
import com.mshy.VInterestSpeed.uikit.bean.NIMVquIntimateChangeBean
import com.mshy.VInterestSpeed.uikit.bean.NIMVquIntimateLeveUpBean
import com.mshy.VInterestSpeed.uikit.business.session.helper.MessageHelper
import com.mshy.VInterestSpeed.uikit.business.session.module.MsgRevokeFilter
import com.mshy.VInterestSpeed.uikit.business.session.viewholder.*
import com.mshy.VInterestSpeed.uikit.event.NotificationIntimateChangeEvent
import com.mshy.VInterestSpeed.uikit.event.NotificationIntimateUpEvent
import com.mshy.VInterestSpeed.uikit.event.NotificationProtectionStatusEvent
import com.mshy.VInterestSpeed.uikit.impl.customization.DefaultRecentCustomization
import com.mshy.VInterestSpeed.uikit.util.IntimateUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.mixpush.MixPushConfig
import com.netease.nimlib.sdk.mixpush.NIMPushClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.msg.model.RevokeMsgNotification
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * FileName: com.live.module
 * Date: 2022/4/12 15:44
 * Description:
 * History:
 */
@AutoService(ApplicationLifecycle::class)
@RequiresApi(Build.VERSION_CODES.M)
class MessageVquApplication : NiMUIKitVquApplication() {

    var mWindowManager: SwipeNotificationManager? = null
    var isAppFont = false
    override fun onCreate(application: Application) {
        val result = SpUtils.initMMKV(BaseApplication.context)


        val helper = AppFrontBackHelper()
        helper.register(application, object : AppFrontBackHelper.OnAppStatusListener {
            override fun onFront() {
                isAppFont = true
                // mWindowManager?.addNotification("吃掉你的胰脏", R.mipmap.ic_launcher)
            }

            override fun onBack() {
                isAppFont = false
                mWindowManager?.removeNotificationAll()
            }
        })
        Log.d("BaseApplication", "MMKV -->> $result")
        super.onCreate(application)

    }

    class MyMsgRevokeFilter :
        MsgRevokeFilter {
        override fun shouldIgnore(message: IMMessage?): Boolean {
            return false
        }

    }

    override fun vquOtherNIMConfig() {
        //是否开启通知栏消息提醒
        NIMClient.toggleNotification(SpUtils.getBoolean(SpKey.NEW_MESSAGE_NOTICE, true) == true)
        // 注册自定义推送消息处理，这个是可选项
        NIMPushClient.registerMixPushMessageHandler(DemoMixPushMessageHandler())
        NimUIKit.setCustomPushContentProvider(DemoPushContentProvider())
        NimUIKit.setMsgRevokeFilter(MyMsgRevokeFilter())
        NimUIKit.setRecentCustomization(getRecentCustomization())
        NimUIKit.setEarPhoneModeEnable(false)
        //消息回调监听
        NIMClient.getService(MsgServiceObserve::class.java)
            .observeRecentContact({
                if (it.isNotEmpty()) {
                    val unreadNum = NIMClient.getService(MsgService::class.java).totalUnreadCount
                    EventBus.getDefault().post(UnReadCountEvent(
                        unreadNum))
                } else {
                    EventBus.getDefault().post(UnReadCountEvent(
                        0))
                }
            }, true)
        NIMClient.getService(MsgServiceObserve::class.java)
            .observeRevokeMessage(revokeMessageObserver, true)
        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOnlineStatus({
                when (it) {
                    //被踢下线
                    StatusCode.KICK_BY_OTHER_CLIENT, StatusCode.KICKOUT -> {
                        IntimateUtils.getInstance().clearData()
                        NimUIKit.logout()
                        NIMClient.getService(AuthService::class.java).logout()
                        UserSpUtils.clear()
                        EventBus.getDefault().post("mainFinish")
                        ARouter.getInstance()
                            .build(RouteUrl.Login.LoginTantaLoginActivity)
                            .navigation()
                        toast("账户在其它客户端登录，被踢下线")
                        EventBus.getDefault().post(ExitAgoraEvent())

                    }
                    StatusCode.UNLOGIN -> {
//                        var userinfo = UserManager.userInfo ?: return@observeOnlineStatus
//                        val account = UserSpUtils.getString(SpKey.im_account)
//                        val token = UserSpUtils.getString(SpKey.im_token)
//                        if (userinfo != null && !TextUtils.isEmpty(account) && !TextUtils.isEmpty(
//                                token)
//                        ) {
//                            vquInitNIM()
//                        } else {
//                            UIKitUtils.loginNIM(userinfo.userId, userinfo.imToken) {
//                                object : RequestCallback<LoginInfo> {
//                                    override fun onSuccess(param: LoginInfo?) {
//                                        Log.d("LoginNIM", "onSuccess: " + param?.account)
//                                        if (param != null) {
//                                            UserSpUtils.put(SpKey.isLogin, true)
//                                            UserSpUtils.putString(SpKey.im_account, param.account)
//                                            UserSpUtils.putString(SpKey.im_token, param.token)
//                                            UserManager.userInfo = userinfo
//                                            UserSpUtils.putBean(SpKey.userInfo, userinfo)
//                                        }
//                                        EventBus.getDefault().post("ImLoginSuccess")
//                                    }
//
//                                    override fun onFailed(code: Int) {
//                                        ("登录失败$code").logI()
//                                    }
//
//                                    override fun onException(exception: Throwable?) {
//                                        ("登录失败${exception.toString()}").logI()
//                                    }
//                                }
//                            }
//                        }
                    }
                    else -> {
                    }
                }
            }, true)
    }

    /**
     * 消息撤回观察者
     */
    private val revokeMessageObserver: Observer<RevokeMsgNotification> =
        Observer { notification ->
            if (notification == null || notification.message == null) {
                return@Observer
            }
            val message = notification.message
            MessageHelper.getInstance().onRevokeMessage(message, message.sessionId) //撤回消息了 插入一条提醒消息
        }

    override fun vquRegisterMsgViewHolder() {
        NIMClient.getService(MsgService::class.java)
            .registerCustomAttachmentParser(MessageVquCustomParser())

        NimUIKit.registerTipMsgViewHolder(TipViewHolder::class.java)
        NimUIKit.registerMsgItemViewHolder(
            MessageVquGiftAttachment::class.java,
            MsgViewHolderGift::class.java
        )//礼物
        NimUIKit.registerMsgItemViewHolder(
            MessageVquSysInfoAttachment::class.java,
            MsgViewHolderSysInfo::class.java
        )
        NimUIKit.registerMsgItemViewHolder(
            SysInfoSingleImageAttachment::class.java,
            MsgViewHolderSysInfoSingleImage::class.java
        )
        NimUIKit.registerMsgItemViewHolder(
            SysInfoDoubleImageAttachment::class.java,
            MsgViewHolderSysInfoDoubleImage::class.java
        )
        NimUIKit.registerMsgItemViewHolder(
            MessageVquVideoAttachment::class.java,
            MsgViewHolderVideoChat::class.java
        ) //视频通知
        NimUIKit.registerMsgItemViewHolder(
            MessageVquFateMatchAttachment::class.java,
            MsgViewHolderFateMatch::class.java
        ) //缘分牵线
        NimUIKit.registerMsgItemViewHolder(
            MessageVquAudioAttachment::class.java,
            MsgViewHolderAudioChat::class.java
        ) //语音通知
        NimUIKit.registerMsgItemViewHolder(
            MessageVquInviteChatAttachment::class.java,
            MsgViewHolderInviteChat::class.java
        ) //邀请通话
    }


    override fun vquRegisterNIMObserver() {

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeCustomNotification({
                val content = it?.content
                LogUtils.d("vquRegisterNIMObserver" + content)
                val json = JSONObject.parseObject(content)
                val id = json.getIntValue("id")
                val gson = Gson()

                when (id) {
//                    1 -> {
//                        val unreadNum = NIMClient.getService(MsgService::class.java).totalUnreadCount
//                        BadgerNum.setBadgerNum(BaseApplication.context,unreadNum)
//                    }
                    17 -> {
                        //亲密值变动通知
                        EventBus.getDefault().post(
                            NotificationIntimateChangeEvent(
                                gson.fromJson(
                                    content,
                                    NIMVquIntimateChangeBean::class.java
                                )
                            )
                        )
                    }
                    18 -> {
                        val upBean = gson.fromJson(
                            content,
                            NIMVquIntimateLeveUpBean::class.java
                        )
                        val userInfo = UserSpUtils.getUserBean()
                        val userId = if (userInfo?.userId?.toInt() == upBean.data.fromUid) {
                            upBean.data.toUid
                        } else {
                            upBean.data.fromUid
                        }
                        IntimateUtils.getInstance().putData(
                            userId,
                            upBean.data.currentScore,
                            upBean.data.currentGrade.toInt()
                        )
                        EventBus.getDefault().post(NotificationIntimateUpEvent(
                            upBean))
                    }
                    19 -> {//红包
                        val task: TimerTask = object : TimerTask() {
                            //延时一秒
                            override fun run() {
                                EventBus.getDefault().post(
                                    RedPacketEvent(
                                        gson.fromJson(
                                            json.getString(
                                                "data"
                                            ),
                                            CommonVquRedPacketBean::class.java
                                        )
                                    )
                                )
                            }
                        }
                        val timer = Timer()
                        timer.schedule(task, 1000)
                    }
                    98 -> {
                        Handler().postDelayed({
                            var matchData = gson.fromJson(
                                json.getString("data"),
                                AgoraMtachCallBean::class.java
                            )
                            mWindowManager?.removeNotification(matchData.match_id)
                        }, 100)

                    }
                    99 -> {//音视频速配
                        try {


                            Handler().postDelayed({
                                var matchData = gson.fromJson(
                                    json.getString("data"),
                                    AgoraMtachCallBean::class.java
                                )
                                if (!UserManager.isVideo) {
                                    // EventBus.getDefault().post(NotificationManagerEvent(1))
                                    if (Settings.canDrawOverlays(BaseApplication.context)) {
                                        if (mWindowManager == null) {
                                            mWindowManager =
                                                SwipeNotificationManager.getInstance(BaseApplication.application)
                                        }
                                        mWindowManager?.addNotification(matchData)
                                    }
                                }
//                                if (UserManager.isMatch) {
//                                    "正在连接中...".toast()
//                                    EventBus.getDefault().post(matchData)
//                                } else {
//
//                                }
                            }, 100)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            LogUtils.d("matchDataex" + ex.printStackTrace())
                        }
                    }

                    20 -> {
                        val isAuth = json.getJSONObject("data").getIntValue("is_auth")
                        val userInfo = UserSpUtils.getUserBean()
                        userInfo?.let {
                            userInfo.isAuth = isAuth
                            UserSpUtils.putUserBean(userInfo)
                        }
                    }
                    22 -> {
                        EventBus.getDefault().post(
                            CommonNotificationEvent(
                                gson.fromJson(
                                    json.getString(
                                        "data"
                                    ),
                                    NotificationBean::class.java
                                )
                            )
                        )
                    }
                    23 -> {
                        EventBus.getDefault().post(
                            DynamicLikeEvent(
                                gson.fromJson(
                                    json.getString(
                                        "data"
                                    ),
                                    DynamicLikeBean::class.java
                                )
                            )
                        )
                    }
                    73 -> {
                        val upBean = gson.fromJson(
                            content,
                            NIMVquIntimateLeveUpBean::class.java
                        )
                        IntimateUtils.getInstance().putProtectionData(
                            upBean.data.userId,
                            1,
                        )
                        EventBus.getDefault().post(
                            NotificationProtectionStatusEvent()
                        )
                    }
                    else -> {

                        var vquVideoRequestBean =
                            gson.fromJson(content, VideoRequestBean::class.java)
                        LogUtils.e("音视频消息推送...")
                        if (vquVideoRequestBean != null) {
                            if (11 == vquVideoRequestBean.id || 10 == vquVideoRequestBean.id
                                || 12 == vquVideoRequestBean.id || 13 == vquVideoRequestBean.id
                                || 14 == vquVideoRequestBean.id || 15 == vquVideoRequestBean.id || 16 == vquVideoRequestBean.id || 21 == vquVideoRequestBean.id
                            ) {

                                if (11 == vquVideoRequestBean.id || 21 == vquVideoRequestBean.id) {//视频拨打
                                    if (!UserManager.isMatch) {
                                        EventBus.getDefault()
                                            .post(NotificationEvent(
                                                vquVideoRequestBean))
                                        CallUtil.call(vquVideoRequestBean, false)
                                    } else if (vquVideoRequestBean.data.isMatch) {
                                        EventBus.getDefault()
                                            .post(NotificationEvent(
                                                vquVideoRequestBean))
                                        CallUtil.call(vquVideoRequestBean, false)
                                    }
                                } else {
                                    Handler().postDelayed({
                                        EventBus.getDefault()
                                            .post(NotificationEvent(
                                                vquVideoRequestBean))

                                    }, 100)
                                }
                            }
                        }
                    }
                }
            }, true)
    }


    override fun getLoginInfo(): () -> LoginInfo? {
        return {
            val account = UserSpUtils.getString(SpKey.im_account)
            val token = UserSpUtils.getString(SpKey.im_token)
            Log.d("BaseApplication", "${this}getLoginInfo: account-->>$account  token-->>$token")
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
                NimUIKit.setAccount(account)
                LoginInfo(account, token)
            } else {
                null
            }
        }
    }

    private fun getRecentCustomization(): RecentCustomization {
        return object : DefaultRecentCustomization() {
            override fun getDefaultDigest(recent: RecentContact?): String {
                when (recent?.msgType) {
                    MsgTypeEnum.custom -> {
                        val attachment = recent.attachment
                        val customAttachment: MessageVquBaseAttachment =
                            attachment as MessageVquBaseAttachment
                        if (customAttachment.type == 11) {
                            val sysInfoAttachment: MessageVquSysInfoAttachment =
                                customAttachment as MessageVquSysInfoAttachment
                            return sysInfoAttachment.sysInfoTextBean.title
                        } else if (customAttachment.type == 12) {
                            val sysInfoSingleImageAttachment: SysInfoSingleImageAttachment =
                                customAttachment as SysInfoSingleImageAttachment
                            return when (sysInfoSingleImageAttachment.act_type) {
                                1 -> {
                                    sysInfoSingleImageAttachment.act_string
                                }
                                2 -> {
                                    sysInfoSingleImageAttachment.title
                                }
                                else -> {
                                    "图文"
                                }
                            }
                        } else if (customAttachment.type == 13) {
                            return (customAttachment as SysInfoDoubleImageAttachment).imageList[0].title
                        } else if (customAttachment.type == 14) {
                            val giftAttachment: MessageVquGiftAttachment =
                                customAttachment as MessageVquGiftAttachment
                            return if (giftAttachment.from_uid == UserSpUtils.getUserBean()?.userId?.toInt()
                            ) {
                                "[赠送礼物]"
                            } else {
                                "[收到礼物]"
                            }
                        } else if (customAttachment.type == 15) {
                            val videoAttachment: MessageVquVideoAttachment =
                                customAttachment as MessageVquVideoAttachment
                            val videoStr = "[视频通话]"
                            if (videoAttachment.from_uid.isNullOrEmpty()) {
                                return videoStr
                            }
                            if (videoAttachment.from_uid.equals(UserSpUtils.getUserBean()?.userId)) {  //发送的
                                return when (videoAttachment.status) {
                                    1 -> {
                                        "$videoStr[已取消]"
                                    }
                                    2 -> {
                                        "$videoStr[对方已拒绝]"
                                    }
                                    3 -> {
                                        "$videoStr[超时未接听]"
                                    }
                                    4 -> {
                                        videoStr + "[通话时长:" + secondToTime(videoAttachment.call_time.toLong()) + "]"
                                    }
                                    else -> {
                                        videoStr
                                    }
                                }
                            } else {  //接收的
                                when (videoAttachment.status) {
                                    1 -> {
                                        return "$videoStr[对方已取消]"
                                    }
                                    2 -> {
                                        return "$videoStr[已拒绝]"
                                    }
                                    3 -> {
                                        return "$videoStr[超时未接听]"
                                    }
                                    4 -> {
                                        return videoStr + "[通话时长:" + secondToTime(videoAttachment.call_time.toLong()) + "]"
                                    }
                                }
                            }
                        } else if (customAttachment.type == 73) {
                            val audioAttachment: MessageVquAudioAttachment =
                                customAttachment as MessageVquAudioAttachment
                            val videoStr = "[语音通话]"
                            if (audioAttachment.from_uid.isNullOrEmpty()) {
                                return videoStr
                            }
                            if (audioAttachment.from_uid.equals(
                                    UserSpUtils.getUserBean()?.userId
                                )
                            ) {  //发送的
                                return when (audioAttachment.status) {
                                    1 -> {
                                        "$videoStr[已取消]"
                                    }
                                    2 -> {
                                        "$videoStr[对方已拒绝]"
                                    }
                                    3 -> {
                                        "$videoStr[超时未接听]"
                                    }
                                    4 -> {
                                        videoStr + "[语音时长:" + secondToTime(audioAttachment.call_time.toLong()) + "]"
                                    }
                                    else -> {
                                        videoStr
                                    }
                                }
                            } else {  //接收的
                                when (audioAttachment.status) {
                                    1 -> {
                                        return "$videoStr[对方已取消]"
                                    }
                                    2 -> {
                                        return "$videoStr[已拒绝]"
                                    }
                                    3 -> {
                                        return "$videoStr[超时未接听]"
                                    }
                                    4 -> {
                                        return videoStr + "[语音时长:" + secondToTime(audioAttachment.call_time.toLong()) + "]"
                                    }
                                }
                            }
                        } else if (customAttachment.type == 16) {
                            val attentionAttachment: MessageVquAttentionAttachment =
                                customAttachment as MessageVquAttentionAttachment
                            return attentionAttachment.nickname.toString() + "关注了你"
                        } else if (customAttachment.type == 17) {
                            return "动态更新"
                        } else if (customAttachment.type == 18) {
                            val onlineNoticeAttachment: MessageVquOnlineNoticeAttachment =
                                customAttachment as MessageVquOnlineNoticeAttachment
                            return onlineNoticeAttachment.nickname.toString() + "上线啦"
                        } else if (customAttachment.type == 74) {
                            return "[邀请通话]"
                        }
                    }
                }
                return super.getDefaultDigest(recent)?:""
            }

            private fun secondToTime(time: Long): String? {
                var second = time
                second %= 86400
                val hours = second / 3600
                second %= 3600
                val minutes = second / 60
                second %= 60
                val strHours = String.format("%02d", hours)
                val strMinutes = String.format("%02d", minutes)
                val strSecond = String.format("%02d", second)
                return "$strHours:$strMinutes:$strSecond"
            }
        }
    }


    override fun vquPushInit() {
        //oppo推送初始化
        HeytapPushManager.init(BaseApplication.application, true)
    }

    override fun vquBuildMixPushConfig(): MixPushConfig {
        val config = MixPushConfig()
        //小米推送
        config.xmAppId = NIMVquConfig.XM_MIX_PUSH_APP_ID
        config.xmAppKey = NIMVquConfig.XM_MIX_PUSH_APP_KEY
        config.xmCertificateName = NIMVquConfig.XM_MIX_PUSH_NAME

        //华为推送
        config.hwAppId = NIMVquConfig.HW_MIX_PUSH_APP_ID
        config.hwCertificateName = NIMVquConfig.HW_MIX_PUSH_NAME

        //vivo推送
        config.vivoCertificateName = NIMVquConfig.VIVO_MIX_PUSH_NAME

        //oppo推送
        config.oppoAppId = NIMVquConfig.OPPO_MIX_APP_ID
        config.oppoAppKey = NIMVquConfig.OPPO_MIX_APP_KEY
        config.oppoAppSercet = NIMVquConfig.OPPO_MIX_APP_SERCET
        config.oppoCertificateName = NIMVquConfig.OPPO_MIX_PUSH_NAME
        return config
    }

}
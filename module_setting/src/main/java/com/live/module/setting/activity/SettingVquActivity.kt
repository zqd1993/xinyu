package com.live.module.setting.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.setting.R
import com.live.module.setting.adapter.SettingVquAdapter
import com.live.module.setting.bean.SettingBean
import com.live.module.setting.databinding.SettingTantaActivitySettingBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.ExitAgoraEvent
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setNbOnItemClickListener
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import com.mshy.VInterestSpeed.common.utils.DataCleanUtil
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.common.utils.login.PhoneAuthLogin
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.msg.MsgService
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * author: Tany
 * date: 2022/4/2
 * description:设置页面
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquActivity)
class SettingVquActivity : BaseActivity<SettingTantaActivitySettingBinding, SettingVquViewModel>() {
    override val mViewModel: SettingVquViewModel by viewModels()

    val mLoginViewModel: LoginViewModel by viewModels()

    var settingVquAdapter: SettingVquAdapter? = null
    var settingList = mutableListOf<SettingBean>()
    var curPos: Int = 0
    var curString: String = ""
    var isAnchor: Boolean = false
    var isSelect: Boolean = false
    var isBind: Boolean = false
    override fun SettingTantaActivitySettingBinding.initView() {
        var settingTitle = getString(R.string.setting_setting)
        if (NetBaseUrlConstant.MAIN_URL.contains("120.78.160.71:8071")) {
            settingTitle += "debug"
        }
        mBinding.includeTitle.toolbar.initClose(settingTitle) {
            finish()
        }

        setSettingInfo()
        tvLogout.setViewClickListener {
            showLogOutDialog()
        }
    }

    private fun setSettingInfo() {//手动设置设置列表
        var gender = UserManager.userInfo?.gender
        var chache: String? = DataCleanUtil.getTotalCacheSize(this)
        settingList.clear()
//        settingList.add(
//            SettingBean(
//                getString(R.string.setting_disturb),
//                getString(R.string.setting_disturb_tip),
//                "",
//                true,
//                false
//            )
//        )
        settingList.add(SettingBean(getString(R.string.setting_account), "", "", false, true))
        settingList.add(SettingBean(getString(R.string.mine_vqu_menu_face), "", "", false, true))
        settingList.add(SettingBean(getString(R.string.setting_msg), "", "", false, true))
        if (gender == 1) {
            settingList.add(
                SettingBean(
                    getString(R.string.contact_vqu_setting_hello),
                    "",
                    "",
                    false,
                    true
                )
            )

            settingList.add(
                SettingBean(
                    getString(R.string.contact_vqu_setting_rate),
                    "",
                    "",
                    false,
                    true
                )
            )
        }



        settingList.add(SettingBean(getString(R.string.setting_teen_mode), "", "", false, true))
        settingList.add(SettingBean(getString(R.string.setting_blacklist), "", "", false, true))
        settingList.add(
            SettingBean(
                getString(R.string.setting_cache),
                "",
                chache.toString(),
                false,
                true
            )
        )
//        settingList.add(SettingBean(getString(R.string.setting_clear_chat), "", "", false, true))
        settingList.add(SettingBean(getString(R.string.setting_score), "", "", false, true))
        settingList.add(
            SettingBean(
                getString(R.string.setting_logoff),
                getString(R.string.setting_clear_info),
                "",
                false,
                true
            )
        )
        settingList.add(SettingBean(getString(R.string.setting_about), "", "", false, true))
        settingList.add(SettingBean("退出登录", "", "", false, true))
        settingVquAdapter = SettingVquAdapter(settingList)
        mBinding.rvSetting.adapter = settingVquAdapter
        settingVquAdapter?.setNbOnItemClickListener { adapter, view, position ->
            curPos = position
            curString = settingVquAdapter!!.data[position].title
            when (settingVquAdapter!!.data[position].title) {
                getString(R.string.setting_account) -> {//绑定手机号
                    if (isBind) {
                        "您已经绑定了手机号".toast()
                    } else {
                        ARouter.getInstance()
                            .build(RouteUrl.Setting.SettingVquBindMobileActivity)
                            .navigation()
                    }

                }

                getString(R.string.mine_vqu_menu_face) -> {//美颜设置
                    var userInCall: com.mshy.VInterestSpeed.common.bean.UserInCallEvent? =
                        SpUtils.getBean(
                            SpKey.user_in_call,
                            com.mshy.VInterestSpeed.common.bean.UserInCallEvent::class.java
                        )
                    if (userInCall?.isUserInCall == true) {
                        val typeAudio = if (userInCall?.isAudio) "语音" else "视频"
                        toast("正在" + typeAudio + "通话中，请稍后再试...")
                        return@setNbOnItemClickListener
                    }
                    PermissionUtils.cameraPermission(
                        this@SettingVquActivity,
                        "需要开启储存及相机权限以便使用相机正常使用美颜设置功能。",
                        "需要开启储存及相机权限以便使用相机正常使用美颜设置功能。",
                        requestCallback = { allGranted, grantedList, deniedList ->
                            if (allGranted) {
                                ARouter.getInstance()
                                    .build(RouteUrl.Agora2.AgoraTantaBeautySettingActivity)
                                    .navigation()
                            } else {
                                toast("美颜功能缺少相关权限")
                            }
                        })
                }

                getString(R.string.setting_msg) -> {//消息通知
                    ARouter.getInstance()
                        .build(RouteUrl.Setting.SettingVquMessageActivity)
                        .navigation()
                }

                getString(R.string.setting_teen_mode) -> {//青少年模式
                    ARouter.getInstance()
                        .build(RouteUrl.Setting.SettingVquTeenModeActivity)
                        .navigation()
                }

                getString(R.string.setting_blacklist) -> {//黑名单
                    ARouter.getInstance()
                        .build(RouteUrl.Setting.SettingVquBlackListActivity)
                        .navigation()
                }

                getString(R.string.setting_cache) -> {//清除缓存
                    showCleanCacheDialog()
                }

                getString(R.string.setting_clear_chat) -> {//清除所有聊天记录
                    deleteAllMsg()
                }

                getString(R.string.setting_about) -> {//关于甜缘
                    ARouter.getInstance()
                        .build(RouteUrl.Setting.SettingVquAboutActivity)
                        .navigation()
                }

                getString(R.string.setting_score) -> {//评分
                    try {
                        val uri = Uri.parse("market://details?id=$packageName")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } catch (e: Exception) {
//                        MaleToast.showFailureMsg(this@SystemSettingActivity, "您的手机没有安装Android应用市场")
                        e.printStackTrace()
                    }
                }

                getString(R.string.setting_logoff) -> {//注销账号
                    ARouter.getInstance()
                        .build(RouteUrl.Setting.SettingVquLogoffActivity)
                        .navigation()
                }

                getString(R.string.contact_vqu_setting_hello) -> {//打招呼设置
                    mViewModel.vquIsAuth()
                }

                getString(R.string.contact_vqu_setting_rate) -> {//收费设置
                    if (UserManager.userInfo?.gender == 1) {
                        mViewModel.vquIsAuth()
                    } else {
                        ARouter.getInstance().build(RouteUrl.Anchor.AnchorTantaRateSettingActivity)
                            .navigation()
                    }
                }

                "退出登录" -> {
                    showLogOutDialog()
                }
            }

        }
        settingVquAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.iv_disturb) {//勿扰
                if (isAnchor) {
                    mViewModel.vquSetVideo(
                        if (isSelect) {
                            0
                        } else {
                            1
                        }
                    )
                } else {
                    mViewModel.vquSetUserVideo(
                        if (isSelect) {
                            0
                        } else {
                            1
                        }
                    )
                }
            }
        }
    }

    fun showCleanCacheDialog() {
        val messageDialog = MessageDialog()
        messageDialog.setTitle(R.string.setting_tip)
        messageDialog.setContent(R.string.setting_clean_tip)
        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                messageDialog.dismiss()
                return true
            }

            override fun onRightClick(): Boolean {
                DataCleanUtil.clearAllCache(this@SettingVquActivity)
                messageDialog.dismiss()
                settingVquAdapter?.data!![curPos].right =
                    DataCleanUtil.getTotalCacheSize(this@SettingVquActivity).toString()
                settingVquAdapter?.notifyItemChanged(curPos)
                return false
            }

        })
        messageDialog.show(supportFragmentManager, "")
    }

    fun showLogOutDialog() {
        val messageDialog = MessageDialog()
        messageDialog.setTitle(R.string.setting_tip)
        messageDialog.setContent(R.string.setting_logout_tip)
        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                messageDialog.dismiss()
                return true
            }

            override fun onRightClick(): Boolean {
                EventBus.getDefault().post(ExitAgoraEvent())
                com.mshy.VInterestSpeed.uikit.api.NimUIKit.logout()
                NIMClient.getService(AuthService::class.java).logout()
                UserSpUtils.clear()
                SpUtils.putInt(SpKey.NEW_VISITOR_COUNT, 0)
                SpUtils.putString(SpKey.DEVICE_ID, "")
                EventBus.getDefault().post("mainFinish")
                com.mshy.VInterestSpeed.uikit.util.IntimateUtils.getInstance().clearData()
                if (Settings.canDrawOverlays(BaseApplication.context)) {
                    com.mshy.VInterestSpeed.common.ui.view.notification.SwipeMessageNotificationManager.getInstance(
                        this@SettingVquActivity
                    )
                        .removeNotificationAll()
                }
                mViewModel.vquSystemIndex()
                mViewModel.vquLoginOut()
//                ARouter.getInstance()
//                    .build(RouteUrl.Login.LoginVquLoginActivity)
//                    .navigation()
//                finish()
                return false
            }

        })
        messageDialog.show(supportFragmentManager, "")
    }

    override fun initObserve() {
        mViewModel.vquUserInfoData.observe(this, Observer {
            var settingBean = settingVquAdapter?.data!![0]
            isAnchor = it.data.userinfo.isAnchor == 1
            if (isAnchor) {//是女神
                isSelect = it.data.anchor.openVideoStatus == 0
            } else {
                isSelect = it.data.userinfo.isMsgRefuse == 1
            }
            settingBean.isSelect = isSelect
            settingVquAdapter?.setData(0, settingBean)
            settingVquAdapter?.notifyItemChanged(0)

            if (UserManager.userInfo?.gender != 1 && it.data.showLocation == 1) {
                val bean = SettingBean(
                    getString(R.string.contact_vqu_setting_rate),
                    "",
                    "",
                    false,
                    true
                )
                settingVquAdapter?.addData(3, bean)
            }
        })
//        mViewModel.vquSetVideoData.observe(this, Observer {
//            var settingBean = settingVquAdapter?.data!![0]
//            isSelect = !isSelect
//            settingBean.isSelect = isSelect
//            settingVquAdapter?.setData(0, settingBean)
//            settingVquAdapter?.notifyItemChanged(0)
//            "设置成功".toast()
//        })
        mViewModel.vquBindInfoData.observe(this, Observer {

            var settingBean = settingVquAdapter?.data!![0]
            if (it.data.mobile.isNullOrEmpty()) {
                isBind = false
                settingBean.right = "去绑定"
            } else {
                isBind = true
                settingBean.right = it.data.mobile
            }
            settingVquAdapter?.setData(0, settingBean)
            settingVquAdapter?.notifyItemChanged(0)
        })
        mViewModel.vquAuthData.observe(this, Observer {
            if (it.data.isRpAuth == 1) {
                if (getString(R.string.contact_vqu_setting_hello) == curString) {//打招呼设置
                    ARouter.getInstance().build(RouteUrl.Anchor.AnchorVquHelloSettingActivity)
                        .navigation()
                } else if (getString(R.string.contact_vqu_setting_rate) == curString) {//收费设置
                    ARouter.getInstance().build(RouteUrl.Anchor.AnchorTantaRateSettingActivity)
                        .navigation()
                }
            } else {
                com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
                    .setTitle("真人认证")
                    .setContent(resources.getString(R.string.common_vqu_auth))
                    .setLeftText("暂不认证")
                    .setRightText("去认证")
                    .setContentSize(15)
                    .setContentGravity(Gravity.CENTER)
                    .setOnClickListener(object :
                        com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                        override fun onLeft(dialogFragment: DialogFragment) {}
                        override fun onRight(dialogFragment: DialogFragment) {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity)
                                .navigation()
                        }
                    })
                    .show(supportFragmentManager)
            }
        })


        mViewModel.vquJumpAble.observe(this) {
            jumpToLogin(it)
        }
    }

    private var phoneAuthLogin: PhoneAuthLogin? = null
    private fun jumpToLogin(oneKeyLoginKey: String?) {
        phoneAuthLogin = PhoneAuthLogin(this)
        phoneAuthLogin?.initOneKeyLoginSdk(oneKeyLoginKey,
            finish = {
                phoneAuthLogin?.quit()
                finish()
            }, success = { token ->
                mLoginViewModel.oneKeyLogin(token, UserManager.inviteCode)
            })
    }

    override fun initRequestData() {
        mViewModel.vquGetUserInfo()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.vquGetBindInfo()
    }

    /**
     * 删除所有消息
     */
    private fun deleteAllMsg() {
        com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
            .setTitle("确定清除所有聊天记录")
            .setContent("删除后数据无法恢复，请谨慎操作")
            .setLeftText("再考虑下")
            .setRightText("确定删除")
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setClickCancelable(false)
            .setBackCanceledOnTouchOutside(false)
            .setOnClickListener(object :
                com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment?) {}
                override fun onRight(dialogFragment: DialogFragment?) {
                    NIMClient.getService(MsgService::class.java).clearMsgDatabase(false)
                }
            }).show(supportFragmentManager)
    }

    //监听青少年模式
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: com.mshy.VInterestSpeed.common.event.KidEvent?) {
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: LoginEvent) {
        when (event.type) {
            1001 -> {
                phoneAuthLogin?.getPhoneNumberAuthHelper()?.setProtocolChecked(true)
                when (event.jumpType) {
                    com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ -> {
                        onClickQQ()
                    }

                    com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN -> {
                        onClickWechat()
                    }

                    com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_OTHER_PHONE -> {
                        ARouter.getInstance().build(RouteUrl.Login.LoginTantaPhoneLoginActivity)
                            .navigation()
                    }

                    com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_CURRENT_PHONE -> {

                    }
                }
            }

            0 -> {
                phoneAuthLogin?.quit()
                finish()
            }
        }
    }


    private fun onClickQQ() {
        if (!com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance()
                .isInstalled(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ)
        ) {
            toast(com.mshy.VInterestSpeed.common.R.string.common_qq_not_install)
            return
        }

//        mLoginViewModel.loginThird(this, ShareManager.TYPE_QQ)
        ARouter.getInstance().build(RouteUrl.Common.LoginEmptyActivity)
            .navigation()
    }

    private fun onClickWechat() {

        if (!com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance()
                .isInstalled(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN)
        ) {
            toast(com.mshy.VInterestSpeed.common.R.string.common_wechat_not_install)
            return
        }

        mLoginViewModel.loginThird(
            this,
            com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
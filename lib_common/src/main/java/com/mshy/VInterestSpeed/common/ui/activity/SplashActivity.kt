package com.mshy.VInterestSpeed.common.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import com.alibaba.android.arouter.launcher.ARouter
import com.bytedance.hume.readapk.HumeSDK
import com.fm.openinstall.OpenInstall
import com.fm.openinstall.listener.AppInstallAdapter
import com.fm.openinstall.model.AppData
import com.gyf.immersionbar.ImmersionBar
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.databinding.CommonActivitySplashBinding
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import com.mshy.VInterestSpeed.common.ui.vm.SplashViewModel
import com.mshy.VInterestSpeed.common.utils.*
import com.mshy.VInterestSpeed.common.utils.login.PhoneAuthLogin
import com.umeng.socialize.UMShareAPI
import dagger.hilt.android.AndroidEntryPoint
import me.jessyan.autosize.internal.CancelAdapt
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *
 * @Description: 启动页
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:11
 *
 */
@AndroidEntryPoint
@EventBusRegister
class SplashActivity : BaseActivity<CommonActivitySplashBinding, SplashViewModel>(), CancelAdapt {

    override val mViewModel: SplashViewModel by viewModels()

    val mLoginViewModel: LoginViewModel by viewModels()

    var bindData = ""
    var channelCode: String? = ""
    private var channel: String? = null

    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentBar().init()
    }

    override fun CommonActivitySplashBinding.initView() {
//
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null
            && intent.action.equals(Intent.ACTION_MAIN)
        ) {

            finish()
            return
        }

        showAgreementDialog()

    }


    override fun onStart() {
        super.onStart()
//        window.decorView.post {
//            mViewModel.getInviteId()
//        }
    }

    /**
     * 订阅LiveData
     */
    override fun initObserve() {
        mViewModel.systemBean.observe(this) {

        }

        mViewModel.jumpAble.observe(this) {
            jump(it)
        }
    }


    private fun jump(oneKeyLoginKey: String?) {
        val isLogin = UserSpUtils.getBoolean(SpKey.isLogin)
        UserManager.userInfo = UserSpUtils.getUserBean()
        if (isLogin == true && UserManager.userInfo != null) {
            LoginUtils.checkLoginStatus(UserManager.userInfo!!, true, true) {
                if (it) {
                    finish()
                } else {
//                    startARouterActivity(RouteUrl.Main.CommonVquMainAcitvity)
//                    jumpToLogin(oneKeyLoginKey)

                    ARouter.getInstance().build(RouteUrl.Main.CommonVquMainAcitvity)
                        .withBoolean(RouteKey.IM_LOGIN_FAIL, true)
                        .navigation()
                }
            }
        } else {
            jumpToLogin(oneKeyLoginKey)
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

    /**
     * 用于在页面创建时进行请求接口
     */
    override fun initRequestData() {


    }

    private fun onClickQQ() {
        if (!ShareManager.getInstance().isInstalled(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ)) {
            toast(R.string.common_qq_not_install)
            return
        }


        ARouter.getInstance().build(RouteUrl.Common.LoginEmptyActivity)
            .navigation()

//        mLoginViewModel.loginThird(this, ShareManager.TYPE_QQ)

    }

    private fun onClickWechat() {

        if (!ShareManager.getInstance().isInstalled(this, ShareManager.TYPE_WEIXIN)) {
            toast(R.string.common_wechat_not_install)
            return
        }

        mLoginViewModel.loginThird(this, ShareManager.TYPE_WEIXIN)
    }

    private fun showAgreementDialog() {
        if (SpUtils.getBoolean(SpKey.agreementIsShow, false) == true) {
            init()
            return
        }

        val messageDialog = MessageDialog()
        messageDialog.setTitle(R.string.login_dialog_user_agreement)
        messageDialog.setContent(TextUtils.getClickableHtml(ResUtils.getString(R.string.agreement_text)))
        messageDialog.setIsHtml(true)
        messageDialog.setCancelAble(false)
        messageDialog.setLeftText("不同意")
        messageDialog.setRightText("同意")
        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                messageDialog.dismiss()
                finish()
                return true
            }

            override fun onRightClick(): Boolean {
                SpUtils.putBoolean(SpKey.agreementIsShow, true)
                BaseApplication.application.initDepends()
                init()
                return false
            }

        })
        messageDialog.show(supportFragmentManager, "")
    }

    private fun init() {

        OpenInstall.getInstall(object : AppInstallAdapter() {
            override fun onInstall(appData: AppData) {
                //获取渠道数据
                channelCode = appData.getChannel()
                if (!channelCode.isNullOrEmpty()) {
                    if ("22" == DeviceManager.getInstance().channel || "30" == DeviceManager.getInstance().channel) {
                        SpUtils.put("channelCode_cover", channelCode ?: "")
                        DeviceManager.getInstance().setChannelName(channelCode)
                    } else {
                        if (!SpUtils.getString("channelCode_cover").isNullOrEmpty()) {
                            channelCode = SpUtils.getString("channelCode_cover") ?: ""
                            DeviceManager.getInstance().setChannelName(channelCode)
                        }
                    }
                } else {
                    if (!SpUtils.getString("channelCode_cover").isNullOrEmpty()) {
                        channelCode = SpUtils.getString("channelCode_cover") ?: ""
                        DeviceManager.getInstance().setChannelName(channelCode)
                    }
                }

                //获取自定义数据
                bindData = appData.getData()
            }
        })

        channel = HumeSDK.getChannel(this) //应用管理中心分包SDK渠道号


        var code: String = channelCode ?: ""

        if (!channel.isNullOrEmpty()) {
            code = channel ?: ""
            DeviceManager.getInstance().setChannelName(code)
        }


        mViewModel.getSystemIndex(bindData, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
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
                    ShareManager.TYPE_WEIXIN -> {
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

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
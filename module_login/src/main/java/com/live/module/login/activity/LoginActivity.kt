package com.live.module.login.activity

import android.content.Intent
import android.view.KeyEvent
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bytedance.hume.readapk.HumeSDK
import com.fm.openinstall.OpenInstall
import com.fm.openinstall.listener.AppInstallAdapter
import com.fm.openinstall.model.AppData
import com.gyf.immersionbar.ImmersionBar
import com.live.module.login.R
import com.live.module.login.databinding.LoginActivityLoginBinding
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import com.umeng.socialize.UMShareAPI
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@AndroidEntryPoint
@EventBusRegister
@Route(path = RouteUrl.Login.LoginTantaLoginActivity)
class LoginActivity : BaseActivity<LoginActivityLoginBinding, LoginViewModel>() {
    var bindData = ""
    var channelCode: String? = ""
    private var channel: String? = null
    override val mViewModel by viewModels<LoginViewModel>()

    private var jumpType = -1

    @Autowired(name = RouteKey.IS_CHECKED)
    @JvmField
    var mIsVquCheckedAgreement = false


    override fun LoginActivityLoginBinding.initView() {
        initOpenInstall()

        mBinding.tvLoginAgreeAgreement.isSelected = mIsVquCheckedAgreement

        mBinding.stvLoginPhone.clickDelay { onClickLoginPhone() }
        mBinding.tvLoginAgreeAgreement.setViewClickListener(1) { onClickAgreeAgreement() }
        mBinding.tvLoginServiceAgreement.setViewClickListener(1) { onClickServiceAgreement() }
        mBinding.tvLoginPrivacyAgreement.setViewClickListener(1) { onClickPrivacyAgreement() }
        mBinding.tvLoginWeixin.clickDelay { onClickWechat() }
        mBinding.tvLoginQq.clickDelay { onClickQQ() }
    }

    private fun initOpenInstall() {
        OpenInstall.getInstall(object : AppInstallAdapter() {
            override fun onInstall(appData: AppData) {
                //获取渠道数据
                channelCode = appData.getChannel()
                if (!channelCode.isNullOrEmpty()) {
                    if ("5000" == DeviceManager.getInstance().channel || "30" == DeviceManager.getInstance().channel) {
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

    }

    private fun onClickQQ() {
//        if (!mBinding.tvLoginAgreeAgreement.isSelected) {
//            ToastUtils.showLong(R.string.agree_agreement)
//            return
//        }

        if (!com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().isInstalled(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ)) {
            toast(R.string.common_qq_not_install)
            return
        }

        jumpType = com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ

        if (mBinding.tvLoginAgreeAgreement.isSelected) {
            mViewModel.loginThird(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ)
        } else {
            ARouter.getInstance().build(RouteUrl.Common.CommonVquLoginDialog)
                .withInt(RouteKey.TYPE, 1001)
                .withInt(RouteKey.PAGE_TYPE, jumpType)
                .navigation()
        }
//        mViewModel.loginThird(this, ShareManager.TYPE_QQ)
    }

    private fun onClickWechat() {
//        if (!mBinding.tvLoginAgreeAgreement.isSelected) {
//            ToastUtils.showLong(R.string.agree_agreement)
//            return
//        }

        if (!com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().isInstalled(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN)) {
            toast(R.string.common_wechat_not_install)
            return
        }


        jumpType = com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN

        if (mBinding.tvLoginAgreeAgreement.isSelected) {
            mViewModel.loginThird(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN)
        } else {
            ARouter.getInstance().build(RouteUrl.Common.CommonVquLoginDialog)
                .withInt(RouteKey.TYPE, 1001)
                .withInt(RouteKey.PAGE_TYPE, jumpType)
                .navigation()
        }


    }

    override fun setStatusBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
            .transparentNavigationBar()
            .transparentStatusBar()
            .statusBarDarkFont(false)
    }


    override fun initObserve() {
//        mViewModel.systemBean.observe(this) {
//            if (it == null) {
//                return@observe
//            }
//            initOneKeyLoginSdk(it.oneKeyLoginKey)
//        }
    }

    override fun initRequestData() {

    }


    private fun onClickPrivacyAgreement() {
        ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
            .withString(
                RouteKey.URL,
                NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.USER_PRIVACY_URL
            )
            .navigation()
    }


    private fun onClickServiceAgreement() {
        ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
            .withString(
                RouteKey.URL,
                NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.AGREEMENT_URL
            )
            .navigation()
    }

    private fun onClickAgreeAgreement() {
        mBinding.tvLoginAgreeAgreement.isSelected = !mBinding.tvLoginAgreeAgreement.isSelected
    }

    private fun onClickLoginPhone() {

//        if (!mBinding.tvLoginAgreeAgreement.isSelected) {
//            ToastUtils.showLong(R.string.agree_agreement)
//            return
//        }

        jumpType = com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_OTHER_PHONE

        if (mBinding.tvLoginAgreeAgreement.isSelected) {
            ARouter.getInstance()
                .build(RouteUrl.Login.LoginTantaPhoneLoginActivity)
                .navigation()
        } else {
            ARouter.getInstance().build(RouteUrl.Common.CommonVquLoginDialog)
                .withInt(RouteKey.TYPE, 1001)
                .withInt(RouteKey.PAGE_TYPE, jumpType)
                .navigation()
        }

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        //如果是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            //重写返回键
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: LoginEvent) {
        if (event.type == 1001) {
            mBinding.tvLoginAgreeAgreement.isSelected = true
            when (event.jumpType) {
                com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ -> {
                    mViewModel.loginThird(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ)
                }
                com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN -> {
                    mViewModel.loginThird(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN)
                }
                com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_OTHER_PHONE -> {
                    ARouter.getInstance()
                        .build(RouteUrl.Login.LoginTantaPhoneLoginActivity)
                        .navigation()
                }
            }
        } else if (event.type == 0) {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
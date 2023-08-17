package com.mshy.VInterestSpeed.common.ui.dialog

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.live.vquonline.base.BaseApplication
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonDialogPhoneAuthBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import org.greenrobot.eventbus.EventBus

/**
 * author: Lau
 * date: 2022/5/27
 * description:
 */
@Route(path = RouteUrl.Common.CommonVquLoginDialog)
class LoginDialog : BaseActivity<CommonDialogPhoneAuthBinding, LoginViewModel>() {

    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = -1

    @Autowired(name = RouteKey.PAGE_TYPE)
    @JvmField
    var jumpType = -1

    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun setStatusBar() {
//        super.setStatusBar()

        ImmersionBar.with(this)
            .transparentBar().init()
    }


    override val mViewModel: LoginViewModel by viewModels()

    override fun CommonDialogPhoneAuthBinding.initView() {

        //这一步很关键（如果不设置背景是黑色或者白色）
        window.setBackgroundDrawable(getDrawable(R.color.transparent))
        val params = window.attributes
//        ScreenUtils.getScreenSize()
        com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil.screenWidth
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.CENTER
        //透明度
        params.dimAmount = 0.8f
//        params.alpha = 0.6f
        window.attributes = params

//        val messageDialog = MessageDialog()
//        messageDialog.setContent(TextUtils.getClickableHtml(ResUtils.getString(R.string.login_agreement_text)))
//        messageDialog.setIsHtml(true)
//        messageDialog.show(supportFragmentManager,"")

        mBinding.tvCommonDialogMessageContent.autoLinkMask = Linkify.WEB_URLS
        mBinding.tvCommonDialogMessageContent.movementMethod =
            LinkMovementMethod.getInstance()

//        ResUtils.getString(R.string.login_agreement_text)
        val html = BaseApplication.application.resources.getString(
            R.string.login_agreement_text,
            NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.AGREEMENT_URL,
            NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.USER_PRIVACY_URL
        )


        mBinding.tvCommonDialogMessageContent.text =
            com.mshy.VInterestSpeed.common.utils.TextUtils.getClickableHtml(html)


        mBinding.flLoginDialogRoot.setViewClickListener {
            finish()
        }

        mBinding.stvCommonDialogMessageBtnTop.setViewClickListener {
//            setResult(RESULT_OK)
            val loginEvent = LoginEvent()
            loginEvent.type = type
            loginEvent.jumpType = jumpType
            EventBus.getDefault().post(loginEvent)
            finish()
        }

        mBinding.stvCommonDialogMessageBtnBottom.setViewClickListener {
            finish()
        }
    }
}
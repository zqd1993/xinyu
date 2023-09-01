package com.mshy.VInterestSpeed.common.utils.login

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.utils.BarUtils
import com.live.vquonline.base.utils.GsonUtil
import com.live.vquonline.base.utils.hasSimCard
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.bean.OnKeyLoginFailedBean
import com.mshy.VInterestSpeed.common.bean.PhoneAuthBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mobile.auth.gatewayauth.*
import com.mobile.auth.gatewayauth.model.TokenRet
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import com.umeng.socialize.UMShareAPI
import org.greenrobot.eventbus.EventBus

/**
 * author: Lau
 * date: 2022/5/27
 * description:
 */
class PhoneAuthLogin(val context: Context) {

    private var mIsChecked = false

    private var mPhoneNumberAuthHelper: PhoneNumberAuthHelper? = null

    private var mFinish: (() -> Unit)? = null

    private var mSuccess: ((token: String) -> Unit)? = null

    private val mLoginButtonMarginTop = 399f
    private val mSLoganMarginTop = 323f
    private val mNumFieldMarginTop = 281f
    private val mLoginButtonHeight = 48f

    private val mTokenResultListener = object : TokenResultListener {
        override fun onTokenSuccess(s: String?) {
            val tokenRet: TokenRet?
            try {
                tokenRet = TokenRet.fromJson(s)
                if (ResultCode.CODE_SUCCESS == tokenRet.code) {
                    mPhoneNumberAuthHelper?.hideLoginLoading()
                    mSuccess?.invoke(tokenRet.token)
                } else if (ResultCode.CODE_ERROR_ENV_CHECK_SUCCESS == tokenRet.code) {
                    mPhoneNumberAuthHelper?.getLoginToken(context, 5000)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onTokenFailed(s: String?) {
//            toast("错误--"+s)
            try {
                val bean = GsonUtil.GsonToBean(s, OnKeyLoginFailedBean::class.java)
                if (bean.code != ResultCode.CODE_ERROR_USER_CANCEL) {
                    mFinish?.invoke()
                    ARouter.getInstance().build(RouteUrl.Login.LoginTantaLoginActivity)
                        .navigation()
                } else {
                    mFinish?.invoke()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            mPhoneNumberAuthHelper?.setAuthListener(null)
        }
    }

    fun initOneKeyLoginSdk(
        oneKeyLoginKey: String?,
        finish: (() -> Unit)? = null,
        success: ((token: String) -> Unit),
    ) {
        mFinish = finish
        mSuccess = success
        mIsChecked = false

        if (!hasSimCard() || oneKeyLoginKey.isNullOrEmpty()) {
            ARouter.getInstance().build(RouteUrl.Login.LoginTantaLoginActivity)
                .navigation()
            mFinish?.invoke()
            return
        }


        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener)
//        mPhoneNumberAuthHelper?.setProtocolChecked()
//        mPhoneNumberAuthHelper?.

        mPhoneNumberAuthHelper?.reporter?.setLoggerEnable(true)

        mPhoneNumberAuthHelper?.setAuthSDKInfo(oneKeyLoginKey)

        initOnKeyUI()

        initOnKeyCustomUI()

        mPhoneNumberAuthHelper?.setUIClickListener { code, context, json ->

            if (code == ResultCode.CODE_ERROR_USER_CHECKBOX || code == ResultCode.CODE_ERROR_USER_LOGIN_BTN) {
                try {
                    val bean = GsonUtil.GsonToBean(json, PhoneAuthBean::class.java)
                    mIsChecked = bean.isChecked

                    if (!mIsChecked && code == ResultCode.CODE_ERROR_USER_LOGIN_BTN) {
                        ARouter.getInstance().build(RouteUrl.Common.CommonVquLoginDialog)
                            .withInt(RouteKey.TYPE, 1001)
                            .withInt(RouteKey.PAGE_TYPE, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_CURRENT_PHONE)
                            .navigation()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }


        mPhoneNumberAuthHelper?.setActivityResultListener { requestCode, resultCode, data ->
            UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data)
        }

//        mPhoneNumberAuthHelper?.getLoginToken(context, 5000)
        mPhoneNumberAuthHelper?.checkEnvAvailable(PhoneNumberAuthHelper.SERVICE_TYPE_LOGIN)
    }

    private fun initOnKeyCustomUI() {
        mPhoneNumberAuthHelper?.removeAuthRegisterXmlConfig()
        mPhoneNumberAuthHelper?.removeAuthRegisterViewConfig()

        val authRegisterXmlConfig = AuthRegisterXmlConfig.Builder()
            .setLayout(R.layout.common_layout_third_login, object : AbstractPnsViewDelegate() {
                override fun onViewCreated(p0: View?) {

                    findViewById(R.id.tv_login_weixin).setViewClickListener(100) {
                        clickThirdLogin(com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN)

                    }
                    val findViewById = findViewById(R.id.tv_login_qq)

                    findViewById(R.id.tv_login_qq).setViewClickListener(100) {
                        clickThirdLogin(com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ)
                    }

                    findViewById(R.id.tv_login_other_phone).setViewClickListener(100) {
                        clickThirdLogin(com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_OTHER_PHONE)
                    }

                    val container = findViewById(R.id.common_layout_third_login_container)

                    val marginLayoutParams = container.layoutParams as ViewGroup.MarginLayoutParams
                    marginLayoutParams.bottomMargin = context.dp2px(43f + 15f + 22f + 29f)

                    //MarginTop = 登录按钮的距离Y轴顶部的偏移量 + 登录按钮高度 + 布局距离按钮距离 + 状态栏高度 + 导航栏高度30f
                    marginLayoutParams.topMargin =
                        context.dp2px(mLoginButtonMarginTop + mLoginButtonHeight + 12f + 30f) + BarUtils.getStatusBarHeight()
                    container.layoutParams = marginLayoutParams
                }
            }).build()
        mPhoneNumberAuthHelper?.addAuthRegisterXmlConfig(authRegisterXmlConfig)

    }

    private fun clickThirdLogin(type: Int) {

        if (!mIsChecked) {
            ARouter.getInstance().build(RouteUrl.Common.CommonVquLoginDialog)
                .withInt(RouteKey.TYPE, 1001)
                .withInt(RouteKey.PAGE_TYPE, type)
                .navigation()
            return
        }

        val loginEvent = LoginEvent()
        loginEvent.type = 1001
        loginEvent.jumpType = type
        EventBus.getDefault().post(loginEvent)
    }

    fun initOnKeyUI(isChecked: Boolean = false) {
        mIsChecked = isChecked

        //沉浸式状态栏
        val create = AuthUIConfig.Builder().setStatusBarColor(Color.TRANSPARENT)
            .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            .setLightColor(false)
            //隐藏ToolBar和ActionBar之类的
            .setNavHidden(true)
            //LOGO相关设置
            .setLogoImgDrawable(ResUtils.getDrawable(R.mipmap.icon_login_logo))
            .setLogoScaleType(ImageView.ScaleType.FIT_XY)
            .setLogoWidth(178)
            .setLogoHeight(133)
            .setLogoOffsetY(55)
            //手机号码
            .setNumberColor(ResUtils.getColor(R.color.color_FFFFFF))
            .setNumberSizeDp(25)
            .setNumFieldOffsetY(mNumFieldMarginTop.toInt())
            //SLogan，就是那行中国移动提供服务之类的标语
            .setSloganTextSizeDp(11)
            .setSloganOffsetY(mSLoganMarginTop.toInt())
            //登录按钮
            .setLogBtnText("本机号码一键登录")
            .setLogBtnTextSizeDp(16)
            .setLogBtnHeight(mLoginButtonHeight.toInt())
            .setLogBtnWidth(300)
            .setLogBtnOffsetY(mLoginButtonMarginTop.toInt())
            .setLogBtnBackgroundDrawable(ResUtils.getDrawable(R.drawable.shape_button_enable))
            //其它手机号登录
            .setSwitchAccHidden(true)
            //隐私协议
            .setCheckedImgPath("ic_login_onkey_checked")
            .setUncheckedImgPath("ic_login_onkey_un_checked")
            .setLogBtnToastHidden(true)
            .setPrivacyState(mIsChecked)
            .setPrivacyBefore("登录注册即表示同意")
            .setVendorPrivacyPrefix("《")
            .setVendorPrivacySuffix("》")
            .setProtocolAction("com.mshy.VInterestSpeed.common.ui.activity.CommonVquWebViewActivity")
            .setPackageName("com.queniang.zhenban.android")
            .setPrivacyOffsetY_B(43)
            .setAppPrivacyOne(
                "《鹊娘用户协议》",
                NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.AGREEMENT_URL
            )
            .setAppPrivacyTwo(
                "《隐私政策》",
                NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.USER_PRIVACY_URL
            )
            .setAppPrivacyColor(ResUtils.getColor(R.color.color_A3AABE), Color.WHITE)
            .setProtocolGravity(Gravity.CENTER_VERTICAL)
            .setPageBackgroundDrawable(ResUtils.getDrawable(R.mipmap.cover_1080))
            .create()

        mPhoneNumberAuthHelper?.setAuthUIConfig(
            create
        )


    }

    fun getPhoneNumberAuthHelper(): PhoneNumberAuthHelper? {
        return mPhoneNumberAuthHelper
    }

    fun quit() {
        mPhoneNumberAuthHelper?.removeAuthRegisterXmlConfig()
        mPhoneNumberAuthHelper?.removeAuthRegisterXmlConfig()
        mPhoneNumberAuthHelper?.setAuthListener(null)
        mPhoneNumberAuthHelper?.quitLoginPage()
        mPhoneNumberAuthHelper = null
    }
}
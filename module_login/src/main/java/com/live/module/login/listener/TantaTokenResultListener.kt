package com.live.module.login.listener

import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet

/**
 * author: Lau
 * date: 2022/4/7
 * description:
 */
class TantaTokenResultListener : TokenResultListener {

    private var mAuthHelper: PhoneNumberAuthHelper? = null
    private var mOnTokenListener: OnTokenListener? = null


    override fun onTokenSuccess(p0: String?) {
        try {
            val tokenRet: TokenRet = TokenRet.fromJson(p0)
            if (ResultCode.CODE_SUCCESS == tokenRet.code) {
                mAuthHelper?.setAuthListener(null)
                mAuthHelper?.hideLoginLoading()
                mAuthHelper?.quitLoginPage()

                mOnTokenListener?.onSuccess(tokenRet.token)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTokenFailed(p0: String?) {
        ARouter.getInstance().build(RouteUrl.Login.LoginTantaPhoneLoginActivity).navigation()
        mAuthHelper?.setAuthListener(null)
    }

    fun setAuthHelper(authHelper: PhoneNumberAuthHelper) {
        mAuthHelper = authHelper
    }

    interface OnTokenListener {
        fun onSuccess(token: String)
    }

    fun setOnTokenListener(listener: OnTokenListener) {
        mOnTokenListener = listener
    }
}
package com.mshy.VInterestSpeed.common.utils

import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.live.vquonline.base.utils.DeviceManager
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.helper.startARouterActivity
import com.mshy.VInterestSpeed.uikit.util.UIKitUtils
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo
import org.greenrobot.eventbus.EventBus

/**
 * author: Lau
 * date: 2022/4/11
 * description:
 */
object LoginUtils {

    fun checkLoginStatus(
        userinfo: VquUserInfo,
        isLoginFailedGoLogin: Boolean = false,
        isFromSplash:Boolean=false,
        finish: (isFinish: Boolean) -> Unit = {}
    ) {
        DeviceManager.getInstance().token = userinfo.token

        if (userinfo.mobile.isEmpty() && !isLoginFailedGoLogin) {
            ARouter.getInstance().build(RouteUrl.Setting.SettingVquBindMobileActivity)
                .withInt(RouteKey.TYPE, 1)
                .withParcelable(RouteKey.USER_INFO, userinfo)
                .navigation()
            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
            return
        }


        if (userinfo.finishStatus == 0) {
            startARouterActivity(RouteUrl.Login.LoginTantaSetInfoActivity)
        } else {
            if(isFromSplash){
                finish(false)
                EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
            }else{
                UIKitUtils.loginNIM(userinfo.userId, userinfo.imToken) {
                    object : RequestCallback<LoginInfo> {
                        override fun onSuccess(param: LoginInfo?) {
                            Log.d("LoginNIM", "onSuccess: " + param?.account)
                            if (param != null) {
                                UserSpUtils.put(SpKey.isLogin, true)
                                UserSpUtils.putString(SpKey.im_account, param.account)
                                UserSpUtils.putString(SpKey.im_token, param.token)

                                UserManager.userInfo = userinfo
                                UserSpUtils.putBean(SpKey.userInfo, userinfo)
                                startARouterActivity(RouteUrl.Main.CommonVquMainAcitvity)

//                            startARouterActivity(RouteUrl.Setting.SettingVquBindMobileActivity)

                                finish(true)
                            } else {
                                if (isLoginFailedGoLogin) {
//                                startARouterActivity(RouteUrl.Login.LoginVquLoginActivity)
                                    finish(false)
                                }
                                EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
                            }
                        }

                        override fun onFailed(code: Int) {
                            Log.e("LoginNIM", "onFailed: $code")
                            if (isLoginFailedGoLogin) {
//                            startARouterActivity(RouteUrl.Login.LoginVquLoginActivity)
                                finish(false)
                            }
                            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
                        }

                        override fun onException(exception: Throwable?) {
                            Log.e("LoginNIM", "onException: ${exception?.message}")
                            if (isLoginFailedGoLogin) {
//                            startARouterActivity(RouteUrl.Login.LoginVquLoginActivity)
                                finish(false)
                            }
                            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
                        }
                    }
                }
            }

        }
    }
}
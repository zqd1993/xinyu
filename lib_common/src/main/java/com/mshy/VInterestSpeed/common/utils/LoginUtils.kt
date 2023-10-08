package com.mshy.VInterestSpeed.common.utils

import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.ishumei.smantifraud.SmAntiFraud
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.ToastUtils
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonCallPriceBean
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.helper.startARouterActivity
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.uikit.util.UIKitUtils
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author: Lau
 * date: 2022/4/11
 * description:
 */
object LoginUtils {

    fun checkLoginStatus(
        userinfo: VquUserInfo,
        isRegister: Boolean = false,
        isOneKey: Boolean = false,
        isLoginFailedGoLogin: Boolean = false,
        isFromSplash: Boolean = false,
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
            startARouterActivity(
                RouteUrl.Login.LoginTantaSetInfoActivity,
                RouteKey.SET_INFO_IS_ONE_KEY_PASS,
                isOneKey
            )
        } else {
            if (isFromSplash) {
                finish(false)
                EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
            } else {
                EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
                var smType = ""
                var smEventId = ""
                if (isRegister) {
                    smEventId = "register"
                    if (isOneKey) {
                        smType = "phoneOnePass"
                    } else {
                        smType = "phoneMessage"
                    }
                } else {
                    smEventId = "login"
                    if (isOneKey) {
                        smType = "phoneOneLogin"
                    } else {
                        smType = "phoneMessage"
                    }
                }
                checkEvent(smType, smEventId)
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

//                                finish(true)
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
                            EventBus.getDefault()
                                .post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
                        }

                        override fun onException(exception: Throwable?) {
                            Log.e("LoginNIM", "onException: ${exception?.message}")
                            if (isLoginFailedGoLogin) {
//                            startARouterActivity(RouteUrl.Login.LoginVquLoginActivity)
                                finish(false)
                            }
                            EventBus.getDefault()
                                .post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
                        }
                    }
                }
            }

        }
    }

    //初始化数美
    fun initSm(): String {
        val option = SmAntiFraud.SmOption()
        option.setOrganization("63yWJFP7kEPMBTWZE5Dg")
        option.setAppId("default")
        option.setPublicKey(
            "MIIDLzCCAhegAwIBAgIBMDANBgkqhkiG9w0BAQUFADAyMQswCQYDVQQGEwJDTjELMAkGA1UECwwCU00xFjAUBgNVBAMMDWUuaXNodW1laS5jb20wHhcNMjMwODA0MTEwMDA3WhcNNDMwNzMwMTEwMDA3WjAyMQswCQYDVQQGEwJDTjELMAkGA1UECwwCU00xFjAUBgNVBAMMDWUuaXNodW1laS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCCsrRkxSkpMgD+ol8gtN5Y4KLLWE9kJG8T1vbEh6kgOTIdT/CqHYRHIFzghbDVDj22CK3nMkhsIAH7yVegevO6r++cuxJjYFheDu+hv3XIYvCadl8A/3MgmWp4FSZHr4gTm+otdk7FZyyxvN1WxX0s00DAyPRE777by6pVM2ollyJDsQZR5FGfA9Q5jRK7X3N/jkkUHO5hWu47BZDUZjrLzTjvxL5NF08QMNBG9+irId5QN9hRO2DyaarzPGkkwpf6hyHm1qawROaAjkiQLv1f2WpSJ0VYE55jbqzO18iJ+Wwt6Hj7sIznI7SxFkh5cU8YVTioyIr/esmf6VHCwm2ZAgMBAAGjUDBOMB0GA1UdDgQWBBQbD81+nw1OApSB/5VOI5CcZJNe2zAfBgNVHSMEGDAWgBQbD81+nw1OApSB/5VOI5CcZJNe2zAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4IBAQBGTd72fkSNycWwutkdeGw2UmbhS4KtEjFJR3ZP8aTdiAH65eCF45M3edWD6qhNN9omTHtehEAKFdbmD4nJceFepdX6KYyWVWkseknR95yT8mmynVF5I/SezujsC042GrrFU/NHou6KIzCHg5t/vRPIv+U+BUEYEyemYBAbvA5SLSNTMNOY/H2IuqUhrJRJ786zwCY5amBWSqoqWnTkn9EJtPVl2afGudC1WVTajKY1ODalaOkVhu8Pps2G93RL+ADTX1bytQSbrfci6vizSkKGSX9wWmcd7FSuILBfhedoxIiGMNQNfUUb/BKm6Lh0AoTxjq9cLLjyAcDa/qCOQ85C"
        )
        SmAntiFraud.registerServerIdCallback(object : SmAntiFraud.IServerSmidCallback {
            override fun onSuccess(boxId: String) {

            }

            override fun onError(code: Int) {

            }
        })
        var isOk = SmAntiFraud.create(BaseApplication.context, option)
        Log.i("initSM", "isOk = $isOk")
        val deviceId = SmAntiFraud.getDeviceId()
        Log.i("initSM", "deviceId = $deviceId")
        return deviceId
    }

    fun checkEvent(smType: String, smEventId: String) {
        val deviceId = initSm()
        if (deviceId.isEmpty()) {
            return
        }
        var globalApiService =
            GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
        val params = mutableMapOf<String, Any>()
        params["type"] = smType
        params["event_id"] = smEventId
        params["device_id"] = deviceId
        globalApiService.checkEvent(params)
            .enqueue(object : Callback<BaseResponse<CommonCallPriceBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<CommonCallPriceBean>>,
                    response: Response<BaseResponse<CommonCallPriceBean>>,
                ) {
                    if (response.body()?.code == 0) {
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<CommonCallPriceBean>>,
                    t: Throwable,
                ) {
                }
            })
    }
}
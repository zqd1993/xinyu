package com.mshy.VInterestSpeed.common.ui.vm

import android.app.Activity
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.SystemBean
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.ui.repo.LoginRepository
import com.mshy.VInterestSpeed.common.utils.LoginUtils
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val mRepo: LoginRepository) :
    BaseViewModel() {


    val systemBean = MutableLiveData<SystemBean?>()

    val time = MutableLiveData<String>()


    val sendCodeResult = MutableLiveData<Boolean>()

    private val mTotalTime = 60 * 1000L
    private val mTimeInterval = 1 * 1000L

    private val timer: CountDownTimer by lazy {
        object : CountDownTimer(mTotalTime, mTimeInterval) {
            override fun onTick(millisUntilFinished: Long) {
                time.postValue("${millisUntilFinished / 1000} s")
            }

            override fun onFinish() {
                time.postValue(ResUtils.getString(R.string.login_send_auth_code))
            }
        }
    }

    public fun tantaStartDownTime() {
        timer.start()
    }

    fun tantaSendCode(phone: String, needTimeDown: Boolean = true) {
        launchIO {
            val params = mutableMapOf<String, Any>()
            params["type"] = "login"
            params["mobile"] = phone
            mRepo.tantaSendCode(params)
                .catch {
                    toast(it.message ?: "")
//
                    time.postValue(ResUtils.getString(R.string.login_send_auth_code))
                }
                .collect {
                    launch(Dispatchers.Main) {
                        if (needTimeDown) {
                            tantaStartDownTime()
                        }
                        sendCodeResult.postValue(true)
                    }
                }
        }
    }

    fun getSystemIndex() {
        launchIO {
            mRepo.getSystemIndex()
                .catch { toast(it.message ?: "") }
                .collect {
                    DeviceManager.getInstance().openGreen = it.data.auditStatus
                    DeviceManager.getInstance().timestamp =
                        (it.data.timestamp - System.currentTimeMillis()).toInt()
                    systemBean.postValue(it.data)
                }
        }
    }

    fun oneKeyLogin(token: String, inviteCode: String?) {
        launchIO {
            val params = mutableMapOf<String, Any>()
            params["accessToken"] = token
            params["agreement"] = 1
            if (!inviteCode.isNullOrEmpty()) {
                params["inviteCode"] = inviteCode
            }
            mRepo.onKeyLogin(params)
                .catch { }
                .collect {
                    launch(Dispatchers.Main) {
                        vquLoginCheck(it.data.userinfo)
                    }
                }
        }
    }

    fun tantaCodeLogin(phone: String, code: String) {
        changeStateView(loading = true)
        launchIO {
            val params = mutableMapOf<String, Any>()
            params["agreement"] = 1
            params["mobile"] = phone
            params["phone_code"] = code
            if (!UserManager.inviteCode.isNullOrEmpty()) {
                params["invite_code"] = UserManager.inviteCode ?: ""
            }
            mRepo.tantaCodeLogin(params)
                .catch {
                    changeStateView(hide = true)
                }
                .collect {
                    launch(Dispatchers.Main) {
                        timer.cancel()
                        vquLoginCheck(it.data.userinfo)
                    }
                }
        }
    }

    private fun vquLoginCheck(userinfo: VquUserInfo) {
        LoginUtils.checkLoginStatus(userinfo, finish = {
            changeStateView(hide = true)
        })
    }

    fun loginThird(activity: Activity, type: Int) {


        com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().verify(object : com.mshy.VInterestSpeed.common.utils.ShareManager.iAuthStatus {
            override fun success(map: MutableMap<String, String>) {

                when (type) {
                    com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_WEIXIN -> thirdLogin(map, "wechat")
                    com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ -> thirdLogin(map, "qq")
                }

            }

            override fun error() {
                EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
            }

            override fun cancel() {
                EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
            }

        }, activity, type)


    }

    private fun thirdLogin(
        params: MutableMap<String, String>,
        platform: String,
    ) {
        val uid =
            if (params["uid"].isNullOrEmpty()) params["unionid"] ?: "" else params["uid"] ?: ""
        val accessToken = params["accessToken"] ?: ""
        val netParams = mutableMapOf<String, Any>()
        netParams["platform"] = platform
        netParams["openid"] = uid
        netParams["access_token"] = accessToken
        val inviteCode = UserManager.inviteCode
        if (!inviteCode.isNullOrEmpty()) {
            netParams["invite_code"] = inviteCode
        }
        launchIO {

            mRepo.tantaThirdLogin(netParams)
                .catch {
                    EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.OnFinishEvent())
                }
                .collect {
                    if (!it.data.userinfo.inviteCode.isNullOrEmpty()) {
                        UserManager.inviteResult = true
                    }
                    launch(Dispatchers.Main) {
                        vquLoginCheck(it.data.userinfo)
                    }
                }
        }
    }
}


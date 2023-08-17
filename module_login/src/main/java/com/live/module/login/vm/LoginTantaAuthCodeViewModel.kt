package com.live.module.login.vm

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.live.module.login.R
import com.mshy.VInterestSpeed.common.ui.repo.LoginRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.utils.ResUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginTantaAuthCodeViewModel @Inject constructor(private val mRepo: LoginRepository) :
    BaseViewModel() {
    val data = MutableLiveData<String>()

    val time = MutableLiveData<String>()

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

    fun tantaStartDownTime() {
        timer.start()
    }

    fun tantaSendCode(phone: String) {
        launchIO {
            val params = mutableMapOf<String, Any>()
            params["type"] = "register"
            params["mobile"] = phone
            mRepo.tantaSendCode(params)
                .catch {
                    toast(it.message ?: "")
//
                    time.postValue(ResUtils.getString(R.string.login_send_auth_code))
                }
                .collect {
                    launch(Dispatchers.Main) {
                        tantaStartDownTime()
                    }
                }
        }
    }

}
package com.live.module.bill.vm

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.live.module.bill.R
import com.live.module.bill.bean.BillVquAccountInfo
import com.live.module.bill.repo.BillVquNewWithdrawalAccountRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.helper.ResponseException
import com.mshy.VInterestSpeed.common.utils.ResUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
@HiltViewModel
class BillTantaNewWithdrawalAccountViewModel @Inject constructor(val repo: BillVquNewWithdrawalAccountRepository) :
    BaseViewModel() {

    val accountInfo = MutableLiveData<BillVquAccountInfo>()

    val saveResult = MutableLiveData<String>()

    private val mTotalTime = 60 * 1000L
    private val mTimeInterval = 1 * 1000L

    val time = MutableLiveData<String>()

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


    fun vquAccountInfo(id: String, isMaster: Int) {
        launchIO {
            val params = mutableMapOf<String, Any>()
            params["id"] = id
            params["is_master"] = isMaster
            repo.vquAccountInfo(params)
                .catch { }
                .collect {
                    accountInfo.postValue(it.data!!)
                }
        }
    }

    fun vquAccountSave(
        account: String?,
        carType: Int,
        isMaster: Int,
        mobile: String?,
        phoneCode: String?,
        id: String?,
        idCard: String?,
        idCardName: String?,
        type: Int
    ) {

        val params = mutableMapOf<String, Any>()

        if (account.isNullOrEmpty()) {
            toast("请输入提现账号")
            return
        }

        params["card_account"] = account
        params["card_type"] = carType
        params["is_master"] = isMaster

        if (type == 1) {
            if (mobile.isNullOrEmpty()) {
                toast("请输入手机号码")
                return
            }

            params["mobile"] = mobile

            if (phoneCode.isNullOrEmpty()) {
                toast("请输入正确的验证码")
                return
            }
            params["phone_code"] = phoneCode
        }

        if (!id.isNullOrEmpty() && id != "0") {
            params["id"] = id
        }

//        val regex15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\$".toRegex()
//        val regex18 =
//            "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[xX1-9]\$".toRegex()

        if (isMaster != 1) {
            if (idCard.isNullOrEmpty()) {
                toast("请输入正确的身份证号码")
                return
            }

            params["id_card"] = idCard

            if (idCardName.isNullOrEmpty()) {
                toast("请输入姓名")
                return
            }
            params["id_card_name"] = idCardName
        }

        launchIO {

            repo.vquAccountSave(params)
                .catch {
                    if (it is ResponseException) {
                        saveResult.postValue(it.msg)
                    } else {
                        toast(it.message ?: "")
                    }

                }
                .collect {
                    saveResult.postValue("success")
                }
        }
    }

    fun sendCode(phone: String?) {
        if (phone.isNullOrEmpty()) {
            toast("请输入手机号码")
            return
        }

        launchIO {
            val params = mutableMapOf<String, Any>()
            params["type"] = "bankcard"
            params["mobile"] = phone
            repo.vquSendCode(params)
                .catch {
                    toast(it.message ?: "")
//
                    time.postValue(ResUtils.getString(R.string.login_send_auth_code))
                }
                .collect {
                    launch(Dispatchers.Main) {
                        vquStartDownTime()
                    }
                }
        }
    }

    private fun vquStartDownTime() {
        timer.start()
    }
}
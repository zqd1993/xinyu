package com.live.module.auth.vm

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.alipay.face.api.ZIMFacadeBuilder
import com.live.module.auth.bean.DescribeVerifyTokenBean
import com.live.module.auth.repo.AuthVquFaceIdentifyRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/28
 * description:
 */
@HiltViewModel
class AuthVquFaceIdentifyViewModel @Inject constructor(val repo: AuthVquFaceIdentifyRepository) :
    BaseViewModel() {

    val verifyData = MutableLiveData<DescribeVerifyTokenBean?>()

    val verifyResultData = MutableLiveData<Int>()

    fun vquAgreeProtocol() {
        launchIO {
            repo.vquAgreeProtocol(1)
                .catch {
                    toast(it.message ?: "")
                }
                .collect {
                    vquDescribeVerifyToken()
                }
        }
    }

    fun vquDescribeVerifyToken() {
        changeStateView(loading = true)
        launchIO {
            repo.vquRPAuth()
                .catch {
                    changeStateView(hide = true)
                    toast(it.message ?: "")
                }
                .collect {
                    changeStateView(hide = true)
                    verifyData.postValue(it.data)
                }
        }
    }

    fun rpVerify(
        activity: Activity,
        bean: DescribeVerifyTokenBean
    ) {
        ZIMFacadeBuilder.create(activity)
            .verify(bean.verifyToken, true, null) { response ->
                when (response?.code) {
                    1000 -> {   //认证通过
//                        changeStateView(loading = true)
                        vquDescribeVerifyResult(bean.bizId, bean.verifyToken)
                    }
                    1003 -> { //认证中断
                        toast("未完成认证")
                    }
                    else -> {   //认证失败
                        toast("认证不通过")
                    }
                }
                true
            }
    }

    private fun vquDescribeVerifyResult(bizId: String,token:String) {
        changeStateView(loading = true)
        launchIO {
            repo.vquDescribeVerifyResult(bizId,token)
                .catch {
                    changeStateView(hide = true)
                    it.printStackTrace()
                    toast(it.message ?: "")
                }
                .collect {
                    changeStateView(hide = true)
                    verifyResultData.postValue(it.data.auth)
                }
        }
    }
}
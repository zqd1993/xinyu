package com.live.module.auth.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.auth.repo.AuthVquRealAuthRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
@HiltViewModel
class AuthVquRealAuthViewModel @Inject constructor(val repo: AuthVquRealAuthRepository) :
    BaseViewModel() {

    val authResult = MutableLiveData<Boolean>()

//    fun vquUserAuth(name: String?, id: String?) {
//        if (name.isNullOrEmpty()) {
//            toast("请填写真实姓名")
//            return
//        }
//
//        if (id.isNullOrEmpty()) {
//            toast("请填写身份证号码")
//            return
//        }
//
//
//        launchIO {
//            repo.vquUserAuth(name, id)
//                .catch {
//                    authResult.postValue(false)
//                }
//                .collect {
//                    authResult.postValue(true)
//                }
//        }
//    }


    fun vquDescribeVerifyToken(name: String?, id: String?) {
        if (name.isNullOrEmpty()) {
            toast("请填写真实姓名")
            return
        }

//        val regex15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}\$".toRegex()
//        val regex18 =
//            "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[xX0-9]$".toRegex()

        if (id.isNullOrEmpty()) {
            toast("请填写身份证号码")
            return
        }


        vquUserAuth(name, id)

//        launchIO {
//            repo.vquDescribeVerifyToken()
//                .catch {
//                    toast(it.message ?: "")
//                }
//                .collect {
////                    verifyData.postValue(it.data)
//
//
//                }
//        }
    }

    private fun vquUserAuth(name: String, id: String) {
        changeStateView(loading = true)

        launchIO {
            val params = mutableMapOf<String, Any>()
            params["id_card_number"] = id
            params["real_name"] = name

            repo.vquUserAuth(params)
                .catch {
                    it.printStackTrace()
                    toast(it.message ?: "")
                    changeStateView(hide = true)
                    authResult.postValue(false)
                }
                .collect {
                    changeStateView(hide = true)
                    authResult.postValue(true)
                }
        }
    }


}
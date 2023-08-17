package com.live.module.auth.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.auth.repo.AuthVquCenterRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.bean.CommonVquAuthBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
@HiltViewModel
class AuthVquCenterViewModel @Inject constructor(private val repo: AuthVquCenterRepository) :
    BaseViewModel() {

    val authData = MutableLiveData<CommonVquAuthBean>()


    fun isAuth() {
        launchIO {
            repo.isAuth()
                .catch { }
                .collect {
                    authData.postValue(it.data!!)
                }
        }
    }
}
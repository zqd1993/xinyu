package com.live.module.home.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.home.repo.HomeVquSearchRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/7/4
 * description:
 */
@HiltViewModel
class HomeVquSearchViewModel @Inject constructor(private val repo: HomeVquSearchRepository) :
    BaseViewModel() {

    val resultData = MutableLiveData<VquRelationBean?>()

    val vquBeckonInt = MutableLiveData<Int>()

    val vquNoMoneyResult = MutableLiveData<Boolean>()

    fun vquGetUserInfoByUserCode(keyword: String?) {
        if (keyword.isNullOrEmpty()) {
            toast("请输入蜜橙号")
            return
        }

        launchIO {
            repo.vquGetUserInfoByUserCode(keyword)
                .catch { resultData.postValue(null) }
                .collect {
                    resultData.postValue(it.data.userInfo)
                }
        }
    }

    fun vquSendBeckon(userId: String,position:Int) {
        launchIO {
            repo.vquSendBeckon(userId)
                .catch { }
                .collect {
                    if (it.code != 0) {
                        when (it.code) {
                            1002 -> {
                                vquBeckonInt.postValue(position)
                                vquNoMoneyResult.postValue(true)
                            }
                            7480 -> {
                                toast(it.message)
                            }
                            else -> {
                                toast(it.message)
                                vquBeckonInt.postValue(position)
                            }
                        }
                    }
                }
        }
    }
}
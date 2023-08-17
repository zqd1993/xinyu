package com.live.module.vip.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.vip.bean.TantaVipInfoBean
import com.live.module.vip.repository.VipTantaCenterRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
@HiltViewModel
class VipTantaCenterModel @Inject constructor(private val repository: VipTantaCenterRepository) :
    BaseViewModel() {

    val vipOrderCreateResultData = MutableLiveData<TantaPayBean>()
    val vipIndexResultData = MutableLiveData<TantaVipInfoBean>()
    val vipDailyGifResultData = MutableLiveData<Any>()

    //开通类型 none  开通    add  升级    start 续费
    fun createRechargeOrder(
        id: String, pay_type: String, type: String, vip_goods_id: String
    ) {
        launchIO {
            repository.createVipOrder(id, pay_type, type, vip_goods_id)
                .catch {
                    changeStateView(hide = true)
                    toast("请求支付出错，请重试")
                }
                .onStart {
                    changeStateView(loading = true)
                }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vipOrderCreateResultData.postValue(it.data!!)
                }
        }
    }

    fun getVipIndexInfo() {
        launchIO {
            repository.getVipIndexInfo()
                .catch { }
                .collect {
                    vipIndexResultData.postValue(it.data!!)
                }
        }
    }

    fun getVipDailyGif() {
        launchIO {
            repository.getVipDailyGif()
                .catch {
                    changeStateView(hide = true)
                    toast("领取失败，请重试")
                }
                .onStart {
                    changeStateView(loading = true)
                }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vipDailyGifResultData.postValue(1)
                }
        }

    }
}
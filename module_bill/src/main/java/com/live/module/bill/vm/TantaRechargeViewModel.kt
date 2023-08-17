package com.live.module.bill.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.bill.bean.BillTantaWechatPayTypeData
import com.live.module.bill.repo.VquRechargeRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/2
 * description:
 */
@HiltViewModel
class TantaRechargeViewModel @Inject constructor(private val repository: VquRechargeRepository) :
    BaseViewModel() {

    val rechargeOptionsListResultData = MutableLiveData<CommonVquRechargeData>()
    val wechatPayTypeResultData = MutableLiveData<BillTantaWechatPayTypeData>()

    val defaultPositionData = MutableLiveData<Int>()

    val walletIndexResultData = MutableLiveData<TantaWalletBean>()

    val rechargeOrderCreateResultData = MutableLiveData<TantaPayBean>()

    val firstRechargeResultData = MutableLiveData<IndexActivityBean>()

    val warningData = MutableLiveData<WarningBean>()

    /**
     * 类型  类型 ：  1 支付宝充值的金额    2.支付宝的彩币
     * 3：内购充值的金额   4：内购充值的彩币   5 .微信充值金额   6.微信购买彩币
     */
    fun getRechargeOptionsListData(type: Int) {
        launchIO {
            repository.vquGoodsList(type)
                .catch {
                }
                .collect {
                    if (!it.data.list.isNullOrEmpty()) {
                        for (index in it.data.list.indices) {
                            val commonVquRechargeOptions = it.data.list[index]
                            if (commonVquRechargeOptions.id == it.data.selId) {
                                it.data.list[index].isSelected = true
                                defaultPositionData.postValue(index)
                                break
                            }
                        }
                    }
                    rechargeOptionsListResultData.postValue(it.data!!)
                }
        }
    }

    fun getWechatPayType(device: Int) {
        launchIO {
            repository.getWechatPayType(device)
                .catch {
                }
                .collect {
                    wechatPayTypeResultData.postValue(it.data!!)
                }
        }
    }

    fun getWalletIndexData() {
        launchIO {
            repository.vquWalletIndex()
                .catch { }
                .collect {
                    walletIndexResultData.postValue(it.data!!)
                }
        }
    }

    fun createRechargeOrder(payType: String, id: Int, polling: Int) {
        launchIO {

            repository.vquRecharge(payType, id, polling)
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
                    rechargeOrderCreateResultData.postValue(it.data!!)
                }
        }
    }

    fun getFirstRechargeInfo() {
        launchIO {
            repository.getFirstRechargeInfo()
                .catch { }
                .collect {
                    firstRechargeResultData.postValue(it.data!!)
                }
        }
    }

    fun rechargeWarning(isClickRecharge: Boolean) {
        launchIO {
            repository.vquRechargeWarning()
                .catch {

                }
                .collect() {
                    it.data.isClickRechargeBtn = isClickRecharge
                    warningData.postValue(it.data!!)
                }
        }
    }
}
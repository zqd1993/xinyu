package com.mshy.VInterestSpeed.common.ui.vm

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.CommonVquRechargeData
import com.mshy.VInterestSpeed.common.bean.IndexActivityBean
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.bean.TantaWalletBean
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.PayOrderInfoBean
import com.mshy.VInterestSpeed.common.ui.dialog.CommonFirstRechargeDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonRechargeDialog
import com.mshy.VInterestSpeed.common.ui.repo.CommonPayRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * author: Lau
 * date: 2022/4/28
 * description:
 */
class CommonPayViewModel : BaseViewModel() {

    private val repo = CommonPayRepository()

    val payData = MutableLiveData<TantaPayBean>()

    val rechargeData = MutableLiveData<CommonVquRechargeData>()

    val defaultPositionData = MutableLiveData<Int>()

    val walletData = MutableLiveData<TantaWalletBean>()

    val firstRechargeResultData = MutableLiveData<IndexActivityBean>()

    val payConfigData = MutableLiveData<MutableList<BillPaymentData>>()

    val orderJson = MutableLiveData<PayOrderInfoBean>()

    fun recharge(type: String, payId: String) {
        launchIO {

            repo.vquRecharge(type, payId)
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
                    payData.postValue(it.data!!)
                }
        }
    }

    fun vquGoodsList(type: Int) {
        launchIO {
            repo.vquGoodsList(type)
                .catch {
                }
                .collect {
                    if (!it.data.list.isNullOrEmpty()) {
                        for (index in it.data.list.indices) {
                            it.data.list[index].isSelected = true
                            defaultPositionData.postValue(index)
                            break
                        }
                    }
                    rechargeData.postValue(it.data!!)
                }
        }
    }

    fun vquWalletIndex() {
        launchIO {
            repo.vquWalletIndex()
                .catch { }
                .collect {
                    walletData.postValue(it.data!!)
                }
        }
    }

    fun getFirstRechargeInfo() {
        launchIO {
            repo.getFirstRechargeInfo()
                .catch { }
                .collect {
                    firstRechargeResultData.postValue(it.data!!)
                }
        }
    }
    fun showRechargeDialog(fragmentManager: FragmentManager) {
        launchIO {
            repo.getFirstRechargeInfo()
                .catch {
                    val rechargeDialog = CommonRechargeDialog()
                    rechargeDialog.show(fragmentManager, "")
                }.collect {
                    if (it.data.isActivity != true) {
                        val firstRechargeDialog = CommonFirstRechargeDialog()
                        firstRechargeDialog.onVquRechargeClickListener {
                            firstRechargeDialog.dismiss()

                            val rechargeDialog = CommonRechargeDialog()
                            rechargeDialog.show(fragmentManager, "")

                            return@onVquRechargeClickListener true
                        }
                        firstRechargeDialog.setList(it.data.list)
                        Log.d("onEnoughCoin", "showRechargeDialog: bbb")
                        firstRechargeDialog.show(fragmentManager, "")
                    } else {
                        Log.d("onEnoughCoin", "showRechargeDialog: ccc")
                        val rechargeDialog = CommonRechargeDialog()
                        rechargeDialog.show(fragmentManager, "")
                    }
                }

        }
    }

    fun payConfig() {
        launchIO {
            repo.getPayConfig()
                .catch {

                }
                .collect() {
                    payConfigData.postValue(it.data!!)
                }
        }
    }

    fun recharge(channel: String, goodsId: Int, polling: Int, scheme: String) {
        launchIO {
            repo.recharge(channel, goodsId, polling, scheme)
                .catch {

                }
                .collect() {
                    orderJson.postValue(it.data!!)
                }
        }
    }
}
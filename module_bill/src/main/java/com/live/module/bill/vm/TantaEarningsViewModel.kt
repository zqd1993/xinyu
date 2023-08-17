package com.live.module.bill.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.bill.R
import com.live.module.bill.bean.BillVquAccountInfo
import com.live.module.bill.bean.BillVquEarningData
import com.live.module.bill.bean.BillTantaWithdrawOptions
import com.live.module.bill.bean.BillVquWithdrawPriceBean
import com.live.module.bill.repo.VquEarningsRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.TantaWalletBean
import com.mshy.VInterestSpeed.common.ext.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
@HiltViewModel
class TantaEarningsViewModel @Inject constructor(val repo: VquEarningsRepository) : BaseViewModel() {

    val earningsData = MutableLiveData<BillVquEarningData>()

    val walletIndexResultData = MutableLiveData<TantaWalletBean>()

    val withdrawPriceData = MutableLiveData<BillVquWithdrawPriceBean>()

    val withdrawResultData = MutableLiveData<Int>()

    val isAuthData = MutableLiveData<Boolean>()

    val accountData = MutableLiveData<BillVquAccountInfo>()


    /**
     * 获取提现选项
     */
    fun vquGetWithdrawOptions() {
        launchIO {
            repo.withdraw()
                .catch { }
                .collect {
                    val options = mutableListOf<BillTantaWithdrawOptions>()
                    if (!it.data.list.isNullOrEmpty()) {
                        it.data.list.forEachIndexed { index, s ->
                            options.add(
                                BillTantaWithdrawOptions(
                                    s,
                                    it.data.incomeMoney,
                                    index == 0 && it.data.incomeMoney.toDouble() >= s.toInt()
                                )
                            )
                        }
                    }

                    it.data.options = options

                    earningsData.postValue(it.data!!)
                }
        }
    }

    /**
     * 获取钱包信息
     */
    fun getWalletIndexData() {
        launchIO {
            repo.vquWalletIndex()
                .catch { }
                .collect {
                    walletIndexResultData.postValue(it.data!!)

                    vquGetWithdrawOptions()
                }
        }
    }

    /**
     * 绑定支付宝账号
     */
    fun vquBindAlipay(name: String, account: String, money: String) {

        if (name.isEmpty()) {
            toast(R.string.vqu_bill_realname_not_null)
            return
        }

        if (account.isEmpty()) {
            toast(R.string.vqu_bill_alipay_account_not_null)
            return
        }

        launchIO {
            repo.vquBindAlipay(name, account)
                .catch { e ->
                    e.message?.logE()
                    toast(R.string.vqu_bill_apply_withdrawal)
                }
                .collect {
                    vquGetWithdrawPrice(money)
                }
        }
    }

    /**
     * 获取实际到账金额
     */
    fun vquGetWithdrawPrice(money: String) {
        launchIO {
            repo.vquGetWithdrawPrice(money)
                .catch {
//                    toast(R.string.vqu_bill_get_withdraw_price_error)
                    toast(it.message ?: "")
                }
                .collect {
                    withdrawPriceData.postValue(it.data!!)
                }
        }
    }

    fun vquUserWithDraw(money: String) {
        launchIO {
            repo.vquUserWithDraw(money)
                .catch {
//                    toast(R.string.vqu_bill_withdraw_error)
                    toast(it.message ?: "")
                }
                .collect {
                    if (it.data !is String) {
                        withdrawResultData.postValue(-1)
                    } else {
                        toast(it.message)
                        withdrawResultData.postValue(1)
                    }
                }
        }
    }

    fun isAuth() {
        launchIO {
            repo.isAuth()
                .catch { }
                .collect {
                    isAuthData.postValue(it.data.mobileStatus == 1 && it.data.isAuth == 1)
                }
        }
    }

    fun vquWalletAlipay() {
        launchIO {
            repo.vquWalletAlipay()
                .catch {
                    it.printStackTrace()
                }
                .collect {
                    accountData.postValue(it.data!!)
                }
        }
    }
}
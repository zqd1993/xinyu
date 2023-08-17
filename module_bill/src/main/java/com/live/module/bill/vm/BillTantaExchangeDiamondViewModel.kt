package com.live.module.bill.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.bill.R
import com.live.module.bill.bean.BillVquEarningData
import com.live.module.bill.repo.BillVquExchangeDiamondRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/19
 * description:
 */
@HiltViewModel
class BillTantaExchangeDiamondViewModel @Inject constructor(val repo: BillVquExchangeDiamondRepository) :
    BaseViewModel() {

    val earningData = MutableLiveData<BillVquEarningData>()

    fun vquWithdraw() {
        launchIO {
            repo.vquWithdraw()
                .catch { }
                .collect {
                    earningData.postValue(it.data!!)
                }
        }
    }

    fun vquChangeCoin(money: Int) {
        launchIO {
            repo.vquChangeCoin(money)
                .catch {
                    toast(R.string.vqu_bill_exchange_error)
                }
                .collect {
                    toast(R.string.vqu_bill_exchange_success)
                    vquWithdraw()
                }
        }
    }

}
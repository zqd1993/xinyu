package com.live.module.bill.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.bill.bean.WithdrawalDetailItem
import com.live.module.bill.repo.BillVquWithdrawalListRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/21
 * description:
 */
@HiltViewModel
class BillVquWithdrawalListViewModel @Inject constructor(private val repo: BillVquWithdrawalListRepository) :
    BaseViewModel() {

    var mVquPage = 1

    val refreshWithdrawalListData = MutableLiveData<MutableList<WithdrawalDetailItem>?>()
    val loadMoreWithdrawalListData = MutableLiveData<MutableList<WithdrawalDetailItem>?>()

    val loadMoreAble = MutableLiveData<Boolean>()

    val emptyData = MutableLiveData<Boolean>()

    fun cashoutRecord(isLoadMore: Boolean) {
        if (isLoadMore) {
            mVquPage++
        } else {
            mVquPage = 1
        }

        launchIO {
            repo.vquCashoutRecord(mVquPage)
                .catch { }
                .collect {


                    loadMoreAble.postValue(it.data.page < mVquPage)

                    if (mVquPage == 1) {
                        emptyData.postValue(it.data.list.isNullOrEmpty())
                        refreshWithdrawalListData.postValue(it.data.list)
                    } else {
                        loadMoreWithdrawalListData.postValue(it.data.list)
                    }
                }
        }
    }
}
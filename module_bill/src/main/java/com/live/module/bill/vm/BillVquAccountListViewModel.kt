package com.live.module.bill.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.bill.bean.BillVquAccountInfo
import com.live.module.bill.repo.BillVquAccountListRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
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
class BillVquAccountListViewModel @Inject constructor(val repo: BillVquAccountListRepository) :
    BaseViewModel() {

    val accountListData = MutableLiveData<MutableList<BillVquAccountInfo>?>()

    val accountUseResult = MutableLiveData<Boolean>()

    fun accountList() {
        launchIO {

            repo.vquAccountList()
                .catch { }
                .collect {
                    accountListData.postValue(it.data.list)
                }
        }
    }

    fun vquAccountUser(id: String) {

        launchIO {

            repo.vquAccountUse(id)
                .catch {
                    toast(it.message ?: "")
                }
                .collect {
                    toast("使用成功")
                    accountUseResult.postValue(true)
                }

        }
    }
}
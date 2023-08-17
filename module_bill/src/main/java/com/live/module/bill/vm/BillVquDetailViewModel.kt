package com.live.module.bill.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.bill.bean.BillVquDetailSectionBean
import com.live.module.bill.repo.BillVquDetailRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/20
 * description:
 */
@HiltViewModel
class BillVquDetailViewModel @Inject constructor(val repo: BillVquDetailRepository) :
    BaseViewModel() {

    var page = 1

    val listData = MutableLiveData<MutableList<BillVquDetailSectionBean>>()

    private var currentListDate = ""

    fun vquWalletRecord(
        type: Int,
        cateId: Int,
        date: String,
        category: Int,
        newType: Int,
        isLoadMore: Boolean
    ) {
        val params = mutableMapOf<String, Any>()
        params["type"] = type
        params["cate_id"] = cateId
        params["date"] = date
        params["category"] = category
        params["new_type"] = newType
        if (isLoadMore) {
            page++
        } else {
            page = 1
        }
        params["page"] = page


        launchIO {
            repo.vquWalletRecord(params)
                .catch { }
                .collect {
//
                    val list = mutableListOf<BillVquDetailSectionBean>()

                    if (!it.data.newList.isNullOrEmpty()) {
                        it.data.newList.forEach { newListBean ->

                            if (currentListDate != newListBean.date) {
                                currentListDate = newListBean.date
                                val headerBean = BillVquDetailSectionBean()
                                headerBean.setHeaderBean(newListBean)
                                list.add(headerBean)
                            }

                            if (!newListBean.list.isNullOrEmpty()) {
                                newListBean.list.forEach { childListBean ->
                                    val childBean = BillVquDetailSectionBean()
                                    childBean.setChildBean(childListBean)
                                    list.add(childBean)
                                }
                            }
                        }
                    }

                    listData.postValue(list)
                }
        }
    }
}
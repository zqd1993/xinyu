package com.live.module.contact.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.contact.bean.ContactVquInvitedData
import com.live.module.contact.bean.ContactVquOverviewData
import com.live.module.contact.repo.ContactTantaMyContactRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
@HiltViewModel
class ContactTantaMyContactViewModel @Inject constructor(private val repo: ContactTantaMyContactRepository) :
    BaseViewModel() {

    var mVquPage = 1

    val vquOverviewData = MutableLiveData<ContactVquOverviewData>()
    val vquExcelViewData = MutableLiveData<BaseResponse<Any>>()

    val vquInvitedCountData = MutableLiveData<Int>()

    val vquLoadMoreData = MutableLiveData<MutableList<ContactVquInvitedData>?>()
    val vquRefreshData = MutableLiveData<MutableList<ContactVquInvitedData>?>()
    val isEmptyData = MutableLiveData<Boolean>()


    fun vquOverview(afterKey: Int, beginTime: String, endTime: String, sortType: String) {
        launchIO {
            repo.vquOverview(afterKey, beginTime, endTime, sortType)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    vquOverviewData.postValue(it.data!!)
                }
        }
    }

    fun vquExcel2email(email: String, beginTime: String, endTime: String) {
        launchIO {
            repo.vquExcel2email(email, beginTime, endTime)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    vquExcelViewData.postValue(it)
                }
        }
    }

    fun vquInviteList(isLoadMore: Boolean, keyword: String) {
        launchIO {
            if (isLoadMore) {
                mVquPage++
            } else {
                mVquPage = 1
            }

            repo.vquInviteList(mVquPage, keyword)
                .catch {
                    if (mVquPage == 1) {
                        isEmptyData.postValue(true)
                        vquRefreshData.postValue(null)
                        vquInvitedCountData.postValue(0)
                    }
                }
                .collect {
                    vquInvitedCountData.postValue(it.data.total)
                    if (mVquPage == 1) {
                        isEmptyData.postValue(it.data.list.isNullOrEmpty())
                        vquRefreshData.postValue(it.data.list)
                    } else {
                        vquLoadMoreData.postValue(it.data.list)
                    }
                }
        }

    }
}
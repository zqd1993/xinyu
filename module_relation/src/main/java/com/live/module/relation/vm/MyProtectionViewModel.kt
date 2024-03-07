package com.live.module.relation.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.relation.bean.MyProtectionListBean
import com.live.module.relation.repo.RelationRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MyProtectionViewModel @Inject constructor(private val repository: RelationRepository) :
    BaseViewModel() {

    var mPage = 1

    val protectionList = MutableLiveData<MutableList<MyProtectionListBean>>()

    fun vquGetList(type: Int, isLoadMore: Boolean) {
        if (isLoadMore) {
            mPage++
        } else {
            mPage = 1
        }

        launchIO {
            when (type) {
                RouteKey.RelationType.PROTECTION_ME -> {
                    repository.vquGuardianMyList(mPage)
                        .catch { }
                        .collect {
                            protectionList.postValue(it.data!!)
                        }
                }
                RouteKey.RelationType.MY_PROTECTION -> {
                    repository.vquMyGuardianList(mPage)
                        .catch { }
                        .collect {
                            protectionList.postValue(it.data!!)
                        }
                }
            }
        }
    }

}
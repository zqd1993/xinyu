package com.live.module.relation.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.relation.repo.RelationRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/2
 * description:
 */
@HiltViewModel
class RelationViewModel @Inject constructor(private val repository: RelationRepository) :
    BaseViewModel() {

    var mPage = 1

    val relationList = MutableLiveData<MutableList<VquRelationBean>>()

    val loadMoreAble = MutableLiveData<Boolean>()

    val followResult = MutableLiveData<VquRelationBean>()

    val vquBeckonInt = MutableLiveData<Int>()

    val vquNoMoneyResult = MutableLiveData<Boolean>()

    fun vquGetList(type: Int, isLoadMore: Boolean) {
        if (isLoadMore) {
            mPage++
        } else {
            mPage = 1
        }

        launchIO {
            when (type) {
                RouteKey.RelationType.FOCUS -> {
                    repository.vquFollowList(mPage)
                        .catch { }
                        .collect {
                            relationList.postValue(it.data.list)
                        }
                }
                RouteKey.RelationType.FANS -> {
                    repository.vquFansList(mPage)
                        .catch { }
                        .collect {
                            relationList.postValue(it.data.list)
                        }
                }
                RouteKey.RelationType.VISTOR -> {
                    repository.vquVisitorList(mPage)
                        .catch { }
                        .collect {
                            relationList.postValue(it.data.list)
                        }
                }
                RouteKey.RelationType.TRACK -> {
                    repository.vquViewerList(mPage)
                        .catch { }
                        .collect {

                            loadMoreAble.postValue(it.data.totalPage > mPage)

                            relationList.postValue(it.data.list)
                        }
                }
            }
        }
    }

    fun vquFocus(item: VquRelationBean) {
        launchIO {
            item.userid?.let { userId ->
                repository.vquFollow(userId)
                    .catch { }
                    .collect {
                        if (it.data.action == "add") {
                            item.isFollow = 1
                        } else {
                            item.isFollow = 0
                        }

                        followResult.postValue(item)

                    }
            }
        }
    }

    fun vquSendBeckon(userId: String, position: Int) {
        launchIO {
            repository.vquSendBeckon(userId)
                .catch { }
                .collect {
                    if (it.code != 0) {
                        when (it.code) {
                            1002 -> {
                                vquBeckonInt.postValue(position)
                                vquNoMoneyResult.postValue(true)
                            }
                            7480 -> {
                                toast(it.message)
                            }
                            else -> {
                                toast(it.message)
                                vquBeckonInt.postValue(position)
                            }
                        }
                    }
                }
        }
    }
}
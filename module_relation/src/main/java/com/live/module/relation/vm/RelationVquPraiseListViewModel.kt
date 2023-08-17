package com.live.module.relation.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.relation.bean.VquRelationPraiseBean
import com.live.module.relation.repo.RelationVquPraiseListRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/13
 * description:获赞列表的ViewModel
 */
@HiltViewModel
class RelationVquPraiseListViewModel @Inject constructor(val repo: RelationVquPraiseListRepository) :
    BaseViewModel() {

    /**
     * 获赞列表LiveData，返回获赞列表，用于RecycleView
     */
    val praiseList = MutableLiveData<MutableList<VquRelationPraiseBean>>()

    /**
     * 是否可以加载更多
     */
    val loadMoreAble = MutableLiveData<Boolean>()

    /**
     * 页码
     */
    var mPage = 1


    /**
     * 获取获赞列表
     * @param isLoadMore 是否加载更多，true加载更多，false，刷新
     */
    fun vquLikeList(isLoadMore: Boolean) {

        /*
            判断是否加载更多
         */
        if (isLoadMore) {
            mPage++
        } else {
            mPage = 1
        }

        launchIO {
            repo.vquLikeList(mPage)
                .catch {
                    if (mPage != 1) {
                        mPage--
                    }

                    toast(it.message ?: "")
                }
                .collect {

                    loadMoreAble.postValue(it.data.totalPage > mPage)

                    praiseList.postValue(it.data.list)
                }
        }
    }

}
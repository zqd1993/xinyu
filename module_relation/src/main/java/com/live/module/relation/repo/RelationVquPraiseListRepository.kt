package com.live.module.relation.repo

import com.live.module.relation.bean.VquRelationPraiseListBean
import com.live.module.relation.net.RelationVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
class RelationVquPraiseListRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: RelationVquApiService

    suspend fun vquLikeList(page: Int) = request<BaseResponse<VquRelationPraiseListBean>> {
        mVquApi.vquLikeList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
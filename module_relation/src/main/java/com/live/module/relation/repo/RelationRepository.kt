package com.live.module.relation.repo

import com.live.module.relation.bean.MyProtectionListBean
import com.live.module.relation.net.RelationVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.VquFollowBean
import com.mshy.VInterestSpeed.common.bean.VquRelationListBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/2
 * description:
 */
class RelationRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: RelationVquApiService

    suspend fun vquFollowList(page: Int) = request<BaseResponse<VquRelationListBean>> {
        mVquApi.vquFollowList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquFansList(page: Int) = request<BaseResponse<VquRelationListBean>> {
        mVquApi.vquFansList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquVisitorList(page: Int) = request<BaseResponse<VquRelationListBean>> {
        mVquApi.vquVisitorList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquViewerList(page: Int) = request<BaseResponse<VquRelationListBean>> {
        mVquApi.vquViewerList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquFollow(userid: String) = request<BaseResponse<VquFollowBean>> {
        mVquApi.vquFollow(userid).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquSendBeckon(userId: String) = request<BaseResponse<Any>> {
        mVquApi.vquSendBeckon(userId).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGuardianMyList(page: Int) = request<BaseResponse<MutableList<MyProtectionListBean>>> {
        mVquApi.vquGuardianMyList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquMyGuardianList(page: Int) = request<BaseResponse<MutableList<MyProtectionListBean>>> {
        mVquApi.vquMyGuardianList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
package com.live.module.home.repo

import com.live.module.home.bean.HomeVquBeckonBean
import com.live.module.home.bean.HomeVquUserInfo
import com.live.module.home.net.HomeVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/7/4
 * description:
 */
class HomeVquSearchRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: HomeVquApiService

    suspend fun vquGetUserInfoByUserCode(keyword: String) =
        request<BaseResponse<HomeVquUserInfo>> {
            mVquApi.vquGetUserInfoByUserCode(keyword).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }


    suspend fun vquSendBeckon(userId: String) = request<BaseResponse<HomeVquBeckonBean>> {
        mVquApi.vquSendBeckon(userId,0,0).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
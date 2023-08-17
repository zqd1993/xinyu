package com.live.module.auth.repo

import com.live.module.auth.net.AuthVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquAuthBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
class AuthVquCenterRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: AuthVquApiService

    @Inject
    lateinit var mVquGlobalApi: GlobalApiService

    suspend fun isAuth() = request<BaseResponse<CommonVquAuthBean>> {
        mVquGlobalApi.vquIsAuth().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }



}
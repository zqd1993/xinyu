package com.live.module.auth.repo

import com.live.module.auth.bean.DescribeVerifyTokenBean
import com.live.module.auth.net.AuthVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
class AuthVquRealAuthRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: AuthVquApiService

    suspend fun vquUserAuth(name: String, id: String) =
        request<BaseResponse<Any>> {
            mVquApi.vquUserAuth(name, id).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquDescribeVerifyToken() =
        request<BaseResponse<DescribeVerifyTokenBean>> {
            mVquApi.vquDescribeVerifyToken().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquUserAuth(params: MutableMap<String, Any>) =
        request<BaseResponse<Any>> {
            mVquApi.vquUserAuth(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
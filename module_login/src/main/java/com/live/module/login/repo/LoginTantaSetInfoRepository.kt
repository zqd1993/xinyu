package com.live.module.login.repo

import com.live.module.login.bean.LoginTantaNicknameBean
import com.live.module.login.net.LoginTantaApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.SystemBean
import com.mshy.VInterestSpeed.common.bean.TantaLoginBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/8
 * description:
 */
class LoginTantaSetInfoRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: LoginTantaApiService

    suspend fun tantaNickname() = request<BaseResponse<LoginTantaNicknameBean>> {
        mVquApi.vquNickname().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun tantaProfileReg(params: MutableMap<String, Any>) =
        request<BaseResponse<TantaLoginBean>> {
            mVquApi.vquProfileReg(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun tantaSystemIndex() =
        request<BaseResponse<SystemBean>> {
            mVquApi.getSystemIndex().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    fun tantaGetStsIndex(type: String) =
        request<BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>> {
            mVquApi.vquGetStsIndex(type).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
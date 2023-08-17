package com.mshy.VInterestSpeed.common.ui.repo

import android.util.Log
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.SystemBean
import com.mshy.VInterestSpeed.common.bean.TantaLoginBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

class LoginRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: GlobalApiService

    suspend fun getSystemIndex() = request<BaseResponse<SystemBean>> {
        mVquApi.getSystemIndex().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun onKeyLogin(params: MutableMap<String, Any>) = request<BaseResponse<TantaLoginBean>> {
        mVquApi.onKeyLogin(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun tantaSendCode(params: MutableMap<String, Any>) = request<BaseResponse<Any>> {
        mVquApi.tantaSendCode(params).run {
            responseCodeExceptionHandler(code, message) {
                Log.d("TAG", "vquSendCode: " + Thread.currentThread())
                emit(this)
            }
        }
    }

    suspend fun tantaCodeLogin(params: MutableMap<String, Any>) =
        request<BaseResponse<TantaLoginBean>> {
            mVquApi.tantaCodeLogin(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    fun tantaThirdLogin(netParams: MutableMap<String, Any>) =
        request<BaseResponse<TantaLoginBean>> {
            mVquApi.tantaPassportThird(netParams).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
package com.mshy.VInterestSpeed.common.ui.repo

import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.PhoneLoginApiService
import javax.inject.Inject

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:32
 *
 */
class LoginActivityRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mLoginApi: PhoneLoginApiService


    /**
     * 获取验证码
     */
    suspend fun sendCode(map:HashMap<String,Any>) = request<BaseResponse<String>> {
        mLoginApi.sendCode(map).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun codeLogin(map:HashMap<String,Any>) = request<BaseResponse<String>> {
        mLoginApi.codeLogin(map).run {
            responseCodeExceptionHandler(code, message){
                emit(this)
            }
        }
    }

}
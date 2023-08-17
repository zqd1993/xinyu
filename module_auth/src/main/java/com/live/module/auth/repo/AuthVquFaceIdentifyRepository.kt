package com.live.module.auth.repo

import com.alipay.face.api.ZIMFacade
import com.live.module.auth.bean.DescribeVerifyTokenBean
import com.live.module.auth.bean.VquAuthResultBean
import com.live.module.auth.net.AuthVquApiService
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/28
 * description:
 */
class AuthVquFaceIdentifyRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: AuthVquApiService

    suspend fun vquAgreeProtocol(agreement: Int) =
        request<BaseResponse<Boolean>> {
            mVquApi.vquAgreeProtocol(agreement).run {
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


    suspend fun vquDescribeVerifyResult(bizId: String,token:String) =
        request<BaseResponse<VquAuthResultBean>> {
            mVquApi.vquAuthGetResult(bizId,token).run {
                responseCodeExceptionHandler(code,message){
                    emit(this)
                }
            }
//            mVquApi.vquDescribeVerifyResult(bizId).run {
//                responseCodeExceptionHandler(code, message) {
//                    emit(this)
//                }
//            }
        }
    suspend fun vquRPAuth() =
        request<BaseResponse<DescribeVerifyTokenBean>> {
            mVquApi.vquRPAuth(ZIMFacade.getMetaInfos(BaseApplication.application)).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
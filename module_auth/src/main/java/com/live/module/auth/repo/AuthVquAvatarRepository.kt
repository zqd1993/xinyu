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
 * date: 2022/5/19
 * description:
 */
class AuthVquAvatarRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: AuthVquApiService

    suspend fun vquGetStsIndex(type: String) =
        request<BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>> {
            mVquApi.vquGetStsIndex(type).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun compareFace(avatar: String) =
        request<BaseResponse<VquAuthResultBean>> {
            mVquApi.vquCompareFace(avatar).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquRPAuth() =
        request<BaseResponse<DescribeVerifyTokenBean>> {
            mVquApi.vquRPAuth(ZIMFacade.getMetaInfos(BaseApplication.application)).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }


    suspend fun vquDescribeVerifyResult(bizId: String, token: String) =
        request<BaseResponse<VquAuthResultBean>> {
            mVquApi.vquAuthGetResult(bizId,token).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
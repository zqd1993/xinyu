package com.live.module.anchor.repo

import com.live.module.anchor.net.AnchorVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/26
 * description:
 */
class AnchorVquNewHelloTemplateRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: AnchorVquApiService

    suspend fun getStsIndex(type: String) =
        request<BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>> {
            mVquApi.vquGetStsIndex(type).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    /**
     * params具体参数，请查看[AnchorVquApiService.vquGreetAdd]
     */
    suspend fun vquGreetAdd(params: MutableMap<String, Any>) =
        request<BaseResponse<Any>> {
            mVquApi.vquGreetAdd(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
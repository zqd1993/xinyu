package com.live.module.anchor.repo

import com.live.module.anchor.bean.AnchorVquHelloListBean
import com.live.module.anchor.net.AnchorVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/25
 * description:
 */
class AnchorVquHelloSettingRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: AnchorVquApiService

    suspend fun vquListNew() =
        request<BaseResponse<AnchorVquHelloListBean>> {
            mVquApi.vquListNew().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }


    suspend fun vquSetName(params: MutableMap<String, Any>) =
        request<BaseResponse<Any>> {
            mVquApi.vquSetName(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }


    suspend fun vquDeleteNew(id: String) =
        request<BaseResponse<Any>> {
            mVquApi.vquDeleteNew(id).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    fun vquSetDefault(id: String) =
        request<BaseResponse<Any>> {
            mVquApi.vquSetDefault(id).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
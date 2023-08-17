package com.mshy.VInterestSpeed.common.ui.repo

import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.DynamicVquReportBean
import com.mshy.VInterestSpeed.common.bean.InfoVoiceListBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

class UploadRepository @Inject constructor():BaseRepository() {
    @Inject
    lateinit var mApi: GlobalApiService
    suspend fun vquGetSts(params: HashMap<String, Any>) = request<BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>> {
        mApi.vquGetSts(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquReportCate(params: HashMap<String, Any>) = request<BaseResponse<MutableList<DynamicVquReportBean>>> {
        mApi.vquReportCate(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquReport(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mApi.vquReport(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquGetVoiceTextList(params: HashMap<String, Any>) = request<BaseResponse<InfoVoiceListBean>> {
        mApi.vquGetVoiceTextList(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
package com.live.module.info.repo

import com.live.module.info.bean.InfoUserTagBean
import com.live.module.info.bean.InfoVquSelectDataBean
import com.live.module.info.bean.InfoVquUserBean
import com.live.module.info.net.InfoVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 *
 *
 */
class InfoEditActivityRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mInfoApi: InfoVquApiService

    suspend fun vquGetUserInfo(params: HashMap<String, Any>) = request<BaseResponse<InfoVquUserBean>> {
        mInfoApi.vquGetUserInfo(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquGetSelectData(params: HashMap<String, Any>) = request<BaseResponse<InfoVquSelectDataBean>> {
        mInfoApi.vquGetSelectData(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquGetUserTag(params: HashMap<String, Any>) = request<BaseResponse<InfoUserTagBean>> {
        mInfoApi.vquGetUserTag(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquGetProvinceList(params: HashMap<String, Any>) = request<BaseResponse<MutableList<VquInfoAddressBean>>> {
        mInfoApi.vquGetProvinceList(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquGetSts(params: HashMap<String, Any>) = request<BaseResponse<StsInfoBean>> {
        mInfoApi.vquGetSts(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquSaveUserInfo(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mInfoApi.vquSaveUserInfo(params).run {
                emit(this)
        }
    }
    suspend fun vquCompareFacesAvatar(params: HashMap<String, Any>) = request<BaseResponse<String>> {
        mInfoApi.vquCompareFacesAvatar(params).run {
            emit(this)
        }
    }
    suspend fun vquIsAuth(params: HashMap<String, Any>) = request<BaseResponse<CommonAuthBean>> {
        mInfoApi.vquIsAuth(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

}
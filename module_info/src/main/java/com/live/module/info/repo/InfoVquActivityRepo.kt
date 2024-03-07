package com.live.module.info.repo

import com.live.module.info.bean.*
import com.live.module.info.net.InfoVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonAuthBean
import com.mshy.VInterestSpeed.common.bean.ProtectionOptionBean
import com.mshy.VInterestSpeed.common.bean.ProtectionStatusBean
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 *
 *
 */
class InfoVquActivityRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mInfoApi: InfoVquApiService

    suspend fun vquGetUserInfo(params: HashMap<String, Any>) = request<BaseResponse<InfoVquInfoBean>> {
        mInfoApi.vquGetUserInfoIndex(params).run {
                emit(this)
        }
    }
    suspend fun vquFollow(params: HashMap<String, Any>) = request<BaseResponse<InfoVquFollowResultBean>> {
        mInfoApi.vquFollow(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquBlack(params: HashMap<String, Any>) = request<BaseResponse<InfoVquFollowResultBean>> {
        mInfoApi.vquBlack(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquSendBeckon(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mInfoApi.vquSendBeckon(params).run {
                emit(this)
        }
    }
    suspend fun vquGetGiftList(params: HashMap<String, Any>) = request<BaseResponse<InfoVquGiftBean>> {
        mInfoApi.vquGetGiftList(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    /**
     * 1v1 视频
     */
    suspend fun vquGetCallInfo(params: HashMap<String, Any>) = request<BaseResponse<VideoVquCallBean>> {
        mInfoApi.vquGetCallInfo(params).run {
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
    suspend fun vquSaveRemarkName(userId: String, remarkName: String) = request<BaseResponse<Any>> {
        mInfoApi.saveRemark(userId, remarkName).run {
            responseCodeExceptionHandler(code,message) {
                emit(this)
            }
        }
    }

    suspend fun vquGuardianStatus(userId: Int) = request<BaseResponse<ProtectionStatusBean>> {
        mInfoApi.vquGuardianStatus(userId).run {
            responseCodeExceptionHandler(code,message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetGuardianOptions() = request<BaseResponse<MutableList<ProtectionOptionBean>>> {
        mInfoApi.vquGetGuardianOptions(100).run {
            responseCodeExceptionHandler(code,message) {
                emit(this)
            }
        }
    }

}
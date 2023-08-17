package com.live.module.setting.repo

import com.live.module.setting.bean.BlackListBean
import com.live.module.setting.bean.FateMatchBean
import com.live.module.setting.bean.SettingVquBindBean
import com.live.module.setting.bean.ShowLocationBean
import com.live.module.setting.net.SettingVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 *
 *
 */
class SettingVquActivityRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var vquSettingApi: SettingVquApiService

    @Inject
    lateinit var vquGlobalApiService: GlobalApiService

    suspend fun vquSetVideoStatus(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquSetVideoStatus(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquSetUserVideoStatus(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquSetUserVideoStatus(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetUserInfo(params: HashMap<String, Any>) =
        request<BaseResponse<VquUserHomeBean>> {
            vquSettingApi.vquGetUserInfo(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquGetBindInfo(params: HashMap<String, Any>) =
        request<BaseResponse<SettingVquBindBean>> {
            vquSettingApi.vquGetBindInfo(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquSendCode(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquSendCode(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquBindMobile(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquBindMobile(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquCloseModeByCode(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquCloseModeByCode(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetFateMatch(params: HashMap<String, Any>) =
        request<BaseResponse<FateMatchBean>> {
            vquSettingApi.vquGetFateMatch(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
    suspend fun vquShowLocation(params: HashMap<String, Any>) =
        request<BaseResponse<ShowLocationBean>> {
            vquSettingApi.vquShowLocation(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
    suspend fun vquSetFateMatch(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquSetFateMatch(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquSetTeenMode(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquSetTeenMode(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetBlackList(params: HashMap<String, Any>) =
        request<BaseResponse<VquListBean<BlackListBean>>> {
            vquSettingApi.vquGetBlackList(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquDoBlack(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquDoBlack(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquCloseAccount(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquCloseAccount(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquCloseTeenMode(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquCloseTeenMode(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquCheckUpdate(params: HashMap<String, Any>) =
        request<BaseResponse<SettingVquVersionBean>> {
            vquSettingApi.vquCheckUpdate(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquIsAuth(params: HashMap<String, Any>) = request<BaseResponse<CommonAuthBean>> {
        vquSettingApi.vquIsAuth(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquLoginOut(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.vquLoginOut(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquSystemIndex() =
        request<BaseResponse<SystemBean>> {
            vquGlobalApiService.getSystemIndex().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun getUserLikeStatus(params: HashMap<String, Any>) = request<BaseResponse<String>> {
        vquSettingApi.getUserLikeStatus(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun setUserLikeStatus(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        vquSettingApi.setUserLikeStatus(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
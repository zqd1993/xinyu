package com.mshy.VInterestSpeed.common.ui.repo

import com.alibaba.fastjson.JSONObject
import com.google.gson.internal.LinkedTreeMap
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.utils.TantaCitySelector
import com.mshy.VInterestSpeed.uikit.bean.UpdateOnlineBean
import javax.inject.Inject

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:32
 *
 */
class SplashActivityRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: GlobalApiService


    /**
     * 获取全局配置数据
     */
    suspend fun getSystemIndex(bindData: String, channelCode: String) =
        request<BaseResponse<SystemBean>> {
            mApi.getSystemIndex(bindData, channelCode).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    /**
     * 1v1 视频
     */
    suspend fun vquGetCallInfoFromRoom(room_id: Int) = request<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>> {
        mApi.vquGetCallInfoFromRoom(room_id.toString()).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }


    /**
     * 获取全局配置数据
     */
    suspend fun vquGetGlobalConfig() = request<BaseResponse<com.mshy.VInterestSpeed.common.bean.CommonVquGlobalConfigBean>> {
        mApi.vquGetGlobalConfig().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetTeenMode() = request<BaseResponse<CommonVquStatusBean>> {
        mApi.vquGetTeenMode().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetBadgeConfig() = request<BaseResponse<JSONObject>> {
        mApi.vquGetBadgeConfig().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }


    suspend fun queryCities() =
        request<BaseResponse<LinkedTreeMap<String, String>>> {
            mApi.citys().run {
                responseCodeExceptionHandler(code, message) {
                    val citiesMap = mutableMapOf<String, String>()
                    for (datum in this.data) {
                        citiesMap[datum.value] = datum.key
                    }
                    TantaCitySelector.vquCityMap.putAll(citiesMap)
                    emit(this)
                }
            }
        }

    suspend fun vquQueryIntimateList() =
        request<BaseResponse<com.mshy.VInterestSpeed.uikit.bean.ChatIntimateBean>> {
            mApi.vquQueryIntimateList().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun isAuth() =
        request<BaseResponse<CommonVquAuthBean>> {
            mApi.vquIsAuth().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun getOnlineUpdate() =
        request<BaseResponse<UpdateOnlineBean>> {
            mApi.getOnlineUpdate().run {
                emit(this)
            }
        }
    suspend fun getOnlineUpdateNotResponse() =
        request<BaseResponse<UpdateOnlineBean>> {
            mApi.getOnlineUpdate()
        }

    suspend fun vquCloseInfoFinishTip() = request<BaseResponse<Any>> {
        mApi.vquCloseInfoFinishTip().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
package com.live.module.anchor.repo

import com.live.module.anchor.bean.AnchorTantaPriceBean
import com.live.module.anchor.bean.AnchorTantaSettingBean
import com.live.module.anchor.bean.AnchorVquSkillSettingBean
import com.live.module.anchor.net.AnchorVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
class AnchorTantaRateSettingRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mVquApi: AnchorVquApiService

    suspend fun vquSkillSetting() =
        request<BaseResponse<MutableList<AnchorVquSkillSettingBean>>> {
            mVquApi.vquSkillSetting().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    /**
     * 获取主播收费设置参数
     *
     * @see [AnchorVquApiService.vquGetAnchorSetting]
     */
    suspend fun tantaGetAnchorSetting(params: MutableMap<String, String>) =

        request<BaseResponse<AnchorTantaSettingBean>> {
            mVquApi.vquGetAnchorSetting(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun tantaGetPrice(params: MutableMap<String, String>) =
        request<BaseResponse<AnchorTantaPriceBean>> {
            mVquApi.vquGetPrice(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun tantaManGetPrice(params: MutableMap<String, String>) =
        request<BaseResponse<AnchorTantaPriceBean>> {
            mVquApi.vquManGetPrice(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquSetPrice(params: MutableMap<String, Any>) =
        request<BaseResponse<String>> {
            mVquApi.vquSetPrice(params).run {
                responseCodeExceptionHandler(code,message){
                    emit(this)
                }
            }
        }

    /**
     * 获取主播收费设置参数
     *
     * @see [AnchorVquApiService.vquGetAnchorSetting]
     */
    suspend fun tantaManGetAnchorSetting(params: MutableMap<String, String>) =

        request<BaseResponse<AnchorTantaSettingBean>> {
            mVquApi.vquManGetAnchorSetting(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquManSetPrice(params: MutableMap<String, Any>) =
        request<BaseResponse<String>> {
            mVquApi.vquManSetPrice(params).run {
                responseCodeExceptionHandler(code,message){
                    emit(this)
                }
            }
        }

    suspend fun vquSetVideoStatus(status: Int) =
        request<BaseResponse<Any>> {
            mVquApi.vquSetVideoStatus(status).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
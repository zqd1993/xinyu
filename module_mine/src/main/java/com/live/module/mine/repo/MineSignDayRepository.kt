package com.live.module.mine.repo

import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.SignDaySuccessData
import com.mshy.VInterestSpeed.common.bean.TodayBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/23
 * description:
 */
class MineSignDayRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquGlobalApi: GlobalApiService

    suspend fun vquTodayActivityIndex() =
        request<BaseResponse<TodayBean>> {
            mVquGlobalApi.tantaTodayActivityIndex().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquTodayActivity() =
        request<BaseResponse<SignDaySuccessData>> {
            mVquGlobalApi.tantaTodayActivity().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
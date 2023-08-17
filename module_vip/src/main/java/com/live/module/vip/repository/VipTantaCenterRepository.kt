package com.live.module.vip.repository

import com.live.module.vip.bean.TantaVipInfoBean
import com.live.module.vip.net.VipVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
class VipTantaCenterRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mVquApi: VipVquApiService

    suspend fun createVipOrder(
        id: String, pay_type: String, type: String,
        vip_goods_id: String
    ) = request<BaseResponse<TantaPayBean>> {
        mVquApi.createVipOrder(id, pay_type, type, vip_goods_id).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun getVipIndexInfo() = request<BaseResponse<TantaVipInfoBean>> {
        mVquApi.getVipIndexInfo().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun getVipDailyGif() = request<BaseResponse<Any>> {
        mVquApi.getVipDailyGif().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
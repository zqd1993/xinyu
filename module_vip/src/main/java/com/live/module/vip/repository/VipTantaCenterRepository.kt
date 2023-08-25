package com.live.module.vip.repository

import android.util.Log
import com.live.module.vip.bean.TantaVipInfoBean
import com.live.module.vip.net.VipVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.live.vquonline.base.utils.GsonUtil
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.PayOrderInfoBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
class VipTantaCenterRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mVquApi: VipVquApiService

    @Inject
    lateinit var mVquGlobalApi: GlobalApiService

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

    suspend fun getPayConfig() = request<BaseResponse<MutableList<BillPaymentData>>> {
        mVquGlobalApi.getPayConfig().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun payNobleOrder(channel: String, goodsId: String, polling: Int, scheme: String, type: String, id: String) = request<BaseResponse<PayOrderInfoBean>> {
        mVquGlobalApi.payNobleOrder(channel, goodsId, polling, scheme, type, id).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
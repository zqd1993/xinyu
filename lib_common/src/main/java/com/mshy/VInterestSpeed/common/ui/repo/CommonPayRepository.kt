package com.mshy.VInterestSpeed.common.ui.repo

import android.util.Log
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.live.vquonline.base.utils.GsonUtil
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.PayOrderInfoBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage

/**
 * author: Lau
 * date: 2022/4/28
 * description:
 */
class CommonPayRepository : BaseRepository() {

    private val mVquGlobalApi: GlobalApiService by lazy {
        GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
    }


    suspend fun vquRecharge(type: String, payId: String) =
        request<BaseResponse<TantaPayBean>> {
            mVquGlobalApi.recharge(type, payId,-1).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquGoodsList(type: Int) = request<BaseResponse<CommonVquRechargeData>> {
        mVquGlobalApi.vquGoodsList(type).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquWalletIndex() =
        request<BaseResponse<TantaWalletBean>> {
            mVquGlobalApi.walletIndex().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun getFirstRechargeInfo() =
        request<BaseResponse<IndexActivityBean>> {
            mVquGlobalApi.getFirstRechargeInfo().run {
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
    suspend fun recharge(channel: String, goodsId: Int, polling: Int, scheme: String) = request<BaseResponse<PayOrderInfoBean>> {
        mVquGlobalApi.recharge(channel, goodsId, polling, scheme).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
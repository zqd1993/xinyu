package com.live.module.bill.repo

import android.util.Log
import com.live.module.bill.bean.BillTantaWechatPayTypeData
import com.live.module.bill.net.BillVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.live.vquonline.base.utils.GsonUtil
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.PayOrderInfoBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/2
 * description:
 */
class VquRechargeRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: BillVquApiService

    @Inject
    lateinit var mVquGlobalApi: GlobalApiService


    suspend fun vquGoodsList(type: Int) = request<BaseResponse<CommonVquRechargeData>> {
        mVquApi.vquGoodsList(type).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun getWechatPayType(type: Int) = request<BaseResponse<BillTantaWechatPayTypeData>> {
        mVquApi.getWechatPayType(type).run {
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

    suspend fun vquRecharge(payType: String, id: Int, polling: Int) =
        request<BaseResponse<TantaPayBean>> {
            mVquGlobalApi.recharge(payType, id.toString(), polling).run {
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


    suspend fun vquRechargeWarning() = request<BaseResponse<WarningBean>> {
        mVquApi.vquRechargeWarning().run {
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
                Log.d("rechargeData", GsonUtil.GsonString(data))
                emit(this)
            }
        }
    }

}
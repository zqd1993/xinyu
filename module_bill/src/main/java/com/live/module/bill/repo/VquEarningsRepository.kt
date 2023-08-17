package com.live.module.bill.repo

import com.live.module.bill.bean.BillVquAccountInfo
import com.live.module.bill.bean.BillVquEarningData
import com.live.module.bill.bean.BillVquWithdrawPriceBean
import com.live.module.bill.net.BillVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquAuthBean
import com.mshy.VInterestSpeed.common.bean.TantaWalletBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
class VquEarningsRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: BillVquApiService

    @Inject
    lateinit var mVquGlobalApi: GlobalApiService

    suspend fun withdraw() =
        request<BaseResponse<BillVquEarningData>> {
            mVquApi.withdraw().run {
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

    suspend fun vquBindAlipay(name: String, account: String) =
        request<BaseResponse<String>> {
            mVquApi.vquBindAlipay(name, account).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquGetWithdrawPrice(money: String) =
        request<BaseResponse<BillVquWithdrawPriceBean>> {
            mVquApi.vquGetWithdrawPrice(money).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquUserWithDraw(money: String) =
        request<BaseResponse<Any>> {
            mVquApi.vquUserWithDrawNew(money).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun isAuth() =
        request<BaseResponse<CommonVquAuthBean>> {
            mVquGlobalApi.vquIsAuth().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquWalletAlipay() = request<BaseResponse<BillVquAccountInfo>> {
        mVquApi.vquWalletAlipay().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}
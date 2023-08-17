package com.live.module.bill.repo

import com.live.module.bill.bean.BillVquEarningData
import com.live.module.bill.net.BillVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/19
 * description:
 */
class BillVquExchangeDiamondRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mVquApi: BillVquApiService

    suspend fun vquWithdraw() =
        request<BaseResponse<BillVquEarningData>> {
            mVquApi.withdraw().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquChangeCoin(money: Int) =
        request<BaseResponse<String>> {
            mVquApi.vquChangeCoin(money).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
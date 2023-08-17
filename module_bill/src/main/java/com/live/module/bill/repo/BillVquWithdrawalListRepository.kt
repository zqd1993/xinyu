package com.live.module.bill.repo

import com.live.module.bill.bean.BillVquWithdrawalListBean
import com.live.module.bill.net.BillVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/21
 * description:
 */
class BillVquWithdrawalListRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mVquApi: BillVquApiService

    fun vquCashoutRecord(page: Int) =
        request<BaseResponse<BillVquWithdrawalListBean>> {
            mVquApi.vquCashoutRecord(page, "").run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
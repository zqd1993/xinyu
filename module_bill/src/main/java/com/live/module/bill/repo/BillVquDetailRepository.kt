package com.live.module.bill.repo

import com.live.module.bill.bean.BillVquDetailBean
import com.live.module.bill.net.BillVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/20
 * description:
 */
class BillVquDetailRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: BillVquApiService


    suspend fun vquWalletRecord(params: MutableMap<String, Any>) =
        request<BaseResponse<BillVquDetailBean>> {
            mVquApi.vquWalletRecord(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
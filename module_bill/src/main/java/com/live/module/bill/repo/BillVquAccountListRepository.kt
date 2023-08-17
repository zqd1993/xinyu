package com.live.module.bill.repo

import com.live.module.bill.bean.BillVquAccountList
import com.live.module.bill.net.BillVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
class BillVquAccountListRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: BillVquApiService

    suspend fun vquAccountList() =
        request<BaseResponse<BillVquAccountList>> {
            mVquApi.vquAccountList().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquAccountUse(id: String) =
        request<BaseResponse<Any>> {
            mVquApi.vquAccountUse(id).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
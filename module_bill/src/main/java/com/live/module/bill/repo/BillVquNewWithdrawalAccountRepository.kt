package com.live.module.bill.repo

import com.live.module.bill.bean.BillVquAccountInfo
import com.live.module.bill.net.BillVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
class BillVquNewWithdrawalAccountRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mVquApi: BillVquApiService

    @Inject
    lateinit var mVquGlobalApi: GlobalApiService

    suspend fun vquAccountInfo(params: MutableMap<String, Any>) =
        request<BaseResponse<BillVquAccountInfo>> {
            mVquApi.vquAccountInfo(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquAccountSave(params: MutableMap<String, Any>) =
        request<BaseResponse<Any>> {
            mVquApi.vquAccountSave(params).run {
                responseCodeExceptionHandler(code, message, false) {
                    emit(this)
                }
            }
        }

    suspend fun vquSendCode(params: MutableMap<String, Any>) =
        request<BaseResponse<Any>> {
            mVquGlobalApi.tantaSendCode(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }

            }
        }
}
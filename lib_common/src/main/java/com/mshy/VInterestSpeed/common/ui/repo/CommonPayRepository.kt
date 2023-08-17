package com.mshy.VInterestSpeed.common.ui.repo

import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.*
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
}
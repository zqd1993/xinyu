package com.live.module.mine.repo

import com.live.module.mine.net.MineVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject


/**
 * author: Lau
 * date: 2022/4/1
 * description:
 */
class MineRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquGlobalApi: GlobalApiService

    @Inject
    lateinit var mVquApi: MineVquApiService


    suspend fun vquGetUserInfo() = request<BaseResponse<VquUserHomeBean>> {
        mVquApi.vquGetUserInfo().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquWalletIndex() = request<BaseResponse<TantaWalletBean>> {
        mVquGlobalApi.walletIndex().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquIndexBanner() =
        request<BaseResponse<com.mshy.VInterestSpeed.common.bean.CommonVquMainBannerBean>> {
            mVquGlobalApi.vquGetMainBanner("4").run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }


}
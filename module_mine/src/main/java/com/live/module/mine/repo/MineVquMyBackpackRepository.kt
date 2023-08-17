package com.live.module.mine.repo

import com.live.module.mine.bean.MineVquCouponBean
import com.live.module.mine.net.MineVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
class MineVquMyBackpackRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquApi: MineVquApiService


    suspend fun vquGetPackage(type: String) =
        request<BaseResponse<MutableList<MineVquCouponBean>>> {
            mVquApi.vquGetPackage(type).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    fun vquUseNobleCard(id: String) =
        request<BaseResponse<String>> {
            mVquApi.vquUseNobleCard(id).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}
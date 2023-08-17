package com.live.module.mine.net

import com.live.module.mine.bean.MineVquCouponBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.VquUserHomeBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author: Lau
 * date: 2022/4/12
 * description:
 */
interface MineVquApiService {

    @POST("user/home")
    suspend fun vquGetUserInfo(): BaseResponse<VquUserHomeBean>

    @POST("package/getPackage")
    @FormUrlEncoded
    suspend fun vquGetPackage(@Field("type") type: String): BaseResponse<MutableList<MineVquCouponBean>>

    @POST("package/useNobleCard")
    @FormUrlEncoded
    suspend fun vquUseNobleCard(@Field("p_id") id: String):BaseResponse<String>


//    @POST("wallet/index")
//    suspend fun vquWalletIndex(): BaseResponse<VquWalletBean>
}
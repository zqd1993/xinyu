package com.live.module.vip.net

import com.live.module.vip.bean.TantaVipInfoBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
interface VipVquApiService {
    @POST("vipOrder/payNobleOrder")
    @FormUrlEncoded
    suspend fun createVipOrder(@Field("id") id: String,
                               @Field("pay_type") pay_type: String,
                               @Field("type") type: String,
                               @Field("vip_goods_id") vip_goods_id: String): BaseResponse<TantaPayBean>

    @POST("vip/nobleRecharge")
    suspend fun getVipIndexInfo(): BaseResponse<TantaVipInfoBean>

    @POST("vip/award")
    suspend fun getVipDailyGif(): BaseResponse<Any>

}
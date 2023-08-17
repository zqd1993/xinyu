package com.mshy.VInterestSpeed.common.net

import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquAdBean
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *
 * @Description: 礼物
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:01
 *
 */
interface GiftApiService {


    /**
     * 获取广告
     *
     * @return
     */
    @POST("user/getAdvert")
    @FormUrlEncoded
    fun vquJavaGetAdvert(@Field("position") position: String): Call<BaseResponse<CommonVquAdBean>>

    /**
     * 获取礼物分类
     *
     * @return
     */
    @POST("gift/getGiftCate")
    fun vquGetGiftCate(): Call<BaseResponse<List<com.mshy.VInterestSpeed.common.bean.gift.GiftCateListBean>>>


    @POST("app/gifts")
    fun vquGetGiftBeans(): Call<BaseResponse<com.mshy.VInterestSpeed.common.bean.gift.GiftListBean>>


    @POST("gift/getGift")
    @FormUrlEncoded
    fun vquGetGift(@Field("gift_type") gift_type: String): Call<BaseResponse<com.mshy.VInterestSpeed.common.bean.gift.GiftNumBean>>


}
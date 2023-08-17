package com.live.module.auth.net

import com.live.module.auth.bean.DescribeVerifyTokenBean
import com.live.module.auth.bean.VquAuthResultBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
interface AuthVquApiService {

    @POST("user/userAuth")
    @FormUrlEncoded
    suspend fun vquUserAuth(
        @Field("real_name") name: String,
        @Field("id_card_number") id: String
    ): BaseResponse<Any>

    @POST("anchor/agree_protocol")
    @FormUrlEncoded
    suspend fun vquAgreeProtocol(@Field("agreement") agreement: Int): BaseResponse<Boolean>

    @POST("live/DescribeVerifyToken")
    suspend fun vquDescribeVerifyToken(): BaseResponse<DescribeVerifyTokenBean>

    @POST("live/DescribeVerifyResult")
    @FormUrlEncoded
    suspend fun vquDescribeVerifyResult(@Field("BizId") bizId: String): BaseResponse<Any>


    @POST("auth.userAuth/userAuth")
    @FormUrlEncoded
    suspend fun vquUserAuth(@FieldMap params: MutableMap<String, Any>): BaseResponse<Any>

    @POST("auth.userAuth/rPAuth")
    @FormUrlEncoded
    suspend fun vquRPAuth(@Field("metainfo") metainfo: String): BaseResponse<DescribeVerifyTokenBean>

    @POST("auth.userAuth/getResult")
    @FormUrlEncoded
    suspend fun vquAuthGetResult(
        @Field("BizId") bizId: String,
        @Field("verifyToken") token: String
    ): BaseResponse<VquAuthResultBean>

    @POST("sts/index")
    @FormUrlEncoded
    suspend fun vquGetStsIndex(@Field("type") type: String): BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>

    @POST("auth.userAuth/compareFace")
    @FormUrlEncoded
    suspend fun vquCompareFace(@Field("avatar") avatar: String): BaseResponse<VquAuthResultBean>
}
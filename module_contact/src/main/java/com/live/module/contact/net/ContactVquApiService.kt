package com.live.module.contact.net

import com.live.module.contact.bean.ContactVquInvitedListBean
import com.live.module.contact.bean.ContactVquOverviewData
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
interface ContactVquApiService {


    @POST("contacts/overview")
    @FormUrlEncoded
    suspend fun vquOverview(
        @Field("after_key") afterKey: Int,
        @Field("begin_time") beginTime: String,
        @Field("end_time") endTime: String,
        @Field("sort") sort: String,
    ): BaseResponse<ContactVquOverviewData>

    @POST("contacts/income")
    @FormUrlEncoded
    suspend fun vquIncome(
        @Field("after_key") afterKey: Int,
        @Field("begin_time") beginTime: String,
        @Field("end_time") endTime: String,
        @Field("sort") sort: String,
    ): BaseResponse<ContactVquOverviewData>

    /**
     * 我的人脉导出
     */
    @POST("contacts/excel2email")
    @FormUrlEncoded
    suspend fun vquExcel2email(
        @Field("email") email: String,
        @Field("begin_time") beginTime: String,
        @Field("end_time") endTime: String
    ): BaseResponse<Any>

    @POST("user/invite_list_new")
    @FormUrlEncoded
    suspend fun vquInviteList(@Field("page") page: Int,@Field("keyword") keyword:String): BaseResponse<ContactVquInvitedListBean>
}
package com.live.module.login.net

import com.live.module.login.bean.LoginTantaNicknameBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.SystemBean
import com.mshy.VInterestSpeed.common.bean.TantaLoginBean
import retrofit2.http.*

/**
 * author: Lau
 * date: 2022/4/7
 * description:
 */
interface LoginTantaApiService {
    /**
     * 发送验证码
     *
     * @return
     */
    @POST("passport/send_code")
    suspend fun vquSendCode(@Body map: HashMap<String, Any>): BaseResponse<SystemBean>

    /**
     * 获取首页全局配置数据
     *
     * @return
     */
    @POST("system/index")
    suspend fun getSystemIndex(): BaseResponse<SystemBean>

    @POST("passport/oneKeyLogin")
    @FormUrlEncoded
    suspend fun onKeyLogin(@FieldMap params: MutableMap<String, Any>): BaseResponse<TantaLoginBean>

    @POST("passport/send_code")
    @FormUrlEncoded
    suspend fun vquSendCode(@FieldMap params: MutableMap<String, Any>): BaseResponse<Any>

    @POST("passport/codeLogin")
    @FormUrlEncoded
    suspend fun vquCodeLogin(@FieldMap params: MutableMap<String, Any>): BaseResponse<TantaLoginBean>

    @POST("user/nickname")
    suspend fun vquNickname(): BaseResponse<LoginTantaNicknameBean>

    @POST("user/profile_reg_new")
    @FormUrlEncoded
    suspend fun vquProfileReg(@FieldMap params: MutableMap<String, Any>): BaseResponse<TantaLoginBean>

    @POST("passport/third")
    @FormUrlEncoded
    suspend fun vquPassportThird(@FieldMap netParams: MutableMap<String, Any>): BaseResponse<TantaLoginBean>

    @POST("sts/index")
    @FormUrlEncoded
    suspend fun vquGetStsIndex(@Field("type") type: String): BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>

}
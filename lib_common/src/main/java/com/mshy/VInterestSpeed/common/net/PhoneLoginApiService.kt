package com.mshy.VInterestSpeed.common.net


import com.mshy.VInterestSpeed.common.bean.BaseResponse
import retrofit2.http.*

/**
 * 登录模块
 *
 * @author Curtis wen
 * @since 2021/12/5 10:55 下午
 */
interface PhoneLoginApiService {

    /**
     * 发送验证码
     *
     * @return
     */
    @POST("passport/send_code")
    suspend fun sendCode(@Body map:HashMap<String,Any>): BaseResponse<String>
    /**
     * 登录
     *
     * @return
     */
    @POST("passport/codeLogin")
    suspend fun codeLogin(@Body map:HashMap<String,Any>): BaseResponse<String>
}

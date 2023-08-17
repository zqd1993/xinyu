package com.mshy.VInterestSpeed.common.net

import com.google.gson.Gson
import com.live.vquonline.base.constant.VersionStatus
import com.mshy.VInterestSpeed.common.BuildConfig
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.converter.JsonConverterFactory
import com.mshy.VInterestSpeed.common.interceptor.CommonVquHttpLoggingInterceptor
import com.mshy.VInterestSpeed.common.interceptor.CommonVquPublicInterceptor
import com.mshy.VInterestSpeed.common.interceptor.GlobalHeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * FileName: com.live.module.message.net
 * Date: 2022/4/25 11:53
 * Description:
 * History:
 */
object GlobalServiceManage {

    private fun getMsgHttpClient(): OkHttpClient {
        // 日志拦截器部分
        val level =
            if (BuildConfig.VERSION_TYPE != VersionStatus.RELEASE) CommonVquHttpLoggingInterceptor.Level.BODY else CommonVquHttpLoggingInterceptor.Level.NONE
        val logInterceptor = CommonVquHttpLoggingInterceptor().setLevel(level)

        return OkHttpClient.Builder()
            .connectTimeout(15L * 1000L, TimeUnit.MILLISECONDS)
            .readTimeout(20L * 1000L, TimeUnit.MILLISECONDS)
            .addInterceptor(GlobalHeaderInterceptor())
            .addInterceptor(CommonVquPublicInterceptor())
            .addInterceptor(logInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    var globalRetrofit:Retrofit? = null

     fun getRetrofit(): Retrofit {
         if(globalRetrofit == null){

             globalRetrofit =  Retrofit.Builder()
                 .baseUrl(NetBaseUrlConstant.MAIN_URL)
                 .addConverterFactory(JsonConverterFactory(Gson()))
                 .client(getMsgHttpClient())
                 .build()
             return globalRetrofit!!
         }
        return globalRetrofit!!
    }


}
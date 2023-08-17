package com.mshy.VInterestSpeed.common.di

import com.google.gson.Gson
import com.live.vquonline.base.constant.VersionStatus
import com.mshy.VInterestSpeed.common.BuildConfig
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.converter.JsonConverterFactory
import com.mshy.VInterestSpeed.common.interceptor.CommonVquHttpLoggingInterceptor
import com.mshy.VInterestSpeed.common.interceptor.CommonVquPublicInterceptor
import com.mshy.VInterestSpeed.common.interceptor.GlobalHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 全局作用域的网络层的依赖注入模块
 *
 * ""
 * @since 6/4/21 8:58 AM
 */
@Module
@InstallIn(SingletonComponent::class)
class DINetworkModule {

    /**
     * [OkHttpClient]依赖提供方法
     *
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        // 日志拦截器部分
        val level =
            if (BuildConfig.VERSION_TYPE != VersionStatus.RELEASE) CommonVquHttpLoggingInterceptor.Level.BODY else CommonVquHttpLoggingInterceptor.Level.NONE
        val logInterceptor = CommonVquHttpLoggingInterceptor().setLevel(level)

        return OkHttpClient.Builder()
            .connectTimeout(15L * 1000L, TimeUnit.MILLISECONDS)
            .readTimeout(20L * 1000L, TimeUnit.MILLISECONDS)
            .addInterceptor(CommonVquPublicInterceptor())
            .addInterceptor(GlobalHeaderInterceptor())
            .addInterceptor(logInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * 项目主要服务器地址的[Retrofit]依赖提供方法
     *
     * @param okHttpClient OkHttpClient OkHttp客户端
     * @return Retrofit
     * JsonResponseBodyConverter
     */
    @Singleton
    @Provides
    fun provideMainRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetBaseUrlConstant.MAIN_URL)
            .addConverterFactory(JsonConverterFactory(Gson()))
            .client(okHttpClient)
            .build()
    }
}